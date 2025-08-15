// Ficheiro: GameController.java (VERSÃO COMPLETA E CORRIGIDA)
package br.ufpel.pokemon.controller;

// IMPORTS NECESSÁRIOS (verifique se todos estes estão presentes)
import br.ufpel.pokemon.exception.RegiaoInvalidaException;
import br.ufpel.pokemon.model.Batalha;
import br.ufpel.pokemon.model.Jogo;
import br.ufpel.pokemon.model.Pokemon;
import br.ufpel.pokemon.model.Tabuleiro;
import br.ufpel.pokemon.view.JanelaBatalha;
import br.ufpel.pokemon.view.JanelaInicial;
import br.ufpel.pokemon.view.JanelaPosicionamento;
import br.ufpel.pokemon.view.JanelaPrincipal;
import br.ufpel.pokemon.view.JanelaTrocarPokemon;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List; // <<< ESTE IMPORT ESTAVA A FALTAR
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class GameController {
    private Jogo jogoModel;
    private JanelaInicial janelaInicial;
    private JanelaPrincipal janelaView;
    private JanelaBatalha janelaBatalha;

    public GameController() {}

    public void setJanelaInicial(JanelaInicial view) {
        this.janelaInicial = view;
    }

    public void iniciarJogoAleatorio() {
        this.jogoModel = new Jogo(true);
        abrirJanelaPrincipal();
    }
    
    // MÉTODO NOVO QUE ESTAVA A FALTAR
    public void iniciarPosicionamento() {
        this.jogoModel = new Jogo(false);
        
        JanelaPosicionamento janelaPos = new JanelaPosicionamento(janelaInicial, this, jogoModel.getTabuleiro());
        janelaPos.setVisible(true);
        
        if (jogoModel.isJogadorPosicionado()) {
            jogoModel.distribuirPokemonsRestantes();
            abrirJanelaPrincipal();
        }
    }
    
    // MÉTODO NOVO QUE ESTAVA A FALTAR
    public boolean tentarPosicionarPokemonInicial(int linha, int coluna) {
        try {
            Pokemon pokemonDoJogador = jogoModel.getPokemonsDoJogador().get(0);
            jogoModel.getTabuleiro().posicionarPokemon(pokemonDoJogador, linha, coluna);
            jogoModel.setJogadorPosicionado(true);
            JOptionPane.showMessageDialog(null, "Pokémon posicionado com sucesso!");
            return true;
        } catch (RegiaoInvalidaException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Região Inválida!", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Esta célula já está ocupada!", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void abrirJanelaPrincipal() {
        janelaView = new JanelaPrincipal(this);
        janelaView.setVisible(true);
        if (janelaInicial != null) {
            janelaInicial.dispose();
        }
    }
    
    public void carregarJogo(File arquivo) {
        try (FileInputStream fis = new FileInputStream(arquivo); ObjectInputStream ois = new ObjectInputStream(fis)) {
            this.jogoModel = (Jogo) ois.readObject();
            abrirJanelaPrincipal();
            JOptionPane.showMessageDialog(janelaView, "Jogo carregado com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar o jogo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void salvarJogo(File arquivo) {
        if(jogoModel == null) return;
        try (FileOutputStream fos = new FileOutputStream(arquivo); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(jogoModel);
            JOptionPane.showMessageDialog(janelaView, "Jogo salvo com sucesso em:\n" + arquivo.getAbsolutePath());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(janelaView, "Erro ao salvar o jogo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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
        verificarCondicaoDeVitoria();
    }
    
    private void verificarCondicaoDeVitoria(){
        if(jogoModel != null && jogoModel.verificarFimDeJogo()){
            int pontuacaoJogador = jogoModel.getPontuacaoJogador();
            int pontuacaoPC = jogoModel.getPontuacaoComputador();
            String mensagemFinal;
            if(pontuacaoJogador > pontuacaoPC){
                mensagemFinal = "Parabéns! Você venceu com " + pontuacaoJogador + " pontos contra " + pontuacaoPC + " do computador!";
            } else if (pontuacaoPC > pontuacaoJogador) {
                mensagemFinal = "Que pena! O computador venceu com " + pontuacaoPC + " pontos contra " + pontuacaoJogador + " seus.";
            } else {
                mensagemFinal = "O jogo terminou em empate com " + pontuacaoJogador + " pontos para cada um!";
            }
            JOptionPane.showMessageDialog(janelaView, mensagemFinal, "Fim de Jogo!", JOptionPane.INFORMATION_MESSAGE);
            janelaView.dispose();
             System.exit(0);
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
        
        verificarCondicaoDeVitoria();
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
    public List<Pokemon> getPokemonsDoJogador() { return jogoModel.getPokemonsDoJogador(); }
}