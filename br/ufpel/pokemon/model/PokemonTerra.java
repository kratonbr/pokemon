// Ficheiro: PokemonTerra.java
package br.ufpel.pokemon.model;

import br.ufpel.pokemon.model.ataque.AtaqueFuriaDaTerra;

public class PokemonTerra extends Pokemon {
    public PokemonTerra(String nome, int energiaMaxima, int forca) {
        super(nome, energiaMaxima, forca, "Terra", new AtaqueFuriaDaTerra(), "/images/diglett.png");
    }
}
