package com.adacar.central.repository.impl;

import com.adacar.central.application.Impl.JsonDataReader;
import com.adacar.central.application.Impl.JsonDataWriter;
import com.adacar.central.enums.StatusVeiculo;
import com.adacar.central.model.Veiculo;
import com.adacar.central.repository.interfaces.IRepository;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VeiculoRepository implements IRepository<Veiculo> {
  private JsonDataWriter<Veiculo> jsonDataWriter = new JsonDataWriter<>();
  private JsonDataReader<Veiculo> jsonDataReader = new JsonDataReader<>();

  private File file = new File("src/main/resources/files/vehicles/veiculos.json");

  @Override
  public List<Veiculo> findAll() throws IOException {
    return jsonDataReader.readList(file, Veiculo.class);
  }

  @Override
  public Veiculo findById(String id) throws IOException {
    return findAll().stream()
        .filter(veiculo -> veiculo.getPlaca().equals(id))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Veículo com placa " + id + " não encontrado."));
  }

  public Veiculo findByPartialName(String partialName) throws IOException {
    return findAll().stream()
            .filter(veiculo -> veiculo.getNome().toLowerCase().contains(partialName.toLowerCase())
                    && veiculo.getStatus() == StatusVeiculo.DISPONIVEL)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Nenhum veículo encontrado com o nome contendo: " + partialName));
  }

  @Override
  public Veiculo findById(String id1, String id2) throws IOException {
    throw new UnsupportedOperationException("Use findById(String placa) para buscar um veículo.");
  }

  @Override
  public void save(Veiculo entity) throws IOException {
    try {
      List<Veiculo> veiculos = new ArrayList<>(findAll());
      for (Veiculo veiculo : veiculos) {
        if (veiculo.getPlaca().equals(entity.getPlaca())) {
          throw new RuntimeException("Veículo com placa " + entity.getPlaca() + " já existe.");
        }
      }
      veiculos.add(entity);
      jsonDataWriter.writeList(file, veiculos);
    } catch (IOException e) {
      throw new RuntimeException("Erro ao salvar Veículo: " + e.getMessage(), e);
    }
  }

  @Override
  public void update(Veiculo entity) throws IOException {
    try {
      Veiculo veiculoJson = findById(entity.getPlaca());
      List<Veiculo> veiculos = new ArrayList<>(findAll());
      veiculos.remove(veiculoJson);
      veiculos.add(entity);
      jsonDataWriter.writeList(file, veiculos);
    } catch (IOException e) {
      throw new RuntimeException("Erro ao atualizar Veículo: " + e.getMessage(), e);
    }
  }

  @Override
  public void delete(String id) throws IOException {
    try {
      Veiculo veiculoJson = findById(id);
      List<Veiculo> veiculos =  new ArrayList<>(findAll());
      veiculos.remove(veiculoJson);
      jsonDataWriter.writeList(file, veiculos);
    } catch (IOException e) {
      throw new RuntimeException("Erro ao deletar Veículo: " + e.getMessage(), e);
    }
  }

  @Override
  public void delete(String id1, String id2) throws IOException {
    throw new UnsupportedOperationException("Use delete(String id) para deletar um veículo.");

  }

  //classes abaixo exclusivos para testes
// Métodos setters para injeção de dependência via Mockito
  public void setJsonDataReader(JsonDataReader<Veiculo> jsonDataReader) {
    this.jsonDataReader = jsonDataReader;
  }

  public void setJsonDataWriter(JsonDataWriter<Veiculo> jsonDataWriter) {
    this.jsonDataWriter = jsonDataWriter;
  }

  public void setFile(File file) {
    this.file = file;
  }
}
