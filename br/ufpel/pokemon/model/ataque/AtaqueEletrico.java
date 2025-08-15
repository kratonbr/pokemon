package br.ufpel.pokemon.model.ataque;

import br.ufpel.pokemon.model.Batalha;
import br.ufpel.pokemon.model.Pokemon;
import br.ufpel.pokemon.model.estado.EstadoParalisado;
import java.util.Random;

public class AtaqueEletrico extends AtaquePadrao implements EstrategiaDeAtaque, java.io.Serializable {
    @Override
    public String executarAtaque(Pokemon atacante, Pokemon defensor, Batalha batalha) {
        double fatorTipo = calcularFatorTipo(atacante, defensor);
        int danoBase = atacante.getForca() + new Random().nextInt(atacante.getNivel() * 5 + 1);
        int danoFinal = (int) (danoBase * fatorTipo);
        
        defensor.receberDano(danoFinal, batalha.getTipoTerreno());
        
        String log = atacante.getNome() + " usou um ataque elétrico e causou " + danoFinal + " de dano!";
        if (fatorTipo > 1.0) log += " Foi super efetivo!";
        if (fatorTipo < 1.0) log += " Não foi muito efetivo...";
        
        if (new Random().nextInt(100) < 30) { // 30% de chance de paralisar
            defensor.setEstado(new EstadoParalisado());
            log += "\n" + defensor.getNome() + " ficou paralisado!";
        }
        
        return log;
    }
}