/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pecas;

import jogo.ExcecaoRegraDoJogo;
import tabuleiro.ExcecaoTabuleiro;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;

/**
 *
 * @author tanak
 */
public abstract class Peca {

    protected Posicao p;
    protected final Tabuleiro tabuleiro;
    protected final Cor cor;// * cor do peão, não aceitar vazio

    public Peca(Posicao p, Tabuleiro tabuleiro, Cor cor) {
        this.p = p;
        this.tabuleiro = tabuleiro;
        this.cor = cor;
    }

    public Peca(int posicaoX, int posicaoY, Tabuleiro tabuleiro, Cor cor) {
        this.p = new Posicao(posicaoX, posicaoY);
        this.tabuleiro = tabuleiro;
        this.cor = cor;
    }

    public Posicao getPosicao() {
        return p;
    }

    public void setP(Posicao p) {
        this.p = p;
    }

    public Cor getCor() {
        return cor;
    }

    public void mover(int posicaoX, int posicaoY) throws ExcecaoTabuleiro, ExcecaoRegraDoJogo {
        if (!tabuleiro.posicaoExiste(posicaoX, posicaoY)) {// * posição não existe
            throw new ExcecaoTabuleiro("A posição informada não existe");
        }
        Posicao destino = new Posicao(posicaoX, posicaoY);
        if (!podeMover(destino)) {
            throw new ExcecaoRegraDoJogo("A peça escolhida não pode ser movida para essa posição");
        }
        this.p.setPosicaoX(posicaoX);
        this.p.setPosicaoY(posicaoY);
    }

    public void desfazerMovimento(Posicao p) throws ExcecaoTabuleiro {
        if (!tabuleiro.posicaoExiste(p)) {// * posição não existe
            throw new ExcecaoTabuleiro("A posição informada não existe");
        }
        tabuleiro.moverPeca(this, p);
        this.p = p;
    }

    public void mover(Posicao p) throws ExcecaoTabuleiro, ExcecaoRegraDoJogo {
        if (!tabuleiro.posicaoExiste(p)) {// * posição não existe
            throw new ExcecaoTabuleiro("A posição informada não existe");
        }
        if (!podeMover(p)) {
            throw new ExcecaoRegraDoJogo("A peça escolhida não pode ser movida para essa posição");
        }
        tabuleiro.moverPeca(this, p);
        this.p = p;
    }

    public abstract boolean[][] movimentosPossiveis();

    public abstract boolean algumMovimentoPossivel();

    public abstract boolean podeMover(Posicao destino);

    public abstract boolean podeContinuarCapturando(Posicao inicio) throws ExcecaoTabuleiro;
}
