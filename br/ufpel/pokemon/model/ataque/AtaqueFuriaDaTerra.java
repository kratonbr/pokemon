package br.ufpel.pokemon.model.ataque;

import br.ufpel.pokemon.model.Batalha;
import br.ufpel.pokemon.model.Pokemon;
import java.util.Random;

public class AtaqueFuriaDaTerra extends AtaquePadrao implements EstrategiaDeAtaque, java.io.Serializable {
    @Override
    public String executarAtaque(Pokemon atacante, Pokemon defensor, Batalha batalha) {
        double fatorTipo = calcularFatorTipo(atacante, defensor);
        int danoBase = atacante.getForca() + new Random().nextInt(atacante.getNivel() * 5 + 1);
        int danoFinal = (int) (danoBase * fatorTipo);
        
        String log = "";

        if (batalha.getTurnoAtual() % 2 != 0) { // Bónus em turno ímpar
            danoFinal *= 2;
            log = atacante.getNome() + " usou sua Fúria da Terra! O dano foi dobrado! ";
        }
        
        defensor.receberDano(danoFinal, batalha.getTipoTerreno());
        log += atacante.getNome() + " causou " + danoFinal + " de dano!";
        if (fatorTipo > 1.0) log += " Foi super efetivo!";
        if (fatorTipo < 1.0) log += " Não foi muito efetivo...";
        return log;
    }
}