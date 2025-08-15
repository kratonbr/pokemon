// Ficheiro: GameController.java
// Pacote: br.ufpel.pokemon.controller
package br.ufpel.pokemon.controller;

import br.ufpel.pokemon.model.Batalha;
import br.ufpel.pokemon.model.Jogo;
import br.ufpel.pokemon.model.Tabuleiro;
import br.ufpel.pokemon.view.JanelaBatalha;
import br.ufpel.pokemon.view.JanelaInicial;
import br.ufpel.pokemon.view.JanelaPrincipal;
import br.ufpel.pokemon.view.JanelaTrocarPokemon;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class GameController {
    private Jogo jogoModel;
    private JanelaInicial janelaInicial;
    private JanelaPrincipal janelaView;
    private JanelaBatalha janelaBatalha;

    public GameController() {
        // O modelo agora é criado quando o jogo começa
    }

    public void setJanelaInicial(JanelaInicial view) {
        this.janelaInicial = view;
    }

    public void iniciarJogoAleatorio() {
        this.jogoModel = new Jogo(); // Cria uma nova instância do jogo
        
        janelaView = new JanelaPrincipal(this);
        janelaView.setVisible(true);
        
        if (janelaInicial != null) {
            janelaInicial.dispose();
        }
    }

    public void onCelulaClicada(int linha, int coluna) {
        String resultado = jogoModel.processarJogada(linha, coluna);
        Batalha batalha = jogoModel.getBatalhaAtual();
        boolean deveIniciarBatalha = (batalha != null && !batalha.isTerminada() && (janelaBatalha == null || !janelaBatalha.isDisplayable()));
        if (deveIniciarBatalha) {
            janelaBatalha = new JanelaBatalha(janelaView, this, batalha);
            janelaBatalha.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(janelaView, resultado);
            janelaView.atualizar();
        }
    }
    
    public void usarDica(int linha, int coluna) {
        String resultadoDica = jogoModel.usarDica(linha, coluna);
        JOptionPane.showMessageDialog(janelaView, resultadoDica, "Resultado da Dica", JOptionPane.INFORMATION_MESSAGE);
        janelaView.atualizar();
    }

    public void abrirJanelaDeTroca() {
        JanelaTrocarPokemon janelaTroca = new JanelaTrocarPokemon(janelaView, this, jogoModel.getPokemonsDoJogador());
        janelaTroca.setVisible(true);
    }

    public void trocarPokemon(int indice) {
        jogoModel.trocarPokemonDoJogador(indice);
    }

    public void ativarModoDebug() {
        jogoModel.revelarTodoTabuleiro();
        janelaView.atualizar();
    }
    
    public void executarTurnoBatalha() {
        Batalha batalha = jogoModel.getBatalhaAtual();
        if (batalha == null || batalha.isTerminada() || !batalha.isTurnoDoJogador()) return;
        janelaBatalha.desabilitarAcoes();
        batalha.executarTurno();
        janelaBatalha.atualizar();
        if (batalha.isTerminada()) {
            finalizarBatalha();
            return;
        }
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Thread.sleep(1500);
                batalha.executarTurno();
                return null;
            }
            @Override
            protected void done() {
                janelaBatalha.atualizar();
                if (batalha.isTerminada()) {
                    finalizarBatalha();
                } else {
                    janelaBatalha.habilitarAcoes();
                }
            }
        }.execute();
    }
    
    private void finalizarBatalha() {
        Batalha batalha = jogoModel.getBatalhaAtual();
        if (batalha == null) return;
        
        if (batalha.getPokemonAdversario().getEnergia() <= 0) {
            jogoModel.removerPokemonVencido();
            janelaView.atualizar();
        }
        
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        JOptionPane.showMessageDialog(janelaBatalha, batalha.getLogBatalha());
        
        janelaBatalha.dispose();
        jogoModel.encerrarBatalha();
        
        jogoModel.restaurarTimesAposBatalha();
    }
    
    public void fugirDaBatalha() {
        if (janelaBatalha != null) {
            janelaBatalha.dispose();
        }
        jogoModel.restaurarTimesAposBatalha();
        jogoModel.encerrarBatalha();
    }

    // Getters para a View
    public Tabuleiro getTabuleiroParaView() { return jogoModel.getTabuleiro(); }
    public int getDicasRestantes() { return jogoModel.getDicasRestantes(); }
    public int getPontuacaoJogador() { return jogoModel.getPontuacaoJogador(); }
    public int getPontuacaoComputador() { return jogoModel.getPontuacaoComputador(); }
}
