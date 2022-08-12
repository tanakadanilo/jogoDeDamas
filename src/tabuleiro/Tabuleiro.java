/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tabuleiro;

import Pecas.PecasPossiveis;

/**
 *
 * @author tanak
 */
public class Tabuleiro {

    private int tamanhoVertical;
    private int tamanhoHorizontal;
    private PecasPossiveis[][] casas;

    public Tabuleiro(int tamanhoVertical, int tamanhoHorizontal) {
        this.tamanhoVertical = tamanhoVertical;
        this.tamanhoHorizontal = tamanhoHorizontal;
        this.casas = new PecasPossiveis[tamanhoVertical][tamanhoHorizontal];
    }

    public int getTamanhoVertical() {
        return tamanhoVertical;
    }

    public void setTamanhoVertical(int tamanhoVertical) {
        this.tamanhoVertical = tamanhoVertical;
    }

    public int getTamanhoHorizontal() {
        return tamanhoHorizontal;
    }

    public void setTamanhoHorizontal(int tamanhoHorizontal) {
        this.tamanhoHorizontal = tamanhoHorizontal;
    }

    public PecasPossiveis[][] getCasas() {
        return casas;
    }

    public void setCasas(PecasPossiveis[][] casas) {
        this.casas = casas;
    }

    public boolean posicaoExiste(int posicaoX, int posicaoY) {
        return (posicaoX <= this.tamanhoHorizontal && posicaoY <= this.tamanhoVertical);
    }

    public boolean posicaoExiste(Posicao p) {
        return (p.posicaoX <= this.tamanhoHorizontal && p.posicaoY <= this.tamanhoVertical);
    }
}
