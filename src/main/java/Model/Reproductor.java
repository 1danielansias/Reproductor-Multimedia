package Model;

import java.io.File;
import java.util.ArrayList;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

/**
 * Clase que gestiona los diferentes eventos de reproducción del reproductor.
 *
 * @author dansias
 */
public class Reproductor {

    private ListaArchivos listaArchivos;
    private MediaPlayer mediaPlayer;
    private MediaView mediaView;
    private ListView listView;
    private int archivoCargadoIndex;
    private boolean reproduccionAleatoria;
    private String status;

    public Reproductor() {
        listaArchivos = new ListaArchivos();
        reproduccionAleatoria = false;
        archivoCargadoIndex = -1;
        status = "NOT_READY";
    }

    public String getStatus() {
        return status;
    }

    public int getArchivoCargadoIndex() {
        return archivoCargadoIndex;
    }

    public void setArchivoCargadoIndex(int archivoCargadoIndex) {
        this.archivoCargadoIndex = archivoCargadoIndex;
    }

    // se pueden establecer diferentes listas al reproductor, como una de favortios, por ejemplo
    public void setListaArchivos(ListaArchivos nuevaListaArchivos) {
        this.listaArchivos = nuevaListaArchivos;
    }

    public void setMediaView(MediaView mediaView) {
        this.mediaView = mediaView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    /**
     * Prepara el reproductor para la reproducción.
     *
     * @param multimedia El multimedia que se va a preparar en el Reproductor.
     */
    public void prepare(Multimedia multimedia) {
        if (multimedia != null) {
            Media media = new Media(multimedia.getURL());
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            mediaPlayer = new MediaPlayer(media);
            addMediaPlayerListeners();
            mediaView.setMediaPlayer(mediaPlayer);
            // actualizar índice actual
            archivoCargadoIndex = listaArchivos.getLista().indexOf(multimedia);
            listView.getSelectionModel().select(archivoCargadoIndex);
            status = "READY";
        } else {
            status = "NOT_READY";
        }
    }

    /**
     * Reproduce un multimedia dado por la lista según los parámetros
     * configurados.
     *
     * @param next Siguiente activado.
     * @param aleatorio Aleatorio activado.
     * @param previous Anterior activado.
     * @throws ReproductorException Excepcion al cargar el media a reproducir.
     */
    public void play(boolean next, boolean aleatorio, boolean previous) throws ReproductorException {
        if (!listaArchivos.getLista().isEmpty()) {
            if (status.equals("PAUSED") && !next) {
                mediaPlayer.play();
            } else {
                Multimedia multimedia = getMultimedia(next, aleatorio, previous);
                prepare(multimedia);
                if (status.equals("READY")) {
                    mediaPlayer.play();                   
                }
            }
            status = "PLAYING";
        } else {
            throw new ReproductorException("No se han encontrado archivos multimedia para reproducir.");
        }
    }

    /**
     * Reproduce el primer multimedia de la lista. Este método se utiliza cuando
     * se cargan por primera vez archivos al reprodutor.
     *
     * @throws ReproductorException Excepcion al cargar el media a reproducir.
     */
    public void play() throws ReproductorException {
        if (archivoCargadoIndex == -1) {
            archivoCargadoIndex = 0;
        }
        if (!"PLAYING".equals(status)) {
            try {
                play(false, reproduccionAleatoria, false);
            } catch (ReproductorException ex) {
                throw new ReproductorException("No se han encontrado archivos multimedia para reproducir.");
            }
        }
    }

    /**
     * Obtiene un Objeto Multimedia de la lista.
     *
     * @param next Siguiente activado.
     * @param aleatorio Aleatorio activado
     * @return Objeto multimedia, null si no se pudo obtener.
     */
    private Multimedia getMultimedia(boolean next, boolean aleatorio, boolean previous) {
        if (!listaArchivos.getLista().isEmpty()) {
            if (previous) {
                return listaArchivos.getAnterior(archivoCargadoIndex);
            }
            if (next && !aleatorio) {
                return listaArchivos.getSiguiente(archivoCargadoIndex);
            } else if (aleatorio) {
                return listaArchivos.getRandom(archivoCargadoIndex);
            } else {
                return listaArchivos.getLista().get(archivoCargadoIndex);
            }
        }
        return null;
    }

    /**
     * Pausa la reproducción del multimedia.
     */
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
            status = "PAUSED";
        }
    }

    /**
     * Detiene la reproducción del multimedia y la reinicia.
     */
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            status = "STOPPED";
        }
    }

    /**
     * Añade diferentes Listeners para controlar el estado del MediaPlayer.
     */
    private void addMediaPlayerListeners() {
        mediaPlayer.setOnEndOfMedia(() -> {
            if (reproduccionAleatoria) {
                try {
                    play(true, true, false);
                } catch (ReproductorException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    play(true, false, false);
                } catch (ReproductorException e) {
                    e.printStackTrace();
                }
            }
        });
        mediaPlayer.setOnReady(() -> {
            mediaPlayer.setAutoPlay(true);
        });
    }

    /**
     * Añade un archivo a la lista multimedia.
     *
     * @param file El archivo a añadir.
     */
    public void agregarArchivo(File file) {
        listaArchivos.agregarArchivo(file);
    }

    /**
     * Elimina un archivo de la lista de reproducción.
     *
     * @param index Indice del archivo a eliminar.
     * @return True si se eliminó correctamente, false si no.
     * @throws ReproductorException ReproductorException Excepcion si no se ha
     * seleccionado ningún archivo para eliminar.
     */
    public boolean eliminarArchivo(int index) throws ReproductorException {
        if (index != -1) {
            if (index != archivoCargadoIndex) {
                Multimedia mediaSeleccionado = this.getLista().get(index);
                listaArchivos.eliminarArchivo(mediaSeleccionado);
                // Ajustar los indices después de eliminar
                if (archivoCargadoIndex > index) {
                    archivoCargadoIndex--;
                }
                return true;
            } else {
                return false;
            }
        } else {
            throw new ReproductorException("Ningún archivo seleccionado para eliminar");
        }
    }

    /**
     * Devuelve la lista multimedia.
     *
     * @return La lista multimedia.
     */
    public ArrayList<Multimedia> getLista() {
        return listaArchivos.getLista();
    }

    /**
     * Establece a true el atributo de reproduccionAleatoria
     */
    public void activarReproduccionAleatoria() {
        reproduccionAleatoria = true;
    }

    /**
     * Establece a false el atributo de reproduccionAleatoria
     */
    public void desactivarReproduccionAleatoria() {
        reproduccionAleatoria = false;
    }

    /**
     * Recupera el valor del atributo reproducciónAleatoria.
     *
     * @return True si la reproducción aleatoria está activada, false si no.
     */
    public boolean isReproduccionAleatoria() {
        return reproduccionAleatoria;
    }

    /**
     * Actualiza el volumen del MediaPlayer y el slider de volumen.
     *
     * @param volumen El nuevo valor de volumen (entre 0 y 1).
     * @param sliderVolumen El slider de volumen que se actualizará.
     */
    public void actualizarVolumen(double volumen, Slider sliderVolumen) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volumen);
            sliderVolumen.setValue(volumen * 100.0);
        }
    }

    /**
     * Actualiza la posición de reproducción del MediaPlayer y el slider de
     * duración.
     *
     * @param duracion La nueva duración de reproducción (en segundos).
     * @param sliderDuracion El slider de duración que se actualizará.
     */
    public void actualizarDuracion(double duracion, Slider sliderDuracion) {
        if (mediaPlayer != null) {
            Duration nuevaDuracion = mediaPlayer.getTotalDuration().multiply(duracion / 100.0);
            mediaPlayer.seek(nuevaDuracion);
            sliderDuracion.setValue(duracion);
        }
    }
}
