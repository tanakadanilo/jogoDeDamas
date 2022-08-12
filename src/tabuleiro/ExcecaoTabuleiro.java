/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tabuleiro;

/**
 *
 * @author tanak
 */
public class ExcecaoTabuleiro extends Exception{

    public ExcecaoTabuleiro() {
    }

    public ExcecaoTabuleiro(String message) {
        super(message);
    }

    public ExcecaoTabuleiro(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcecaoTabuleiro(Throwable cause) {
        super(cause);
    }

    public ExcecaoTabuleiro(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    
}
