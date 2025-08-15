
// Ficheiro: PokemonFloresta.java
package br.ufpel.pokemon.model;

import br.ufpel.pokemon.model.ataque.AtaqueComRegeneracao;

public class PokemonFloresta extends Pokemon {
    public PokemonFloresta(String nome, int energiaMaxima, int forca) {
        super(nome, energiaMaxima, forca, "Floresta", new AtaqueComRegeneracao(), "/images/bulbasaur.png");
    }
}