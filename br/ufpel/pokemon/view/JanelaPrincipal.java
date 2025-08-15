// Ficheiro: JanelaPrincipal.java (VERSÃO COMPLETA E CORRIGIDA)
package br.ufpel.pokemon.view;

import br.ufpel.pokemon.controller.GameController;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class JanelaPrincipal extends JFrame {
    private GameController controller;
    private PainelTabuleiro painelTabuleiro;
    private JLabel lblDicas;
    private JLabel lblPontuacao;
    private JLabel lblStatus;

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
        
        // --- BOTÃO USAR DICA ---
        JButton btnDica = new JButton("Usar Dica");
        btnDica.addActionListener((ActionEvent e) -> {
            try {
                // Esta linha abre a primeira caixa de diálogo
                String linhaStr = JOptionPane.showInputDialog(this, "Digite o número da linha (0-7):");
                if (linhaStr == null) return; // Se o utilizador cancelar, não faz mais nada
                
                // Esta linha abre a segunda caixa de diálogo
                String colunaStr = JOptionPane.showInputDialog(this, "Digite o número da coluna (0-7):");
                if (colunaStr == null) return; // Se o utilizador cancelar, não faz mais nada
                
                controller.usarDica(Integer.parseInt(linhaStr), Integer.parseInt(colunaStr));
            } catch (NumberFormatException ex) {
                mostrarMensagem("Por favor, digite um número válido.");
            }
        });
        
        JButton btnTrocar = new JButton("Trocar Pokémon");
        btnTrocar.addActionListener(e -> controller.abrirJanelaDeTroca());

        JButton btnDebug = new JButton("Debug");
        btnDebug.addActionListener(e -> controller.ativarModoDebug());
        
        // --- BOTÃO SALVAR JOGO ---
        JButton btnSalvar = new JButton("Salvar Jogo");
        btnSalvar.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Salvar o jogo atual");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Jogo Pokémon Salvo (*.sav)", "sav"));
            
            // Esta linha abre a janela para escolher o ficheiro
            int userSelection = fileChooser.showSaveDialog(this);
            
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                if (!fileToSave.getAbsolutePath().endsWith(".sav")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".sav");
                }
                controller.salvarJogo(fileToSave);
            }
        });

        lblDicas = new JLabel();
        lblPontuacao = new JLabel();
        
        painelInfo.add(lblPontuacao);
        painelInfo.add(btnDica);
        painelInfo.add(lblDicas);
        painelInfo.add(btnTrocar);
        painelInfo.add(btnDebug);
        painelInfo.add(btnSalvar);
        add(painelInfo, BorderLayout.NORTH);
        
        JPanel painelLog = new JPanel();
        lblStatus = new JLabel("Sua Vez de Jogar");
        painelLog.add(lblStatus);
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
    
    public void atualizarStatus(String status) {
        lblStatus.setText(status);
    }
    
    public void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Aconteceu Algo!", JOptionPane.INFORMATION_MESSAGE);
    }
}