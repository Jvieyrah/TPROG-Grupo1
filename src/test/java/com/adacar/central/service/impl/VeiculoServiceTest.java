package com.adacar.central.service.impl;

import com.adacar.central.enums.StatusVeiculo;
import com.adacar.central.enums.TipoVeiculo;
import com.adacar.central.model.Veiculo;
import com.adacar.central.repository.impl.VeiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para a classe VeiculoService")
class VeiculoServiceTest {

    @InjectMocks
    private VeiculoService veiculoService;

    @Mock
    private VeiculoRepository veiculoRepository;

    @BeforeEach
    void setUp() {
        veiculoService = new VeiculoService();
        veiculoService.setVeiculoRepository(veiculoRepository);
    }

    @Test
    @DisplayName("Deve salvar um veículo válido")
    void cadastrar_DeveSalvarVeiculo_QuandoDadosValidos() throws IOException {
        Veiculo veiculoValido = new Veiculo("ABC1D24", "Gol 1.0", TipoVeiculo.SUV);
        // Arrange
        doNothing().when(veiculoRepository).save(veiculoValido);

        // Act
        String resultado = veiculoService.cadastrar(veiculoValido);

        // Assert
        verify(veiculoRepository, times(1)).save(veiculoValido);
        assertEquals("Veiculo inserido com sucesso!", resultado);
    }


    @Test
    void cadastrar_DeveLancarExcecao_QuandoVeiculoNulo() {
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> veiculoService.cadastrar(null));

