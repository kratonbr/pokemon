// Ficheiro: JanelaTrocarPokemon.java
package br.ufpel.pokemon.view;

import br.ufpel.pokemon.controller.GameController;
import br.ufpel.pokemon.model.Pokemon;
import br.ufpel.pokemon.model.estado.EstadoDesmaiado;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class JanelaTrocarPokemon extends JDialog {
    public JanelaTrocarPokemon(JFrame parent, GameController controller, List<Pokemon> pokemonsNaMochila) {
        super(parent, "Trocar Pokémon", true);
        setSize(300, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        DefaultListModel<Pokemon> listModel = new DefaultListModel<>();
        for (Pokemon p : pokemonsNaMochila) {
            listModel.addElement(p);
        }

        JList<Pokemon> listaPokemons = new JList<>(listModel);
        listaPokemons.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaPokemons.setCellRenderer(new PokemonListRenderer());

        add(new JScrollPane(listaPokemons), BorderLayout.CENTER);

        JButton btnConfirmar = new JButton("Definir como Ativo");
        btnConfirmar.addActionListener(e -> {
            Pokemon pokemonSelecionado = listaPokemons.getSelectedValue();
            if (pokemonSelecionado != null) {
                if (pokemonSelecionado.getEstado() instanceof EstadoDesmaiado) {
                    JOptionPane.showMessageDialog(this, "Este Pokémon está desmaiado!", "Aviso", JOptionPane.WARNING_MESSAGE);
                } else {
                    controller.trocarPokemon(listaPokemons.getSelectedIndex());
                    dispose();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecione um Pokémon.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        add(btnConfirmar, BorderLayout.SOUTH);
    }

    private static class PokemonListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Pokemon) {
                Pokemon pokemon = (Pokemon) value;
                String status = (pokemon.getEstado() instanceof EstadoDesmaiado) ? " (Desmaiado)" : "";
                setText("Nvl " + pokemon.getNivel() + " - " + pokemon.getNome() + status);
            }
            return renderer;
        }
    }
}