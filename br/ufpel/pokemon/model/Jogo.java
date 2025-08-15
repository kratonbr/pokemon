// Ficheiro: Jogo.java (VERSÃO ATUALIZADA)
package br.ufpel.pokemon.model;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Jogo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Treinador treinadorUsuario;
    private Treinador treinadorComputador;
    private Tabuleiro tabuleiro;
    private transient Random random;
    private Batalha batalhaAtual;
    private Celula celulaDaBatalhaAtual;
    private boolean jogadorPosicionado = false; // NOVO: para controlar o fluxo

    // CONSTRUTOR ALTERADO
    public Jogo(boolean distribuirTudo) {
        this.tabuleiro = new Tabuleiro(8);
        this.random = new Random();
        this.batalhaAtual = null;
        this.celulaDaBatalhaAtual = null;
        
        inicializarTreinadores();
        
        if (distribuirTudo) {
            distribuirTodosOsPokemons();
        }
    }
    
    private void readObject(ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.random = new Random();
    }

    private void inicializarTreinadores() {
        Pokemon pokemonInicialJogador = new PokemonAgua("Meu Magikarp", 10, 1);
        this.treinadorUsuario = new Treinador("Ash", pokemonInicialJogador, false);
        
        Pokemon pokemonInicialPC = new PokemonTerra("Meu Sandshrew", 10, 1);
        this.treinadorComputador = new Treinador("Gary", pokemonInicialPC, true);
    }
    
    // MÉTODO RENOMEADO (antes era distribuirPokemonsAleatoriamente)
    public void distribuirTodosOsPokemons() {
        List<Pokemon> todosOsPokemons = new ArrayList<>();
        todosOsPokemons.addAll(treinadorUsuario.getPokemonsNaMochila());
        todosOsPokemons.addAll(treinadorComputador.getPokemonsNaMochila());
        
        // Adiciona os selvagens
        todosOsPokemons.add(new PokemonAgua("Squirtle", 100, 15));
        todosOsPokemons.add(new PokemonFloresta("Bulbasaur", 110, 12));
        todosOsPokemons.add(new PokemonTerra("Diglett", 90, 18));
        todosOsPokemons.add(new PokemonEletrico("Pikachu", 95, 16));
        
        posicionarListaDePokemon(todosOsPokemons);
    }

    // NOVO MÉTODO: para distribuir apenas os que ainda não estão no mapa
    public void distribuirPokemonsRestantes() {
        List<Pokemon> pokemonsParaDistribuir = new ArrayList<>();
        
        // Pega todos os pokemons que não são do jogador principal
        pokemonsParaDistribuir.addAll(treinadorComputador.getPokemonsNaMochila());
        pokemonsParaDistribuir.add(new PokemonAgua("Squirtle", 100, 15));
        pokemonsParaDistribuir.add(new PokemonFloresta("Bulbasaur", 110, 12));
        pokemonsParaDistribuir.add(new PokemonTerra("Diglett", 90, 18));
        pokemonsParaDistribuir.add(new PokemonEletrico("Pikachu", 95, 16));

        posicionarListaDePokemon(pokemonsParaDistribuir);
    }
    
    private void posicionarListaDePokemon(List<Pokemon> lista) {
        for (Pokemon p : lista) {
            boolean posicionado = false;
            while (!posicionado) {
                try {
                    int linha = random.nextInt(tabuleiro.getTamanho());
                    int coluna = random.nextInt(tabuleiro.getTamanho());
                    tabuleiro.posicionarPokemon(p, linha, coluna);
                    posicionado = true;
                } catch (Exception e) {
                    // Continua tentando
                }
            }
        }
    }

    // ... (O resto da classe Jogo continua igual a partir daqui)
    public String processarJogada(int linha, int coluna) {
        if (batalhaAtual != null && !batalhaAtual.isTerminada()) {
            return "Termine a batalha atual antes de explorar!";
        }
        Celula celulaClicada = tabuleiro.getCelula(linha, coluna);
        if (celulaClicada.isRevelada()) {
            return "Você já explorou esta área!";
        }
        if (celulaClicada.estaOcupada()) {
            Pokemon pokemonEncontrado = celulaClicada.getPokemon();
            String mensagem;
            if (pokemonEncontrado.isSelvagem()) {
                celulaClicada.revelar();
                if (random.nextInt(100) < 60) {
                    treinadorUsuario.capturar(pokemonEncontrado);
                    mensagem = "Você encontrou um " + pokemonEncontrado.getNome() + " selvagem e o capturou!";
                } else {
                    mensagem = "Ah, não! O " + pokemonEncontrado.getNome() + " selvagem escapou!";
                }
            } else if (pokemonEncontrado.getTreinador() != this.treinadorUsuario) {
                iniciarBatalha(treinadorUsuario.getProximoPokemonParaBatalha(), pokemonEncontrado, celulaClicada);
                mensagem = "O " + pokemonEncontrado.getNome() + " de " + pokemonEncontrado.getTreinador().getNome() + " quer batalhar!";
            } else {
                mensagem = "Você encontrou seu próprio Pokémon: " + pokemonEncontrado.getNome();
                celulaClicada.revelar();
            }
            return mensagem;
        } else {
            celulaClicada.revelar();
            return "Não há nada aqui...";
        }
    }
    
    public String usarDica(int linha, int coluna) {
        if (!treinadorUsuario.usarDica()) {
            return "Você não tem mais dicas para usar!";
        }
        if (linha < 0 || linha >= tabuleiro.getTamanho() || coluna < 0 || coluna >= tabuleiro.getTamanho()) {
            return "Posição inválida!";
        }
        boolean pokemonEncontrado = false;
        for (int j = 0; j < tabuleiro.getTamanho(); j++) {
            if (tabuleiro.getCelula(linha, j).estaOcupada()) {
                pokemonEncontrado = true;
                break;
            }
        }
        if (!pokemonEncontrado) {
            for (int i = 0; i < tabuleiro.getTamanho(); i++) {
                if (tabuleiro.getCelula(i, coluna).estaOcupada()) {
                    pokemonEncontrado = true;
                    break;
                }
            }
        }
        return pokemonEncontrado ? "Dica: Há um Pokémon na linha " + linha + " ou na coluna " + coluna + "!" : "Dica: Não há nenhum Pokémon na linha " + linha + " ou na coluna " + coluna + ".";
    }

    private void iniciarBatalha(Pokemon desafiante, Pokemon desafiado, Celula celulaDaBatalha) {
        this.batalhaAtual = new Batalha(desafiante, desafiado, celulaDaBatalha.getTipoTerreno());
        this.celulaDaBatalhaAtual = celulaDaBatalha;
    }

    public void encerrarBatalha() {
        this.batalhaAtual = null;
        this.celulaDaBatalhaAtual = null;
    }

    public void removerPokemonVencido() {
        if (celulaDaBatalhaAtual != null) {
            celulaDaBatalhaAtual.setPokemon(null);
            celulaDaBatalhaAtual.revelar();
        }
    }

    public void revelarTodoTabuleiro() {
        for (int i = 0; i < tabuleiro.getTamanho(); i++) {
            for (int j = 0; j < tabuleiro.getTamanho(); j++) {
                tabuleiro.getCelula(i, j).revelar();
            }
        }
    }

    public void restaurarTimesAposBatalha() {
        treinadorUsuario.restabelecerEnergiaDoTime();
        treinadorComputador.restabelecerEnergiaDoTime();
    }

    public boolean verificarFimDeJogo() {
        for (int i = 0; i < tabuleiro.getTamanho(); i++) {
            for (int j = 0; j < tabuleiro.getTamanho(); j++) {
                Celula c = tabuleiro.getCelula(i, j);
                if (c.estaOcupada() && c.getPokemon().isSelvagem()) {
                    return false;
                }
            }
        }
        return true;
    }

    // Getters e Setters
    public Tabuleiro getTabuleiro() { return this.tabuleiro; }
    public Batalha getBatalhaAtual() { return this.batalhaAtual; }
    public int getDicasRestantes() { return this.treinadorUsuario.getDicasRestantes(); }
    public int getPontuacaoJogador() { return treinadorUsuario.getPontuacao(); }
    public int getPontuacaoComputador() { return treinadorComputador.getPontuacao(); }
    public List<Pokemon> getPokemonsDoJogador() { return this.treinadorUsuario.getPokemonsNaMochila(); }
    public void trocarPokemonDoJogador(int indice) { this.treinadorUsuario.trocarPokemonPrincipal(indice); }
    public boolean isJogadorPosicionado() { return jogadorPosicionado; }
    public void setJogadorPosicionado(boolean jogadorPosicionado) { this.jogadorPosicionado = jogadorPosicionado; }
}