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

    public Movimentos(Peca pecaMovida, Posicao inicio, Posicao fim, Peca pecaCapturada) {
        this.pecaMovida = pecaMovida;
        this.inicio = inicio;
        this.fim = fim;
        this.pecaCapturada = pecaCapturada;
    }

    public Peca getPecaMovida() {
        return pecaMovida;
    }

    public void setPecaMovida(Peca pecaMovida) {
        this.pecaMovida = pecaMovida;
    }

    public Posicao getInicio() {
        return inicio;
    }

    public void setInicio(Posicao inicio) {
        this.inicio = inicio;
    }

    public Posicao getFim() {
        return fim;
    }

    public void setFim(Posicao fim) {
        this.fim = fim;
    }

    public Peca getPecaCapturada() {
        return pecaCapturada;
    }

    public void setPecaCapturada(Peca pecaCapturada) {
        this.pecaCapturada = pecaCapturada;
    }

}
