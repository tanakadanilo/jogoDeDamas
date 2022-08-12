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
public class Peao extends Peca {

    private final PecasPossiveis cor;// * cor do peão, não aceitar vazio

    public Peao(Tabuleiro tabuleiro, PecasPossiveis cor, int posicaoX, int posicaoy) {
        super(posicaoX, posicaoy, tabuleiro);
        if (cor == PecasPossiveis.VAZIO) {
            throw new IllegalStateException("Tentando criar uma peça no tabuleiro que não é nem branca nem preta");
        }
        this.cor = cor;
    }

    public PecasPossiveis getCor() {
        return cor;
    }

    public void mover(boolean moverPraDireita) throws ExcecaoTabuleiro {
        //  * método para mover a peça, o parâmetro define a direção, já que um peão só pode se mover em 2 direções

        Posicao posicaoFinal;
        if (moverPraDireita) {
            posicaoFinal = new Posicao(this.p.getPosicaoX() + 1, this.p.getPosicaoY() + 1);
            if (!tabuleiro.posicaoExiste(posicaoFinal)) {//  * a posição existe
                throw new ExcecaoTabuleiro("Tentando mover uma peça para uma posição inválida");
            }
            if (!podeMover()) {//    * posição já está ocupada

            }

        }
    }

    public boolean posicaoEstaVazia(Posicao p) {
        return ((tabuleiro.posicaoExiste(p)) && (tabuleiro.getCasas()[p.getPosicaoX()][p.getPosicaoY()] == PecasPossiveis.VAZIO));//    * posicao existe e não tem nenhuma peça nesta posição
    }

    public boolean podeMover(boolean moverPraDirei) {

    }

    private boolean podeCapturarDireita() {
        if (this.cor == PecasPossiveis.BRANCO) {
            Posicao posicaoFinal = new Posicao(this.p.getPosicaoX() + 2, this.p.getPosicaoY() - 2);//   * mover para diagonal direita superior
            return tabuleiro.getCasas()[posicaoFinal.getPosicaoX() - 1][posicaoFinal.getPosicaoY() + 1] == PecasPossiveis.PRETA//    * tem uma peça adversária nessa posicao
                    && tabuleiro.getCasas()[posicaoFinal.getPosicaoX()][posicaoFinal.getPosicaoY()] == PecasPossiveis.VAZIO;//  * não tem nenhuma peça na posição final
        } else {//    * é uma peça preta
            Posicao posicaoFinal = new Posicao(this.p.getPosicaoX() - 2, this.p.getPosicaoY() - 2);//   * mover para diagonal direita inferior
            return tabuleiro.getCasas()[posicaoFinal.getPosicaoX() + 1][posicaoFinal.getPosicaoY() + 1] == PecasPossiveis.BRANCO//    * tem uma peça adversária nessa posicao
                    && tabuleiro.getCasas()[posicaoFinal.getPosicaoX()][posicaoFinal.getPosicaoY()] == PecasPossiveis.VAZIO;//  * não tem nenhuma peça na posição final
        }
    }
    private boolean podeCapturarEsquerda() {
        if (this.cor == PecasPossiveis.BRANCO) {
            Posicao posicaoFinal = new Posicao(this.p.getPosicaoX() - 2, this.p.getPosicaoY() + 2);//   * mover para diagonal direita superior
            return tabuleiro.getCasas()[posicaoFinal.getPosicaoX() + 1][posicaoFinal.getPosicaoY() - 1] == PecasPossiveis.PRETA//    * tem uma peça adversária nessa posicao
                    && tabuleiro.getCasas()[posicaoFinal.getPosicaoX()][posicaoFinal.getPosicaoY()] == PecasPossiveis.VAZIO;//  * não tem nenhuma peça na posição final
        } else {//    * é uma peça preta
            Posicao posicaoFinal = new Posicao(this.p.getPosicaoX() - 2, this.p.getPosicaoY() - 2);//   * mover para diagonal direita superior
            return tabuleiro.getCasas()[posicaoFinal.getPosicaoX() + 1][posicaoFinal.getPosicaoY() + 1] == PecasPossiveis.BRANCO//    * tem uma peça adversária nessa posicao
                    && tabuleiro.getCasas()[posicaoFinal.getPosicaoX()][posicaoFinal.getPosicaoY()] == PecasPossiveis.VAZIO;//  * não tem nenhuma peça na posição final
        }
    }
}
