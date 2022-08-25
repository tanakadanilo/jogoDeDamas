/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package tabuleiro;

import Pecas.Cor;
import Pecas.Dama;
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
import jogo.ExcecaoRegraDoJogo;
import jogo.Movimentos;

/**
 *
 * @author tanak
 */
public class TelaTabuleiro extends javax.swing.JFrame {

    ArrayList<JButton> listaBotoes = new ArrayList<>();
    ArrayList<Peca> pecasCapturadas = new ArrayList<>();
    ArrayList<Movimentos> listaMovimentos = new ArrayList<>();
    Tabuleiro tabuleiro = new Tabuleiro(8, 8);
    Peca pecaCapturada = null;
    boolean mover = false;
    Posicao posicaoInicial;
    Cor turno = Cor.BRANCO;
    boolean manterJogo = true;
    boolean continuarCapturando = false;

    /**
     * Creates new form NewJFrame
     */
    public TelaTabuleiro() {
        initComponents();
        loadBotoes();
        preencheTabuleiro();
        montaPecas();
    }

    private void desfazerMovimento() {
        try {
            if (listaMovimentos.isEmpty()) {//  * não foi feito nenhum movimento ainda
                throw new ExcecaoRegraDoJogo("nenhuma jogada para ser desfeita... vc tá querendo trapacear de novo?");
            }
            Movimentos movimento = listaMovimentos.get(listaMovimentos.size() - 1);
            Peca pecaMovida = movimento.getPecaMovida();
            Peca pecaCapturada = movimento.getPecaCapturada();
            if (pecaCapturada != null) {
                tabuleiro.adicionaPeca(pecaCapturada);
                pecasCapturadas.remove(pecaCapturada);
            }
            pecaMovida.desfazerMovimento(movimento.getInicio());
            trocaTurno();
            listaMovimentos.remove(movimento);
        } catch (ExcecaoTabuleiro | ExcecaoRegraDoJogo ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
            Logger.getLogger(TelaTabuleiro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void salvaMovimentos(Peca pecaMovida, Posicao posicaoInicio, Posicao posicaoFinal, Peca pecaCapturada) {

        Movimentos movimento = new Movimentos(pecaMovida, posicaoInicio, posicaoFinal, pecaCapturada);
        listaMovimentos.add(movimento);
    }

    private void recomeçarPartida() {
        new TelaTabuleiro().setVisible(true);
        this.dispose();
    }

    private void validaFim() {
        Peca[][] todasAsPecas = tabuleiro.getCasas();
        boolean brancoVivo = false;
        boolean pretoVivo = false;
        for (var linha : todasAsPecas) {
            for (var peca : linha) {//  * passando por cada peça no tabuleiro
                if (peca != null) {
                    if (peca.algumMovimentoPossivel()) {
                        if (peca.getCor() == Cor.BRANCO) {
                            brancoVivo = true;
                        } else {
                            pretoVivo = true;
                        }
                    }
                }
            }
        }
        manterJogo = brancoVivo && pretoVivo;
        if (!manterJogo) {//  * acabou a partida

            if (!brancoVivo) {
                JOptionPane.showMessageDialog(rootPane, "O jogo acabou! O jogador branco perdeu :(");
            } else {
                JOptionPane.showMessageDialog(rootPane, "O jogo acabou! O jogador preto perdeu :(");
            }
            recomeçarPartida();
        }
    }

    private Peca promover(Peca peao) {

        if (peao == null) {
            return peao;
        }
        if (peao instanceof Dama) {
            return peao;
        }
        if (peao.getPosicao().getPosicaoX() == 0 && peao.getCor() == Cor.BRANCO) {
            peao = new Dama(peao.getPosicao(), tabuleiro, peao.getCor());
            JOptionPane.showMessageDialog(rootPane, "Promoveu");
        } else if (peao.getPosicao().getPosicaoX() == 7 && peao.getCor() == Cor.PRETA) {
            peao = new Dama(peao.getPosicao(), tabuleiro, peao.getCor());
            JOptionPane.showMessageDialog(rootPane, "Promoveu");
        }
        return peao;
    }

    private Peca rainhaCapturou(Posicao inicio, Posicao fim) {
        int variacaoX = (fim.getPosicaoX() - inicio.getPosicaoX() > 0) ? -1 : 1;
        int variacaoY = (fim.getPosicaoY() - inicio.getPosicaoY() > 0) ? -1 : 1;
        Posicao capturada = new Posicao(fim.getPosicaoX() + variacaoX, fim.getPosicaoY() + variacaoY);//    * posicao da peça q foi capturada
        while (tabuleiro.getCasas()[capturada.getPosicaoX()][capturada.getPosicaoY()] == null//  * voltando por onde a rainha passou e procurando a peça capturada
                && capturada.getPosicaoX() != inicio.getPosicaoX() && capturada.getPosicaoY() != inicio.getPosicaoY()) {//   * ainda não chegou na posição inicial
            capturada.setPosicaoX(capturada.getPosicaoX() + variacaoX);
            capturada.setPosicaoY(capturada.getPosicaoY() + variacaoY);
        }
        Peca pecaCap = tabuleiro.getCasas()[capturada.getPosicaoX()][capturada.getPosicaoY()];
        return pecaCap;// * passou por uma peça
    }

    private boolean capturou(Peca pecaAtual, Posicao inicio, Posicao fim) {
        if (pecaAtual instanceof Dama) {
            return rainhaCapturou(inicio, fim) != null;// pulou alguma peça enquanto andava
        } else {//  * é um peão
            return (inicio.getPosicaoX() - fim.getPosicaoX() >= 2) || (inicio.getPosicaoX() - fim.getPosicaoX() <= (-2));// * se moveu 2 casas e não é dama é porque capturou
        }
    }

    private void capturaPeca(Peca pecaAtual, Posicao posicaoInicial, Posicao posicaoFinal) {
        Posicao posicaoCaptura = null;
        if (pecaAtual instanceof Dama) {
            pecaCapturada = rainhaCapturou(posicaoInicial, posicaoFinal);// * salvando a peça como capturada
            posicaoCaptura = new Posicao(pecaCapturada.getPosicao().getPosicaoX(), pecaCapturada.getPosicao().getPosicaoY());
            tabuleiro.getCasas()[pecaCapturada.getPosicao().getPosicaoX()][pecaCapturada.getPosicao().getPosicaoY()] = null;//  * tirando peça do tabuleiro
        } else {//  * é um peão
            int posicaoX = 0;
            int posicaoY = 0;
            if (posicaoFinal.getPosicaoX() - posicaoInicial.getPosicaoX() >= 2) {//    * moveu pra baixo
                posicaoX = posicaoFinal.getPosicaoX() - 1;
            } else {//    * moveu pra cima
                posicaoX = posicaoFinal.getPosicaoX() + 1;
            }
            if (posicaoFinal.getPosicaoY() - posicaoInicial.getPosicaoY() >= 2) {//   * moveu pra direita do tabuleiro
                posicaoY = posicaoFinal.getPosicaoY() - 1;
            } else {//   * moveu pra esquerda do tabuleiro
                posicaoY = posicaoFinal.getPosicaoY() + 1;
            }
            posicaoCaptura = new Posicao(posicaoX, posicaoY);
            pecaCapturada = tabuleiro.getCasas()[posicaoCaptura.getPosicaoX()][posicaoCaptura.getPosicaoY()];// * salvando a peça como capturada
            tabuleiro.getCasas()[posicaoCaptura.getPosicaoX()][posicaoCaptura.getPosicaoY()] = null;//  * tirando peça do tabuleiro
            pecasCapturadas.add(pecaCapturada);//   * salvando na lista de peças capturadas
        }
    }

    private void continuarCapturando(Peca pecaSendoMovida, Posicao fim) throws ExcecaoRegraDoJogo, ExcecaoTabuleiro {
        Posicao inicio = new Posicao(pecaSendoMovida.getPosicao().getPosicaoX(), pecaSendoMovida.getPosicao().getPosicaoY());
// * salvando posição para caso tenha de desfazer o movimento
        if (pecaSendoMovida == null) {
            throw new IllegalStateException("A peça que acabou de fazer um movimento de captura, não existe");
        }
        if (pecaSendoMovida.getCor() != turno) {
            throw new IllegalStateException("Tentando continuar capturando com umma peça que não é a do turno atual, peça sendo movida: " + pecaSendoMovida);
        }
        pecaSendoMovida.mover(fim);
        this.mover = false;
        if (!capturou(pecaSendoMovida, posicaoInicial, fim)) {//  * fez um movimento que não é de captura
            pecaSendoMovida.desfazerMovimento(inicio);
            throw new ExcecaoRegraDoJogo("Está tentando fazer capturas em sequencia, por isso só é permitido capturar peças adversárias, não podendo ser nenhum outro movimento");
        }
        capturaPeca(pecaSendoMovida, posicaoInicial, fim);
    }

    private void mover(Posicao inicio, Posicao fim) {

        Peca pecaSendoMovida = tabuleiro.getCasas()[inicio.getPosicaoX()][inicio.getPosicaoY()];
        try {
            if (continuarCapturando) {
                continuarCapturando(pecaSendoMovida, fim);
            } else {//  * movimento normal
                if (pecaSendoMovida == null) {//    * não escolheu peça inicial
                    mover = false;
                    continuarCapturando = false;
                    throw new ExcecaoTabuleiro("Escolha a peça que deseja mover");
                }
                if (pecaSendoMovida.getCor() != turno) {
//  * talvez dê um bug de travar, mas acho q não, memso assim testar isso
                    throw new ExcecaoRegraDoJogo("Não é a sua vez de jogar, tava querendo trapacear, né? safado");
                }
                pecaSendoMovida.mover(fim);
                this.mover = false;
                if (capturou(pecaSendoMovida, inicio, fim)) {
                    capturaPeca(pecaSendoMovida, posicaoInicial, fim);
                } else {//  * não capturou nenhuma peça
                    pecaCapturada = null;
                }
            }
            if (pecaCapturada != null && pecaSendoMovida.podeContinuarCapturando(fim)) {
                posicaoInicial = pecaSendoMovida.getPosicao();//    * manter movimentando a mesma peça
                mover = true;
                continuarCapturando = true;
            } else {//   * só trocar de turno se não puder continuar capturando
                posicaoInicial = null;
                mover = false;
                continuarCapturando = false;
                trocaTurno();
            }
            tabuleiro.getCasas()[fim.getPosicaoX()][fim.getPosicaoY()] = promover(pecaSendoMovida);//   * ver se a peça  foi promovida
            montaPecas();
            salvaMovimentos(pecaSendoMovida, posicaoInicial, fim, pecaCapturada);
            preenchePecasCapturadas();
            validaFim();

            if (continuarCapturando) {
                mostraJogadasPossiveis(tabuleiro.getCasas()[fim.getPosicaoX()][fim.getPosicaoY()]);//   * mostrar novos movimentos possiveis
            }
        } catch (ExcecaoTabuleiro | ExcecaoRegraDoJogo ex) {
            mover = false;
            continuarCapturando = false;
            montaPecas();
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
            Logger.getLogger(TelaTabuleiro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void preenchePecasCapturadas() {

        if (pecasCapturadas.isEmpty()) {
            return;
        }
        int peoesBrancosCapturados = 0;
        int damasBrancasCapturadas = 0;
        int peoesPretosCapturados = 0;
        int damasPretasCapturadas = 0;
        for (Peca p : pecasCapturadas) {
            if (p instanceof Dama) {
                if (p.getCor() == Cor.BRANCO) {// * dama branca
                    damasBrancasCapturadas++;
                } else {//    * dama preta
                    damasPretasCapturadas++;
                }
            } else if (p instanceof Peao) {
                if (p.getCor() == Cor.BRANCO) {// * peao branco
                    peoesBrancosCapturados++;
                } else {//    * peao preto
                    peoesPretosCapturados++;
                }
            } else {
                throw new IllegalStateException("Capturou uma peça que nem é peão e nem Dama, peça capturada: " + p);
            }
        }
        jLabel_peoesBrancosCapturados.setText("Peões brancos capturados: " + peoesBrancosCapturados);
        jLabel_damasBrancasCapturadas.setText("Damas brancas capturadas: " + damasBrancasCapturadas);
        jLabel_peoesPretosCapturados.setText("Peões pretos capturados: " + peoesPretosCapturados);
        jLabel_damasPretasCapturadas.setText("Damas pretas capturadas: " + damasPretasCapturadas);
    }

    private void trocaTurno() {
        if (turno == Cor.BRANCO) {
            jLabel_turno.setIcon(new ImageIcon("src/assets/peao_amarelo_fundo_preto.jpeg"));
            turno = Cor.PRETA;
        } else {
            jLabel_turno.setIcon(new ImageIcon("src/assets/peao_branco_fundo_preto.jpeg"));
            turno = Cor.BRANCO;
        }
    }

    private void mostraJogadasPossiveis(Peca peca) {
        boolean[][] movimentosPossiveis = peca.movimentosPossiveis();
        for (int i = 0; i < movimentosPossiveis.length; i++) {
            for (int j = 0; j < movimentosPossiveis[i].length; j++) {
                if (movimentosPossiveis[i][j]) {// * é um movimento possível
                    listaBotoes.get((8 * i) + j).setIcon(new ImageIcon("src/assets/fundo_azul.jpeg"));
                }
            }
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
                        if (casa instanceof Peao) {
                            listaBotoes.get(((p.getPosicaoX() * 8) + p.getPosicaoY())).setIcon(new ImageIcon("src/assets/peao_branco_fundo_preto.jpeg"));
                        } else {//    * é uma dama
                            listaBotoes.get(((p.getPosicaoX() * 8) + p.getPosicaoY())).setIcon(new ImageIcon("src/assets/dama_branca_fundo_preto.jpeg"));
                        }
                    } else {
                        Posicao p = casa.getPosicao();
                        if (casa instanceof Peao) {
                            listaBotoes.get(((p.getPosicaoX() * 8) + p.getPosicaoY())).setIcon(new ImageIcon("src/assets/peao_amarelo_fundo_preto.jpeg"));
                        } else {//    * é uma dama
                            listaBotoes.get(((p.getPosicaoX() * 8) + p.getPosicaoY())).setIcon(new ImageIcon("src/assets/dama_amarela_fundo_preto.jpeg"));
                        }
                    }
                }
            }
        }
    }

    private void preencheTabuleiro() {

        try {
            tabuleiro.adicionaPeca(new Peao(7, 7, tabuleiro, Cor.BRANCO));
            tabuleiro.adicionaPeca(new Peao(7, 5, tabuleiro, Cor.BRANCO));
            tabuleiro.adicionaPeca(new Peao(7, 3, tabuleiro, Cor.BRANCO));
            tabuleiro.adicionaPeca(new Peao(7, 1, tabuleiro, Cor.BRANCO));
            tabuleiro.adicionaPeca(new Peao(6, 6, tabuleiro, Cor.BRANCO));
            tabuleiro.adicionaPeca(new Peao(6, 4, tabuleiro, Cor.BRANCO));
            tabuleiro.adicionaPeca(new Peao(6, 2, tabuleiro, Cor.BRANCO));
            tabuleiro.adicionaPeca(new Peao(6, 0, tabuleiro, Cor.BRANCO));
            tabuleiro.adicionaPeca(new Peao(5, 7, tabuleiro, Cor.BRANCO));
            tabuleiro.adicionaPeca(new Peao(5, 5, tabuleiro, Cor.BRANCO));
            tabuleiro.adicionaPeca(new Peao(5, 3, tabuleiro, Cor.BRANCO));
            tabuleiro.adicionaPeca(new Peao(5, 1, tabuleiro, Cor.BRANCO));

            tabuleiro.adicionaPeca(new Peao(0, 0, tabuleiro, Cor.PRETA));
            tabuleiro.adicionaPeca(new Peao(0, 2, tabuleiro, Cor.PRETA));
            tabuleiro.adicionaPeca(new Peao(0, 4, tabuleiro, Cor.PRETA));
            tabuleiro.adicionaPeca(new Peao(0, 6, tabuleiro, Cor.PRETA));
            tabuleiro.adicionaPeca(new Peao(1, 1, tabuleiro, Cor.PRETA));
            tabuleiro.adicionaPeca(new Peao(1, 3, tabuleiro, Cor.PRETA));
            tabuleiro.adicionaPeca(new Peao(1, 5, tabuleiro, Cor.PRETA));
            tabuleiro.adicionaPeca(new Peao(1, 7, tabuleiro, Cor.PRETA));
            tabuleiro.adicionaPeca(new Peao(2, 0, tabuleiro, Cor.PRETA));
            tabuleiro.adicionaPeca(new Peao(2, 2, tabuleiro, Cor.PRETA));
            tabuleiro.adicionaPeca(new Peao(3, 3, tabuleiro, Cor.PRETA));
//            tabuleiro.adicionaPeca(new Peao(2, 6, tabuleiro, Cor.PRETA));

            Peca p = new Peao(0, 6, tabuleiro, Cor.BRANCO);
            tabuleiro.adicionaPeca(p);
            tabuleiro.getCasas()[p.getPosicao().getPosicaoX()][p.getPosicao().getPosicaoY()] = promover(p);
            System.out.println(p.getClass());

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
        jLabel1 = new javax.swing.JLabel();
        jLabel_turno = new javax.swing.JLabel();
        jLabel_peoesBrancosCapturados = new javax.swing.JLabel();
        jLabel_peoesPretosCapturados = new javax.swing.JLabel();
        jLabel_damasBrancasCapturadas = new javax.swing.JLabel();
        jLabel_damasPretasCapturadas = new javax.swing.JLabel();
        jToggleButton_desfazerJogada = new javax.swing.JToggleButton();

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

        jLabel1.setText("Vez do jogador:");

        jLabel_turno.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/peao_branco_fundo_preto.jpeg"))); // NOI18N

        jLabel_peoesBrancosCapturados.setText("Peões brancos capturados: 0");

        jLabel_peoesPretosCapturados.setText("Peões pretos capturados: 0");

        jLabel_damasBrancasCapturadas.setText("Damas brancas capturadas: 0");

        jLabel_damasPretasCapturadas.setText("Damas pretas capturadas: 0");

        jToggleButton_desfazerJogada.setText("Desfazer jogada");
        jToggleButton_desfazerJogada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_desfazerJogadaActionPerformed(evt);
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
                                .addComponent(jButton_g8, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
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
                                        .addComponent(jButton_e8, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(51, 51, 51)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel_peoesPretosCapturados)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel_turno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel_peoesBrancosCapturados)
                                    .addComponent(jLabel_damasBrancasCapturadas)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jToggleButton_desfazerJogada, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel_damasPretasCapturadas)))))))
                .addContainerGap(132, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                            .addComponent(jButton_c8, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel_turno, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(89, 89, 89)
                        .addComponent(jLabel_peoesBrancosCapturados)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel_damasBrancasCapturadas)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
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
                            .addComponent(jButton_h8, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel_peoesPretosCapturados)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel_damasPretasCapturadas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jToggleButton_desfazerJogada)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 341, Short.MAX_VALUE))
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
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_a1ActionPerformed

    private void jButton_a3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_a3ActionPerformed
        Posicao PosicaoThis = new Posicao(0, 2);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_a3ActionPerformed

    private void jButton_a5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_a5ActionPerformed
        Posicao PosicaoThis = new Posicao(0, 4);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_a5ActionPerformed

    private void jButton_a7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_a7ActionPerformed
        Posicao PosicaoThis = new Posicao(0, 6);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_a7ActionPerformed

    private void jButton_b2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_b2ActionPerformed
        Posicao PosicaoThis = new Posicao(1, 1);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_b2ActionPerformed

    private void jButton_b4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_b4ActionPerformed
        Posicao PosicaoThis = new Posicao(1, 3);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_b4ActionPerformed

    private void jButton_b6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_b6ActionPerformed
        Posicao PosicaoThis = new Posicao(1, 5);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_b6ActionPerformed

    private void jButton_b8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_b8ActionPerformed
        Posicao PosicaoThis = new Posicao(1, 7);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_b8ActionPerformed

    private void jButton_c1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_c1ActionPerformed
        Posicao PosicaoThis = new Posicao(2, 0);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_c1ActionPerformed

    private void jButton_c3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_c3ActionPerformed
        Posicao PosicaoThis = new Posicao(2, 2);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_c3ActionPerformed

    private void jButton_c5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_c5ActionPerformed
        Posicao PosicaoThis = new Posicao(2, 4);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_c5ActionPerformed

    private void jButton_c7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_c7ActionPerformed
        Posicao PosicaoThis = new Posicao(2, 6);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_c7ActionPerformed

    private void jButton_d2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_d2ActionPerformed
        Posicao PosicaoThis = new Posicao(3, 1);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_d2ActionPerformed

    private void jButton_d4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_d4ActionPerformed
        Posicao PosicaoThis = new Posicao(3, 3);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_d4ActionPerformed

    private void jButton_d6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_d6ActionPerformed
        Posicao PosicaoThis = new Posicao(3, 5);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_d6ActionPerformed

    private void jButton_d8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_d8ActionPerformed
        Posicao PosicaoThis = new Posicao(3, 7);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_d8ActionPerformed

    private void jButton_e1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_e1ActionPerformed
        Posicao PosicaoThis = new Posicao(4, 0);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_e1ActionPerformed

    private void jButton_e3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_e3ActionPerformed
        Posicao PosicaoThis = new Posicao(4, 2);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_e3ActionPerformed

    private void jButton_e5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_e5ActionPerformed
        Posicao PosicaoThis = new Posicao(4, 4);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_e5ActionPerformed

    private void jButton_e7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_e7ActionPerformed
        Posicao PosicaoThis = new Posicao(4, 6);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_e7ActionPerformed

    private void jButton_f2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_f2ActionPerformed
        Posicao PosicaoThis = new Posicao(5, 1);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_f2ActionPerformed

    private void jButton_f4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_f4ActionPerformed
        Posicao PosicaoThis = new Posicao(5, 3);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_f4ActionPerformed

    private void jButton_f6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_f6ActionPerformed
        Posicao PosicaoThis = new Posicao(5, 5);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_f6ActionPerformed

    private void jButton_f8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_f8ActionPerformed
        Posicao PosicaoThis = new Posicao(5, 7);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_f8ActionPerformed

    private void jButton_g1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_g1ActionPerformed
        Posicao PosicaoThis = new Posicao(6, 0);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_g1ActionPerformed

    private void jButton_g3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_g3ActionPerformed
        Posicao PosicaoThis = new Posicao(6, 2);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_g3ActionPerformed

    private void jButton_g5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_g5ActionPerformed
        Posicao PosicaoThis = new Posicao(6, 4);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_g5ActionPerformed

    private void jButton_g7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_g7ActionPerformed
        Posicao PosicaoThis = new Posicao(6, 6);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_g7ActionPerformed

    private void jButton_h3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_h3ActionPerformed
        Posicao PosicaoThis = new Posicao(7, 2);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_h3ActionPerformed

    private void jButton_h2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_h2ActionPerformed
        Posicao PosicaoThis = new Posicao(7, 1);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_h2ActionPerformed

    private void jButton_h4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_h4ActionPerformed
        Posicao PosicaoThis = new Posicao(7, 3);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_h4ActionPerformed

    private void jButton_h6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_h6ActionPerformed
        Posicao PosicaoThis = new Posicao(7, 5);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_h6ActionPerformed

    private void jButton_h8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_h8ActionPerformed
        Posicao PosicaoThis = new Posicao(7, 7);
        if (!mover) {
            posicaoInicial = PosicaoThis;
            mover = true;
            mostraJogadasPossiveis(tabuleiro.getCasas()[PosicaoThis.getPosicaoX()][PosicaoThis.getPosicaoY()]);
        } else {
            mover(posicaoInicial, PosicaoThis);
        }
    }//GEN-LAST:event_jButton_h8ActionPerformed

    private void jToggleButton_desfazerJogadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_desfazerJogadaActionPerformed
        desfazerMovimento();
        montaPecas();
    }//GEN-LAST:event_jToggleButton_desfazerJogadaActionPerformed

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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel_damasBrancasCapturadas;
    private javax.swing.JLabel jLabel_damasPretasCapturadas;
    private javax.swing.JLabel jLabel_peoesBrancosCapturados;
    private javax.swing.JLabel jLabel_peoesPretosCapturados;
    private javax.swing.JLabel jLabel_turno;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToggleButton jToggleButton_desfazerJogada;
    // End of variables declaration//GEN-END:variables
}
