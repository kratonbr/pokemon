// Ficheiro: Tabuleiro.java (CORRIGIDO)
package br.ufpel.pokemon.model;

import br.ufpel.pokemon.exception.RegiaoInvalidaException;

// Adicionada a permissão para ser salvo
public class Tabuleiro implements java.io.Serializable {
    private Celula[][] celulas;
    private int tamanhoN;

    public Tabuleiro(int tamanhoN) {
        this.tamanhoN = tamanhoN;
        this.celulas = new Celula[tamanhoN][tamanhoN];
        for (int i = 0; i < tamanhoN; i++) {
            for (int j = 0; j < tamanhoN; j++) {
                celulas[i][j] = new Celula(i, j, tamanhoN);
            }
        }
    }

    public void posicionarPokemon(Pokemon p, int linha, int coluna) throws Exception {
        validarRegiao(p, linha, coluna);
        Celula celulaAlvo = getCelula(linha, coluna);
        if (celulaAlvo.estaOcupada()) {
            throw new Exception("Celula ja ocupada");
        } else {
            celulaAlvo.setPokemon(p);
        }
    }
    
    private void validarRegiao(Pokemon p, int linha, int coluna) throws RegiaoInvalidaException {
        String tipoPokemon = p.getTipo();
        String tipoTerreno = getCelula(linha, coluna).getTipoTerreno();
        boolean regiaoCorreta = false;
        switch (tipoPokemon) {
            case "Agua":
                if (tipoTerreno.equals("Agua")) regiaoCorreta = true;
                break;
            case "Floresta":
                if (tipoTerreno.equals("Floresta")) regiaoCorreta = true;
                break;
            case "Terra":
                if (tipoTerreno.equals("Terra")) regiaoCorreta = true;
                break;
            case "Eletrico":
                if (tipoTerreno.equals("Eletricidade")) regiaoCorreta = true;
                break;
        }
        if (!regiaoCorreta) {
            throw new RegiaoInvalidaException("Pokemon do tipo " + tipoPokemon + " nao pode ser posicionado em terreno do tipo " + tipoTerreno + ".");
        }
    }
    
    public Celula getCelula(int linha, int coluna) {
        return celulas[linha][coluna];
    }

    public int getTamanho() { return this.tamanhoN; }
}