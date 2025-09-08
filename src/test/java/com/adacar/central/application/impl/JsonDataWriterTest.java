package com.adacar.central.application.impl;

import com.adacar.central.application.Impl.JsonDataWriter;
import com.adacar.central.model.PessoaFisica;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.List;

import static org.mockito.Mockito.doNothing;

class JsonDataWriterTest {

  @Test
  void testWriteList() throws Exception {
    // Mock do JsonDataWriter
    JsonDataWriter<PessoaFisica> jsonWriterMock = Mockito.mock(JsonDataWriter.class);

    // Dados mockados
    File mockFile = new File("mock_clientesPF.json");
    List<PessoaFisica> mockClientes = List.of(
        new PessoaFisica("João Vieira", "123.456.789-00"),
        new PessoaFisica("Maria Silva", "987.654.321-00")
    );

    // Configuração do comportamento do mock
    doNothing().when(jsonWriterMock).writeList(mockFile, mockClientes);

    // Chamada do método
    jsonWriterMock.writeList(mockFile, mockClientes);

    // Verificação do comportamento
    Mockito.verify(jsonWriterMock, Mockito.times(1)).writeList(mockFile, mockClientes);
  }
}