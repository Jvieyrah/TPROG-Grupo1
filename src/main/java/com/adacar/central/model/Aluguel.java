package com.adacar.central.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Aluguel {
    private Cliente cliente;
    private Veiculo veiculo;


    private String localRetirada;
    private LocalDateTime dataHoraRetirada;
    private LocalDateTime dataHoraDevolucao;

    public Aluguel() {
    }


    public Aluguel(Cliente cliente, Veiculo veiculo, String localRetirada, LocalDateTime dataHoraRetirada) {
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.localRetirada = localRetirada;
        this.dataHoraRetirada = dataHoraRetirada;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public LocalDateTime getDataHoraRetirada() {
        return dataHoraRetirada;
    }

    public LocalDateTime getDataHoraDevolucao() {
        return dataHoraDevolucao;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public String getLocalRetirada() {
        return localRetirada;
    }

    public void setLocalRetirada(String localRetirada) {
        this.localRetirada = localRetirada;
    }

    public void setDataHoraRetirada(LocalDateTime dataHoraRetirada) {
        this.dataHoraRetirada = dataHoraRetirada;
    }

    public void setDataHoraDevolucao(LocalDateTime dataHoraDevolucao) {
        this.dataHoraDevolucao = dataHoraDevolucao;
    }

    @Override
    public String toString() {
        return "Aluguel{" +
                "cliente=" + cliente.getNome() +
                ", veiculo=" + veiculo.getNome() + " (" + veiculo.getPlaca() + ")" +
                ", localRetirada='" + localRetirada + '\'' +
                ", dataHoraRetirada=" + dataHoraRetirada +
                ", dataHoraDevolucao=" + dataHoraDevolucao +
                '}';
    }
}