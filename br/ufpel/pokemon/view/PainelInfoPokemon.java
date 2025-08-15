// Ficheiro: PainelInfoPokemon.java
package br.ufpel.pokemon.view;

import br.ufpel.pokemon.model.Pokemon;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

public class PainelInfoPokemon extends JPanel {
    private Pokemon pokemon;
    private JLabel lblNome;
    private JLabel lblImagem;
    private JProgressBar barraVida;

    public PainelInfoPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        
        lblNome = new JLabel("", SwingConstants.CENTER);
        lblNome.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblNome, BorderLayout.NORTH);

        lblImagem = new JLabel();
        lblImagem.setPreferredSize(new Dimension(150, 150));
        try {
            java.net.URL imgUrl = getClass().getResource(pokemon.getImagePath());
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                Image newImg = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                lblImagem.setIcon(new ImageIcon(newImg));
            }
        } catch (Exception e) {
            lblImagem.setText("Sem Imagem");
        }
        add(lblImagem, BorderLayout.CENTER);

        barraVida = new JProgressBar();
        barraVida.setStringPainted(true);
        barraVida.setBackground(Color.LIGHT_GRAY);
        add(barraVida, BorderLayout.SOUTH);

        atualizar();
    }

    public void atualizar() {
        lblNome.setText(pokemon.getNome() + " (Nvl " + pokemon.getNivel() + ")");
        barraVida.setMaximum(pokemon.getEnergiaMaxima());
        barraVida.setValue(pokemon.getEnergia());
        barraVida.setString(pokemon.getEnergia() + " / " + pokemon.getEnergiaMaxima());
        
        if (pokemon.getEnergia() <= 0) {
            barraVida.setForeground(Color.BLACK);
        } else if (pokemon.getEnergia() < pokemon.getEnergiaMaxima() * 0.2) {
            barraVida.setForeground(Color.RED);
        } else if (pokemon.getEnergia() < pokemon.getEnergiaMaxima() * 0.5) {
            barraVida.setForeground(Color.ORANGE);
        } else {
            barraVida.setForeground(Color.GREEN);
        }
    }
}
