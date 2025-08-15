// Ficheiro: Batalha.java
package br.ufpel.pokemon.model;

import br.ufpel.pokemon.model.estado.EstadoNormal;
import br.ufpel.pokemon.model.estado.EstadoParalisado;

public class Batalha  {
    private Pokemon pokemonJogador;
    private Pokemon pokemonAdversario;
    private boolean turnoDoJogador;
    private boolean terminada;
    private String logBatalha;
    private int turnoAtual;
    private String tipoTerreno;

    public Batalha(Pokemon pokemonJogador, Pokemon pokemonAdversario, String tipoTerreno) {
        this.pokemonJogador = pokemonJogador;
        this.pokemonAdversario = pokemonAdversario;
        this.tipoTerreno = tipoTerreno;
        this.turnoDoJogador = true;
        this.terminada = false;
        this.turnoAtual = 1;
        this.logBatalha = "A batalha começou! É a sua vez.";
    }

    public void executarTurno() {
        if (terminada) return;

        Pokemon atacante = turnoDoJogador ? pokemonJogador : pokemonAdversario;
        Pokemon defensor = turnoDoJogador ? pokemonAdversario : pokemonJogador;

        if (atacante.getEstado() instanceof EstadoParalisado) {
            logBatalha = atacante.getNome() + " está paralisado e não pode se mover!";
            atacante.setEstado(new EstadoNormal());
        } else {
            logBatalha = atacante.getEstrategiaDeAtaque().executarAtaque(atacante, defensor, this);
        }
        if (defensor.getEnergia() <= 0) {
            terminada = true;
            logBatalha += "\n" + defensor.getNome() + " desmaiou! " + atacante.getNome() + " venceu!";
            Pokemon vencedor = atacante;
            Pokemon perdedor = defensor;
            int xpGanha = perdedor.getNivel() * 80;
            String logXP = vencedor.ganharExperiencia(xpGanha);
            
            logBatalha += "\n" + vencedor.getNome() + " ganhou " + xpGanha + " de XP!";
            if (!logXP.isEmpty()) {
                logBatalha += logXP;
            }
        } else {
            turnoDoJogador = !turnoDoJogador;
            turnoAtual++;
        }
    }

    public Pokemon getPokemonJogador() { return pokemonJogador; }
    public Pokemon getPokemonAdversario() { return pokemonAdversario; }
    public boolean isTerminada() { return terminada; }
    public String getLogBatalha() { return logBatalha; }
    public boolean isTurnoDoJogador() { return turnoDoJogador; }
    public int getTurnoAtual() { return turnoAtual; }
    public String getTipoTerreno() { return tipoTerreno; }
}