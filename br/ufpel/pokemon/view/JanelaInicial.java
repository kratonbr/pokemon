// Ficheiro: JanelaInicial.java (VERSÃO ATUALIZADA)
package br.ufpel.pokemon.view;

import br.ufpel.pokemon.controller.GameController;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class JanelaInicial extends JFrame {
    private GameController controller;

    public JanelaInicial(GameController controller) {
        this.controller = controller;

        setTitle("Bem-vindo ao Batalha Pokémon!");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton btnAleatorio = new JButton("Distribuir Pokémons de maneira aleatória");
        btnAleatorio.addActionListener((ActionEvent e) -> {
            controller.iniciarJogoAleatorio();
        });

        JButton btnCarregar = new JButton("Carregar Jogo Salvo");
        btnCarregar.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Selecione o jogo salvo");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Jogo Pokémon Salvo (*.sav)", "sav"));
            int userSelection = fileChooser.showOpenDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                controller.carregarJogo(fileChooser.getSelectedFile());
            }
        });
        
        // BOTÃO ALTERADO
        JButton btnPosicionar = new JButton("Posicionar seu time Pokémon");
        btnPosicionar.setEnabled(true); // Agora está ativo
        btnPosicionar.addActionListener((ActionEvent e) -> {
            controller.iniciarPosicionamento(); // Chama o novo método
        });

        painel.add(btnAleatorio);
        painel.add(btnCarregar);
        painel.add(btnPosicionar);

        add(painel);
    }
}