        assertEquals("O veículo não pode ser nulo", exception.getMessage());
    }

    @Test
    void cadastrar_DeveLancarExcecao_QuandoPlacaInvalida() {
        // Arrange
        Veiculo veiculoInvalido = new Veiculo("123", "Carro Invalido", TipoVeiculo.PEQUENO);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> veiculoService.cadastrar(veiculoInvalido));

        assertTrue(exception.getMessage().contains("Formato de placa inválido"));
    }

    @Test
    void cadastrar_DeveLancarExcecao_QuandoNomeVazio() {
        // Arrange
        Veiculo veiculoInvalido = new Veiculo("ABC1D23", "", TipoVeiculo.SUV);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> veiculoService.cadastrar(veiculoInvalido));

        assertEquals("O nome do veículo é obrigatório", exception.getMessage());
    }

    @Test
    void alterar_DeveLancarExcecao_QuandoErroNoRepositorio() throws IOException {
        Veiculo veiculoValido = new Veiculo("ABC1D23", "Carro sem tipo", TipoVeiculo.PEQUENO);
        // Arrange
        doThrow(new IOException("Erro de IO")).when(veiculoRepository).update(any(Veiculo.class));

        // Act
        String resultado = veiculoService.alterar(veiculoValido);

        // Assert
        assertEquals("Erro ao alterar veículo", resultado);
    }

    @Test
    void buscarPorPlaca_DeveRetornarVeiculo_QuandoPlacaExistente() throws IOException {
        Veiculo veiculoValido = new Veiculo("ABC1D23", "Carro sem tipo", TipoVeiculo.PEQUENO);
        // Arrange
        String placa = "ABC1D23";
        when(veiculoRepository.findById(placa)).thenReturn(veiculoValido);

        // Act
        Veiculo resultado = veiculoService.buscarPorPlaca(placa);

        // Assert
        assertNotNull(resultado);
        assertEquals(placa, resultado.getPlaca());
        verify(veiculoRepository, times(1)).findById(placa);
    }

    @Test
    void buscarPorPlaca_DeveRetornarNull_QuandoErroNoRepositorio() throws IOException {
        // Arrange
        String placa = "ABC1D23";
        when(veiculoRepository.findById(placa)).thenThrow(new IOException("Erro de IO"));

        // Act
        Veiculo resultado = veiculoService.buscarPorPlaca(placa);

        // Assert
        assertNull(resultado);
    }

    @Test
    void buscarPorParteNome_DeveRetornarVeiculo_QuandoEncontrado() throws IOException {
        Veiculo veiculoValido = new Veiculo("ABC1D23", "Carro sem tipo", TipoVeiculo.PEQUENO);
        // Arrange
        String nomeParcial = "Gol";
        when(veiculoRepository.findById(nomeParcial)).thenReturn(veiculoValido);

        // Act
        Veiculo resultado = veiculoService.buscarPorParteNome(nomeParcial);

        // Assert
        assertNotNull(resultado);
        assertEquals(veiculoValido.getNome(), resultado.getNome());
        verify(veiculoRepository, times(1)).findById(nomeParcial);
    }

    @Test
    void buscarPorParteNome_DeveLancarExcecao_QuandoNomeNuloOuVazio() {
        // Act & Assert
        assertThrows(RuntimeException.class, () -> veiculoService.buscarPorParteNome(null));
        assertThrows(RuntimeException.class, () -> veiculoService.buscarPorParteNome(""));
    }

    @Test
    void listarTodos_DeveRetornarListaDeVeiculos_QuandoExistiremRegistros() throws IOException {
        Veiculo veiculoValido = new Veiculo("ABC1D23", "Carro sem tipo", TipoVeiculo.PEQUENO);
        // Arrange
        List<Veiculo> veiculos = Arrays.asList(veiculoValido, new Veiculo("XYZ9A87", "HB20", TipoVeiculo.PEQUENO));
        when(veiculoRepository.findAll()).thenReturn(veiculos);

        // Act
        Optional<List<Veiculo>> resultado = veiculoService.listarTodos();

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(2, resultado.get().size());
        verify(veiculoRepository, times(1)).findAll();
    }

    @Test
    void listarTodos_DeveRetornarOptionalVazio_QuandoErroNoRepositorio() throws IOException {
        // Arrange
        when(veiculoRepository.findAll()).thenThrow(new IOException("Erro de IO"));

        // Act
        Optional<List<Veiculo>> resultado = veiculoService.listarTodos();

        // Assert
        assertTrue(resultado.isEmpty());
    }

    @Test
    void remover_DeveDeletarVeiculo_QuandoPlacaValida() throws IOException {
        // Arrange
        String placa = "ABC1D23";
        doNothing().when(veiculoRepository).delete(placa);

        // Act
        String resultado = veiculoService.remover(placa);

        // Assert
        assertEquals("ok", resultado);
        verify(veiculoRepository, times(1)).delete(placa);
    }

    @Test
    void remover_DeveRetornarMensagemDeErro_QuandoErroNoRepositorio() throws IOException {
        // Arrange
        String placa = "ABC1D23";
        doThrow(new IOException("Erro de IO")).when(veiculoRepository).delete(placa);

        // Act
        String resultado = veiculoService.remover(placa);

        // Assert
        assertEquals("Erro ao remover veículo", resultado);
    }

    @Test
    void listarDisponiveis_DeveRetornarApenasVeiculosDisponiveis() throws IOException {
        // Arrange
        Veiculo veiculoDisponivel = new Veiculo("ABC1D23", "Gol 1.0", TipoVeiculo.PEQUENO);
        Veiculo veiculoIndisponivel = new Veiculo("XYZ9A87", "HB20", TipoVeiculo.MEDIO);
        veiculoIndisponivel.setStatus(StatusVeiculo.ALUGADO);

        List<Veiculo> todosVeiculos = Arrays.asList(veiculoDisponivel, veiculoIndisponivel);
        when(veiculoRepository.findAll()).thenReturn(todosVeiculos);

        // Act
        List<Veiculo> resultado = veiculoService.listarDisponiveis();

        // Assert
        assertEquals(1, resultado.size());
        assertEquals(StatusVeiculo.DISPONIVEL, resultado.getFirst().getStatus());
        verify(veiculoRepository, times(1)).findAll();
    }

    @Test
    void listarDisponiveis_DeveRetornarListaVazia_QuandoNaoHouverVeiculos() throws IOException {
        // Arrange
        when(veiculoRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Veiculo> resultado = veiculoService.listarDisponiveis();

        // Assert
        assertTrue(resultado.isEmpty());
    }

    @Test
    void listarDisponiveis_DeveLancarExcecao_QuandoErroNoRepositorio() throws IOException {
        // Arrange
        when(veiculoRepository.findAll()).thenThrow(new IOException("Erro de IO"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> veiculoService.listarDisponiveis());
    }
}
