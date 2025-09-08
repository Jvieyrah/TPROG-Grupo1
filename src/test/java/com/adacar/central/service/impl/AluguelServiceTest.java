package com.adacar.central.service.impl;

import com.adacar.central.enums.StatusLocacao;
import com.adacar.central.enums.StatusVeiculo;
import com.adacar.central.enums.TipoVeiculo;
import com.adacar.central.model.Aluguel;
import com.adacar.central.model.PessoaFisica;
import com.adacar.central.model.Veiculo;
import com.adacar.central.repository.impl.AluguelRepository;
import com.adacar.central.repository.impl.VeiculoRepository;
import com.adacar.central.service.interfaces.IPagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para a classe AluguelService")
class AluguelServiceTest {

    @InjectMocks
    private AluguelService aluguelService;

    @Mock
    private AluguelRepository aluguelRepository;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private IPagamento pagamentoMock;

    @BeforeEach
    void setUp() {
        // Inicializa o serviço a ser testado, injetando os mocks
        aluguelService = new AluguelService(aluguelRepository, veiculoRepository, pagamentoMock);
    }

    @Test
    @DisplayName("Deve alugar um veículo com sucesso")
    void alugarVeiculoComSucesso() throws IOException {
        PessoaFisica cliente = new PessoaFisica("Maria", "11122233344");
        Veiculo veiculo = new Veiculo("ABC1234", "Ford", "Ka", TipoVeiculo.PEQUENO);
        veiculo.setStatus(StatusVeiculo.DISPONIVEL);
        veiculo.setValorDiaria(100.0);
        LocalDateTime dataRetirada = LocalDateTime.of(2025, 9, 10, 10, 0);

        Aluguel resultado = aluguelService.alugar(cliente, veiculo, "Filial A", dataRetirada);

        assertEquals(cliente, resultado.getCliente());
        assertEquals(veiculo, resultado.getVeiculo());
        assertEquals(StatusVeiculo.ALUGADO, veiculo.getStatus());

        verify(aluguelRepository, times(1)).save(any(Aluguel.class));
        verify(veiculoRepository, times(1)).update(any(Veiculo.class));
    }

    @Test
    @DisplayName("Deve lançar exceção se o veículo não estiver disponível")
    void alugarVeiculoIndisponivel() throws IOException {
        PessoaFisica cliente = new PessoaFisica("Maria", "11122233344");
        Veiculo veiculo = new Veiculo("ABC1234", "Ford", "Ka", TipoVeiculo.PEQUENO);
        veiculo.setStatus(StatusVeiculo.ALUGADO);
        veiculo.setValorDiaria(100.0);
        LocalDateTime dataRetirada = LocalDateTime.of(2025, 9, 10, 10, 0);

        assertThrows(IllegalStateException.class, () ->
                aluguelService.alugar(cliente, veiculo, "Filial A", dataRetirada));

        verify(aluguelRepository, never()).save(any(Aluguel.class));
        verify(veiculoRepository, never()).update(any(Veiculo.class));
    }

    @Test
    @DisplayName("Deve devolver um veículo e atualizar status do aluguel e veiculo")
    void devolverVeiculoEAtualizarStatus() throws IOException {
        PessoaFisica cliente = new PessoaFisica("Maria", "11122233344");
        Veiculo veiculo = new Veiculo("ABC1234", "Ford", "Ka", TipoVeiculo.PEQUENO);
        veiculo.setStatus(StatusVeiculo.ALUGADO);
        veiculo.setValorDiaria(100.0);
        LocalDateTime dataRetirada = LocalDateTime.of(2025, 9, 10, 10, 0);
        LocalDateTime dataDevolucao = LocalDateTime.of(2025, 9, 12, 10, 0);

        Aluguel aluguel = new Aluguel(cliente, veiculo, "Filial A", dataRetirada);

        when(pagamentoMock.calcularValorTotal(aluguel)).thenReturn(200.0);

        aluguelService.devolver(aluguel, "Filial A", dataDevolucao);

        assertEquals(StatusLocacao.FINALIZADA, aluguel.getStatus());
        assertEquals(StatusVeiculo.DISPONIVEL, veiculo.getStatus());

        verify(aluguelRepository, times(1)).update(aluguel);
        verify(veiculoRepository, times(1)).update(veiculo);
    }
}
