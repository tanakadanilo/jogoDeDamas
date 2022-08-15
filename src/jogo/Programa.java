/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jogo;

import Pecas.Cor;
import Pecas.Peca;
import tabuleiro.ExcecaoTabuleiro;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;

/**
 *
 * @author tanak
 */
public class Programa {

    private static tabuleiro.Tabuleiro tabuleiro = new Tabuleiro(8, 8);

    public static void main(String[] args) throws ExcecaoTabuleiro {
        Posicao p = new Posicao(7, 7);
        Peca vazia = new Peca(p, tabuleiro, Cor.BRANCO);
        tabuleiro.adicionaPeca(vazia);
        p = new Posicao(p.getPosicaoX() - 1, p.getPosicaoY() - 1);
        vazia.mover(p);
        p = new Posicao(p.getPosicaoX() - 1, p.getPosicaoY() - 1);
        vazia.mover(p);

        Peca[][] casas = tabuleiro.getCasas();
        int cont = 0;
        for (var i : casas) {
            for (var j : i) {
                if (j == null) {
                    System.out.print("- ");
                    cont++;
                } else if (j.getCor() == Cor.BRANCO) {
                    System.out.print("B ");
                    cont++;
                }
                if (cont == 8) {
                    cont = 0;
                    System.out.println();
                }
            }
        }
    }
}
