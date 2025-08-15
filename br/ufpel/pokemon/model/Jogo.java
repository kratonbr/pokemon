// Ficheiro: Jogo.java (VERSÃO COMPLETA E CORRIGIDA)
package br.ufpel.pokemon.model;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Jogo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Treinador treinadorUsuario;
    private Treinador treinadorComputador;
    private Tabuleiro tabuleiro;
    private transient Random random;
    private Batalha batalhaAtual;
    private Celula celulaDaBatalhaAtual;
    private boolean jogadorPosicionado = false;

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
    
    public void distribuirTodosOsPokemons() {
        List<Pokemon> todosOsPokemons = new ArrayList<>();
        // Adiciona os Pokémon dos treinadores
        todosOsPokemons.addAll(treinadorUsuario.getPokemonsNaMochila());
        
        // Adiciona os Pokémon do PC, garantindo que não são duplicados se já estiverem em `todosOsPokemons`
        Pokemon pidgeyDoPC = new PokemonFloresta("Pidgey", 70, 10);
        treinadorComputador.capturar(pidgeyDoPC);
        todosOsPokemons.add(pidgeyDoPC);
        Pokemon rattataDoPC = new PokemonTerra("Rattata", 65, 12);
        treinadorComputador.capturar(rattataDoPC);
        todosOsPokemons.add(rattataDoPC);

        // Adiciona os selvagens
        todosOsPokemons.add(new PokemonAgua("Squirtle", 100, 15));
        todosOsPokemons.add(new PokemonFloresta("Bulbasaur", 110, 12));
        todosOsPokemons.add(new PokemonTerra("Diglett", 90, 18));
        todosOsPokemons.add(new PokemonEletrico("Pikachu", 95, 16));
        
        posicionarListaDePokemon(todosOsPokemons);
    }

    public void distribuirPokemonsRestantes() {
        List<Pokemon> pokemonsParaDistribuir = new ArrayList<>();
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
                    // Verifica se a célula já não está ocupada ANTES de tentar posicionar
                    if (!tabuleiro.getCelula(linha, coluna).estaOcupada()) {
                        tabuleiro.posicionarPokemon(p, linha, coluna);
                        posicionado = true;
                    }
                } catch (Exception e) {
                    // Continua tentando
                }
            }
        }
    }

    // ESTE É UM DOS MÉTODOS QUE ESTAVA A FALTAR
// MÉTODO PROCESSARJOGADA TOTALMENTE CORRIGIDO
    public String processarJogada(int linha, int coluna) {
        if (batalhaAtual != null && !batalhaAtual.isTerminada()) {
            return "Termine a batalha atual antes de explorar!";
        }
        Celula celulaClicada = tabuleiro.getCelula(linha, coluna);

        // Se a célula já foi revelada e está vazia, não há nada a fazer.
        if (celulaClicada.isRevelada() && !celulaClicada.estaOcupada()) {
            return "Você já explorou esta área e não há nada aqui.";
        }
        
        // Verifica se é a primeira vez que se clica nesta célula.
        boolean primeiraVez = !celulaClicada.isRevelada();
        
        // Se for a primeira vez, a célula é revelada para o jogador.
        if (primeiraVez) {
            celulaClicada.revelar();
        }

        // Se a célula está ocupada, processa a interação (captura ou batalha).
        if (celulaClicada.estaOcupada()) {
            Pokemon pokemonEncontrado = celulaClicada.getPokemon();
            String mensagem;

            // Lógica para Pokémon Selvagem
            if (pokemonEncontrado.isSelvagem()) {
                if (random.nextInt(100) < 60) { // Chance de captura
                    treinadorUsuario.capturar(pokemonEncontrado);
                    mensagem = (primeiraVez ? "Você encontrou e capturou um " : "Você tentou de novo e capturou o ") + pokemonEncontrado.getNome() + " selvagem!";
                } else {
                    mensagem = "Ah, não! O " + pokemonEncontrado.getNome() + " selvagem escapou" + (primeiraVez ? "!" : " novamente!");
                }
            } 
            // Lógica para Pokémon do Computador
            else if (pokemonEncontrado.getTreinador() == treinadorComputador) {
                iniciarBatalha(treinadorUsuario.getProximoPokemonParaBatalha(), pokemonEncontrado, celulaClicada);
                mensagem = "O " + pokemonEncontrado.getNome() + " de " + pokemonEncontrado.getTreinador().getNome() + " quer batalhar!";
            } 
            // Lógica para Pokémon do próprio jogador
            else if (pokemonEncontrado.getTreinador() == treinadorUsuario) {
                 mensagem = "Você encontrou seu próprio Pokémon: " + pokemonEncontrado.getNome();
            } else {
                mensagem = "Um Pokémon misterioso está aqui.";
            }
            return mensagem;
        } 
        // Se a célula não está ocupada (e era a primeira vez a clicar)
        else {
            return "Não há nada aqui...";
        }
    }

    // ESTE É O OUTRO MÉTODO QUE ESTAVA A FALTAR
    public String processarJogadaPC(int linha, int coluna) {
        Celula celulaClicada = tabuleiro.getCelula(linha, coluna);
        if (celulaClicada.estaOcupada()) {
            Pokemon pokemonEncontrado = celulaClicada.getPokemon();
            String mensagem;
            if (pokemonEncontrado.isSelvagem()) {
                celulaClicada.revelar();
                if (random.nextInt(100) < 60) {
                    treinadorComputador.capturar(pokemonEncontrado);
                    mensagem = "O computador encontrou um " + pokemonEncontrado.getNome() + " selvagem e o capturou!";
                } else {
                    mensagem = "O " + pokemonEncontrado.getNome() + " selvagem escapou do computador!";
                }
            } else {
                mensagem = "O computador encontrou o Pokémon " + pokemonEncontrado.getNome() + ".";
                celulaClicada.revelar();
            }
            return mensagem;
        } else {
            celulaClicada.revelar();
            return "O computador não encontrou nada...";
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
     public boolean existemCelulasNaoReveladas() {
        for (int i = 0; i < tabuleiro.getTamanho(); i++) {
            for (int j = 0; j < tabuleiro.getTamanho(); j++) {
                if (!tabuleiro.getCelula(i, j).isRevelada()) {
                    return true; // Encontrou pelo menos uma célula jogável
                }
            }
        }
        return false; // Nenhuma célula não revelada foi encontrada
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