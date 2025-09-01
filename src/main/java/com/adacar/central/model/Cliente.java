package com.adacar.central.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(
    use = Id.DEDUCTION,
    include = JsonTypeInfo.As.EXTERNAL_PROPERTY // propriedade não existe no JSON. Jackson usa isso internamente para sua lógica de dedução.
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PessoaFisica.class),
    @JsonSubTypes.Type(value = PessoaJuridica.class)
})
public abstract class Cliente {
    protected String nome;

    protected Cliente() {
    }

    protected Cliente(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public abstract String getDocumento();

    @Override
    public String toString() {
        return "Cliente{" +
                "nome='" + nome + '\'' +
                ", documento='" + getDocumento() + '\'' +
                '}';
    }
}