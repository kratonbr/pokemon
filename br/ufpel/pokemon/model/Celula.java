package br.ufpel.pokemon.model;

public class Celula {
    private Pokemon pokemon;
    private final int linha;
    private final int coluna;
    private boolean foiRevelada;
    private String tipoTerreno;

    public Celula(int linha, int coluna, int tamanhoN) {
        this.linha = linha;
        this.coluna = coluna;
        this.foiRevelada = false;
        this.pokemon = null;
        
        int metade = tamanhoN / 2;
        if (linha < metade && coluna < metade) {
            this.tipoTerreno = "Agua";
        } else if (linha < metade && coluna >= metade) {
            this.tipoTerreno = "Floresta";
        } else if (linha >= metade && coluna < metade) {
            this.tipoTerreno = "Terra";
        } else {
            this.tipoTerreno = "Eletricidade";
        }
    }
    
    public boolean estaOcupada() { return pokemon != null; }
    public Pokemon getPokemon() { return this.pokemon; }
    public void setPokemon(Pokemon p) { this.pokemon = p; }
    public boolean isRevelada() { return this.foiRevelada; }
    public void revelar() { this.foiRevelada = true; }
    public String getTipoTerreno() { return this.tipoTerreno; }
}