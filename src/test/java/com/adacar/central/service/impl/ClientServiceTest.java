package com.adacar.central.service.impl;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import com.adacar.central.model.Cliente;
import com.adacar.central.model.PessoaFisica;
import com.adacar.central.model.PessoaJuridica;
import com.adacar.central.repository.impl.PessoaFisicaRepository;
import com.adacar.central.repository.impl.PessoaJuridicaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para a classe ClienteService")
class ClienteServiceTest {

  @InjectMocks
  private ClienteService clienteService;

  @Mock
  private PessoaFisicaRepository pessoaFisicaRepository;

  @Mock
  private PessoaJuridicaRepository pessoaJuridicaRepository;

  @Mock
  private CPFValidator cpfValidator;

  @Mock
  private CNPJValidator cnpjValidator;

  private final PessoaFisica pessoaFisicaMock = new PessoaFisica("João", "11111111111");
  private final PessoaJuridica pessoaJuridicaMock = new PessoaJuridica("Empresa Ltda", "11111111111111");

  @BeforeEach
  void setUp() {
    // Inicializa a classe de serviço e injeta os mocks
    clienteService = new ClienteService();
    clienteService.setPessoaFisicaRepository(pessoaFisicaRepository);
    clienteService.setPessoaJuridicaRepository(pessoaJuridicaRepository);
    clienteService.setCpfValidator(cpfValidator);
    clienteService.setCnpjValidator(cnpjValidator);
  }

  @Test
  @DisplayName("Deve cadastrar uma Pessoa Física com sucesso")
  void cadastrarPessoaFisicaComSucesso() throws IOException {
    // Cenário: O CPF é válido e não existe no repositório
    doNothing().when(cpfValidator).assertValid(anyString());
    when(pessoaFisicaRepository.findAll()).thenReturn(new ArrayList<>());
    when(pessoaJuridicaRepository.findAll()).thenReturn(new ArrayList<>());

    String resultado = clienteService.cadastrar("João", "11111111111");

    // Verificação
    assertEquals("Pessoa Física cadastrada com sucesso: João - 11111111111", resultado);
    verify(pessoaFisicaRepository, times(1)).save(any(PessoaFisica.class));
  }

  @Test
  @DisplayName("Deve retornar 'CPF inválido' se o documento for inválido")
  void cadastrarComCpfInvalido() throws IOException {
    // Cenário: O validador lança uma exceção para um CPF inválido
    doThrow(InvalidStateException.class).when(cpfValidator).assertValid(anyString());

    String resultado = clienteService.cadastrar("Maria", "12345678900");

    // Verificação
    assertEquals("CPF inválido: 12345678900", resultado);
    verify(pessoaFisicaRepository, never()).save(any(PessoaFisica.class));
  }

  @Test
  @DisplayName("Deve retornar 'Cliente já existe' se o documento já estiver cadastrado")
  void cadastrarComDocumentoJaExistente() throws IOException {
    // Cenário: O documento já existe no repositório
    List<PessoaFisica> pFisicas = new ArrayList<>();
    pFisicas.add(pessoaFisicaMock);

    when(pessoaFisicaRepository.findAll()).thenReturn(pFisicas);
    when(pessoaJuridicaRepository.findAll()).thenReturn(new ArrayList<>());

    String resultado = clienteService.cadastrar("João", "11111111111");

    // Verificação
    assertEquals("Cliente com documento 11111111111 já existe: João", resultado);
    verify(pessoaFisicaRepository, never()).save(any(PessoaFisica.class));
  }

  // --- Testes para o método ALTERAR ---

  @Test
  @DisplayName("Deve alterar uma Pessoa Jurídica com sucesso")
  void alterarPessoaJuridicaComSucesso() throws IOException {
    // Cenário: O cliente a ser alterado é uma Pessoa Jurídica
    List<PessoaJuridica> pJuridicas = new ArrayList<>();
    pJuridicas.add(pessoaJuridicaMock);

    when(pessoaFisicaRepository.findAll()).thenReturn(new ArrayList<>());
    when(pessoaJuridicaRepository.findAll()).thenReturn(pJuridicas);

    String resultado = clienteService.alterar("Nova Empresa", "11111111111111");

    // Verificação
    assertEquals("Pessoa Jurídica atualizada com sucesso: Nova Empresa - 11111111111111", resultado);
    verify(pessoaJuridicaRepository, times(1)).update(any(PessoaJuridica.class));
  }

  @Test
  @DisplayName("Deve retornar 'Cliente não encontrado' se o documento não existir")
  void alterarDocumentoNaoExistente() throws IOException {
    // Cenário: O documento não é encontrado em nenhum repositório
    when(pessoaFisicaRepository.findAll()).thenReturn(new ArrayList<>());
    when(pessoaJuridicaRepository.findAll()).thenReturn(new ArrayList<>());

    String resultado = clienteService.alterar("Nome Inexistente", "99999999999999");

    // Verificação
    assertEquals("Cliente com documento 99999999999999 não encontrado.", resultado);
    verify(pessoaFisicaRepository, never()).update(any());
    verify(pessoaJuridicaRepository, never()).update(any());
  }

  // --- Testes para o método LISTAR TODOS ---

  @Test
  @DisplayName("Deve listar todas as Pessoas Físicas e Jurídicas")
  void listarTodosClientes() throws IOException {
    // Cenário: Repositórios retornam dados
    List<PessoaFisica> pFisicas = Collections.singletonList(pessoaFisicaMock);
    List<PessoaJuridica> pJuridicas = Collections.singletonList(pessoaJuridicaMock);

    when(pessoaFisicaRepository.findAll()).thenReturn(pFisicas);
    when(pessoaJuridicaRepository.findAll()).thenReturn(pJuridicas);

    Optional<List<Cliente>> resultado = clienteService.listarTodos();

    // Verificação
    assertEquals(2, resultado.get().size());
    assertEquals("João", resultado.get().get(1).getNome());
    assertEquals("Empresa Ltda", resultado.get().get(0).getNome());
  }

  @Test
  @DisplayName("Deve retornar Optional.empty() em caso de exceção na listagem")
  void listarTodosComErro() throws Exception {
    // Cenário: O repositório lança uma exceção
    when(pessoaFisicaRepository.findAll()).thenThrow(new RuntimeException("Erro de conexão"));

    Optional<List<Cliente>> resultado = clienteService.listarTodos();

    // Verificação
    assertEquals(Optional.empty(), resultado);
  }
}