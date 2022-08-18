/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;

/**
 *
 * @author tanak
 */
public class Dama extends Peca {

    public Dama(Posicao p, Tabuleiro tabuleiro, Cor cor) {
        super(p, tabuleiro, cor);
    }

    public Dama(int posicaoX, int posicaoY, Tabuleiro tabuleiro, Cor cor) {
        super(posicaoX, posicaoY, tabuleiro, cor);
    }

    private boolean[][] movimentosPossiveis() {
        boolean[][] movimentosPossiveis = new boolean[tabuleiro.getTamanhoVertical()][tabuleiro.getTamanhoHorizontal()];
        Posicao destino = null;
        destino = new Posicao(this.p.getPosicaoX() + 1, this.p.getPosicaoY() + 1);
        while (tabuleiro.posicaoExiste(destino) && tabuleiro.getCasas()[destino.getPosicaoX()][destino.getPosicaoY()] == null) {//  * movendo pra baixo e direita sem capturar
            movimentosPossiveis[destino.getPosicaoX()][destino.getPosicaoY()] = true;// * pode se mover pra essa posição
            destino.setPosicaoX(destino.getPosicaoX() + 1);
            destino.setPosicaoY(destino.getPosicaoY() + 1);
        }
        //  * iniciando testes de se pode capturar
//        if (tabuleiro.posicaoExiste(destino)) {
//            if (tabuleiro.getCasas()[destino.getPosicaoX()][destino.getPosicaoY()].getCor() != this.cor) {// * peca inimiga na frente
//                destino.setPosicaoX(destino.getPosicaoX() + 1);
//                destino.setPosicaoY(destino.getPosicaoY() + 1);
//                if (tabuleiro.posicaoExiste(destino)) {
//                    if (tabuleiro.getCasas()[destino.getPosicaoX()][destino.getPosicaoY()].getCor() == null) {// * posicao final está vazia
//                        movimentosPossiveis[destino.getPosicaoX()][destino.getPosicaoY()] = true;// * pode se mover pra essa posição
//                    }
//                }
//            }
//        }
        if (podeCapturar(destino, 1, 1)) {
            movimentosPossiveis[destino.getPosicaoX()][destino.getPosicaoY()] = true;// * pode se mover pra essa posição
        }
        destino = new Posicao(this.p.getPosicaoX() + 1, this.p.getPosicaoY() - 1);
        while (tabuleiro.posicaoExiste(destino) && tabuleiro.getCasas()[destino.getPosicaoX()][destino.getPosicaoY()] == null) {//  * movendo pra baixo e esquerda sem capturar
            movimentosPossiveis[destino.getPosicaoX()][destino.getPosicaoY()] = true;// * pode se mover pra essa posição
            destino.setPosicaoX(destino.getPosicaoX() + 1);
            destino.setPosicaoY(destino.getPosicaoY() - 1);
        }
        //  * iniciando testes de se pode capturar
//        if (tabuleiro.posicaoExiste(destino)) {
//            if (tabuleiro.getCasas()[destino.getPosicaoX()][destino.getPosicaoY()].getCor() != this.cor) {// * pode capturar
//                movimentosPossiveis[destino.getPosicaoX()][destino.getPosicaoY()] = true;// * pode se mover pra essa posição
//            }
//        }
        if (podeCapturar(destino, 1, -1)) {
            movimentosPossiveis[destino.getPosicaoX()][destino.getPosicaoY()] = true;// * pode se mover pra essa posição
        }
        destino = new Posicao(this.p.getPosicaoX() - 1, this.p.getPosicaoY() + 1);
        while (tabuleiro.posicaoExiste(destino) && tabuleiro.getCasas()[destino.getPosicaoX()][destino.getPosicaoY()] == null) {//  * movendo pra cima e direita sem capturar
            movimentosPossiveis[destino.getPosicaoX()][destino.getPosicaoY()] = true;// * pode se mover pra essa posição
            destino.setPosicaoX(destino.getPosicaoX() - 1);
            destino.setPosicaoY(destino.getPosicaoY() + 1);
        }
        //  * iniciando testes de se pode capturar
//        if (tabuleiro.posicaoExiste(destino)) {
//            if (tabuleiro.getCasas()[destino.getPosicaoX()][destino.getPosicaoY()].getCor() != this.cor) {// * pode capturar
//                movimentosPossiveis[destino.getPosicaoX()][destino.getPosicaoY()] = true;// * pode se mover pra essa posição
//            }
//        }
        if (podeCapturar(destino, -1, 1)) {
            movimentosPossiveis[destino.getPosicaoX()][destino.getPosicaoY()] = true;// * pode se mover pra essa posição
        }
        destino = new Posicao(this.p.getPosicaoX() - 1, this.p.getPosicaoY() - 1);
        while (tabuleiro.posicaoExiste(destino) && tabuleiro.getCasas()[destino.getPosicaoX()][destino.getPosicaoY()] == null) {//  * movendo pra cima e esquerda sem capturar
            movimentosPossiveis[destino.getPosicaoX()][destino.getPosicaoY()] = true;// * pode se mover pra essa posição
            destino.setPosicaoX(destino.getPosicaoX() - 1);
            destino.setPosicaoY(destino.getPosicaoY() - 1);
        }
        //  * iniciando testes de se pode capturar
//        if (tabuleiro.posicaoExiste(destino)) {
//            if (tabuleiro.getCasas()[destino.getPosicaoX()][destino.getPosicaoY()].getCor() != this.cor) {// * pode capturar
//                movimentosPossiveis[destino.getPosicaoX()][destino.getPosicaoY()] = true;// * pode se mover pra essa posição
//            }
//        }
        if (podeCapturar(destino, -1, -1)) {
            movimentosPossiveis[destino.getPosicaoX()][destino.getPosicaoY()] = true;// * pode se mover pra essa posição
        }

        return movimentosPossiveis;
    }

    public boolean podeCapturar(Posicao destino, int variacaoX, int variacaoY) {
        if (tabuleiro.posicaoExiste(destino)) {
            if (tabuleiro.getCasas()[destino.getPosicaoX()][destino.getPosicaoY()].getCor() != this.cor) {// * peca inimiga na frente
                destino.setPosicaoX(destino.getPosicaoX() + variacaoX);
                destino.setPosicaoY(destino.getPosicaoY() + variacaoY);
                if (tabuleiro.posicaoExiste(destino)) {
                    if (tabuleiro.getCasas()[destino.getPosicaoX()][destino.getPosicaoY()] == null) {// * posicao final está vazia
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean podeMover(Posicao destino) {
        return movimentosPossiveis()[destino.getPosicaoX()][destino.getPosicaoY()];
    }

}
