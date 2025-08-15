// Ficheiro: AtaquePadrao.java (ATUALIZADO)
package br.ufpel.pokemon.model.ataque;

import br.ufpel.pokemon.model.Batalha;
import br.ufpel.pokemon.model.Pokemon;
import java.util.Random;

public class AtaquePadrao implements EstrategiaDeAtaque, java.io.Serializable {
    
    // NOVO MÉTODO: Centraliza a lógica de vantagem de tipo
    protected double calcularFatorTipo(Pokemon atacante, Pokemon defensor) {
        String tipoAtacante = atacante.getTipo();
        String tipoDefensor = defensor.getTipo();

        // Regras de Vantagem (Super Efetivo = 1.5x)
        if (tipoAtacante.equals("Agua") && tipoDefensor.equals("Terra")) return 1.5;
        if (tipoAtacante.equals("Terra") && tipoDefensor.equals("Eletrico")) return 1.5;
        if (tipoAtacante.equals("Eletrico") && tipoDefensor.equals("Agua")) return 1.5;
        if (tipoAtacante.equals("Floresta") && tipoDefensor.equals("Agua")) return 1.5;

        // Regras de Desvantagem (Não Muito Efetivo = 0.5x)
        if (tipoAtacante.equals("Terra") && tipoDefensor.equals("Floresta")) return 0.5;
        if (tipoAtacante.equals("Agua") && tipoDefensor.equals("Agua")) return 0.5;
        
        // Dano Neutro
        return 1.0;
    }
    
    @Override
    public String executarAtaque(Pokemon atacante, Pokemon defensor, Batalha batalha) {
        // LÓGICA DE DANO ATUALIZADA para ser mais elaborada
        double fatorTipo = calcularFatorTipo(atacante, defensor);
        int danoBase = atacante.getForca() + new Random().nextInt(atacante.getNivel() * 5 + 1);
        int danoFinal = (int) (danoBase * fatorTipo);
        
        defensor.receberDano(danoFinal, batalha.getTipoTerreno());
        
        String log = atacante.getNome() + " usou um ataque padrão e causou " + danoFinal + " de dano!";
        if (fatorTipo > 1.0) {
            log += " Foi super efetivo!";
        } else if (fatorTipo < 1.0) {
            log += " Não foi muito efetivo...";
        }
        return log;
    }
}