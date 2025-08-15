package br.ufpel.pokemon.model;

import br.ufpel.pokemon.model.ataque.AtaqueEletrico;

public class PokemonEletrico extends Pokemon {
    public PokemonEletrico(String nome, int energiaMaxima, int forca) {
        super(nome, energiaMaxima, forca, "Eletrico", new AtaqueEletrico(), "/images/pikachu.png");
    }
}
