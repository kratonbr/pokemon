// Pacote: br.ufpel.pokemon.model
package br.ufpel.pokemon.model;

import br.ufpel.pokemon.model.ataque.EstrategiaDeAtaque;
import br.ufpel.pokemon.model.estado.EstadoPokemon;
import br.ufpel.pokemon.model.estado.EstadoNormal;
import br.ufpel.pokemon.model.estado.EstadoDesmaiado;

public abstract class Pokemon {
    protected String nome;
    protected int energia;
    protected int energiaMaxima;
    protected int forca;
    protected int experiencia;
    protected int nivel;
    protected String tipo;
    protected boolean selvagem;
    protected Treinador treinador;
    protected EstrategiaDeAtaque estrategiaDeAtaque;
    protected EstadoPokemon estado;
    protected String imagePath;
    private int experienciaParaProximoNivel;

    public Pokemon(String nome, int energiaMaxima, int forca, String tipo, EstrategiaDeAtaque estrategia, String imagePath) {
        this.nome = nome;
        this.energiaMaxima = energiaMaxima;
        this.energia = energiaMaxima;
        this.forca = forca;
        this.nivel = 1;
        this.experiencia = 0;
        this.selvagem = true;
        this.treinador = null;
        this.tipo = tipo;
        this.estrategiaDeAtaque = estrategia;
        this.estado = new EstadoNormal();
        this.imagePath = imagePath;
        this.experienciaParaProximoNivel = 100;
    }

    public void restabelecerEnergia() {
        this.energia = this.energiaMaxima;
        this.setEstado(new EstadoNormal());
    }

    public String ganharExperiencia(int xpGanha) {
        this.experiencia += xpGanha;
        String log = "";
        while (this.experiencia >= this.experienciaParaProximoNivel) {
            log += subirDeNivel();
        }
        return log;
    }

    private String subirDeNivel() {
        this.nivel++;
        this.experiencia -= this.experienciaParaProximoNivel;
        this.experienciaParaProximoNivel *= 1.5;
        this.energiaMaxima += 10;
        this.forca += 5;
        this.energia = this.energiaMaxima;
        return "\n" + this.nome + " subiu para o Nível " + this.nivel + "!";
    }

    public void receberDano(int dano, String tipoTerreno) {
        this.energia -= dano;
        if (this.energia <= 0) {
            this.energia = 0;
            setEstado(new EstadoDesmaiado());
        }
    }

    public void curar(int quantidade) {
        this.energia += quantidade;
        if (this.energia > this.energiaMaxima) {
            this.energia = this.energiaMaxima;
        }
    }

    /**
     * NOVO MÉTODO: Verifica se este Pokémon pertence ao computador.
     */
    public boolean isDoComputador() {
        return this.treinador != null && this.treinador.isComputador();
    }

    // Getters e Setters
    public EstadoPokemon getEstado() { return estado; }
    public void setEstado(EstadoPokemon estado) { this.estado = estado; }
    public String getNome() { return nome; }
    public String getTipo() { return this.tipo; }
    public boolean isSelvagem() { return this.selvagem; }
    public int getEnergia() { return energia; }
    public int getEnergiaMaxima() { return energiaMaxima; }
    public int getForca() { return forca; }
    public int getNivel() { return nivel; }
    public EstrategiaDeAtaque getEstrategiaDeAtaque() { return estrategiaDeAtaque; }
    public String getImagePath() { return imagePath; }
    public Treinador getTreinador() { return this.treinador; }
    public void setTreinador(Treinador treinador) {
        this.treinador = treinador;
        this.selvagem = (treinador == null);
    }
}
