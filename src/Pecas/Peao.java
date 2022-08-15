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
public class Peao extends Peca {

    public Peao(Posicao p, Tabuleiro tabuleiro, Cor cor) {
        super(p, tabuleiro, cor);
        if (cor == Cor.VAZIO) {
            throw new IllegalStateException("Tentando criar uma peça no tabuleiro que não é nem branca nem preta");
        }
    }

    public Peao(int posicaoX, int posicaoY, Tabuleiro tabuleiro, Cor cor) {
        super(posicaoX, posicaoY, tabuleiro, cor);
        if (cor == Cor.VAZIO) {
            throw new IllegalStateException("Tentando criar uma peça no tabuleiro que não é nem branca nem preta");
        }
    }

    public void moverPeao(Posicao posicaoFinal) throws ExcecaoTabuleiro, ExcecaoRegraDoJogo {
        //  * método para mover a peça, o parâmetro define a direção, já que um peão só pode se mover em 2 direções

        if (!podeMover()) {//  * nenhum movimento possivel
            throw new ExcecaoRegraDoJogo("A peça selecionada não tem nenhum movimento possível");
        }
        if (!tabuleiro.posicaoExiste(posicaoFinal)) {
            throw new ExcecaoTabuleiro("A posição informada está fora do tabuleiro");
        }
        if (movimentosPossiveis()[posicaoFinal.getPosicaoX()][posicaoFinal.getPosicaoY()]) {//  * pode mover pra posição informada
            this.p = posicaoFinal;
        }
    }

    public boolean[][] movimentosPossiveis() {
        boolean[][] movimentosPossiveis = new boolean[tabuleiro.getTamanhoHorizontal()][tabuleiro.getTamanhoVertical()];
        if (this.cor == Cor.BRANCO) {//  * gerando movimentos possiveis para uma peças brancas
            if (podeMoverSemCapturar(true)) {// * pode mover pra direita sem capturar
                movimentosPossiveis[this.p.getPosicaoX() + 1][this.p.getPosicaoY() - 1] = true;
            }
            if (podeMoverSemCapturar(false)) {// * pode mover pra esquerda sem capturar
                movimentosPossiveis[this.p.getPosicaoX() - 1][this.p.getPosicaoY() - 1] = true;
            }
            if (podeCapturarDireita()) {
                movimentosPossiveis[this.p.getPosicaoX() + 2][this.p.getPosicaoY() - 2] = true;
            }
            if (podeCapturarEsquerda()) {
                movimentosPossiveis[this.p.getPosicaoX() - 2][this.p.getPosicaoY() - 2] = true;
            }
        } else {//  * gerando movimentos possiveis para uma peças preta
            if (podeMoverSemCapturar(true)) {// * pode mover pra direita sem capturar
                movimentosPossiveis[this.p.getPosicaoX() - 1][this.p.getPosicaoY() + 1] = true;
            }
            if (podeMoverSemCapturar(false)) {// * pode mover pra esquerda sem capturar
                movimentosPossiveis[this.p.getPosicaoX() + 1][this.p.getPosicaoY() - 1] = true;
            }
            if (podeCapturarDireita()) {
                movimentosPossiveis[this.p.getPosicaoX() - 2][this.p.getPosicaoY() - 2] = true;
            }
            if (podeCapturarEsquerda()) {
                movimentosPossiveis[this.p.getPosicaoX() + 2][this.p.getPosicaoY() - 2] = true;
            }
        }

        return movimentosPossiveis;
    }

    public boolean posicaoEstaVazia(Posicao p) {
        return ((tabuleiro.posicaoExiste(p)) && (tabuleiro.getCasas()[p.getPosicaoX()][p.getPosicaoY()].getCor() == Cor.VAZIO));//    * posicao existe e não tem nenhuma peça nesta posição
    }

