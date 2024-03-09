package edu.cotarelo.audioavanzado;

import Model.FileChooserHandler;
import Model.Reproductor;
import Model.ReproductorException;
import java.util.concurrent.Callable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PrimaryController {

    @FXML
    private CheckBox aleatorioCheck;

    @FXML
    private ListView<String> listView;

    @FXML
    private Label currentTimeLabel;
    
    @FXML
    private Label totalTimeLabel;

    @FXML
    private Label labelInfo;

    @FXML
    private MediaView mediaView;

    @FXML
    private Slider sliderDuracion;

    @FXML
    private Slider sliderVolumen;

    @FXML
    private VBox vBoxControles;
    
    @FXML
    private VBox container;
    
    private Reproductor reproductor;

    private boolean aleatorioActivado;

    @FXML
    public void initialize() {
        reproductor = new Reproductor();
        reproductor.setMediaView(mediaView);
        reproductor.setListView(listView);
        reproductor.setSliderVolumen(sliderVolumen);
        aleatorioActivado = false;
        aleatorioCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    aleatorioActivado = true;
                    reproductor.activarReproduccionAleatoria();
                } else {
                    aleatorioActivado = false;
                    reproductor.desactivarReproduccionAleatoria();
                }
            }
        });
        container.sceneProperty().addListener(new ChangeListener<Scene>() {
            @Override
            public void changed(ObservableValue<? extends Scene> observableValue, Scene scene, Scene newScene) {
                if (scene == null && newScene != null) {
                    mediaView.fitHeightProperty().bind(newScene.heightProperty().subtract(vBoxControles.heightProperty().add(60)));
                    mediaView.fitWidthProperty().bind(newScene.widthProperty());
                }
            }
        });
    }

    @FXML
    void chooseMedia(MouseEvent event) {
        try {
            if (FileChooserHandler.selectAndPlayMedia(getStage(), reproductor, aleatorioActivado)) {
                cargarListView();
                setup();
            } else {
                ventanaError("Por favor, seleccione un archivo/s para reproducir.");
            }
        } catch (ReproductorException ex) {
            ventanaError(ex.getMessage());
        }
    }

    @FXML
    void pause(MouseEvent event) {
        reproductor.pause();
        setup();
    }

    @FXML
    void stop(MouseEvent event) {
        reproductor.stop();
        setup();
    }

    @FXML
    void play(MouseEvent event) {
        try {
            reproductor.play(false, aleatorioActivado, false);
            setup();
        } catch (ReproductorException ex) {
            ventanaError(ex.getMessage());
        }
    }

    @FXML
    void previous(MouseEvent event) {
        try {
            reproductor.play(false, aleatorioActivado, true);
            setup();
        } catch (ReproductorException ex) {
            ventanaError(ex.getMessage());
        }
    }

    @FXML
    void next(MouseEvent event) {
        try {
            reproductor.play(true, aleatorioActivado, false);
            setup();
        } catch (ReproductorException ex) {
            ventanaError(ex.getMessage());
        }
    }

    @FXML
    void onListViewDragOver(DragEvent event) {
        if (event.getGestureSource() != listView && event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            listView.setStyle("-fx-border-color: linear-gradient(to right,#61D8DE, #E839F6); -fx-border-width: 2px;");
        }
        event.consume();
    }

    @FXML
    void onListViewDragExited(DragEvent event) {
        listView.setStyle("-fx-border-color: transparent;");
        event.consume();
    }

    @FXML
    void onListViewDragDropped(DragEvent event) {
        var db = event.getDragboard();
        boolean exito = false;
        if (db.hasFiles()) {
            for (var file : db.getFiles()) {
                reproductor.agregarArchivo(file);
            }
            cargarListView();
            try {
                reproductor.play();
                setup();
            } catch (ReproductorException ex) {
                ventanaError(ex.getMessage());
            }
            exito = true;
        }
        event.setDropCompleted(exito);
        event.consume();
    }

    @FXML
    void deleteMedia(MouseEvent event) {
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        try {
            if (reproductor.eliminarArchivo(selectedIndex)) {
                ventanaInfo("Multimedia eliminado.");
                cargarListView();
            } else {
                ventanaError("No se pudo eliminar el archivo seleccionado. Reproducción en curso.");
            }
        } catch (ReproductorException ex) {
            ventanaError("Por favor, seleccione un archivo multimedia para eliminar.");
        }
    }

    /**
     * Recupera el escenario de la vista.
     *
     * @return El escenario.
     */
    private Stage getStage() {
        return (Stage) listView.getScene().getWindow();
    }

    /**
     * Carga los nombre de los elementos de la lista de reproducción al listView
     * de la interfaz de usuario. Añade también un Listener para Click a cada
     * elemento.
     */
    private void cargarListView() {
        listView.getItems().clear();
        for (int i = 0; i < reproductor.getLista().size(); i++) {
            listView.getItems().add(reproductor.getLista().get(i).getNombre());
            listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent click) {
                    if (click.getClickCount() == 2) {
                        try {
                            reproductor.setArchivoCargadoIndex(listView.getSelectionModel().getSelectedIndex());
                            reproductor.play(false, false, false);
                        } catch (ReproductorException ex) {
                            ventanaError(ex.getMessage());
                        }
                    }
                }
            });
        }
    }
    
    private void setup() {
        bindCurrentTimeLabel();
        bindTotalTimeLabel();
        labelInfo.setText("Reproduciendo: " + reproductor.getLista().get(reproductor.getArchivoCargadoIndex()).getNombre());
    }
    
    /**
     * 
     */
    private void bindCurrentTimeLabel() {
        // Establecer el valor del valor actual de duracion 
        currentTimeLabel.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return reproductor.getTime(reproductor.getMediaPlayer().getCurrentTime()) + " / ";
            } 
        }, reproductor.getMediaPlayer().currentTimeProperty()));
    }
    
    /**
     * 
     */
    private void bindTotalTimeLabel() {
        // Establecer label de duración total
        reproductor.getMediaPlayer().totalDurationProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                sliderDuracion.setMax(newValue.toSeconds());
                totalTimeLabel.setText(reproductor.getTime(newValue));
            }
        });
        
        // Establecer evento de drag and drop del slider de tiempo
        sliderDuracion.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean wasChanging, Boolean isChanging) {
                bindCurrentTimeLabel();
                if (!isChanging) {
                    reproductor.getMediaPlayer().seek(Duration.seconds(sliderDuracion.getValue()));
                }
            }
        });
        
        // Establecer listener de movimiento del slider de tiempo
        sliderDuracion.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                bindCurrentTimeLabel();
                // Get the current time of the video in seconds.
                double currentTime = reproductor.getMediaPlayer().getCurrentTime().toSeconds();
                if (Math.abs(currentTime - newValue.doubleValue()) > 0.5) {
                    reproductor.getMediaPlayer().seek(Duration.seconds(newValue.doubleValue()));
                }
            }
        });
        
        reproductor.getMediaPlayer().currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration oldTime, Duration newTime) {
                bindCurrentTimeLabel();
                if (!sliderDuracion.isValueChanging()) {
                    sliderDuracion.setValue(newTime.toSeconds());
                }
            }
        });
    }

    /**
     * Muestra una ventana con un mensaje de error.
     *
     * @param mensaje Mensaje de error.
     */
    private void ventanaError(String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra una ventana con un mensaje informativo.
     *
     * @param mensaje Mensaje informativo.
     */
    private void ventanaInfo(String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
