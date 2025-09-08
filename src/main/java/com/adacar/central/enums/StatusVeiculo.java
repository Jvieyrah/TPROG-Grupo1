package com.adacar.central.enums;

public enum StatusVeiculo {
    DISPONIVEL("Disponível para locação"),
    ALUGADO("Atualmente com um cliente"),
    EM_MANUTENCAO("Indisponível devido a manutenção");

    private final String descricao;

    StatusVeiculo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
