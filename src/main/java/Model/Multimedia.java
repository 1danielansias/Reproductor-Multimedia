/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.File;

/**
 * Clase padre que representa diferentes archivos multimedia.
 * 
 * @author dansias
 */
public class Multimedia {
    
    private String nombre;
    private File fichero;
    private double duracion;
    private String rutaFichero;
    private String extension;
    private String url;
    
    public Multimedia() {
    }
    
    public Multimedia(String nombre, String rutaFichero, String extension) {
        this.nombre = nombre;
        this.rutaFichero = rutaFichero;
        this.extension = extension;
        this.fichero = new File(rutaFichero);
        this.url = fichero.toURI().toString();
    }
    
    public Multimedia(double duracion, String rutaFichero, String extension) {
        this.duracion = duracion;
        this.rutaFichero = rutaFichero;
        this.extension = extension;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public double getDuracion() {
        return duracion;
    }

    public void setDuracion(double duracion) {
        this.duracion = duracion;
    }

    public String getRutaFichero() {
        return rutaFichero;
    }

    public void setRutaFichero(String rutaFichero) {
        this.rutaFichero = rutaFichero;
    }

    public File getFichero() {
        return fichero;
    }

    public void setFichero(File fichero) {
        this.fichero = fichero;
    }
    
    public String getURL() {
        return url;
    }
}
