package Model;

import java.io.File;
import java.util.List;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Clase que gestiona la selección y reproducción de archivos multimedia.
 * 
 * @author dansias
 */
public class FileChooserHandler {

    /**
     * Selecciona archivos multimedia y los reproduce utilizando el reproductor
     * proporcionado.
     * 
     * @param stage La ventana de la aplicación donde se mostrará el diálogo de selección de archivos.
     * @param reproductor El reproductor multimedia donde se reproducirán los archivos seleccionados.
     * @param aleatorioActivado Indica si la reproducción aleatoria está activada.
     * @return true si se seleccionaron y reprodujeron archivos correctamente, false si no.
     * @throws ReproductorException si hay un error al reproducir los archivos seleccionados.
     */
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

    /**
     * Abre un diálogo de selección de archivos multimedia y devuelve la lista de archivos seleccionados.
     * 
     * @param stage La ventana de la aplicación donde se mostrará el diálogo de selección de archivos.
     * @return una lista de archivos seleccionados, o null si no se seleccionaron archivos.
     */
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
