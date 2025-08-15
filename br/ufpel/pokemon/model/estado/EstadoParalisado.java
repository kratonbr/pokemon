package br.ufpel.pokemon.model.estado;

import br.ufpel.pokemon.model.Pokemon;

/**
 * Representa o estado paralisado, onde o Pokémon não pode atacar.
 */
public class EstadoParalisado implements EstadoPokemon, java.io.Serializable {
    @Override
    public void executarAcao(Pokemon pokemon, Pokemon oponente, String tipoTerreno) {
        System.out.println(pokemon.getNome() + " está paralisado e não pode se mover!");
        // O Pokémon se recupera da paralisia para o próximo turno
        pokemon.setEstado(new EstadoNormal());
    }
}