/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

    public void agregarArchivo(File file) {
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

    public void eliminarArchivo(Multimedia multimedia) {
        listaArchivos.remove(multimedia);
    }

    public ArrayList<Multimedia> getLista() {
        return listaArchivos;
    }

    public Multimedia getSiguiente(int archivoActual) {
        if (listaArchivos.isEmpty()) {
            return null;
        }
        return listaArchivos.get((archivoActual + 1) % listaArchivos.size());
    }

    public Multimedia getAnterior(int archivoActual) {
        if (listaArchivos.isEmpty()) {
            return null;
        }
        if (archivoActual == 0) {
            return listaArchivos.get(listaArchivos.size() - 1);
        }
        return listaArchivos.get((archivoActual - 1) % listaArchivos.size());
    }

    public Multimedia getRandom() {
        if (listaArchivos.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(listaArchivos.size());
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
