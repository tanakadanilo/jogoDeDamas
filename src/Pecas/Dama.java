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
public class Dama extends Peca {

    //  * a captura da dama tá buggando toda a lógica da peça
    public Dama(Posicao p, Tabuleiro tabuleiro, Cor cor) {
        super(p, tabuleiro, cor);
    }

    public Dama(int posicaoX, int posicaoY, Tabuleiro tabuleiro, Cor cor) {
        super(posicaoX, posicaoY, tabuleiro, cor);
    }

    @Override
    public boolean[][] movimentosPossiveis() {
        boolean[][] movimentosPossiveis = new boolean[tabuleiro.getTamanhoVertical()][tabuleiro.getTamanhoHorizontal()];
        Posicao destino = null;
        int variacaoX = 1;
        int variacaoY = 1;
        destino = new Posicao(this.p.getPosicaoX() + variacaoX, this.p.getPosicaoY() + variacaoY);
        while (tabuleiro.posicaoExiste(destino) && tabuleiro.getCasas()[destino.getPosicaoX()][destino.getPosicaoY()] == null) {//  * movendo pra baixo e direita sem capturar
            movimentosPossiveis[destino.getPosicaoX()][destino.getPosicaoY()] = true;// * pode se mover pra essa posição
            destino.setPosicaoX(destino.getPosicaoX() + variacaoX);
            destino.setPosicaoY(destino.getPosicaoY() + variacaoY);
        }

//  * desfazendo movimento
        destino.setPosicaoX(destino.getPosicaoX() - variacaoX);
        destino.setPosicaoY(destino.getPosicaoY() - variacaoY);
        if (podeCapturar(destino, variacaoX, variacaoY)) {
            destino.setPosicaoX(destino.getPosicaoX() + (2 * variacaoX));// * 2 casas, pois deve pular a peça inimiga para capturar
            destino.setPosicaoY(destino.getPosicaoY() + (2 * variacaoY));// * 2 casas, pois deve pular a peça inimiga para capturar
            while (tabuleiro.posicaoExiste(destino) && tabuleiro.getCasas()[destino.getPosicaoX()][destino.getPosicaoY()] == null) {
                movimentosPossiveis[destino.getPosicaoX()][destino.getPosicaoY()] = true;// * pode se mover pra essa posição
                destino.setPosicaoX(destino.getPosicaoX() + variacaoX);// * testando se pode se mover mais uma casa
                destino.setPosicaoY(destino.getPosicaoY() + variacaoY);// * testando se pode se mover mais uma casa
            }
        }

        variacaoX = 1;
        variacaoY = -1;
        destino = new Posicao(this.p.getPosicaoX() + variacaoX, this.p.getPosicaoY() + variacaoY);
        while (tabuleiro.posicaoExiste(destino) && tabuleiro.getCasas()[destino.getPosicaoX()][destino.getPosicaoY()] == null) {//  * movendo pra baixo e esquerda sem capturar
            movimentosPossiveis[destino.getPosicaoX()][destino.getPosicaoY()] = true;// * pode se mover pra essa posição
            destino.setPosicaoX(destino.getPosicaoX() + variacaoX);
            destino.setPosicaoY(destino.getPosicaoY() + variacaoY);
        }
//  * desfazendo movimento
        destino.setPosicaoX(destino.getPosicaoX() - variacaoX);
        destino.setPosicaoY(destino.getPosicaoY() - variacaoY);
        if (podeCapturar(destino, variacaoX, variacaoY)) {
            destino.setPosicaoX(destino.getPosicaoX() + (2 * variacaoX));// * 2 casas, pois deve pular a peça inimiga para capturar
            destino.setPosicaoY(destino.getPosicaoY() + (2 * variacaoY));// * 2 casas, pois deve pular a peça inimiga para capturar
            while (tabuleiro.posicaoExiste(destino) && tabuleiro.getCasas()[destino.getPosicaoX()][destino.getPosicaoY()] == null) {
                movimentosPossiveis[destino.getPosicaoX()][destino.getPosicaoY()] = true;// * pode se mover pra essa posição
                destino.setPosicaoX(destino.getPosicaoX() + variacaoX);// * testando se pode se mover mais uma casa
                destino.setPosicaoY(destino.getPosicaoY() + variacaoY);// * testando se pode se mover mais uma casa
            }
        }
        variacaoX = -1;
        variacaoY = 1;
        destino = new Posicao(this.p.getPosicaoX() + variacaoX, this.p.getPosicaoY() + variacaoY);
        while (tabuleiro.posicaoExiste(destino) && tabuleiro.getCasas()[destino.getPosicaoX()][destino.getPosicaoY()] == null) {//  * movendo pra cima e direita sem capturar
            movimentosPossiveis[destino.getPosicaoX()][destino.getPosicaoY()] = true;// * pode se mover pra essa posição
            destino.setPosicaoX(destino.getPosicaoX() + variacaoX);
            destino.setPosicaoY(destino.getPosicaoY() + variacaoY);
        }
//  * desfazendo movimento
        destino.setPosicaoX(destino.getPosicaoX() - variacaoX);
        destino.setPosicaoY(destino.getPosicaoY() - variacaoY);
        if (podeCapturar(destino, variacaoX, variacaoY)) {
            destino.setPosicaoX(destino.getPosicaoX() + (2 * variacaoX));// * 2 casas, pois deve pular a peça inimiga para capturar
            destino.setPosicaoY(destino.getPosicaoY() + (2 * variacaoY));// * 2 casas, pois deve pular a peça inimiga para capturar
            while (tabuleiro.posicaoExiste(destino) && tabuleiro.getCasas()[destino.getPosicaoX()][destino.getPosicaoY()] == null) {
                movimentosPossiveis[destino.getPosicaoX()][destino.getPosicaoY()] = true;// * pode se mover pra essa posição
                destino.setPosicaoX(destino.getPosicaoX() + variacaoX);// * testando se pode se mover mais uma casa
                destino.setPosicaoY(destino.getPosicaoY() + variacaoY);// * testando se pode se mover mais uma casa
            }
        }
        variacaoX = -1;
        variacaoY = -1;
        destino = new Posicao(this.p.getPosicaoX() + variacaoX, this.p.getPosicaoY() + variacaoY);
        while (tabuleiro.posicaoExiste(destino) && tabuleiro.getCasas()[destino.getPosicaoX()][destino.getPosicaoY()] == null) {//  * movendo pra cima e esquerda sem capturar
            movimentosPossiveis[destino.getPosicaoX()][destino.getPosicaoY()] = true;// * pode se mover pra essa posição
            destino.setPosicaoX(destino.getPosicaoX() + variacaoX);
            destino.setPosicaoY(destino.getPosicaoY() + variacaoY);
        }
//  * desfazendo movimento
        destino.setPosicaoX(destino.getPosicaoX() - variacaoX);
        destino.setPosicaoY(destino.getPosicaoY() - variacaoY);
        if (podeCapturar(destino, variacaoX, variacaoY)) {
            destino.setPosicaoX(destino.getPosicaoX() + (2 * variacaoX));// * 2 casas, pois deve pular a peça inimiga para capturar
            destino.setPosicaoY(destino.getPosicaoY() + (2 * variacaoY));// * 2 casas, pois deve pular a peça inimiga para capturar
            while (tabuleiro.posicaoExiste(destino) && tabuleiro.getCasas()[destino.getPosicaoX()][destino.getPosicaoY()] == null) {
                movimentosPossiveis[destino.getPosicaoX()][destino.getPosicaoY()] = true;// * pode se mover pra essa posição
                destino.setPosicaoX(destino.getPosicaoX() + variacaoX);// * testando se pode se mover mais uma casa
                destino.setPosicaoY(destino.getPosicaoY() + variacaoY);// * testando se pode se mover mais uma casa
            }
        }
        return movimentosPossiveis;
    }

