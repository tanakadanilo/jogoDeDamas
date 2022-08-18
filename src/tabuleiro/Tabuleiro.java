/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tabuleiro;

import Pecas.Cor;
import Pecas.Peca;

/**
 *
 * @author tanak
 */
public class Tabuleiro {

    private int tamanhoVertical;
    private int tamanhoHorizontal;
    private Peca[][] casas;

    public Tabuleiro(int tamanhoVertical, int tamanhoHorizontal) {
        this.tamanhoVertical = tamanhoVertical;
        this.tamanhoHorizontal = tamanhoHorizontal;
        this.casas = new Peca[tamanhoVertical][tamanhoHorizontal];
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

    public Peca[][] getCasas() {
        return casas;
    }

    public void setCasas(Peca[][] casas) {
        this.casas = casas;
    }

    public boolean posicaoExiste(int posicaoX, int posicaoY) {
        return (posicaoX <= this.tamanhoHorizontal && posicaoY <= this.tamanhoVertical);
    }

    public boolean posicaoExiste(Posicao p) {
        return (p.posicaoX >= 0 && p.posicaoX < this.tamanhoHorizontal
                && p.posicaoY >= 0 && p.posicaoY < this.tamanhoVertical);
    }

    public void adicionaPeca(Peca peca) throws ExcecaoTabuleiro {
        Posicao p = peca.getPosicao();
        if (!posicaoExiste(p)) {
            throw new ExcecaoTabuleiro("A posicao: " + p.getPosicaoX() + ", " + p.getPosicaoY() + " não existe");
        }
        casas[p.getPosicaoX()][p.getPosicaoY()] = peca;
    }

    public void moverPeca(Peca peca, Posicao posicaoFinal) {
        casas[peca.getPosicao().getPosicaoX()][peca.getPosicao().getPosicaoY()] = null;
        casas[posicaoFinal.getPosicaoX()][posicaoFinal.getPosicaoY()] = peca;
    }
}
