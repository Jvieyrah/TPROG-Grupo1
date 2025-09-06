package com.adacar.central.service.impl;

import com.adacar.central.service.interfaces.IDesconto;

public class DescontoPessoaJuridica implements IDesconto {

    private final String tipoPessoa = "Pessoa Jurídica";

    @Override
    public double calcular(int diarias) {
        if (diarias > 3) {
            return 0.10;
        }
        return 0.0;
    }

    public String getTipoPessoa() {
        return tipoPessoa;
    }
}