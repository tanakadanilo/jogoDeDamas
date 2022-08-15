/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pecas;

import tabuleiro.ExcecaoTabuleiro;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;

/**
 *
 * @author tanak
 */
public class Peca {

    protected Posicao p;
    protected final Tabuleiro tabuleiro;

    public Peca(Posicao p, Tabuleiro tabuleiro) {
        this.p = p;
        this.tabuleiro = tabuleiro;
    }

    public Peca(int posicaoX, int posicaoY, Tabuleiro tabuleiro) {
        this.p = new Posicao(posicaoX, posicaoY);
        this.tabuleiro = tabuleiro;
    }

    public Posicao getP() {
        return p;
    }

    public void setP(Posicao p) {
        this.p = p;
    }

    public void mover(int posicaoX, int posicaoY) throws ExcecaoTabuleiro {
        if (!tabuleiro.posicaoExiste(posicaoX, posicaoY)) {// * posição não existe
            throw new ExcecaoTabuleiro("A posição informada não existe");
        }
        this.p.setPosicaoX(posicaoX);
        this.p.setPosicaoY(posicaoY);
    }

    public void mover(Posicao p) throws ExcecaoTabuleiro {
        if (!tabuleiro.posicaoExiste(p)) {// * posição não existe
            throw new ExcecaoTabuleiro("A posição informada não existe");
        }
        this.p = p;
    }

}
