package com.adacar.central.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PessoaJuridica extends Cliente {
    @JsonProperty("cnpj")
    @JsonAlias({"documento"})
    private String cnpj;

    public PessoaJuridica() {
        super();
    }
    public PessoaJuridica(String nome, String cnpj) {
        super(nome);
        this.cnpj = cnpj;
    }

    @Override
    public String getDocumento() {
        return this.cnpj;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}