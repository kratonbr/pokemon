// Ficheiro: PokemonBatalha.java
package br.ufpel.pokemon;

import br.ufpel.pokemon.controller.GameController;
import br.ufpel.pokemon.view.JanelaInicial; // A importação correta
import javax.swing.SwingUtilities;

public class PokemonBatalha {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GameController controller = new GameController();
                // A linha abaixo agora corresponde ao construtor de JanelaInicial
                JanelaInicial janelaInicial = new JanelaInicial(controller); 
                controller.setJanelaInicial(janelaInicial);
                janelaInicial.setVisible(true); // Este método agora será encontrado
            }
        });
    }
}