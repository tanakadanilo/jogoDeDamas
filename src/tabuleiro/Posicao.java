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

    protected int posicaoX;//   * numero da linha
    protected int posicaoY;//   * numero da coluna

    public Posicao(int posicaoX, int posicaoy) {
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoy;
    }

    public int getPosicaoX() {
        return posicaoX;
    }

    public void setPosicaoX(int posicaoX) {
        this.posicaoX = posicaoX;
    }

    public int getPosicaoY() {
        return posicaoY;
    }

    public void setPosicaoY(int posicaoY) {
        this.posicaoY = posicaoY;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.posicaoX;
        hash = 97 * hash + this.posicaoY;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Posicao other = (Posicao) obj;
        if (this.posicaoX != other.posicaoX) {
            return false;
        }
        return this.posicaoY == other.posicaoY;
    }

    
}
