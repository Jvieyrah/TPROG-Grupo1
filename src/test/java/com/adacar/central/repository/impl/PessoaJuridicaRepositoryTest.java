package com.adacar.central.repository.impl;

import com.adacar.central.application.Impl.JsonDataReader;
import com.adacar.central.application.Impl.JsonDataWriter;
import com.adacar.central.model.PessoaJuridica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PessoaJuridicaRepositoryTest {

  @Mock
  private JsonDataReader<PessoaJuridica> jsonDataReader;

  @Mock
  private JsonDataWriter<PessoaJuridica> jsonDataWriter;

  @InjectMocks
  private PessoaJuridicaRepository pessoaJuridicaRepository;


  @TempDir
  File tempDir;

  private File testFile;

  @BeforeEach
  void setUp() {
    testFile = new File(tempDir, "pjTest.json");
    // Configura o repositório para usar o arquivo de teste temporário
    pessoaJuridicaRepository.setFile(testFile);
  }

  @Test
  void testFindAll() throws IOException {
    // Arrange
    List<PessoaJuridica> mockPessoas = Arrays.asList(
        new PessoaJuridica("123456789", "Empresa A"),
        new PessoaJuridica("987654321", "Empresa B")
    );
    when(jsonDataReader.readList(testFile, PessoaJuridica.class)).thenReturn(mockPessoas);

    // Act
    List<PessoaJuridica> pessoas = pessoaJuridicaRepository.findAll();

    // Assert
    assertEquals(2, pessoas.size());
    verify(jsonDataReader, times(1)).readList(testFile, PessoaJuridica.class);
  }

  @Test
  void testFindById() throws IOException {
    // Arrange
    PessoaJuridica mockPessoa1 = new PessoaJuridica("Empresa A","123456789" );
    PessoaJuridica mockPessoa2 = new PessoaJuridica( "Empresa B", "987654321");
    when(jsonDataReader.readList(eq(testFile), eq(PessoaJuridica.class)))
        .thenReturn(List.of(mockPessoa1, mockPessoa2));


    // Act
    PessoaJuridica pessoa = pessoaJuridicaRepository.findById("123456789");

    // Assert
    assertNotNull(pessoa);
    assertEquals("123456789", pessoa.getDocumento());
    verify(jsonDataReader, times(1)).readList(testFile, PessoaJuridica.class);
  }

  @Test
  void testSave() throws IOException {
    // Arrange
    PessoaJuridica newPessoa = new PessoaJuridica("Empresa B", "987654321");
    when(jsonDataReader.readList(testFile, PessoaJuridica.class)).thenReturn(
        List.of(new PessoaJuridica( "Empresa A", "123456789")));

    // Act
    pessoaJuridicaRepository.save(newPessoa);

    // Assert
    verify(jsonDataReader, times(1)).readList(testFile, PessoaJuridica.class);
    verify(jsonDataWriter, times(1)).writeList(eq(testFile), anyList());
  }

  @Test
  void testUpdate() throws IOException {
    // Arrange
    PessoaJuridica existingPessoa = new PessoaJuridica("Empresa A", "123456789");
    PessoaJuridica updatedPessoa = new PessoaJuridica("Empresa A Atualizada", "123456789");
    when(jsonDataReader.readList(testFile, PessoaJuridica.class)).thenReturn(List.of(existingPessoa));

    // Act
    pessoaJuridicaRepository.update(updatedPessoa);

    // Assert
    verify(jsonDataReader, times(2)).readList(testFile, PessoaJuridica.class);
    verify(jsonDataWriter, times(1)).writeList(eq(testFile), anyList());
  }

  @Test
  void testDelete() throws IOException {
    // Arrange
    PessoaJuridica existingPessoa = new PessoaJuridica("Empresa A", "123456789");
    when(jsonDataReader.readList(testFile, PessoaJuridica.class)).thenReturn(List.of(existingPessoa));

    // Act
    pessoaJuridicaRepository.delete("123456789");

    // Assert
    verify(jsonDataReader, times(2)).readList(testFile, PessoaJuridica.class);
    verify(jsonDataWriter, times(1)).writeList(eq(testFile), anyList());
  }

  @Test
  void testDelete_ShouldThrowException_WhenPessoaNotFound() throws IOException {
    // Arrange
    when(jsonDataReader.readList(testFile, PessoaJuridica.class)).thenReturn(Collections.emptyList());

    // Act & Assert
    Exception exception = assertThrows(RuntimeException.class, () -> {
      pessoaJuridicaRepository.delete("NONEXISTENT");
    });
    assertEquals("Pessoa Jurídica com documento NONEXISTENT não encontrada.", exception.getMessage());
    verify(jsonDataReader, times(1)).readList(testFile, PessoaJuridica.class);
    verify(jsonDataWriter, never()).writeList(any(), anyList());
  }
}