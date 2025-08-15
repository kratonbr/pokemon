// Ficheiro: JanelaBatalha.java
package br.ufpel.pokemon.view;

import br.ufpel.pokemon.controller.GameController;
import br.ufpel.pokemon.model.Batalha;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class JanelaBatalha extends JDialog {
    private GameController controller;
    private Batalha batalha;
    private PainelInfoPokemon painelJogador;
    private PainelInfoPokemon painelAdversario;
    private JLabel lblLog;
    private JButton btnAtacar;
    private JButton btnFugir;

    public JanelaBatalha(JFrame parent, GameController controller, Batalha batalha) {
        super(parent, "Batalha em Andamento!", true);
        this.controller = controller;
        this.batalha = batalha;
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel painelCombatentes = new JPanel(new GridLayout(1, 3, 10, 10));
        painelJogador = new PainelInfoPokemon(batalha.getPokemonJogador());
        painelAdversario = new PainelInfoPokemon(batalha.getPokemonAdversario());
        JLabel lblVs = new JLabel("VS", SwingConstants.CENTER);
        lblVs.setFont(new Font("Arial", Font.BOLD, 48));
        painelCombatentes.add(painelJogador);
        painelCombatentes.add(lblVs);
        painelCombatentes.add(painelAdversario);
        add(painelCombatentes, BorderLayout.CENTER);

        JPanel painelAcoes = new JPanel(new FlowLayout());
        btnAtacar = new JButton("Atacar");
        btnAtacar.addActionListener(e -> controller.executarTurnoBatalha());
        btnFugir = new JButton("Fugir");
        btnFugir.addActionListener(e -> controller.fugirDaBatalha());
        painelAcoes.add(btnAtacar);
        painelAcoes.add(btnFugir);
        add(painelAcoes, BorderLayout.SOUTH);
        
        lblLog = new JLabel(" ", SwingConstants.CENTER);
        lblLog.setFont(new Font("Serif", Font.ITALIC, 14));
        add(lblLog, BorderLayout.NORTH);
        
        atualizar();
    }

    public void atualizar() {
        painelJogador.atualizar();
        painelAdversario.atualizar();
        lblLog.setText("<html><div style='text-align: center;'>" + batalha.getLogBatalha().replaceAll("\n", "<br>") + "</div></html>");
    }
    
    public void desabilitarAcoes() {
        btnAtacar.setEnabled(false);
        btnFugir.setEnabled(false);
    }
    
    public void habilitarAcoes() {
        btnAtacar.setEnabled(true);
        btnFugir.setEnabled(true);
    }
}