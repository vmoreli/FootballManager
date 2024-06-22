package com.footballmanager;

public class Jogador {

    private int id;
    private int idade;
    private String nomeJogador;
    private String nacionalidade;
    private String nomeClube;

    public Jogador(int id, int idade, String nomeJogador, String nacionalidade, String nomeClube) {
        this.id = id;
        this.idade = idade;
        this.nomeJogador = nomeJogador;
        this.nacionalidade = nacionalidade;
        this.nomeClube = nomeClube;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getNomeJogador() {
        return nomeJogador;
    }

    public void setNomeJogador(String nomeJogador) {
        this.nomeJogador = nomeJogador;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public String getNomeClube() {
        return nomeClube;
    }

    public void setNomeClube(String nomeClube) {
        this.nomeClube = nomeClube;
    }

    @Override
    public String toString() {
        return "ID: " + this.id + " IDADE: " + this.idade + " NOME: " + this.nomeJogador +
                " PAÍS: " + this.nacionalidade + " CLUBE: " + this.nomeClube;
    }
}
