/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jogo;

import Pecas.Peca;
import tabuleiro.Posicao;

/**
 *
 * @author tanak
 */
public class Movimentos {

    Pecas.Peca pecaMovida;
    protected Posicao inicio;
    protected Posicao fim;
    Pecas.Peca pecaCapturada;
    boolean capturaEmSequencia;

    public Movimentos(Peca pecaMovida, Posicao inicio, Posicao fim, Peca pecaCapturada, boolean capturaEmSequencia) {
        this.pecaMovida = pecaMovida;
        this.inicio = inicio;
        this.fim = fim;
        this.pecaCapturada = pecaCapturada;
        this.capturaEmSequencia = capturaEmSequencia;
    }

    public Peca getPecaMovida() {
        return pecaMovida;
    }

    public Posicao getInicio() {
        return inicio;
    }

    public Posicao getFim() {
        return fim;
    }

    public Peca getPecaCapturada() {
        return pecaCapturada;
    }

    public boolean isCapturaEmSequencia() {
        return capturaEmSequencia;
    }

}
