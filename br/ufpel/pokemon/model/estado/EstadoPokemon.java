// Ficheiro: EstadoPokemon.java
package br.ufpel.pokemon.model.estado;

import br.ufpel.pokemon.model.Pokemon;

/**
 * Interface que define o contrato para todos os estados de um Pokémon.
 * Este é o coração do Padrão de Projeto State.
 */
public interface EstadoPokemon {
    void executarAcao(Pokemon pokemon, Pokemon oponente, String tipoTerreno);
}