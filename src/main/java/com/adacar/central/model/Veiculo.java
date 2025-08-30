package com.adacar.central.model;


import com.adacar.central.enums.TipoVeiculo;

import java.util.Objects;

public class Veiculo {
    private String placa;
    private String nome;
    private TipoVeiculo tipo;
    private double valorDiaria;
    private boolean isAlugado;

    public Veiculo(String placa, String nome, TipoVeiculo tipo) {
        this.placa = placa;
        this.nome = nome;
        this.tipo = tipo;
        this.valorDiaria = tipo.getValorDiaria();
        this.isAlugado = false;
    }

    public String getPlaca() {
        return placa;
    }

    public String getNome() {
        return nome;
    }

    public TipoVeiculo getTipo() {
        return tipo;
    }

    public double getValorDiaria() {
        return valorDiaria;
    }

    public boolean isAlugado() {
        return isAlugado;
    }

    public void setAlugado(boolean alugado) {
        isAlugado = alugado;
    }

    @Override
    public String toString() {
        return "Veiculo{" +
                "placa='" + placa + '\'' +
                ", nome='" + nome + '\'' +
                ", tipo=" + tipo +
                ", valorDiaria=" + valorDiaria +
                ", isAlugado=" + isAlugado +
                '}';
    }
}
