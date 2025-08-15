// Pacote: br.ufpel.pokemon.model
package br.ufpel.pokemon.model;

import br.ufpel.pokemon.model.estado.EstadoDesmaiado;
import java.io.Serializable; // Garante que a classe pode ser salva
import java.util.List;
import java.util.ArrayList;

public class Treinador implements Serializable { // Implementa Serializable
    private static final long serialVersionUID = 1L; // Para versionamento da serialização
    private String nome;
    private List<Pokemon> pokemonsNaMochila;
    private Pokemon pokemonAtivo;
    private int dicasRestantes;
    private boolean isComputador;

    public Treinador(String nome, Pokemon pokemonInicial, boolean isComputador) {
        this.nome = nome;
        this.isComputador = isComputador;
        this.pokemonsNaMochila = new ArrayList<>();
        this.dicasRestantes = 3;
        
        if (pokemonInicial != null) {
            capturar(pokemonInicial);
            this.pokemonAtivo = pokemonInicial;
        }
    }

    // MÉTODO ALTERADO
    public int getPontuacao() {
        int pontuacaoTotal = 0;
        for (Pokemon p : pokemonsNaMochila) {
            // A pontuação agora é a soma dos NÍVEIS GANHOS.
            // Um pokémon de nível 1 (base) contribui com 0 pontos (1 - 1 = 0).
            // Um pokémon de nível 2 contribui com 1 ponto (2 - 1 = 1), e assim por diante.
            pontuacaoTotal += (p.getNivel() - 1);
        }
        return pontuacaoTotal;
    }

    public void restabelecerEnergiaDoTime() {
        for (Pokemon p : pokemonsNaMochila) {
            p.restabelecerEnergia();
        }
    }

    public void capturar(Pokemon p) {
        if (p == null) return;

        p.setTreinador(this);
        this.pokemonsNaMochila.add(p);
        
        if (this.pokemonAtivo == null || this.pokemonAtivo.getEstado() instanceof EstadoDesmaiado) {
            this.pokemonAtivo = p;
        }
    }

    public Pokemon getProximoPokemonParaBatalha() {
        if (pokemonAtivo != null && !(pokemonAtivo.getEstado() instanceof EstadoDesmaiado)) {
            return pokemonAtivo;
        }
        for (Pokemon p : pokemonsNaMochila) {
            if (!(p.getEstado() instanceof EstadoDesmaiado)) {
                this.pokemonAtivo = p;
                return p;
            }
        }
        return null;
    }

    public void trocarPokemonPrincipal(int indiceNaMochila) {
        if (indiceNaMochila >= 0 && indiceNaMochila < this.pokemonsNaMochila.size()) {
            Pokemon novoPokemonAtivo = this.pokemonsNaMochila.get(indiceNaMochila);
            if (novoPokemonAtivo.getEstado() instanceof EstadoDesmaiado) {
                System.out.println(novoPokemonAtivo.getNome() + " está desmaiado e não pode lutar!");
            } else {
                this.pokemonAtivo = novoPokemonAtivo;
                System.out.println(this.nome + " escolheu " + this.pokemonAtivo.getNome() + " como seu Pokémon ativo!");
            }
        }
    }

    public boolean usarDica() {
        if (dicasRestantes > 0) {
            dicasRestantes--;
            return true;
        }
        return false;
    }

    // Getters
    public String getNome() { return this.nome; }
    public List<Pokemon> getPokemonsNaMochila() { return this.pokemonsNaMochila; }
    public Pokemon getPokemonAtivo() { return this.pokemonAtivo; }
    public int getDicasRestantes() { return this.dicasRestantes; }
    public boolean isComputador() { return this.isComputador; }
}