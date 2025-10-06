package com.adacar.central.util; // Você pode criar um pacote 'util' para organização

import com.adacar.central.enums.TipoVeiculo;
import com.adacar.central.model.PessoaFisica;
import com.adacar.central.model.PessoaJuridica;
import com.adacar.central.model.Veiculo;

import java.util.Random;
import java.util.function.Supplier;

public class DadosDeTesteFactory {

    private static final String[] NOMES_PF = {"João Silva", "Maria Oliveira", "Carlos Pereira", "Ana Costa"};
    private static final String[] NOMES_PJ = {"Tech Solutions", "Build Corp", "Food Services", "Digital Marketing"};
    private static final Random random = new Random();


     // upplier que gera sempre a MESMA instância de Pessoa Física.

    public static final Supplier<PessoaFisica> geradorJoaoPF = () ->
            new PessoaFisica("João Vieira", "123.456.789-00");


    //Supplier que gera sempre a MESMA instância de Pessoa Jurídica.

    public static final Supplier<PessoaJuridica> geradorEmpresaXYZ = () ->
            new PessoaJuridica("Empresa XYZ", "12.345.678/0001-00");

    //Supplier que gera sempre o MESMO Veículo.

    public static final Supplier<Veiculo> geradorTesla = () ->
            new Veiculo("ABC-1234", "Tesla Model X", TipoVeiculo.SUV);



     //Supplier DINÂMICO que gera uma NOVA e DIFERENTE Pessoa Física a cada chamada.

    public static final Supplier<PessoaFisica> geradorPfAleatorio = () -> {
        String nome = NOMES_PF[random.nextInt(NOMES_PF.length)];
        String cpf = String.format("%03d.%03d.%03d-%02d",
                random.nextInt(1000),
                random.nextInt(1000),
                random.nextInt(1000),
                random.nextInt(100));
        return new PessoaFisica(nome, cpf);
    };


     //Supplier DINÂMICO que gera um NOVO e DIFERENTE Veiculo a cada chamada.

    public static final Supplier<Veiculo> geradorVeiculoAleatorio = () -> {
        String[] modelos = {"Fiat Mobi", "Hyundai HB20", "Jeep Renegade", "Toyota Corolla"};
        String placa = String.format("%c%c%c-%d%d%d%d",
                (char)('A' + random.nextInt(26)),
                (char)('A' + random.nextInt(26)),
                (char)('A' + random.nextInt(26)),
                random.nextInt(10),
                random.nextInt(10),
                random.nextInt(10),
                random.nextInt(10));
        return new Veiculo(placa, modelos[random.nextInt(modelos.length)], TipoVeiculo.PEQUENO);
    };
}