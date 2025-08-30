package com.adacar.central.model;

public abstract class Cliente {
    protected String nome;

    public Cliente(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public abstract String getDocumento();

    @Override
    public String toString() {
        return "Cliente{" +
                "nome='" + nome + '\'' +
                ", documento='" + getDocumento() + '\'' +
                '}';
    }
}