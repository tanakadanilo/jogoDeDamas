/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tabuleiro;

/**
 *
 * @author tanak
 */
public class Posicao {

    protected int posicaoX;
    protected int posicaoy;

    public Posicao(int posicaoX, int posicaoy) {
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
    
}
