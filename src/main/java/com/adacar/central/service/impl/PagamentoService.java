package com.adacar.central.service.impl;

import com.adacar.central.model.Aluguel;
import com.adacar.central.service.interfaces.IPagamento;
import java.time.Duration;
import java.util.function.Function; // Importar

public class PagamentoService implements IPagamento {

    public PagamentoService() {
    }

    private long calcularDiarias(Aluguel aluguel) {
        if (aluguel.getDataHoraRetirada() == null || aluguel.getDataHoraDevolucao() == null) {
            throw new IllegalArgumentException("Datas de retirada e devolução devem ser informadas.");
        }
        Duration duracao = Duration.between(aluguel.getDataHoraRetirada(), aluguel.getDataHoraDevolucao());
        long horas = duracao.toHours();
        // Garante que qualquer fração de dia seja contada como uma diária completa
        return (horas / 24) + (horas % 24 > 0 ? 1 : 0);
    }

    @Override
    public double calcularValorTotal(Aluguel aluguel, Function<Integer, Double> politicaDesconto) {
        long diarias = calcularDiarias(aluguel);
        double valorBase = aluguel.getVeiculo().getValorDiaria() * diarias;

        double percentualDesconto = politicaDesconto.apply((int) diarias);

        return valorBase - (valorBase * percentualDesconto);
    }
}