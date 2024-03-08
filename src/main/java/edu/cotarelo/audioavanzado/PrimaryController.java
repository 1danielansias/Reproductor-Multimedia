package edu.cotarelo.audioavanzado;

import Model.FileChooserHandler;
import Model.Reproductor;
import Model.ReproductorException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class PrimaryController {

    private Reproductor reproductor;

    private boolean aleatorioActivado;

    @FXML
    private CheckBox aleatorioCheck;

    @FXML
    private ListView<String> listView;

    @FXML
    private Label infoDuracion;

    @FXML
    private Label labelInfo;

    @FXML
    private MediaView mediaView;

    @FXML
    private Slider sliderDuracion;

    @FXML
    private Slider sliderVolumen;

    @FXML
    private HBox videoContainer;

    @FXML
    public void initialize() {
        reproductor = new Reproductor();
        reproductor.setMediaView(mediaView);
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
    }

    @FXML
    void chooseMedia(MouseEvent event) {
        try {
            if (FileChooserHandler.selectAndPlayMedia(getStage(), reproductor, aleatorioActivado)) {
                cargarListView();
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
    }

    @FXML
    void stop(MouseEvent event) {
        reproductor.stop();
    }

    @FXML
    void play(MouseEvent event) {
        try {
            reproductor.play(false, aleatorioActivado, false);
        } catch (ReproductorException ex) {
            ventanaError("No se pudo obtener el multimedia para reproducir.");
        }
    }

    @FXML
    void previous(MouseEvent event) {
        try {
            reproductor.play(false, aleatorioActivado, true);
        } catch (ReproductorException ex) {
            ventanaError("No se pudo obtener el multimedia para reproducir.");
        }
    }

    @FXML
    void next(MouseEvent event) {
        try {
            reproductor.play(true, aleatorioActivado, false);
        } catch (ReproductorException ex) {
            ventanaError("No se pudo obtener el multimedia para reproducir.");
        }
    }

    @FXML
    void onListViewDragOver(DragEvent event) {
    }

    @FXML
    void onListViewDragExited(DragEvent event) {
    }

    @FXML
    void onListViewDragDropped(DragEvent event) {
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
     * Recupera la escena de la vista.
     *
     * @return La escena.
     */
    private Stage getStage() {
        return (Stage) listView.getScene().getWindow();
    }

    /**
     * Carga los nombre de los elementos de la lista de reproducción al listView
     * de la interfaz de usuario.
     */
    private void cargarListView() {
        listView.getItems().clear();
        for (int i = 0; i < reproductor.getLista().size(); i++) {
            listView.getItems().add(reproductor.getLista().get(i).getNombre());
        }
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
