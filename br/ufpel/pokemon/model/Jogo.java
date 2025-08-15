// Pacote: br.ufpel.pokemon.model
package br.ufpel.pokemon.model;

import br.ufpel.pokemon.exception.RegiaoInvalidaException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Jogo {
    private Treinador treinadorUsuario;
    private Treinador treinadorComputador;
    private Tabuleiro tabuleiro;
    private Random random;
    private Batalha batalhaAtual;
    private Celula celulaDaBatalhaAtual;

    public Jogo() {
        this.tabuleiro = new Tabuleiro(8);
        this.random = new Random();
        this.batalhaAtual = null;
        this.celulaDaBatalhaAtual = null;
        
        inicializarTreinadores();
        distribuirPokemonsAleatoriamente();
    }

    private void inicializarTreinadores() {
        Pokemon pokemonInicialJogador = new PokemonAgua("Meu Magikarp", 10, 1);
        this.treinadorUsuario = new Treinador("Ash", pokemonInicialJogador, false);
        
        Pokemon pokemonInicialPC = new PokemonTerra("Meu Sandshrew", 10, 1);
        this.treinadorComputador = new Treinador("Gary", pokemonInicialPC, true);
    }

    public void distribuirPokemonsAleatoriamente() {
        List<Pokemon> pokemonsParaDistribuir = new ArrayList<>();
        pokemonsParaDistribuir.add(new PokemonAgua("Squirtle", 100, 15));
        pokemonsParaDistribuir.add(new PokemonFloresta("Bulbasaur", 110, 12));
        pokemonsParaDistribuir.add(new PokemonTerra("Diglett", 90, 18));
        pokemonsParaDistribuir.add(new PokemonEletrico("Pikachu", 95, 16));
        Pokemon pidgeyDoPC = new PokemonFloresta("Pidgey", 70, 10);
        treinadorComputador.capturar(pidgeyDoPC);
        pokemonsParaDistribuir.add(pidgeyDoPC);
        Pokemon rattataDoPC = new PokemonTerra("Rattata", 65, 12);
        treinadorComputador.capturar(rattataDoPC);
        pokemonsParaDistribuir.add(rattataDoPC);

        for (Pokemon p : pokemonsParaDistribuir) {
            boolean posicionado = false;
            while (!posicionado) {
                try {
                    int linha = random.nextInt(tabuleiro.getTamanho());
                    int coluna = random.nextInt(tabuleiro.getTamanho());
                    tabuleiro.posicionarPokemon(p, linha, coluna);
                    posicionado = true;
                } catch (Exception e) {
                    // Continua a tentar se a célula estiver ocupada ou a região for inválida
                }
            }
        }
    }

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
            } else {
                iniciarBatalha(treinadorUsuario.getProximoPokemonParaBatalha(), pokemonEncontrado, celulaClicada);
                mensagem = "O " + pokemonEncontrado.getNome() + " de " + pokemonEncontrado.getTreinador().getNome() + " quer batalhar!";
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

    public List<Pokemon> getPokemonsDoJogador() {
        return this.treinadorUsuario.getPokemonsNaMochila();
    }

    public void trocarPokemonDoJogador(int indice) {
        this.treinadorUsuario.trocarPokemonPrincipal(indice);
    }

    // Getters
    public Tabuleiro getTabuleiro() { return this.tabuleiro; }
    public Batalha getBatalhaAtual() { return this.batalhaAtual; }
    public int getDicasRestantes() { return this.treinadorUsuario.getDicasRestantes(); }
    public int getPontuacaoJogador() { return treinadorUsuario.getPontuacao(); }
    public int getPontuacaoComputador() { return treinadorComputador.getPontuacao(); }
}
