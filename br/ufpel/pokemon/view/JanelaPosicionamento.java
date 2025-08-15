// Ficheiro: JanelaPosicionamento.java (NOVO FICHEIRO)
package br.ufpel.pokemon.view;

import br.ufpel.pokemon.controller.GameController;
import br.ufpel.pokemon.model.Tabuleiro;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class JanelaPosicionamento extends JDialog {
    private JButton[][] botoesCelulas;

    public JanelaPosicionamento(JFrame parent, GameController controller, Tabuleiro tabuleiro) {
        super(parent, "Posicione seu Pokémon Inicial", true); // "true" torna a janela modal
        setSize(600, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JLabel lblInstrucao = new JLabel("Clique na célula onde deseja posicionar o seu Pokémon inicial.", SwingConstants.CENTER);
        add(lblInstrucao, BorderLayout.NORTH);

        JPanel painelTabuleiro = new JPanel(new GridLayout(tabuleiro.getTamanho(), tabuleiro.getTamanho(), 5, 5));
        painelTabuleiro.setBackground(Color.BLACK);
        
        this.botoesCelulas = new JButton[tabuleiro.getTamanho()][tabuleiro.getTamanho()];

        for (int i = 0; i < tabuleiro.getTamanho(); i++) {
            for (int j = 0; j < tabuleiro.getTamanho(); j++) {
                final int linha = i;
                final int coluna = j;
                JButton botao = new JButton();
                
                // Define a cor de fundo com base no tipo de terreno da célula
                switch (tabuleiro.getCelula(i, j).getTipoTerreno()) {
                    case "Agua": botao.setBackground(new Color(173, 216, 230)); break;
                    case "Floresta": botao.setBackground(new Color(144, 238, 144)); break;
                    case "Terra": botao.setBackground(new Color(210, 180, 140)); break;
                    case "Eletricidade": botao.setBackground(new Color(255, 255, 224)); break;
                }
                
                // Ação a ser executada quando o botão é clicado
                botao.addActionListener((ActionEvent e) -> {
                    // Tenta posicionar o Pokémon através do controller
                    boolean sucesso = controller.tentarPosicionarPokemonInicial(linha, coluna);
                    if (sucesso) {
                        dispose(); // Fecha esta janela se o posicionamento for bem-sucedido
                    }
                });
                
                botoesCelulas[i][j] = botao;
                painelTabuleiro.add(botao);
            }
        }
        
        add(painelTabuleiro, BorderLayout.CENTER);
    }
}