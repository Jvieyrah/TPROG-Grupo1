package com.adacar.central.repository.impl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.adacar.central.application.Impl.JsonDataReader;
import com.adacar.central.application.Impl.JsonDataWriter;
import com.adacar.central.model.Veiculo;
import com.adacar.central.repository.impl.VeiculoRepository;
import com.adacar.central.enums.TipoVeiculo;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VeiculoRepositoryTest {

  @Mock
  private JsonDataReader<Veiculo> jsonDataReader;

  @Mock
  private JsonDataWriter<Veiculo> jsonDataWriter;

  @InjectMocks
  private VeiculoRepository veiculoRepository;

  @TempDir
  File tempDir;

  private File testFile;

  @BeforeEach
  void setUp() {
    testFile = new File(tempDir, "veiculosTest.json");
    // Configura o repositório para usar o arquivo de teste temporário
    veiculoRepository.setFile(testFile);
  }

  @Test
  void testFindAll_ShouldReturnAllVehicles() throws IOException {
    // Arrange
    List<Veiculo> mockVeiculos = Arrays.asList(
        new Veiculo("ABC1234", "Carro", TipoVeiculo.SUV),
        new Veiculo("XYZ5678", "Carro", TipoVeiculo.PEQUENO)
    );
    when(jsonDataReader.readList(eq(testFile), eq(Veiculo.class))).thenReturn(mockVeiculos);

    // Act
    List<Veiculo> veiculos = veiculoRepository.findAll();

    // Assert
    assertEquals(2, veiculos.size());
    assertEquals("ABC1234", veiculos.get(0).getPlaca());
    verify(jsonDataReader, times(1)).readList(eq(testFile), eq(Veiculo.class));
  }

  @Test
  void testFindById_ShouldReturnCorrectVehicle_WhenFound() throws IOException {
    // Arrange
    Veiculo mockVeiculo = new Veiculo("ABC1234", "Carro", TipoVeiculo.SUV);
    when(jsonDataReader.readList(eq(testFile), eq(Veiculo.class))).thenReturn(List.of(mockVeiculo));

    // Act
    Veiculo veiculo = veiculoRepository.findById("ABC1234");

    // Assert
    assertNotNull(veiculo);
    assertEquals("ABC1234", veiculo.getPlaca());
    verify(jsonDataReader, times(1)).readList(eq(testFile), eq(Veiculo.class));
  }

  @Test
  void testFindById_ShouldThrowException_WhenNotFound() throws IOException {
    // Arrange
    when(jsonDataReader.readList(eq(testFile), eq(Veiculo.class))).thenReturn(Collections.emptyList());

    // Act & Assert
    Exception exception = assertThrows(RuntimeException.class, () -> {
      veiculoRepository.findById("NONEXISTENT");
    });
    assertEquals("Veículo com placa NONEXISTENT não encontrado.", exception.getMessage());
    verify(jsonDataReader, times(1)).readList(eq(testFile), eq(Veiculo.class));
  }

  @Test
  void testSave_ShouldThrowException_WhenVehicleAlreadyExists() throws IOException {
    // Arrange
    Veiculo existingVeiculo = new Veiculo("EXIST123", "Carro", TipoVeiculo.PEQUENO);
    when(jsonDataReader.readList(eq(testFile), eq(Veiculo.class))).thenReturn(List.of(existingVeiculo));

    // Act & Assert
    Exception exception = assertThrows(RuntimeException.class, () -> {
      veiculoRepository.save(new Veiculo("EXIST123", "Carro", TipoVeiculo.SUV));
    });
    assertEquals("Veículo com placa EXIST123 já existe.", exception.getMessage());
    verify(jsonDataReader, times(1)).readList(eq(testFile), eq(Veiculo.class));
    verify(jsonDataWriter, never()).writeList(any(), anyList());
  }

  @Test
  void testSave_ShouldSaveNewVehicle() throws IOException {
    // Arrange
    Veiculo newVeiculo = new Veiculo("NEW1234", "Carro", TipoVeiculo.SUV);
    when(jsonDataReader.readList(eq(testFile), eq(Veiculo.class))).thenReturn(Collections.emptyList());

    // Act
    veiculoRepository.save(newVeiculo);

    // Assert
    verify(jsonDataReader, times(1)).readList(eq(testFile), eq(Veiculo.class));
    verify(jsonDataWriter, times(1)).writeList(eq(testFile), anyList());
  }

  @Test
  void testUpdate_ShouldThrowException_WhenVehicleNotFound() throws IOException {
    // Arrange
    when(jsonDataReader.readList(eq(testFile), eq(Veiculo.class))).thenReturn(Collections.emptyList());
    Veiculo updatedVeiculo = new Veiculo("NONEXISTENT", "Carro", TipoVeiculo.SUV);

    // Act & Assert
    Exception exception = assertThrows(RuntimeException.class, () -> {
      veiculoRepository.update(updatedVeiculo);
    });
    assertEquals("Veículo com placa NONEXISTENT não encontrado.", exception.getMessage());
    verify(jsonDataReader, times(1)).readList(eq(testFile), eq(Veiculo.class));
    verify(jsonDataWriter, never()).writeList(any(), anyList());
  }

  @Test
  void testDelete_ShouldThrowException_WhenVehicleNotFound() throws IOException {
    // Arrange
    when(jsonDataReader.readList(eq(testFile), eq(Veiculo.class))).thenReturn(Collections.emptyList());

    // Act & Assert
    Exception exception = assertThrows(RuntimeException.class, () -> {
      veiculoRepository.delete("NONEXISTENT");
    });
    assertEquals("Veículo com placa NONEXISTENT não encontrado.", exception.getMessage());
    verify(jsonDataReader, times(1)).readList(eq(testFile), eq(Veiculo.class));
    verify(jsonDataWriter, never()).writeList(any(), anyList());
  }
}