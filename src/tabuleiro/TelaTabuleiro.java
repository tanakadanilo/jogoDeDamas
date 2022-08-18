/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package tabuleiro;

import Pecas.Cor;
import Pecas.Peao;
import Pecas.Peca;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import jogo.ExcecaoRegraDoJogo;

/**
 *
 * @author tanak
 */
public class TelaTabuleiro extends javax.swing.JFrame {

    ArrayList<JButton> listaBotoes = new ArrayList<>();
    Tabuleiro tabuleiro = new Tabuleiro(8, 8);
    Peca pecaCapturada = null;
    boolean mover = false;
    Posicao posicaoInicial;

    /**
     * Creates new form NewJFrame
     */
    public TelaTabuleiro() {
        initComponents();
        loadBotoes();
//        mostraTabuleiro();
        preencheTabuleiro();
        montaPecas();
    }

    private boolean capturou(Posicao inicio, Posicao fim) {
        return (inicio.getPosicaoX() - fim.getPosicaoX() == 2) || (inicio.getPosicaoX() - fim.getPosicaoX() == (-2));// * se moveu 2 casas é porque capturou
    }

    private void capturaPeca(Posicao posicaoInicial, Posicao posicaoFinal) {
        Posicao posicaoCaptura = null;
        int posicaoX = 0;
        int posicaoY = 0;
        if (posicaoFinal.getPosicaoX() - posicaoInicial.getPosicaoX() == 2) {//    * moveu pra baixo
            posicaoX = posicaoFinal.getPosicaoX() - 1;
        } else {//    * moveu pra cima
            posicaoX = posicaoFinal.getPosicaoX() + 1;
        }
        if (posicaoFinal.getPosicaoY() - posicaoInicial.getPosicaoY() == 2) {//   * moveu pra direita do tabuleiro
            posicaoY = posicaoFinal.getPosicaoY() - 1;
        } else {//   * moveu pra esquerda do tabuleiro
            posicaoY = posicaoFinal.getPosicaoY() + 1;
        }
        posicaoCaptura = new Posicao(posicaoX, posicaoY);
        pecaCapturada = tabuleiro.getCasas()[posicaoCaptura.getPosicaoX()][posicaoCaptura.getPosicaoY()];// * salvando a peça como capturada
        tabuleiro.getCasas()[posicaoCaptura.getPosicaoX()][posicaoCaptura.getPosicaoY()] = null;//  * tirando peça do tabuleiro
    }

