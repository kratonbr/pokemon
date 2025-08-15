// Ficheiro: AtaqueComRegeneracao.java
package br.ufpel.pokemon.model.ataque;

import br.ufpel.pokemon.model.Batalha;
import br.ufpel.pokemon.model.Pokemon;
import java.util.Random;

public class AtaqueComRegeneracao implements EstrategiaDeAtaque, java.io.Serializable {
    @Override
    public String executarAtaque(Pokemon atacante, Pokemon defensor, Batalha batalha) {
        int dano = atacante.getForca() + new Random().nextInt(atacante.getNivel() * 5 + 1);
        defensor.receberDano(dano, batalha.getTipoTerreno());
        
        int cura = (int) (atacante.getEnergiaMaxima() * 0.1);
        atacante.curar(cura);

        return atacante.getNome() + " atacou, causando " + dano + " de dano e regenerando " + cura + " de vida!";
    }
}