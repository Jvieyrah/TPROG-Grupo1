package com.adacar.central.model;

public class PessoaFisica extends Cliente {
    private String cpf;

    public PessoaFisica(String nome, String cpf) {
        super(nome);
        this.cpf = cpf;
    }

    @Override
    public String getDocumento() {
        return this.cpf;
    }
}
