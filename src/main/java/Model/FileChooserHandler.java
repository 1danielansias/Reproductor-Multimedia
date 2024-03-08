/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.File;
import java.util.List;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author dansias
 */
public class FileChooserHandler {

    public static boolean selectAndPlayMedia(Stage stage, Reproductor reproductor, boolean aleatorioActivado) throws ReproductorException {
        List<File> selectedFiles = getFiles(stage);
        if (selectedFiles != null) {
            for (File selectedMedia : selectedFiles) {
                reproductor.agregarArchivo(selectedMedia);
            }
            reproductor.play();
            return true;
        } else {
            return false;
        }
    }

    private static List<File> getFiles(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter audioFilter
                = new FileChooser.ExtensionFilter("Formato audio (.mp3, .wav)", "*.mp3", "*.wav");
        FileChooser.ExtensionFilter videoFilter
                = new FileChooser.ExtensionFilter("Formato vídeo (.mp4, .mov)", "*.mp4", "*.mov");
        fileChooser.getExtensionFilters().addAll(videoFilter, audioFilter);
        fileChooser.setTitle("Selecciona un archivo multimedia");

        return fileChooser.showOpenMultipleDialog(stage);
    }
}
