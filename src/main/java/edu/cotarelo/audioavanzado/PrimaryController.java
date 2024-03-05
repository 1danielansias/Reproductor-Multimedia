package edu.cotarelo.audioavanzado;

import Model.Audio;
import Model.Multimedia;
import Model.Reproductor;
import Model.ReproductorException;
import Model.Video;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class PrimaryController {

    private Reproductor reproductor;

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

    public PrimaryController() {
        reproductor = new Reproductor();
    }

    @FXML
    void chooseMedia(MouseEvent event) {
        // Elección de fichero
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona un archivo multimedia");
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

        // Comprobaciones
        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            for (File file : selectedFiles) {
                // Comprobar si es un archivo de audio o video
                String extension = obtenerExtension(file.getName());
                if (extension != null && (extension.equalsIgnoreCase("mp3") || extension.equalsIgnoreCase("wav"))) {
                    // Es un archivo de audio
                    Audio audio = new Audio(file.getName(), file.getPath(), extension);
                    reproductor.agregarArchivo(audio);
                } else if (extension != null && (extension.equalsIgnoreCase("mp4") || extension.equalsIgnoreCase("mov"))) {
                    // Es un archivo de video
                    Video video = new Video(file.getName(), file.getPath(), extension);
                    reproductor.agregarArchivo(video);
                } else {
                    mostrarMensajeError("El archivo seleccionado no es compatible.");
                }
            }
        } else {
            mostrarMensajeError("Seleccione un archivo válido.");
        }
    }

    @FXML
    void pause(MouseEvent event) {
        reproductor.pausar();
    }

    @FXML
    void play(MouseEvent event) {
        try {
            reproductor.reproducir(archivoSeleccionado());
        } catch (ReproductorException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void stop(MouseEvent event) {
        reproductor.detener();
    }

    @FXML
    void previous(MouseEvent event) {
        try {
            reproductor.anterior();
        } catch (ReproductorException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void next(MouseEvent event) {
        try {
            reproductor.siguiente();
        } catch (ReproductorException ex) {
            ex.printStackTrace();
        }
    }

    // Método para manejar el evento onDragOver del ListView
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
     * Método para obtener el archivo multimedia seleccionado en la lista
     * 
     * @return Fichero seleccionado.
     */
    private Multimedia archivoSeleccionado() {
        String nombreArchivo = listView.getSelectionModel().getSelectedItem();
        if (nombreArchivo != null) {
            ArrayList<Multimedia> archivos = reproductor.getLista();
            for (Multimedia multimedia : archivos) {
                if (multimedia.getNombre().equals(nombreArchivo)) {
                    return multimedia;
                }
            }
        }
        return null;
    }

    /**
     * Devuelve la extensión de un archivo.
     *
     * @param nombreArchivo Nombre del archivo.
     * @return Extensión del archivo.
     */
    private String obtenerExtension(String nombreArchivo) {
        int index = nombreArchivo.lastIndexOf('.');
        if (index > 0 && index < nombreArchivo.length() - 1) {
            return nombreArchivo.substring(index + 1).toLowerCase();
        }
        return null;
    }

    /**
     * Muestra un mensaje de error durante 1 segundo.
     *
     * @param mensaje Mensaje de error.
     */
    private void mostrarMensajeError(String mensaje) {
        labelInfo.setText(mensaje);
        labelInfo.setTextFill(Color.RED);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), (ActionEvent event) -> {
            labelInfo.setText("");
            labelInfo.setTextFill(Color.web("#ffffff"));
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }

    /**
     * Comprueba si un fichero ya existe dentro del ListView.
     *
     * @param nombreFichero Nombre del fichero.
     * @return True si existe, false si no.
     */
    private boolean comprobarListView(String nombreFichero) {
        for (String nombre : listView.getItems()) {
            if (nombre.equals(nombreFichero)) {
                return true;
            }
        }
        return false;
    }

}
