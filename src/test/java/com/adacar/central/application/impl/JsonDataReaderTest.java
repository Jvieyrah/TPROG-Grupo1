package com.adacar.central.application.impl;

import com.adacar.central.application.Impl.JsonDataReader;
import com.adacar.central.model.PessoaFisica;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class JsonDataReaderTest {

  @Test
  void testReadList() throws Exception {
    // Mock do JsonDataReader
    JsonDataReader<PessoaFisica> jsonReaderMock = Mockito.mock(JsonDataReader.class);

    // Dados mockados
    File mockFile = new File("mock_clientesPF.json");
    List<PessoaFisica> mockClientes = Arrays.asList(
        new PessoaFisica("João Vieira", "123.456.789-00"),
        new PessoaFisica("Maria Silva", "987.654.321-00")
    );

    // Configuração do comportamento do mock
    when(jsonReaderMock.readList(mockFile, PessoaFisica.class)).thenReturn(mockClientes);

    // Chamada do método
    List<PessoaFisica> clientes = jsonReaderMock.readList(mockFile, PessoaFisica.class);

    // Verificações
    assertEquals(2, clientes.size());
    assertEquals("João Vieira", clientes.get(0).getNome());
    assertEquals("Maria Silva", clientes.get(1).getNome());
  }
}