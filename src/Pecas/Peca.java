/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pecas;

/**
 *
 * @author tanak
 */
public class Peca {

    private int posicaoX;
    private int posicaoy;

    public Peca(int posicaoX, int posicaoy) {
        this.posicaoX = posicaoX;
        this.posicaoy = posicaoy;
    }

    public int getPosicaoX() {
        return posicaoX;
    }

    public void setPosicaoX(int posicaoX) {
        this.posicaoX = posicaoX;
    }

    public int getPosicaoy() {
        return posicaoy;
    }

    public void setPosicaoy(int posicaoy) {
        this.posicaoy = posicaoy;
    }
    public void mover(int posicaoX, int posicaoY){
        this.posicaoX = posicaoX;
        this.posicaoy = posicaoY;
    }
    
}
