package com.adacar.central.repository.impl;

import com.adacar.central.application.Impl.JsonDataReader;
import com.adacar.central.application.Impl.JsonDataWriter;
import com.adacar.central.enums.TipoVeiculo;
import com.adacar.central.model.Aluguel;
import com.adacar.central.model.PessoaFisica;
import com.adacar.central.model.PessoaJuridica;
import com.adacar.central.model.Veiculo;
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
public class AluguelRepositoryTest {

  @Mock
  private JsonDataReader<Aluguel> jsonDataReader;

  @Mock
  private JsonDataWriter<Aluguel> jsonDataWriter;

  @InjectMocks
  private AluguelRepository aluguelRepository;


  @TempDir
  File tempDir;

  private File testFile;

  @BeforeEach
  void setUp() {
    testFile = new File(tempDir, "pfTest.json");
    // Configura o repositório para usar o arquivo de teste temporário
    aluguelRepository.setFile(testFile);
  }

  @Test
  void testFindAll() throws IOException {
    Veiculo tesla1 = new Veiculo("ABC-1234", "Tesla Model X", TipoVeiculo.SUV);
    Veiculo tesla2 = new Veiculo("ABC-1235", "Tesla Model X", TipoVeiculo.SUV);
    PessoaJuridica empresa1 = new PessoaJuridica("Empresa XYZ", "12.345.678/0001-00");
    PessoaJuridica empresa2 = new PessoaJuridica("Empresa ABC", "12.345.678/0001-01");
    // Arrange
    List<Aluguel> mockAlugueis = Arrays.asList(
        new Aluguel(empresa1, tesla1, "Loja Centro", java.time.LocalDateTime.now()),
        new Aluguel(empresa2, tesla2, "Loja Zona Sul", java.time.LocalDateTime.now())

    );
    when(jsonDataReader.readList(testFile, Aluguel.class)).thenReturn(mockAlugueis);

    // Act
    List<Aluguel> alugueis = aluguelRepository.findAll();

    // Assert
    assertEquals(2, alugueis.size());
    verify(jsonDataReader, times(1)).readList(testFile, Aluguel.class);
  }

  @Test
  void testFindById() throws IOException {
    // Arrange
    Veiculo tesla1 = new Veiculo("ABC-1234", "Tesla Model X", TipoVeiculo.SUV);
    Veiculo tesla2 = new Veiculo("ABC-1235", "Tesla Model X", TipoVeiculo.SUV);
    PessoaJuridica empresa1 = new PessoaJuridica("Empresa XYZ", "12.345.678/0001-00");
    PessoaJuridica empresa2 = new PessoaJuridica("Empresa ABC", "12.345.678/0001-01");
    // Arrange
    List<Aluguel> mockAlugueis = Arrays.asList(
        new Aluguel(empresa1, tesla1, "Loja Centro", java.time.LocalDateTime.now()),
        new Aluguel(empresa2, tesla2, "Loja Zona Sul", java.time.LocalDateTime.now())

    );
    when(jsonDataReader.readList(eq(testFile), eq( Aluguel.class)))
        .thenReturn(mockAlugueis);


    // Act
    Aluguel aluguelEncontrado = aluguelRepository.findById("12.345.678/0001-00", "ABC-1234");

    // Assert
    assertNotNull(aluguelEncontrado);
    assertEquals("12.345.678/0001-00", aluguelEncontrado.getCliente().getDocumento());
    assertEquals("ABC-1234", aluguelEncontrado.getVeiculo().getPlaca());
    verify(jsonDataReader, times(1)).readList(testFile, Aluguel.class);
  }

  @Test
  void testSave() throws IOException {
    // Arrange
    Veiculo tesla2 = new Veiculo("ABC-1235", "Tesla Model X", TipoVeiculo.SUV);
    Veiculo tesla1 = new Veiculo("ABC-1234", "Tesla Model X", TipoVeiculo.SUV);
    PessoaJuridica empresa1 = new PessoaJuridica("Empresa XYZ", "12.345.678/0001-00");
    PessoaJuridica empresa2 = new PessoaJuridica("Empresa ABC", "12.345.678/0001-01");

    when(jsonDataReader.readList(testFile, Aluguel.class)).thenReturn(
        List.of(new Aluguel(empresa1, tesla2, "Loja Centro", java.time.LocalDateTime.now())));

    // Act
    aluguelRepository.save(new Aluguel(empresa2, tesla1, "Loja Zona Sul", java.time.LocalDateTime.now()));

    // Assert
    verify(jsonDataReader, times(1)).readList(testFile, Aluguel.class);
    verify(jsonDataWriter, times(1)).writeList(eq(testFile), anyList());
  }

  @Test
  void testUpdate() throws IOException {
    // Arrange
    Veiculo tesla2 = new Veiculo("ABC-1235", "Tesla Model X", TipoVeiculo.SUV);
    Veiculo tesla1 = new Veiculo("ABC-1234", "Tesla Model X", TipoVeiculo.SUV);
    PessoaJuridica empresa1 = new PessoaJuridica("Empresa XYZ", "12.345.678/0001-00");
    PessoaJuridica empresa2 = new PessoaJuridica("Empresa ABC", "12.345.678/0001-01");
    when(jsonDataReader.readList(testFile, Aluguel.class)).thenReturn(
        List.of(new Aluguel(empresa1, tesla2, "Loja Centro", java.time.LocalDateTime.now())));


    // Act
    aluguelRepository.update(new Aluguel(empresa1, tesla2, "Loja Atualizada", java.time.LocalDateTime.now()));

    // Assert
    verify(jsonDataReader, times(2)).readList(testFile, Aluguel.class);
    verify(jsonDataWriter, times(1)).writeList(eq(testFile), anyList());
  }

  @Test
  void testDelete() throws IOException {
    // Arrange
    Veiculo tesla1 = new Veiculo("ABC-1234", "Tesla Model X", TipoVeiculo.SUV);
    Veiculo tesla2 = new Veiculo("ABC-1235", "Tesla Model X", TipoVeiculo.SUV);
    PessoaJuridica empresa1 = new PessoaJuridica("Empresa XYZ", "12.345.678/0001-00");
    PessoaJuridica empresa2 = new PessoaJuridica("Empresa ABC", "12.345.678/0001-01");
    // Arrange
    List<Aluguel> mockAlugueis = Arrays.asList(
        new Aluguel(empresa1, tesla1, "Loja Centro", java.time.LocalDateTime.now()),
        new Aluguel(empresa2, tesla2, "Loja Zona Sul", java.time.LocalDateTime.now())

    );
    when(jsonDataReader.readList(eq(testFile), eq( Aluguel.class)))
        .thenReturn(mockAlugueis);

    // Act
    aluguelRepository.delete("12.345.678/0001-00", "ABC-1234");

    // Assert
    verify(jsonDataReader, times(2)).readList(testFile, Aluguel.class);
    verify(jsonDataWriter, times(1)).writeList(eq(testFile), anyList());
  }

  @Test
  void testDelete_ShouldThrowException_WhenPessoaNotFound() throws IOException {
    // Arrange
    when(jsonDataReader.readList(testFile, Aluguel.class)).thenReturn(Collections.emptyList());

    // Act & Assert
    Exception exception = assertThrows(RuntimeException.class, () -> {
      aluguelRepository.delete("NONEXISTENT", "NONEXISTENT");
    });
    assertEquals("Aluguel com documento NONEXISTENT e placa NONEXISTENT não encontrado.", exception.getMessage());
    verify(jsonDataReader, times(1)).readList(testFile, Aluguel.class);
    verify(jsonDataWriter, never()).writeList(any(), anyList());
  }
}
