package com.adacar.central.service.impl;

import com.adacar.central.service.interfaces.IDesconto;

public class DescontoPessoaFisica implements IDesconto {

    private final String tipoPessoa = "Pessoa Física";

    @Override
    public double calcular(int diarias) {
        if (diarias > 5) {
            return 0.05;
        }
        return 0.0;
    }

    public String getTipoPessoa() {
        return tipoPessoa;
    }

}
