package Model;

import java.io.File;
import java.util.ArrayList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 * Clase que gestiona los diferentes eventos de reproducción del reproductor.
 *
 * @author dansias
 */
public class Reproductor {

    private ListaArchivos listaArchivos;
    private MediaPlayer mediaPlayer;
    private MediaView mediaView;
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

    public void setListaArchivos(ListaArchivos nuevaListaArchivos) {
        this.listaArchivos = nuevaListaArchivos;
    }

    public void setMediaView(MediaView mediaView) {
        this.mediaView = mediaView;
    }

    /**
     * Prepara el reproductor para la reproducción.
     */
    public void prepare() {
        if (!listaArchivos.getLista().isEmpty()) {
            archivoCargadoIndex = 0;
            status = "OKAY";
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
        Multimedia multimedia = getMultimedia(next, aleatorio, previous);
        if (multimedia != null) {
            Media media = new Media(multimedia.getURL());
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.play();
            status = "PLAYING";
            archivoCargadoIndex = listaArchivos.getLista().indexOf(multimedia);
            addMediaPlayerListener();
        } else {
            throw new ReproductorException("No se pudo obtener el multimedia para reproducir.");
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
                return listaArchivos.getRandom();
            } else {
                return listaArchivos.getLista().get(archivoCargadoIndex);
            }
        }
        return null;
    }

    /**
     * Restablece el estado del reproductor a su estado inicial.
     */
    public void reset() {
        listaArchivos = new ListaArchivos();
        reproduccionAleatoria = false;
        archivoCargadoIndex = -1;
        status = "NOT_READY";
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
        if (mediaView != null) {
            mediaView.setMediaPlayer(null);
        }
    }
    
    /**
     * Añade un ChangeListener al estado de reproducción del MediaPlayer para pasar al siguiente
     * multimedia cuando el actual termine de reproducirse.
     */
    private void addMediaPlayerListener() {
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
    }

    public void agregarArchivo(File file) {
        listaArchivos.agregarArchivo(file);
    }

    public void eliminarArchivo(Multimedia multimedia) {
        listaArchivos.eliminarArchivo(multimedia);
    }

    public ArrayList<Multimedia> getLista() {
        return listaArchivos.getLista();
    }

    public void activarReproduccionAleatoria() {
        reproduccionAleatoria = true;
    }

    public void desactivarReproduccionAleatoria() {
        reproduccionAleatoria = false;
    }

    public boolean isReproduccionAleatoria() {
        return reproduccionAleatoria;
    }
}
