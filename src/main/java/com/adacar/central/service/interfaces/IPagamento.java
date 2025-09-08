package com.adacar.central.service.interfaces;

import com.adacar.central.model.Aluguel;

public interface IPagamento {
    double calcularValorTotal(Aluguel aluguel);
}
