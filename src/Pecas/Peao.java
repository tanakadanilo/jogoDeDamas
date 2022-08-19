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
    }

    public Peao(int posicaoX, int posicaoY, Tabuleiro tabuleiro, Cor cor) {
        super(posicaoX, posicaoY, tabuleiro, cor);
    }

    public void moverPeao(Posicao posicaoFinal) throws ExcecaoTabuleiro, ExcecaoRegraDoJogo {
        //  * método para mover a peça, o parâmetro define a direção, já que um peão só pode se mover em 2 direções

        if (!podeMover(posicaoFinal)) {//  * nenhum movimento possivel
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
            if (podeMoverDireita()) {// * pode mover pra direita sem capturar
                movimentosPossiveis[this.p.getPosicaoX() - 1][this.p.getPosicaoY() + 1] = true;
            }
            if (podeMoverEsquerda()) {// * pode mover pra esquerda sem capturar
                movimentosPossiveis[this.p.getPosicaoX() - 1][this.p.getPosicaoY() - 1] = true;
            }
            if (podeCapturarDireita()) {
                movimentosPossiveis[this.p.getPosicaoX() - 2][this.p.getPosicaoY() + 2] = true;
            }
            if (podeCapturarEsquerda()) {
                movimentosPossiveis[this.p.getPosicaoX() - 2][this.p.getPosicaoY() - 2] = true;
            }
            if (podeCapturarAtrasDireita()) {
                movimentosPossiveis[this.p.getPosicaoX() + 2][this.p.getPosicaoY() + 2] = true;
            }
            if (podeCapturarAtrasEsquerda()) {
                movimentosPossiveis[this.p.getPosicaoX() + 2][this.p.getPosicaoY() - 2] = true;
            }
        } else {//  * gerando movimentos possiveis para uma peças preta
            if (podeMoverDireita()) {// * pode mover pra direita sem capturar
                movimentosPossiveis[this.p.getPosicaoX() + 1][this.p.getPosicaoY() - 1] = true;
            }
            if (podeMoverEsquerda()) {// * pode mover pra esquerda sem capturar
                movimentosPossiveis[this.p.getPosicaoX() + 1][this.p.getPosicaoY() + 1] = true;
            }
            if (podeCapturarDireita()) {
                movimentosPossiveis[this.p.getPosicaoX() + 2][this.p.getPosicaoY() - 2] = true;
            }
            if (podeCapturarEsquerda()) {
                movimentosPossiveis[this.p.getPosicaoX() + 2][this.p.getPosicaoY() + 2] = true;
            }
            if (podeCapturarAtrasDireita()) {
                movimentosPossiveis[this.p.getPosicaoX() - 2][this.p.getPosicaoY() - 2] = true;
            }
            if (podeCapturarAtrasEsquerda()) {
                movimentosPossiveis[this.p.getPosicaoX() - 2][this.p.getPosicaoY() + 2] = true;
            }
        }

        return movimentosPossiveis;
    }

    public boolean podeMoverEsquerda() {
        Posicao destino = null;
        if (this.cor == Cor.BRANCO) {//    * é uma peça branca
            destino = new Posicao(this.p.getPosicaoX() - 1, this.p.getPosicaoY() - 1);
        } else {//  * é uma peça preta
            destino = new Posicao(this.p.getPosicaoX() + 1, this.p.getPosicaoY() + 1);
        }
        if (!tabuleiro.posicaoExiste(destino)) {
            return false;
        }
        return tabuleiro.getCasas()[destino.getPosicaoX()][destino.getPosicaoY()] == null;
    }

    public boolean podeMoverDireita() {
        Posicao destino = null;
        if (this.cor == Cor.BRANCO) {//    * é uma peça branca
            destino = new Posicao(this.p.getPosicaoX() - 1, this.p.getPosicaoY() + 1);
        } else {//  * é uma peça preta
            destino = new Posicao(this.p.getPosicaoX() + 1, this.p.getPosicaoY() - 1);
        }
        if (!tabuleiro.posicaoExiste(destino)) {
            return false;
        }
        return tabuleiro.getCasas()[destino.getPosicaoX()][destino.getPosicaoY()] == null;
    }

    private boolean podeCapturarDireita() {

        Posicao posicaoFinal = null;
        Posicao posicaoCaptura = null;
        if (this.cor == Cor.BRANCO) {
            posicaoFinal = new Posicao(this.p.getPosicaoX() - 2, this.p.getPosicaoY() + 2);//   * mover para diagonal direita superior
            posicaoCaptura = new Posicao(posicaoFinal.getPosicaoX() + 1, posicaoFinal.getPosicaoY() - 1);
        } else {//    * é uma peça preta
            posicaoFinal = new Posicao(this.p.getPosicaoX() + 2, this.p.getPosicaoY() - 2);//   * mover para diagonal direita inferior
            posicaoCaptura = new Posicao(posicaoFinal.getPosicaoX() - 1, posicaoFinal.getPosicaoY() + 1);
        }
        if (!tabuleiro.posicaoExiste(posicaoFinal)) {// * posição final não existe
            return false;
        }
        if (!tabuleiro.posicaoExiste(posicaoCaptura)) {// * posição final não existe
            return false;
        }
        if (tabuleiro.getCasas()[posicaoFinal.getPosicaoX()][posicaoFinal.getPosicaoY()] != null) {//  *  tem uma peça na posição final
            return false;
        }
        if (tabuleiro.getCasas()[posicaoCaptura.getPosicaoX()][posicaoCaptura.getPosicaoY()] == null) {//    * não tem uma peça na posição para capturar
            return false;
        }

        return tabuleiro.getCasas()[posicaoCaptura.getPosicaoX()][posicaoCaptura.getPosicaoY()].getCor() != this.getCor();
    }

    private boolean podeCapturarEsquerda() {
        Posicao posicaoFinal = null;
        Posicao posicaoCaptura = null;
        if (this.cor == Cor.BRANCO) {
            posicaoFinal = new Posicao(this.p.getPosicaoX() - 2, this.p.getPosicaoY() - 2);//   * mover para diagonal esquerda superior
            posicaoCaptura = new Posicao((posicaoFinal.getPosicaoX() + 1), (posicaoFinal.getPosicaoY() + 1));
        } else {//    * é uma peça preta
            posicaoFinal = new Posicao(this.p.getPosicaoX() + 2, this.p.getPosicaoY() + 2);//   * mover para diagonal esquerda superior
            posicaoCaptura = new Posicao((posicaoFinal.getPosicaoX() - 1), (posicaoFinal.getPosicaoY() - 1));
        }
        if (!tabuleiro.posicaoExiste(posicaoFinal)) {// * posição final não existe
            return false;
        }
        if (tabuleiro.getCasas()[posicaoFinal.getPosicaoX()][posicaoFinal.getPosicaoY()] != null) {//  *  tem uma peça na posição final
            return false;
        }
        if (tabuleiro.getCasas()[posicaoCaptura.getPosicaoX()][posicaoCaptura.getPosicaoY()] == null) {//    * não tem uma peça na posição para capturar
            return false;
        }

        return tabuleiro.getCasas()[posicaoCaptura.getPosicaoX()][posicaoCaptura.getPosicaoY()].getCor() != this.getCor();
    }

    public boolean podeCapturarAtrasDireita() {
        Posicao posicaoFinal = null;
        Posicao posicaoCaptura = null;
        if (this.cor == Cor.BRANCO) {
            posicaoFinal = new Posicao(this.p.getPosicaoX() + 2, this.p.getPosicaoY() + 2);//   * mover para diagonal direita inferior
            posicaoCaptura = new Posicao((posicaoFinal.getPosicaoX() - 1), (posicaoFinal.getPosicaoY() - 1));
        } else {//    * é uma peça preta
            posicaoFinal = new Posicao(this.p.getPosicaoX() - 2, this.p.getPosicaoY() - 2);//   * mover para diagonal direita inferior
            posicaoCaptura = new Posicao((posicaoFinal.getPosicaoX() + 1), (posicaoFinal.getPosicaoY() + 1));
        }
        if (!tabuleiro.posicaoExiste(posicaoFinal)) {// * posição final não existe
            return false;
        }
        if (tabuleiro.getCasas()[posicaoFinal.getPosicaoX()][posicaoFinal.getPosicaoY()] != null) {//  *  tem uma peça na posição final
            return false;
        }
        if (tabuleiro.getCasas()[posicaoCaptura.getPosicaoX()][posicaoCaptura.getPosicaoY()] == null) {//    * não tem uma peça na posição para capturar
            return false;
        }
        return tabuleiro.getCasas()[posicaoCaptura.getPosicaoX()][posicaoCaptura.getPosicaoY()].getCor() != this.getCor();
    }

    public boolean podeCapturarAtrasEsquerda() {
        Posicao posicaoFinal = null;
        Posicao posicaoCaptura = null;
        if (this.cor == Cor.BRANCO) {
            posicaoFinal = new Posicao(this.p.getPosicaoX() + 2, this.p.getPosicaoY() - 2);//   * mover para diagonal esquerda inferior
            posicaoCaptura = new Posicao((posicaoFinal.getPosicaoX() - 1), (posicaoFinal.getPosicaoY() + 1));
        } else {//    * é uma peça preta
            posicaoFinal = new Posicao(this.p.getPosicaoX() - 2, this.p.getPosicaoY() + 2);//   * mover para diagonal esquerda inferior
            posicaoCaptura = new Posicao((posicaoFinal.getPosicaoX() + 1), (posicaoFinal.getPosicaoY() - 1));
        }
        if (!tabuleiro.posicaoExiste(posicaoFinal)) {// * posição final não existe
            return false;
        }
        if (tabuleiro.getCasas()[posicaoFinal.getPosicaoX()][posicaoFinal.getPosicaoY()] != null) {//  *  tem uma peça na posição final
            return false;
        }
        if (tabuleiro.getCasas()[posicaoCaptura.getPosicaoX()][posicaoCaptura.getPosicaoY()] == null) {//    * não tem uma peça na posição para capturar
            return false;
        }
        return tabuleiro.getCasas()[posicaoCaptura.getPosicaoX()][posicaoCaptura.getPosicaoY()].getCor() != this.getCor();
    }

    @Override
    public boolean podeMover(Posicao destino) {
        int posicaoXDestino = destino.getPosicaoX();
        int posicaoYDestino = destino.getPosicaoY();
        return movimentosPossiveis()[posicaoXDestino][posicaoYDestino];//   * ve se a posição de destino é um movimento válidos
    }

    @Override
    public boolean algumMovimentoPossivel() {
        boolean podeMover = false;
        boolean[][] movimentosPossiveis = movimentosPossiveis();
        for (var linha : movimentosPossiveis) {
            for (var casa : linha) {
                if (casa) {
                    podeMover = true;
                }
            }
        }
        return podeMover;
    }

}
