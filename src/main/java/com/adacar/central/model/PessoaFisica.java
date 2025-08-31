package com.adacar.central.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PessoaFisica extends Cliente {
    @JsonProperty("cpf")
    @JsonAlias({"documento"})
    private String cpf;

    public PessoaFisica() {
      super();
    }

    public PessoaFisica(String nome, String cpf) {
        super(nome);
        this.cpf = cpf;
    }

    @Override
    public String getDocumento() {
        return this.cpf;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
