package br.ufpel.pokemon.model.ataque;

import br.ufpel.pokemon.model.Batalha;
import br.ufpel.pokemon.model.Pokemon;
import java.util.Random;

// Agora estende AtaquePadrao para herdar o método de cálculo
public class AtaqueComRegeneracao extends AtaquePadrao implements EstrategiaDeAtaque, java.io.Serializable {
    @Override
    public String executarAtaque(Pokemon atacante, Pokemon defensor, Batalha batalha) {
        double fatorTipo = calcularFatorTipo(atacante, defensor); // Usa o método herdado
        int danoBase = atacante.getForca() + new Random().nextInt(atacante.getNivel() * 5 + 1);
        int danoFinal = (int) (danoBase * fatorTipo);
        
        defensor.receberDano(danoFinal, batalha.getTipoTerreno());
        
        int cura = (int) (atacante.getEnergiaMaxima() * 0.1);
        atacante.curar(cura);

        String log = atacante.getNome() + " atacou, causando " + danoFinal + " de dano e regenerando " + cura + " de vida!";
        if (fatorTipo > 1.0) log += " Foi super efetivo!";
        if (fatorTipo < 1.0) log += " Não foi muito efetivo...";
        return log;
    }
}