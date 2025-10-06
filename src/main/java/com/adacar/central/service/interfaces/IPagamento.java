package com.adacar.central.service.interfaces;

import com.adacar.central.model.Aluguel;

import java.util.function.Function;

public interface IPagamento {
    double calcularValorTotal(Aluguel aluguel, Function<Integer, Double> politicaDesconto);
}
