// Pacote: br.ufpel.pokemon.model.estado
package br.ufpel.pokemon.model.estado;

import br.ufpel.pokemon.model.Pokemon;

/**
 * Representa o estado normal de um Pokémon.
 * Esta classe agora serve como um "marcador" para o padrão State.
 * A lógica de ataque foi movida para o padrão Strategy.
 */
public class EstadoNormal implements EstadoPokemon {
    /**
     * No estado Normal, a ação é o ataque, que agora é gerido
     * diretamente pela classe Batalha através do padrão Strategy.
     * Este método existe para cumprir o contrato da interface EstadoPokemon.
     */
    @Override
    public void executarAcao(Pokemon pokemon, Pokemon oponente, String tipoTerreno) {
        // A lógica de ataque foi movida para a classe Batalha e para as Estratégias.
        // Este método pode permanecer vazio, pois a verificação do estado é feita
        // com 'instanceof' na classe Batalha.
    }
}
