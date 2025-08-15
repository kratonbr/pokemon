// Ficheiro: PainelTabuleiro.java
package br.ufpel.pokemon.view;

import br.ufpel.pokemon.controller.GameController;
import br.ufpel.pokemon.model.Celula;
import br.ufpel.pokemon.model.Pokemon;
import br.ufpel.pokemon.model.Tabuleiro;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class PainelTabuleiro extends JPanel {
    private GameController controller;
    private JButton[][] botoesCelulas;

    public PainelTabuleiro(GameController controller, int tamanho) {
        this.controller = controller;
        this.botoesCelulas = new JButton[tamanho][tamanho];
        setLayout(new GridLayout(tamanho, tamanho, 5, 5));
        setBackground(Color.BLACK);

        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                final int linha = i;
                final int coluna = j;
                JButton botao = new JButton();
                botao.addActionListener(e -> controller.onCelulaClicada(linha, coluna));
                botoesCelulas[i][j] = botao;
                add(botao);
            }
        }
    }

    public void redesenhar(Tabuleiro tabuleiro) {
        for (int i = 0; i < tabuleiro.getTamanho(); i++) {
            for (int j = 0; j < tabuleiro.getTamanho(); j++) {
                Celula celulaDoModel = tabuleiro.getCelula(i, j);
                JButton botaoCorrespondente = botoesCelulas[i][j];
                botaoCorrespondente.setIcon(null);
                botaoCorrespondente.setText("");
                botaoCorrespondente.setBorder(null);

                if (celulaDoModel.isRevelada()) {
                    if (celulaDoModel.estaOcupada()) {
                        Pokemon pokemon = celulaDoModel.getPokemon();
                        try {
                            java.net.URL imgUrl = getClass().getResource(pokemon.getImagePath());
                            if (imgUrl != null) {
                                ImageIcon icon = new ImageIcon(imgUrl);
                                Image newImg = icon.getImage().getScaledInstance(botaoCorrespondente.getWidth(), botaoCorrespondente.getHeight(), Image.SCALE_SMOOTH);
                                botaoCorrespondente.setIcon(new ImageIcon(newImg));
                            }
                        } catch (Exception e) {
                            botaoCorrespondente.setText(pokemon.getNome());
                        }
                        if (pokemon.isDoComputador()) {
                            botaoCorrespondente.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
                        }
                    }
                    botaoCorrespondente.setBackground(new Color(60, 60, 60));
                } else {
                    botaoCorrespondente.setEnabled(true);
                    switch (celulaDoModel.getTipoTerreno()) {
                        case "Agua": botaoCorrespondente.setBackground(new Color(173, 216, 230)); break;
                        case "Floresta": botaoCorrespondente.setBackground(new Color(144, 238, 144)); break;
                        case "Terra": botaoCorrespondente.setBackground(new Color(210, 180, 140)); break;
                        case "Eletricidade": botaoCorrespondente.setBackground(new Color(255, 255, 224)); break;
                    }
                }
            }
        }
    }
}
