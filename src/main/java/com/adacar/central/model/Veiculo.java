package com.adacar.central.model;


import com.adacar.central.enums.StatusVeiculo;
import com.adacar.central.enums.TipoVeiculo;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.Objects;

public class Veiculo {
    private String placa;
    private String nome;
    private TipoVeiculo tipo;
    private double valorDiaria;
    @JsonAlias({"status"})
    private StatusVeiculo statusVeiculo;


    public Veiculo() {
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Veiculo veiculo = (Veiculo) o;
        return Objects.equals(getPlaca(), veiculo.getPlaca());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getPlaca());
    }

    public Veiculo(String abc1234, String ford, String ka, TipoVeiculo pequeno) {
    }

    public Veiculo(String placa, String nome, TipoVeiculo tipo) {
        this.placa = placa;
        this.nome = nome;
        this.tipo = tipo;
        this.valorDiaria = tipo.getValorDiaria();
        this.statusVeiculo = StatusVeiculo.DISPONIVEL;
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

    public StatusVeiculo getStatus() {
        return statusVeiculo;
    }

    public void setStatus(StatusVeiculo status) {
        this.statusVeiculo = status;
    }

    public boolean podeSerAlugado() {
        return this.statusVeiculo == StatusVeiculo.DISPONIVEL;
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

    @Override
    public String toString() {
        return "Veiculo{" +
                "placa='" + placa + '\'' +
                ", nome='" + nome + '\'' +
                ", tipo=" + tipo +
                ", valorDiaria=" + valorDiaria +
                '}';
    }
}
