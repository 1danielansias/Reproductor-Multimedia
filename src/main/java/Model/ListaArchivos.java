/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.ArrayList;

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
    
    public void agregarArchivo(Multimedia multimedia) {
       listaArchivos.add(multimedia);
    }
    
    public void eliminarArchivo(Multimedia multimedia) {
        listaArchivos.remove(multimedia);
    }
    
    public ArrayList<Multimedia> getLista() {
        return listaArchivos;
    }
}
