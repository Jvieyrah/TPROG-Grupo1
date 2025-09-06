package com.adacar.central.service.impl;

import com.adacar.central.enums.TipoVeiculo;
import com.adacar.central.model.Aluguel;
import com.adacar.central.model.PessoaFisica;
import com.adacar.central.model.PessoaJuridica;
import com.adacar.central.model.Veiculo;
import com.adacar.central.service.interfaces.IDesconto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para a classe PagamentoService")
class PagamentoServiceTest {

    @InjectMocks
    private PagamentoService pagamentoService;

    @Mock
    private IDesconto descontoMock;

    @BeforeEach
    void setUp() {
        // Inicializa o serviço que será testado, injetando o mock de desconto.
        pagamentoService = new PagamentoService(descontoMock);
    }

    @Test
    @DisplayName("Deve calcular o valor total para 2 dias de aluguel sem desconto")
    void calcularValorTotalSemDesconto() {
        PessoaFisica cliente = new PessoaFisica("João", "12345678900");
        Veiculo veiculo = new Veiculo("DEF5678", "Chevrolet", "Onix", TipoVeiculo.PEQUENO);
        veiculo.setValorDiaria(100.0);

        LocalDateTime dataRetirada = LocalDateTime.of(2025, 10, 10, 10, 0);
        LocalDateTime dataDevolucao = LocalDateTime.of(2025, 10, 12, 10, 0); // 2 dias

        Aluguel aluguel = new Aluguel(cliente, veiculo, "Filial B", dataRetirada);
        aluguel.setDataHoraDevolucao(dataDevolucao);

        when(descontoMock.calcular(2)).thenReturn(0.0);

        double valorFinal = pagamentoService.calcularValorTotal(aluguel);

        assertEquals(200.0, valorFinal);
    }

    @Test
    @DisplayName("Deve calcular o valor total com desconto de 10%")
    void calcularValorTotalComDesconto() {
        PessoaFisica cliente = new PessoaFisica("Maria", "11122233344");
        Veiculo veiculo = new Veiculo("ABC1234", "Ford", "Ka", TipoVeiculo.PEQUENO);
        veiculo.setValorDiaria(100.0);

        LocalDateTime dataRetirada = LocalDateTime.of(2025, 10, 10, 10, 0);
        LocalDateTime dataDevolucao = LocalDateTime.of(2025, 10, 13, 10, 0); // 3 dias

        Aluguel aluguel = new Aluguel(cliente, veiculo, "Filial C", dataRetirada);
        aluguel.setDataHoraDevolucao(dataDevolucao);

        when(descontoMock.calcular(3)).thenReturn(0.10);

        // Ação: Chama o método a ser testado.
        double valorFinal = pagamentoService.calcularValorTotal(aluguel);

        // Verificação: O valor esperado é (3 * R$ 100,00) - 10% = R$ 270,00.
        assertEquals(270.0, valorFinal);
    }

    @Test
    @DisplayName("Deve calcular 1 dia de aluguel mesmo com menos de 24h")
    void calcularValorDiarioMenos24h() {
        // Cenário: Um aluguel de 12 horas com um veículo de R$ 100,00 a diária.
        PessoaJuridica cliente = new PessoaJuridica("Empresa", "12345678901234");
        Veiculo veiculo = new Veiculo("XYZ9876", "Honda", "Civic", TipoVeiculo.SUV);
        veiculo.setValorDiaria(150.0);

        LocalDateTime dataRetirada = LocalDateTime.of(2025, 10, 15, 10, 0);
        LocalDateTime dataDevolucao = LocalDateTime.of(2025, 10, 15, 22, 0); // 12 horas

        Aluguel aluguel = new Aluguel(cliente, veiculo, "Filial D", dataRetirada);
        aluguel.setDataHoraDevolucao(dataDevolucao);

        when(descontoMock.calcular(1)).thenReturn(0.0);

        double valorFinal = pagamentoService.calcularValorTotal(aluguel);

        assertEquals(150.0, valorFinal);
    }

    @Test
    @DisplayName("Deve calcular valor total com desconto para Pessoa Física")
    void calcularValorTotalComDescontoPessoaFisica() {
        // Cenário: Um aluguel de 5 dias para Pessoa Física com 5% de desconto.
        PessoaFisica cliente = new PessoaFisica("Ana", "99988877766");
        Veiculo veiculo = new Veiculo("GHI7890", "Toyota", "Corolla", TipoVeiculo.MEDIO);
        veiculo.setValorDiaria(120.0);

        LocalDateTime dataRetirada = LocalDateTime.of(2025, 11, 1, 10, 0);
        LocalDateTime dataDevolucao = LocalDateTime.of(2025, 11, 6, 10, 0); // 5 dias

        Aluguel aluguel = new Aluguel(cliente, veiculo, "Filial E", dataRetirada);
        aluguel.setDataHoraDevolucao(dataDevolucao);

        when(descontoMock.calcular(5)).thenReturn(0.05);

        // Ação: Chama o método a ser testado.
        double valorBase = 5 * 120.0; // 600.0
        double valorFinalEsperado = valorBase - (valorBase * 0.05);
        double valorFinal = pagamentoService.calcularValorTotal(aluguel);

        // Verificação
        assertEquals(valorFinalEsperado, valorFinal);
    }

    @Test
    @DisplayName("Deve calcular valor total com desconto para Pessoa Jurídica")
    void calcularValorTotalComDescontoPessoaJuridica() {
        // Cenário: Um aluguel de 3 dias para Pessoa Jurídica com 10% de desconto.
        PessoaJuridica cliente = new PessoaJuridica("TechCorp", "00112233445566");
        Veiculo veiculo = new Veiculo("JKL1234", "Ford", "Transit", TipoVeiculo.SUV);
        veiculo.setValorDiaria(150.0);

        LocalDateTime dataRetirada = LocalDateTime.of(2025, 11, 10, 10, 0);
        LocalDateTime dataDevolucao = LocalDateTime.of(2025, 11, 13, 10, 0); // 3 dias

        Aluguel aluguel = new Aluguel(cliente, veiculo, "Filial F", dataRetirada);
        aluguel.setDataHoraDevolucao(dataDevolucao);

        // Ação: O mock do desconto retorna 0.10 (10%).
        when(descontoMock.calcular(3)).thenReturn(0.10);

        // Ação: Chama o método a ser testado.
        double valorBase = 3 * 150.0; // 450.0
        double valorFinalEsperado = valorBase - (valorBase * 0.10); // 450.0 - 45.0 = 405.0
        double valorFinal = pagamentoService.calcularValorTotal(aluguel);

        // Verificação
        assertEquals(valorFinalEsperado, valorFinal);
    }

    @Test
    @DisplayName("Deve lançar exceção se as datas não forem informadas")
    void deveLancarExcecaoSeDatasNaoInformadas() {
        // Cenário: Aluguel com data de devolução nula.
        PessoaFisica cliente = new PessoaFisica("Ana", "55566677788");
        Veiculo veiculo = new Veiculo("DEF5678", "Chevrolet", "Onix", TipoVeiculo.PEQUENO);

        Aluguel aluguel = new Aluguel(cliente, veiculo, "Filial E", LocalDateTime.now());
        aluguel.setDataHoraDevolucao(null);

        // Ação e Verificação: O método deve lançar uma exceção.
        assertThrows(IllegalArgumentException.class, () -> pagamentoService.calcularValorTotal(aluguel));
    }
}
