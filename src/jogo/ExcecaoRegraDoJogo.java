/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jogo;

/**
 *
 * @author tanak
 */
public class ExcecaoRegraDoJogo extends Exception{

    public ExcecaoRegraDoJogo() {
    }

    public ExcecaoRegraDoJogo(String message) {
        super(message);
    }

    public ExcecaoRegraDoJogo(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcecaoRegraDoJogo(Throwable cause) {
        super(cause);
    }

    public ExcecaoRegraDoJogo(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    
}
