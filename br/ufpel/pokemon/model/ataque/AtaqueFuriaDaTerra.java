package br.ufpel.pokemon.model.ataque;

import br.ufpel.pokemon.model.Batalha;
import br.ufpel.pokemon.model.Pokemon;
import java.util.Random;

public class AtaqueFuriaDaTerra implements EstrategiaDeAtaque {
    @Override
    public String executarAtaque(Pokemon atacante, Pokemon defensor, Batalha batalha) {
        int dano = atacante.getForca() + new Random().nextInt(atacante.getNivel() * 5 + 1);
        String log = "";

        if (batalha.getTurnoAtual() % 2 != 0) {
            dano *= 2;
            log = atacante.getNome() + " usou sua Fúria da Terra! O dano foi dobrado! ";
        }
        
        defensor.receberDano(dano, batalha.getTipoTerreno());
        log += atacante.getNome() + " causou " + dano + " de dano!";
        return log;
    }
}