    public boolean podeMover() {
        return podeMoverSemCapturar(true) //    * pode mover pra direita
                || podeMoverSemCapturar(false)//    * pode mover pra esquerda
                || podeCapturarDireita()//  * pode capturar pra direita
                || podeCapturarEsquerda();// * pode capturar pra esquerda
    }

    public boolean podeMoverSemCapturar(boolean moverPraDireita) {

        if (moverPraDireita) {//    * mover pra direita
            if (this.cor == Cor.BRANCO) {//    * é uma peça branca
                return tabuleiro.getCasas()[this.p.getPosicaoX() + 1][this.p.getPosicaoY() - 1].getCor() == Cor.VAZIO;
            } else {//  * é uma peça preta
                return tabuleiro.getCasas()[this.p.getPosicaoX() - 1][this.p.getPosicaoY() + 1].getCor() == Cor.VAZIO;
            }
        } else {//    * mover pra esquerda
            if (this.cor == Cor.BRANCO) {//    * é uma peça branca
                return tabuleiro.getCasas()[this.p.getPosicaoX() - 1][this.p.getPosicaoY() - 1].getCor() == Cor.VAZIO;
            } else {//  * é uma peça preta
                return tabuleiro.getCasas()[this.p.getPosicaoX() + 1][this.p.getPosicaoY() + 1].getCor() == Cor.VAZIO;
            }
        }
    }

    private boolean podeCapturarDireita() {
        if (this.cor == Cor.BRANCO) {
            Posicao posicaoFinal = new Posicao(this.p.getPosicaoX() + 2, this.p.getPosicaoY() - 2);//   * mover para diagonal direita superior
            return tabuleiro.getCasas()[posicaoFinal.getPosicaoX() - 1][posicaoFinal.getPosicaoY() + 1].getCor() == Cor.PRETA//    * tem uma peça adversária nessa posicao
                    && tabuleiro.getCasas()[posicaoFinal.getPosicaoX()][posicaoFinal.getPosicaoY()].getCor() == Cor.VAZIO;//  * não tem nenhuma peça na posição final
        } else {//    * é uma peça preta
            Posicao posicaoFinal = new Posicao(this.p.getPosicaoX() - 2, this.p.getPosicaoY() + 2);//   * mover para diagonal direita inferior
            return tabuleiro.getCasas()[posicaoFinal.getPosicaoX() + 1][posicaoFinal.getPosicaoY() - 1].getCor() == Cor.BRANCO//    * tem uma peça adversária nessa posicao
                    && tabuleiro.getCasas()[posicaoFinal.getPosicaoX()][posicaoFinal.getPosicaoY()].getCor() == Cor.VAZIO;//  * não tem nenhuma peça na posição final
        }
    }

    private boolean podeCapturarEsquerda() {
        if (this.cor == Cor.BRANCO) {
            Posicao posicaoFinal = new Posicao(this.p.getPosicaoX() - 2, this.p.getPosicaoY() - 2);//   * mover para diagonal direita superior
            return tabuleiro.getCasas()[posicaoFinal.getPosicaoX() + 1][posicaoFinal.getPosicaoY() + 1].getCor() == Cor.PRETA//    * tem uma peça adversária nessa posicao
                    && tabuleiro.getCasas()[posicaoFinal.getPosicaoX()][posicaoFinal.getPosicaoY()].getCor() == Cor.VAZIO;//  * não tem nenhuma peça na posição final
        } else {//    * é uma peça preta
            Posicao posicaoFinal = new Posicao(this.p.getPosicaoX() - 2, this.p.getPosicaoY() + 2);//   * mover para diagonal direita superior
            return tabuleiro.getCasas()[posicaoFinal.getPosicaoX() + 1][posicaoFinal.getPosicaoY() - 1].getCor() == Cor.BRANCO//    * tem uma peça adversária nessa posicao
                    && tabuleiro.getCasas()[posicaoFinal.getPosicaoX()][posicaoFinal.getPosicaoY()].getCor() == Cor.VAZIO;//  * não tem nenhuma peça na posição final
        }
    }
}
