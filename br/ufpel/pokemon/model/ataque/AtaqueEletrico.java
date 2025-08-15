package br.ufpel.pokemon.model.ataque;

import br.ufpel.pokemon.model.Batalha;
import br.ufpel.pokemon.model.Pokemon;
import br.ufpel.pokemon.model.estado.EstadoParalisado;
import java.util.Random;

public class AtaqueEletrico implements EstrategiaDeAtaque, java.io.Serializable {
    @Override
    public String executarAtaque(Pokemon atacante, Pokemon defensor, Batalha batalha) {
        int dano = atacante.getForca() + new Random().nextInt(atacante.getNivel() * 5 + 1);
        defensor.receberDano(dano, batalha.getTipoTerreno());
        
        String log = atacante.getNome() + " usou um ataque elétrico e causou " + dano + " de dano!";
        
        if (new Random().nextInt(100) < 30) { // 30% de chance
            defensor.setEstado(new EstadoParalisado());
            log += "\n" + defensor.getNome() + " ficou paralisado!";
        }
        
        return log;
    }
}
