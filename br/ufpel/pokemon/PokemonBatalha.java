// Ficheiro: PokemonBatalha.java (VERSÃO ATUALIZADA)
package br.ufpel.pokemon;

import br.ufpel.pokemon.controller.GameController;
import br.ufpel.pokemon.view.JanelaInicial;
import javax.swing.SwingUtilities;

public class PokemonBatalha {

    public static void main(String[] args) {
        // Apenas chama o nosso novo método para começar
        iniciarAplicacao();
    }

    // NOVO MÉTODO: Encapsula a criação da janela inicial
    public static void iniciarAplicacao() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GameController controller = new GameController();
                JanelaInicial janelaInicial = new JanelaInicial(controller);
                // A linha abaixo foi movida para dentro do GameController para um melhor controlo
                // controller.setJanelaInicial(janelaInicial); 
                janelaInicial.setVisible(true);
            }
        });
    }
}