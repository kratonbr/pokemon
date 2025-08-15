// Ficheiro: EstrategiaDeAtaque.java
package br.ufpel.pokemon.model.ataque;

import br.ufpel.pokemon.model.Batalha;
import br.ufpel.pokemon.model.Pokemon;

public interface EstrategiaDeAtaque {
    String executarAtaque(Pokemon atacante, Pokemon defensor, Batalha batalha);
}