    private void mover(Posicao inicio, Posicao fim) {

        try {
            if (tabuleiro.getCasas()[inicio.getPosicaoX()][inicio.getPosicaoY()] == null) {//    * não escolheu peça inicial
                mover = false;
                throw new ExcecaoTabuleiro("Escolha a peça que deseja mover");
            }
            pecaCapturada = tabuleiro.getCasas()[fim.getPosicaoX()][fim.getPosicaoY()];
            tabuleiro.getCasas()[inicio.getPosicaoX()][inicio.getPosicaoY()].mover(fim);
            this.mover = false;
            if (capturou(inicio, fim)) {
                capturaPeca(posicaoInicial, fim);
            }
            montaPecas();
        } catch (ExcecaoTabuleiro | ExcecaoRegraDoJogo ex) {
            mover = false;
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
            Logger.getLogger(TelaTabuleiro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void montaPecas() {
        mostraTabuleiro();
        Peca[][] casas = tabuleiro.getCasas();
        for (var linha : casas) {
            for (var casa : linha) {//  * percorrendo todo o tabuleiro
                if (casa != null) {//   * casa não vazia
                    if (casa.getCor() == Cor.BRANCO) {
                        Posicao p = casa.getPosicao();
                        listaBotoes.get(((p.getPosicaoX() * 8) + p.getPosicaoY())).setIcon(new ImageIcon("src/assets/peao_branco_fundo_preto3.jpeg"));
                    } else {
                        Posicao p = casa.getPosicao();
                        listaBotoes.get(((p.getPosicaoX() * 8) + p.getPosicaoY())).setIcon(new ImageIcon("src/assets/peca_amarela_fundo_preto2.jpeg"));
                    }
                }
            }
        }
    }

    private void preencheTabuleiro() {

        try {
            Posicao p = new Posicao(7, 1);
            tabuleiro.adicionaPeca(new Peao(7, 7, tabuleiro, Cor.BRANCO));
            tabuleiro.adicionaPeca(new Peao(7, 5, tabuleiro, Cor.BRANCO));
            tabuleiro.adicionaPeca(new Peao(7, 3, tabuleiro, Cor.BRANCO));
            tabuleiro.adicionaPeca(new Peao(7, 1, tabuleiro, Cor.BRANCO));
            tabuleiro.adicionaPeca(new Peao(6, 6, tabuleiro, Cor.BRANCO));
            tabuleiro.adicionaPeca(new Peao(6, 4, tabuleiro, Cor.BRANCO));
            tabuleiro.adicionaPeca(new Peao(6, 2, tabuleiro, Cor.BRANCO));
            tabuleiro.adicionaPeca(new Peao(6, 0, tabuleiro, Cor.BRANCO));

            tabuleiro.adicionaPeca(new Peao(0, 0, tabuleiro, Cor.PRETA));
            tabuleiro.adicionaPeca(new Peao(0, 2, tabuleiro, Cor.PRETA));
            tabuleiro.adicionaPeca(new Peao(0, 4, tabuleiro, Cor.PRETA));
            tabuleiro.adicionaPeca(new Peao(0, 6, tabuleiro, Cor.PRETA));
            tabuleiro.adicionaPeca(new Peao(1, 1, tabuleiro, Cor.PRETA));
            tabuleiro.adicionaPeca(new Peao(1, 3, tabuleiro, Cor.PRETA));
            tabuleiro.adicionaPeca(new Peao(1, 5, tabuleiro, Cor.PRETA));
            tabuleiro.adicionaPeca(new Peao(1, 7, tabuleiro, Cor.PRETA));

        } catch (ExcecaoTabuleiro ex) {
            Logger.getLogger(TelaTabuleiro.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadBotoes() {
        Component[] compsPanel = jPanel1.getComponents();
        for (var c : compsPanel) {
            if (c instanceof JButton jButton) {
                listaBotoes.add(jButton);
                jButton.setIcon(new javax.swing.ImageIcon("src/assets/fundo_preto.jpeg"));
            }
        }
        Comparator c = (o1, o2) -> {

            JButton bt1 = (JButton) o1;
            JButton bt2 = (JButton) o2;
            if (bt1.getLocation().getY() == bt2.getLocation().getY()) {// * mesma altura
                return (int) (bt1.getLocation().getX() - bt2.getLocation().getX());
            } else {
                return (int) (bt1.getLocation().getY() - bt2.getLocation().getY());
            }
        };

        listaBotoes.sort(c);
    }

    private void mostraTabuleiro() {

        String caminho1 = "src/assets/fundo_preto.jpeg";
        String caminho2 = "src/assets/fundo_branco.jpeg";
        for (int i = 0; i < listaBotoes.size(); i++) {
            if (i % 8 != 0) {
                String aux = caminho1;
                caminho1 = caminho2;
                caminho2 = aux;
            }
            listaBotoes.get(i).setIcon(new javax.swing.ImageIcon(caminho1));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton_f3 = new javax.swing.JButton();
        jButton_f4 = new javax.swing.JButton();
        jButton_f5 = new javax.swing.JButton();
        jButton_f6 = new javax.swing.JButton();
        jButton_f7 = new javax.swing.JButton();
        jButton_f8 = new javax.swing.JButton();
        jButton_g1 = new javax.swing.JButton();
        jButton_g2 = new javax.swing.JButton();
        jButton_g3 = new javax.swing.JButton();
        jButton_g4 = new javax.swing.JButton();
        jButton_b1 = new javax.swing.JButton();
        jButton_g5 = new javax.swing.JButton();
        jButton_b2 = new javax.swing.JButton();
        jButton_g6 = new javax.swing.JButton();
        jButton_b3 = new javax.swing.JButton();
        jButton_g7 = new javax.swing.JButton();
        jButton_b4 = new javax.swing.JButton();
        jButton_g8 = new javax.swing.JButton();
        jButton_b5 = new javax.swing.JButton();
        jButton_h1 = new javax.swing.JButton();
        jButton_b6 = new javax.swing.JButton();
        jButton_h2 = new javax.swing.JButton();
        jButton_b7 = new javax.swing.JButton();
        jButton_b8 = new javax.swing.JButton();
        jButton_c1 = new javax.swing.JButton();
        jButton_c2 = new javax.swing.JButton();
        jButton_a1 = new javax.swing.JButton();
        jButton_a2 = new javax.swing.JButton();
        jButton_a3 = new javax.swing.JButton();
        jButton_a4 = new javax.swing.JButton();
        jButton_a5 = new javax.swing.JButton();
        jButton_a6 = new javax.swing.JButton();
        jButton_a7 = new javax.swing.JButton();
        jButton_a8 = new javax.swing.JButton();
        jButton_h3 = new javax.swing.JButton();
        jButton_h4 = new javax.swing.JButton();
        jButton_h5 = new javax.swing.JButton();
        jButton_h6 = new javax.swing.JButton();
        jButton_c3 = new javax.swing.JButton();
        jButton_h7 = new javax.swing.JButton();
        jButton_c4 = new javax.swing.JButton();
        jButton_h8 = new javax.swing.JButton();
        jButton_c5 = new javax.swing.JButton();
        jButton_c6 = new javax.swing.JButton();
        jButton_c7 = new javax.swing.JButton();
        jButton_c8 = new javax.swing.JButton();
        jButton_d1 = new javax.swing.JButton();
        jButton_d2 = new javax.swing.JButton();
        jButton_d3 = new javax.swing.JButton();
        jButton_d4 = new javax.swing.JButton();
        jButton_d5 = new javax.swing.JButton();
        jButton_d6 = new javax.swing.JButton();
        jButton_d7 = new javax.swing.JButton();
        jButton_d8 = new javax.swing.JButton();
        jButton_e1 = new javax.swing.JButton();
        jButton_e2 = new javax.swing.JButton();
        jButton_e3 = new javax.swing.JButton();
        jButton_e4 = new javax.swing.JButton();
        jButton_e5 = new javax.swing.JButton();
        jButton_e6 = new javax.swing.JButton();
        jButton_e7 = new javax.swing.JButton();
        jButton_e8 = new javax.swing.JButton();
        jButton_f1 = new javax.swing.JButton();
        jButton_f2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton_f4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_f4ActionPerformed(evt);
            }
        });

        jButton_f6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_f6ActionPerformed(evt);
            }
        });

        jButton_f8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_f8ActionPerformed(evt);
            }
        });

        jButton_g1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_g1ActionPerformed(evt);
            }
        });

        jButton_g3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_g3ActionPerformed(evt);
            }
        });

        jButton_g5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_g5ActionPerformed(evt);
            }
        });

        jButton_b2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_b2ActionPerformed(evt);
            }
        });

        jButton_g7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_g7ActionPerformed(evt);
            }
        });

        jButton_b4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_b4ActionPerformed(evt);
            }
        });

        jButton_b6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_b6ActionPerformed(evt);
            }
        });

        jButton_h2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_h2ActionPerformed(evt);
            }
        });

        jButton_b8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_b8ActionPerformed(evt);
            }
        });

        jButton_c1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_c1ActionPerformed(evt);
            }
        });

        jButton_a1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_a1ActionPerformed(evt);
            }
        });

        jButton_a3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_a3ActionPerformed(evt);
            }
        });

        jButton_a5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_a5ActionPerformed(evt);
            }
        });

        jButton_a7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_a7ActionPerformed(evt);
            }
        });

        jButton_h3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_h3ActionPerformed(evt);
            }
        });

        jButton_h4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_h4ActionPerformed(evt);
            }
        });

        jButton_h6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_h6ActionPerformed(evt);
            }
        });

        jButton_c3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_c3ActionPerformed(evt);
            }
        });

        jButton_h8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_h8ActionPerformed(evt);
            }
        });

        jButton_c5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_c5ActionPerformed(evt);
            }
        });

        jButton_c7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_c7ActionPerformed(evt);
            }
        });

        jButton_d2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_d2ActionPerformed(evt);
            }
        });

        jButton_d4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_d4ActionPerformed(evt);
            }
        });

        jButton_d6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_d6ActionPerformed(evt);
            }
        });

        jButton_d8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_d8ActionPerformed(evt);
            }
        });

        jButton_e1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_e1ActionPerformed(evt);
            }
        });

        jButton_e3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_e3ActionPerformed(evt);
            }
        });

        jButton_e5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_e5ActionPerformed(evt);
            }
        });

        jButton_e7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_e7ActionPerformed(evt);
            }
        });

        jButton_f2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_f2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton_h1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_h2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_h3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_h4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_h5, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_h6, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_h7, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_h8, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton_a1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_a2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_a3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_a4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton_b1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_b2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_b3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_b4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton_c1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_c2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_c3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_c4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton_d1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_d2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_d3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_d4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton_e1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_e2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_e3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_e4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton_f1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_f2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_f3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_f4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton_g1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_g2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_g3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_g4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton_a5, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_a6, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_a7, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_a8, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton_b5, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_b6, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_b7, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_b8, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton_c5, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_c6, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_c7, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_c8, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton_d5, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_d6, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_d7, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_d8, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton_e5, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_e6, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_e7, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_e8, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton_f5, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_f6, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_f7, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_f8, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton_g5, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_g6, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_g7, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_g8, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton_a8, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_a7, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_a6, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_a5, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton_b5, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_b6, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_b7, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_b8, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton_a4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_a3, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_a2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_a1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton_b1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_b2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_b3, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_b4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton_c1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_c2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_c3, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_c4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_c5, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_c6, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_c7, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_c8, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton_d1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_d2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_d3, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_d4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_d5, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_d6, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_d7, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_d8, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton_e2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_e1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_e4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_e3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_e6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_e5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_e8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_e7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton_f1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_f2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_f3, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_f4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_f5, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_f6, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_f7, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_f8, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton_g1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_g2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_g3, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_g4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_g5, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_g6, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_g7, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_g8, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton_h1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_h2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_h3, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_h4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_h5, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_h6, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_h7, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_h8, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_a1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_a1ActionPerformed
        Posicao PosicaoThis = new Posicao(0, 0);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_a1ActionPerformed

    private void jButton_a3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_a3ActionPerformed
        Posicao PosicaoThis = new Posicao(0, 2);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_a3ActionPerformed

    private void jButton_a5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_a5ActionPerformed
        Posicao PosicaoThis = new Posicao(0, 4);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_a5ActionPerformed

    private void jButton_a7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_a7ActionPerformed
        Posicao PosicaoThis = new Posicao(0, 6);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_a7ActionPerformed

    private void jButton_b2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_b2ActionPerformed
        Posicao PosicaoThis = new Posicao(1, 1);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_b2ActionPerformed

    private void jButton_b4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_b4ActionPerformed
        Posicao PosicaoThis = new Posicao(1, 3);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_b4ActionPerformed

    private void jButton_b6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_b6ActionPerformed
        Posicao PosicaoThis = new Posicao(1, 5);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_b6ActionPerformed

    private void jButton_b8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_b8ActionPerformed
        Posicao PosicaoThis = new Posicao(1, 7);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_b8ActionPerformed

    private void jButton_c1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_c1ActionPerformed
        Posicao PosicaoThis = new Posicao(2, 0);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_c1ActionPerformed

    private void jButton_c3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_c3ActionPerformed
        Posicao PosicaoThis = new Posicao(2, 2);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_c3ActionPerformed

    private void jButton_c5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_c5ActionPerformed
        Posicao PosicaoThis = new Posicao(2, 4);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_c5ActionPerformed

    private void jButton_c7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_c7ActionPerformed
        Posicao PosicaoThis = new Posicao(2, 6);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_c7ActionPerformed

    private void jButton_d2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_d2ActionPerformed
        Posicao PosicaoThis = new Posicao(3, 1);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_d2ActionPerformed

    private void jButton_d4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_d4ActionPerformed
        Posicao PosicaoThis = new Posicao(3, 3);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_d4ActionPerformed

    private void jButton_d6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_d6ActionPerformed
        Posicao PosicaoThis = new Posicao(3, 5);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_d6ActionPerformed

    private void jButton_d8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_d8ActionPerformed
        Posicao PosicaoThis = new Posicao(3, 7);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_d8ActionPerformed

    private void jButton_e1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_e1ActionPerformed
        Posicao PosicaoThis = new Posicao(4, 0);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_e1ActionPerformed

    private void jButton_e3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_e3ActionPerformed
        Posicao PosicaoThis = new Posicao(4, 2);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_e3ActionPerformed

    private void jButton_e5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_e5ActionPerformed
        Posicao PosicaoThis = new Posicao(4, 4);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_e5ActionPerformed

    private void jButton_e7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_e7ActionPerformed
        Posicao PosicaoThis = new Posicao(4, 6);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_e7ActionPerformed

    private void jButton_f2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_f2ActionPerformed
        Posicao PosicaoThis = new Posicao(5, 1);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_f2ActionPerformed

    private void jButton_f4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_f4ActionPerformed
        Posicao PosicaoThis = new Posicao(5, 3);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_f4ActionPerformed

    private void jButton_f6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_f6ActionPerformed
        Posicao PosicaoThis = new Posicao(5, 5);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_f6ActionPerformed

    private void jButton_f8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_f8ActionPerformed
        Posicao PosicaoThis = new Posicao(5, 7);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_f8ActionPerformed

    private void jButton_g1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_g1ActionPerformed
        Posicao PosicaoThis = new Posicao(6, 0);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_g1ActionPerformed

    private void jButton_g3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_g3ActionPerformed
        Posicao PosicaoThis = new Posicao(6, 2);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_g3ActionPerformed

    private void jButton_g5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_g5ActionPerformed
        Posicao PosicaoThis = new Posicao(6, 4);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_g5ActionPerformed

    private void jButton_g7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_g7ActionPerformed
        Posicao PosicaoThis = new Posicao(6, 6);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_g7ActionPerformed

    private void jButton_h3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_h3ActionPerformed
        Posicao PosicaoThis = new Posicao(7, 2);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_h3ActionPerformed

    private void jButton_h2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_h2ActionPerformed
        Posicao PosicaoThis = new Posicao(7, 1);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_h2ActionPerformed

    private void jButton_h4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_h4ActionPerformed
        Posicao PosicaoThis = new Posicao(7, 3);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_h4ActionPerformed

    private void jButton_h6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_h6ActionPerformed
        Posicao PosicaoThis = new Posicao(7, 5);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_h6ActionPerformed

    private void jButton_h8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_h8ActionPerformed
        Posicao PosicaoThis = new Posicao(7, 7);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_h8ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaTabuleiro.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaTabuleiro.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaTabuleiro.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaTabuleiro.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaTabuleiro().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_a1;
    private javax.swing.JButton jButton_a2;
    private javax.swing.JButton jButton_a3;
    private javax.swing.JButton jButton_a4;
    private javax.swing.JButton jButton_a5;
    private javax.swing.JButton jButton_a6;
    private javax.swing.JButton jButton_a7;
    private javax.swing.JButton jButton_a8;
    private javax.swing.JButton jButton_b1;
    private javax.swing.JButton jButton_b2;
    private javax.swing.JButton jButton_b3;
    private javax.swing.JButton jButton_b4;
    private javax.swing.JButton jButton_b5;
    private javax.swing.JButton jButton_b6;
    private javax.swing.JButton jButton_b7;
    private javax.swing.JButton jButton_b8;
    private javax.swing.JButton jButton_c1;
    private javax.swing.JButton jButton_c2;
    private javax.swing.JButton jButton_c3;
    private javax.swing.JButton jButton_c4;
    private javax.swing.JButton jButton_c5;
    private javax.swing.JButton jButton_c6;
    private javax.swing.JButton jButton_c7;
    private javax.swing.JButton jButton_c8;
    private javax.swing.JButton jButton_d1;
    private javax.swing.JButton jButton_d2;
    private javax.swing.JButton jButton_d3;
    private javax.swing.JButton jButton_d4;
    private javax.swing.JButton jButton_d5;
    private javax.swing.JButton jButton_d6;
    private javax.swing.JButton jButton_d7;
    private javax.swing.JButton jButton_d8;
    private javax.swing.JButton jButton_e1;
    private javax.swing.JButton jButton_e2;
    private javax.swing.JButton jButton_e3;
    private javax.swing.JButton jButton_e4;
    private javax.swing.JButton jButton_e5;
    private javax.swing.JButton jButton_e6;
    private javax.swing.JButton jButton_e7;
    private javax.swing.JButton jButton_e8;
    private javax.swing.JButton jButton_f1;
    private javax.swing.JButton jButton_f2;
    private javax.swing.JButton jButton_f3;
    private javax.swing.JButton jButton_f4;
    private javax.swing.JButton jButton_f5;
    private javax.swing.JButton jButton_f6;
    private javax.swing.JButton jButton_f7;
    private javax.swing.JButton jButton_f8;
    private javax.swing.JButton jButton_g1;
    private javax.swing.JButton jButton_g2;
    private javax.swing.JButton jButton_g3;
    private javax.swing.JButton jButton_g4;
    private javax.swing.JButton jButton_g5;
    private javax.swing.JButton jButton_g6;
    private javax.swing.JButton jButton_g7;
    private javax.swing.JButton jButton_g8;
    private javax.swing.JButton jButton_h1;
    private javax.swing.JButton jButton_h2;
    private javax.swing.JButton jButton_h3;
    private javax.swing.JButton jButton_h4;
    private javax.swing.JButton jButton_h5;
    private javax.swing.JButton jButton_h6;
    private javax.swing.JButton jButton_h7;
    private javax.swing.JButton jButton_h8;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
