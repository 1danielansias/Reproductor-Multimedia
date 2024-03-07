package edu.cotarelo.audioavanzado;

import Model.Reproductor;
import Model.ReproductorException;
import java.io.File;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;

public class PrimaryController {

    private Reproductor reproductor;

    private boolean aleatorioActivado;

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
    }

    @FXML
    void chooseMedia(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter audioFilter
                = new FileChooser.ExtensionFilter(".mp3, .wav", "*.mp3", "*.wav");
        FileChooser.ExtensionFilter videoFilter
                = new FileChooser.ExtensionFilter(".mp4, .mov", "*.mp4", "*.mov");
        fileChooser.getExtensionFilters().addAll(audioFilter, videoFilter);
        fileChooser.setTitle("Selecciona un archivo multimedia");

        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

        for (File selectedMedia : selectedFiles) {
            reproductor.agregarArchivo(selectedMedia);
        }
        cargarListView();
        reproductor.prepare();
    }

    @FXML
    void pause(MouseEvent event) {

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
    void stop(MouseEvent event) {

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
}
