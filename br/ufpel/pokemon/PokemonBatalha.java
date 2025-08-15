// Ficheiro: PokemonBatalha.java
// Pacote: br.ufpel.pokemon
package br.ufpel.pokemon;

import br.ufpel.pokemon.controller.GameController;
import br.ufpel.pokemon.view.JanelaInicial;
import javax.swing.SwingUtilities;

/**
 * Classe principal que inicia a aplicação.
 */
public class PokemonBatalha {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GameController controller = new GameController();
                JanelaInicial janelaInicial = new JanelaInicial(controller);
                controller.setJanelaInicial(janelaInicial);
                janelaInicial.setVisible(true);
            }
        });
    }
}
