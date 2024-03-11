package Model;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/**
 * Clase que gestiona la lista de archivos multimedia del usuario.
 *
 * @author dansias
 */
public class ListaArchivos {

    private ArrayList<Multimedia> listaArchivos;

    public ListaArchivos() {
        listaArchivos = new ArrayList<>();
    }

    /**
     * Añade un archivo a la lista de medios, creando los objetos correspondientes según el formato.
     * 
     * @param file El archivo a añadir.
     */
    public void agregarArchivo(File file) throws ReproductorException {
        String extension = obtenerExtension(file.getName());
        if (extension != null && (extension.equalsIgnoreCase("mp3") || extension.equalsIgnoreCase("wav"))) {
            Multimedia audio = new Audio(file.getName(), file.getPath(), extension);
            if (!archivoYaExiste(audio)) {
                listaArchivos.add(audio);
            }
        } else if (extension != null && (extension.equalsIgnoreCase("mp4") || extension.equalsIgnoreCase("mov"))) {
            Multimedia video = new Video(file.getName(), file.getPath(), extension);
            if (!archivoYaExiste(video)) {
                listaArchivos.add(video);
            }
        } else {
            throw new ReproductorException("Formato no soportado.");
        }
    }

    /**
     * Comprueba si un archivo Multimedia ya existe dentro de la lista.
     *
     * @param multimedia El archivo multimedia.
     * @return True si ya existe, false si no.
     */
    private boolean archivoYaExiste(Multimedia multimedia) {
        for (Multimedia archivo : listaArchivos) {
            if (archivo.getRutaFichero().equals(multimedia.getRutaFichero())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Elimina un Multimedia de la lista.
     * 
     * @param multimedia El Multimedia a eliminar.
     */
    public void eliminarArchivo(Multimedia multimedia) {
        listaArchivos.remove(multimedia);
    }

    /**
     * Obtiene la lista Multimedia.
     * 
     * @return La lista Multimedia.
     */
    public ArrayList<Multimedia> getLista() {
        return listaArchivos;
    }

    /**
     * Devuelve el Multimedia correspondiente al siguiente del pasado por parámetro.
     * 
     * @param archivoActual Índice del archivo cargado.
     * @return El siguiente Multimedia en la lista.
     */
    public Multimedia getSiguiente(int archivoActual) {
        if (listaArchivos.isEmpty()) {
            return null;
        }
        return listaArchivos.get((archivoActual + 1) % listaArchivos.size());
    }

    /**
     * Devuelve el Multimedia correspondiente al anterior del pasado por parámetro.
     * 
     * @param archivoActual Índice del archivo cargado.
     * @return El Multimedia anterior en la lista.
     */
    public Multimedia getAnterior(int archivoActual) {
        if (listaArchivos.isEmpty()) {
            return null;
        }
        if (archivoActual == 0) {
            return listaArchivos.get(listaArchivos.size() - 1);
        }
        return listaArchivos.get((archivoActual - 1) % listaArchivos.size());
    }

    /**
     * Devuelve un Multimedia aleatorio.
     * 
     * @param archivoActual Índice del archivo cargado.
     * @return Un Multimedia aleatorio.
     */
    public Multimedia getRandom(int archivoActual) {
        if (listaArchivos.isEmpty()) {
            return null;
        }
        
        if (listaArchivos.size() == 1) {
            return listaArchivos.get(0);
        }
        
        Random random = new Random();
        int index;
        
        // manejar que no se devuelva el mismo video que se 
        // está reproduciendo
        do {
            index = random.nextInt(listaArchivos.size());
        } while (index == archivoActual);

        return listaArchivos.get(index);
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
}
