// Ficheiro: JanelaPrincipal.java
package br.ufpel.pokemon.view;

import br.ufpel.pokemon.controller.GameController;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class JanelaPrincipal extends JFrame {
    private GameController controller;
    private PainelTabuleiro painelTabuleiro;
    private JLabel lblDicas;
    private JLabel lblPontuacao;

    public JanelaPrincipal(GameController controller) {
        this.controller = controller;
        
        setTitle("Batalha Pokémon - Trabalho POO");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        this.painelTabuleiro = new PainelTabuleiro(controller, controller.getTabuleiroParaView().getTamanho());
        add(this.painelTabuleiro, BorderLayout.CENTER);

        JPanel painelInfo = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        
        JButton btnDica = new JButton("Usar Dica");
        btnDica.addActionListener(e -> {
            try {
                String linhaStr = JOptionPane.showInputDialog(this, "Digite o número da linha (0-7):");
                if (linhaStr == null) return;
                String colunaStr = JOptionPane.showInputDialog(this, "Digite o número da coluna (0-7):");
                if (colunaStr == null) return;
                controller.usarDica(Integer.parseInt(linhaStr), Integer.parseInt(colunaStr));
            } catch (NumberFormatException ex) {
                mostrarMensagem("Por favor, digite um número válido.");
            }
        });
        
        JButton btnTrocar = new JButton("Trocar Pokémon");
        btnTrocar.addActionListener(e -> controller.abrirJanelaDeTroca());

        JButton btnDebug = new JButton("Debug");
        btnDebug.addActionListener(e -> controller.ativarModoDebug());

        lblDicas = new JLabel();
        lblPontuacao = new JLabel();
        
        painelInfo.add(lblPontuacao);
        painelInfo.add(btnDica);
        painelInfo.add(lblDicas);
        painelInfo.add(btnTrocar);
        painelInfo.add(btnDebug);
        add(painelInfo, BorderLayout.NORTH);
        
        JPanel painelLog = new JPanel();
        painelLog.add(new JLabel("Log de Batalha aparecerá aqui."));
        add(painelLog, BorderLayout.SOUTH);
        
        atualizar();
    }

    public void atualizar() {
        painelTabuleiro.redesenhar(controller.getTabuleiroParaView());
        lblDicas.setText("Dicas restantes: " + controller.getDicasRestantes());
        lblPontuacao.setText("Placar: Você " + controller.getPontuacaoJogador() + " x " + controller.getPontuacaoComputador() + " PC");
        revalidate();
        repaint();
    }
    
    public void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Aconteceu Algo!", JOptionPane.INFORMATION_MESSAGE);
    }
}

// --------------------------------------------------------------------------------
