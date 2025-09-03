package com.adacar.central.repository.impl;

import com.adacar.central.application.Impl.JsonDataReader;
import com.adacar.central.application.Impl.JsonDataWriter;
import com.adacar.central.model.Aluguel;
import com.adacar.central.model.PessoaFisica;
import com.adacar.central.repository.interfaces.IRepository;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AluguelRepository implements IRepository<Aluguel> {
  private JsonDataWriter<Aluguel> jsonDataWriter = new JsonDataWriter<>();
  private JsonDataReader<Aluguel> jsonDataReader = new JsonDataReader<>();

  private File file = new File("src/main/resources/files/rentals/alugueis.json");

  @Override
  public List<Aluguel> findAll() throws IOException {
    return jsonDataReader.readList(file, Aluguel.class);
  }

  @Override
  public Aluguel findById(String id) throws IOException {
    throw new UnsupportedOperationException("Use findById(String documento , String placa) para buscar um aluguel.");
  }

  @Override
  public Aluguel findById(String documento, String Placa) throws IOException {
    return findAll().stream()
        .filter(aluguel -> aluguel.getCliente().getDocumento().equals(documento) && aluguel.getVeiculo().getPlaca().equals(Placa))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Aluguel com documento " + documento + " e placa " + Placa + " não encontrado."));
  }

  @Override
  public void save(Aluguel entity) throws IOException {
    try {
      List<Aluguel> alugueis = new ArrayList<>(findAll());
      for (Aluguel aluguel : alugueis) {
        if (aluguel.getCliente().getDocumento().equals(entity.getCliente().getDocumento())
            && aluguel.getVeiculo().getPlaca().equals(entity.getVeiculo().getPlaca())) {
          throw new RuntimeException("Aluguel cujo o cliente com documento " + entity.getCliente().getDocumento()
              + " e placa " + entity.getVeiculo().getPlaca() + " já existe.");
        }
      }
      alugueis.add(entity);
      jsonDataWriter.writeList(file, alugueis);
    } catch (IOException e) {
      throw new RuntimeException("Erro ao salvar Aluguel: " + e.getMessage(), e);
    }
  }

  @Override
  public void update(Aluguel entity) throws IOException {
    try {
      Aluguel aluguelJson = findById(
          entity.getCliente().getDocumento(), entity.getVeiculo().getPlaca()
      );
      List<Aluguel> alugueis =  new ArrayList<>(findAll());
      alugueis.remove(aluguelJson);
      alugueis.add(entity);
      jsonDataWriter.writeList(file, alugueis);
    } catch (IOException e) {
      throw new RuntimeException("Erro ao atualizar Aluguel: " + e.getMessage(), e);
    }
  }

  @Override
  public void delete(String id) throws IOException {
    throw new UnsupportedOperationException("Use delete(String id1, String id2) para deletar um aluguel.");
  }

  @Override
  public void delete(String documento, String placa) throws IOException {
    try {
      Aluguel aluguelJson = findById(documento, placa);
      List<Aluguel> alugueis = new ArrayList<>(findAll());
      alugueis.remove(aluguelJson);
      jsonDataWriter.writeList(file, alugueis);
    } catch (IOException e) {
      throw new RuntimeException("Erro ao deletar Aluguel: " + e.getMessage(), e);
    }
  }

  //classes abaixo exclusivos para testes
  // Métodos setters para injeção de dependência via Mockito
  public void setJsonDataReader(JsonDataReader<Aluguel> jsonDataReader) {
    this.jsonDataReader = jsonDataReader;
  }

  public void setJsonDataWriter(JsonDataWriter<Aluguel> jsonDataWriter) {
    this.jsonDataWriter = jsonDataWriter;
  }

  public void setFile(File file) {
    this.file = file;
  }
}