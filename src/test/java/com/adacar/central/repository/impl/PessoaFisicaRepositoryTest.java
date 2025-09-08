package com.adacar.central.repository.impl;
import com.adacar.central.application.Impl.JsonDataReader;
import com.adacar.central.application.Impl.JsonDataWriter;
import com.adacar.central.model.PessoaFisica;
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
public class PessoaFisicaRepositoryTest {

  @Mock
  private JsonDataReader<PessoaFisica> jsonDataReader;

  @Mock
  private JsonDataWriter<PessoaFisica> jsonDataWriter;

  @InjectMocks
  private PessoaFisicaRepository pessoaFisicaRepository;


  @TempDir
  File tempDir;

  private File testFile;

  @BeforeEach
  void setUp() {
    testFile = new File(tempDir, "pfTest.json");
    // Configura o repositório para usar o arquivo de teste temporário
    pessoaFisicaRepository.setFile(testFile);
  }

  @Test
  void testFindAll() throws IOException {
    // Arrange
    List<PessoaFisica> mockPessoas = Arrays.asList(
        new PessoaFisica("123456789", "Empresa A"),
        new PessoaFisica("987654321", "Empresa B")
    );
    when(jsonDataReader.readList(testFile, PessoaFisica.class)).thenReturn(mockPessoas);

    // Act
    List<PessoaFisica> pessoas = pessoaFisicaRepository.findAll();

    // Assert
    assertEquals(2, pessoas.size());
    verify(jsonDataReader, times(1)).readList(testFile, PessoaFisica.class);
  }

  @Test
  void testFindById() throws IOException {
    // Arrange
    PessoaFisica mockPessoa1 = new PessoaFisica("Empresa A","123456789" );
    PessoaFisica mockPessoa2 = new PessoaFisica( "Empresa B", "987654321");
    when(jsonDataReader.readList(eq(testFile), eq(PessoaFisica.class)))
        .thenReturn(List.of(mockPessoa1, mockPessoa2));


    // Act
    PessoaFisica pessoa = pessoaFisicaRepository.findById("123456789");

    // Assert
    assertNotNull(pessoa);
    assertEquals("123456789", pessoa.getDocumento());
    verify(jsonDataReader, times(1)).readList(testFile, PessoaFisica.class);
  }

  @Test
  void testSave() throws IOException {
    // Arrange
    PessoaFisica newPessoa = new PessoaFisica("Empresa B", "987654321");
    when(jsonDataReader.readList(testFile, PessoaFisica.class)).thenReturn(
        List.of(new PessoaFisica( "Empresa A", "123456789")));

    // Act
    pessoaFisicaRepository.save(newPessoa);

    // Assert
    verify(jsonDataReader, times(1)).readList(testFile, PessoaFisica.class);
    verify(jsonDataWriter, times(1)).writeList(eq(testFile), anyList());
  }

  @Test
  void testUpdate() throws IOException {
    // Arrange
    PessoaFisica existingPessoa = new PessoaFisica("Empresa A", "123456789");
    PessoaFisica updatedPessoa = new PessoaFisica("Empresa A Atualizada", "123456789");
    when(jsonDataReader.readList(testFile, PessoaFisica.class)).thenReturn(List.of(existingPessoa));

    // Act
    pessoaFisicaRepository.update(updatedPessoa);

    // Assert
    verify(jsonDataReader, times(2)).readList(testFile, PessoaFisica.class);
    verify(jsonDataWriter, times(1)).writeList(eq(testFile), anyList());
  }

  @Test
  void testDelete() throws IOException {
    // Arrange
    PessoaFisica existingPessoa = new PessoaFisica("Empresa A", "123456789");
    when(jsonDataReader.readList(testFile, PessoaFisica.class)).thenReturn(List.of(existingPessoa));

    // Act
    pessoaFisicaRepository.delete("123456789");

    // Assert
    verify(jsonDataReader, times(2)).readList(testFile, PessoaFisica.class);
    verify(jsonDataWriter, times(1)).writeList(eq(testFile), anyList());
  }

  @Test
  void testDelete_ShouldThrowException_WhenPessoaNotFound() throws IOException {
    // Arrange
    when(jsonDataReader.readList(testFile, PessoaFisica.class)).thenReturn(Collections.emptyList());

    // Act & Assert
    Exception exception = assertThrows(RuntimeException.class, () -> {
      pessoaFisicaRepository.delete("NONEXISTENT");
    });
    assertEquals("Pessoa Física com documento NONEXISTENT não encontrada.", exception.getMessage());
    verify(jsonDataReader, times(1)).readList(testFile, PessoaFisica.class);
    verify(jsonDataWriter, never()).writeList(any(), anyList());
  }

}
