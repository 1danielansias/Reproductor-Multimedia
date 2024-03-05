/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 * Clase encargada de capturar las excepciones que puedan ocurrir en el Reproductor.
 * 
 * @author dansias
 */
public class ReproductorException extends Exception {

    public ReproductorException() {
        super();
    }

    public ReproductorException(String message) {
        super(message);
    }

    public ReproductorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReproductorException(Throwable cause) {
        super(cause);
    }
    
}
