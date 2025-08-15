// Ficheiro: AtaquePadrao.java
package br.ufpel.pokemon.model.ataque;

import br.ufpel.pokemon.model.Batalha;
import br.ufpel.pokemon.model.Pokemon;
import java.util.Random;

public class AtaquePadrao implements EstrategiaDeAtaque {
    @Override
    public String executarAtaque(Pokemon atacante, Pokemon defensor, Batalha batalha) {
        int dano = atacante.getForca() + new Random().nextInt(atacante.getNivel() * 5 + 1);
        defensor.receberDano(dano, batalha.getTipoTerreno());
        return atacante.getNome() + " usou um ataque padrão e causou " + dano + " de dano!";
    }
}