    public boolean podeCapturar(Posicao posicaoAtual, int variacaoX, int variacaoY) {
//        for (var e : tabuleiro.getCasas()) {
//            for (var r : e) {
//                if (r == null) {
//                    System.out.print("- " + "\t");
//                } else {
//
//                    System.out.print(r.getClass() + "\t");
//                }
//            }
//            System.out.println("");
//        }

        Posicao aux = new Posicao(posicaoAtual.getPosicaoX(), posicaoAtual.getPosicaoY());
        aux.setPosicaoX(aux.getPosicaoX() + variacaoX);
        aux.setPosicaoY(aux.getPosicaoY() + variacaoY);
        if (!tabuleiro.posicaoExiste(aux)) {
            return false;
        }
        if (tabuleiro.getCasas()[aux.getPosicaoX()][aux.getPosicaoY()] == null) {
            return false;
        }
        if (tabuleiro.getCasas()[aux.getPosicaoX()][aux.getPosicaoY()].getCor() == this.cor) {// * peca aliada na frente
            return false;
        }
        aux.setPosicaoX(aux.getPosicaoX() + variacaoX);
        aux.setPosicaoY(aux.getPosicaoY() + variacaoY);
        if (!tabuleiro.posicaoExiste(aux)) {
            return false;
        }
        return tabuleiro.getCasas()[aux.getPosicaoX()][aux.getPosicaoY()] == null;
    }

    private boolean algumaCapturaPossivel(Posicao posicaoAtual) {

        boolean possivel = false;
        if (podeCapturar(posicaoAtual, 1, 1)) {
            possivel = true;
        }
        if (podeCapturar(posicaoAtual, 1, -1)) {
            possivel = true;
        }
        if (podeCapturar(posicaoAtual, -1, -1)) {
            possivel = true;
        }
        if (podeCapturar(posicaoAtual, -1, 1)) {
            possivel = true;
        }
        return possivel;
    }

    @Override
    public boolean podeMover(Posicao destino) {
        for (var linha : movimentosPossiveis()) {
            for (var casa : linha) {
                if (casa) {
                    System.out.print("+\t");
                } else {
                    System.out.print("-\t");
                }
            }
            System.out.println("");
        }
        return movimentosPossiveis()[destino.getPosicaoX()][destino.getPosicaoY()];
    }

    @Override
    public boolean algumMovimentoPossivel() {
        boolean[][] movimentosPossiveis = movimentosPossiveis();
        for (var linha : movimentosPossiveis) {
            for (var casa : linha) {
                if (casa) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean podeContinuarCapturando(Posicao inicio) throws ExcecaoTabuleiro {
        return algumaCapturaPossivel(inicio);
    }

}
