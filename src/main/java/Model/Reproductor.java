package Model;

import java.util.ArrayList;
import java.util.Random;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Clase que gestiona los diferentes eventos de reproducción del reproductor.
 *
 * @author dansias
 */
public class Reproductor {

    private ListaArchivos listaArchivos;
    private MediaPlayer mediaPlayer;
    private Multimedia archivoActual;
    private boolean reproduciendo;
    private boolean reproduccionAleatoria;

    public Reproductor() {
        listaArchivos = new ListaArchivos();
        reproduciendo = false;
        reproduccionAleatoria = false;
    }

    public void reproducir(Multimedia multimedia) throws ReproductorException {
        if (multimedia == null) {
            throw new ReproductorException("El objeto Multimedia es nulo");
        }

        archivoActual = multimedia;
        reproduciendo = true;

        String ruta = multimedia.getRutaFichero();
        Media media = new Media(ruta);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        reproduciendo = true;

        // Listener para detectar el fin de la reproducción
        mediaPlayer.setOnEndOfMedia(() -> {
            try {
                if (reproduccionAleatoria) {
                    reproducirAleatorio();
                } else {
                    siguiente();
                }
            } catch (ReproductorException e) {
                System.out.println("Error al reproducir el siguiente archivo: " + e.getMessage());
            }
        });
    }

    public void reproducirAleatorio() throws ReproductorException {
        ArrayList<Multimedia> archivos = listaArchivos.getLista();
        if (archivos == null || archivos.isEmpty()) {
            throw new ReproductorException("No hay archivos multimedia para reproducir aleatoriamente");
        }

        Random random = new Random();
        int index = random.nextInt(archivos.size());
        reproducir(archivos.get(index));
    }

    public void pausar() {
        if (mediaPlayer != null && reproduciendo) {
            mediaPlayer.pause();
            reproduciendo = false;
        }
    }

    public void detener() {
        if (mediaPlayer != null && reproduciendo) {
            mediaPlayer.stop();
            reproduciendo = false;
        }
    }

    public void siguiente() throws ReproductorException {
        ArrayList<Multimedia> archivos = listaArchivos.getLista();
        if (archivos == null || archivos.isEmpty()) {
            throw new ReproductorException("No hay archivos multimedia para reproducir");
        }

        if (reproduccionAleatoria) {
            reproducirAleatorio();
        } else {
            int index = archivos.indexOf(archivoActual);
            if (index == -1) {
                throw new ReproductorException("No se puede encontrar el archivo actual en la lista");
            }

            int siguienteIndex = (index + 1) % archivos.size();
            reproducir(archivos.get(siguienteIndex));
        }
    }

    public void anterior() throws ReproductorException {
        ArrayList<Multimedia> archivos = listaArchivos.getLista();
        if (archivos == null || archivos.isEmpty()) {
            throw new ReproductorException("No hay archivos multimedia para reproducir");
        }

        if (reproduccionAleatoria) {
            reproducirAleatorio();
        } else {
            int index = archivos.indexOf(archivoActual);
            if (index == -1) {
                throw new ReproductorException("No se puede encontrar el archivo actual en la lista");
            }

            int anteriorIndex = (index - 1 + archivos.size()) % archivos.size();
            reproducir(archivos.get(anteriorIndex));
        }
    }

    public void agregarArchivo(Multimedia multimedia) {
        listaArchivos.agregarArchivo(multimedia);
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
