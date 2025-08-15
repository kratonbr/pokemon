// Ficheiro: PokemonAgua.java
package br.ufpel.pokemon.model;

import br.ufpel.pokemon.model.ataque.AtaquePadrao;

public class PokemonAgua extends Pokemon {
    public PokemonAgua(String nome, int energiaMaxima, int forca) {
        super(nome, energiaMaxima, forca, "Agua", new AtaquePadrao(), "/images/squirtle.png");
    }

    @Override
    public void receberDano(int dano, String tipoTerreno) {
        int danoFinal = dano;
        if (!tipoTerreno.equals("Agua")) {
            danoFinal = (int) (dano * 0.8);
            System.out.println(this.getNome() + " (habilidade de água) reduziu o dano!");
        }
        super.receberDano(danoFinal, tipoTerreno);
    }
}