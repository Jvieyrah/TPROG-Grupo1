package com.adacar.central.model;


import com.adacar.central.enums.TipoVeiculo;

import java.util.Objects;

public class Veiculo {
    private String placa;
    private String nome;
    private TipoVeiculo tipo;
    private double valorDiaria;
    private boolean isAlugado;

    public Veiculo() {
    }

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

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoVeiculo getTipo() {
        return tipo;
    }

    public void setTipo(TipoVeiculo tipo) {
        this.tipo = tipo;
        this.valorDiaria = tipo.getValorDiaria();
    }

    public double getValorDiaria() {
        return valorDiaria;
    }

    public void setValorDiaria(double valorDiaria) {
        this.valorDiaria = valorDiaria;
    }

    public boolean getIsAlugado() {
        return isAlugado;
    }

    public void setIsAlugado(boolean isAlugado) {
        this.isAlugado = isAlugado;
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
