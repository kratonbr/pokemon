package br.ufpel.pokemon.model.estado;

import br.ufpel.pokemon.model.Pokemon;

/**
 * Representa o estado desmaiado, onde o Pokémon não pode fazer nenhuma ação.
 */
public class EstadoDesmaiado implements EstadoPokemon, java.io.Serializable {
    @Override
    public void executarAcao(Pokemon pokemon, Pokemon oponente, String tipoTerreno) {
        System.out.println(pokemon.getNome() + " está fora de combate e não pode fazer nada.");
    }
}