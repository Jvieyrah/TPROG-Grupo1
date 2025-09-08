package com.adacar.central.repository.impl;

import com.adacar.central.application.Impl.JsonDataWriter;
import com.adacar.central.application.Impl.JsonDataReader;
import com.adacar.central.model.PessoaJuridica;
import com.adacar.central.model.Veiculo;
import com.adacar.central.repository.interfaces.IRepository;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PessoaJuridicaRepository implements IRepository<PessoaJuridica> {
  private JsonDataWriter<PessoaJuridica> jsonDataWriter = new JsonDataWriter<>();
  private JsonDataReader<PessoaJuridica> jsonDataReader = new JsonDataReader<>();

  private File file = new File("src/main/resources/files/clients/clientesPJ.json");

  @Override
  public List<PessoaJuridica> findAll() throws IOException {
    return jsonDataReader.readList(file, PessoaJuridica.class);
  }

  @Override
  public PessoaJuridica findById(String id) throws IOException {
    return findAll().stream()
        .filter(pessoaJuridica -> pessoaJuridica.getDocumento().equals(id))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Pessoa Jurídica com documento " + id + " não encontrada."));
  }

  @Override
  public PessoaJuridica findById(String id1, String id2) throws IOException {
    throw new UnsupportedOperationException("Use findById(String documento) para buscar uma pessoa jurídica.");
  }

  @Override
  public void save(PessoaJuridica entity) throws IOException {
    try {
      List<PessoaJuridica> pessoasJuridicas = new ArrayList<>(findAll());
      for (PessoaJuridica pessoaJuridica : pessoasJuridicas) {
        if (pessoaJuridica.getDocumento().equals(entity.getDocumento())) {
          throw new RuntimeException("Pessoa Jurídica com documento " + entity.getDocumento() + " já existe.");
        }
      }
      pessoasJuridicas.add(entity);
      jsonDataWriter.writeList(file, pessoasJuridicas);
    } catch (IOException e) {
      throw new RuntimeException("Erro ao salvar Pessoa Jurídica: " + e.getMessage(), e);
    }
  }

  @Override
  public void update(PessoaJuridica entity) throws IOException {
    try {
      PessoaJuridica PessoaJuridicaJson = findById(entity.getDocumento());
      List<PessoaJuridica> PessoaJuridica = new ArrayList<>(findAll());
      PessoaJuridica.remove(PessoaJuridicaJson);
      PessoaJuridica.add(entity);
      jsonDataWriter.writeList(file, PessoaJuridica);
    } catch (IOException e) {
      throw new RuntimeException("Erro ao atualizar Pessoa Física: " + e.getMessage(), e);
    }
  }

  @Override
  public void delete(String id)  throws IOException {
    try {
      PessoaJuridica pessoaJuridicaJson = findById(id);
      List<PessoaJuridica> pessoasJuridicas = new ArrayList<>(findAll());
      pessoasJuridicas.remove(pessoaJuridicaJson);
      jsonDataWriter.writeList(file, pessoasJuridicas);
    } catch (IOException e) {
      throw new RuntimeException("Erro ao deletar Pessoa Jurídica: " + e.getMessage(), e);
    }
  }

  @Override
  public void delete(String id1, String id2) throws IOException {
    throw new UnsupportedOperationException("Use delete(String id) para deletar uma pessoa jurídica.");
  }

  //classes abaixo exclusivos para testes
  // Métodos setters para injeção de dependência via Mockito
  public void setJsonDataReader(JsonDataReader<PessoaJuridica> jsonDataReader) {
    this.jsonDataReader = jsonDataReader;
  }

  public void setJsonDataWriter(JsonDataWriter<PessoaJuridica> jsonDataWriter) {
    this.jsonDataWriter = jsonDataWriter;
  }

  public void setFile(File file) {
    this.file = file;
  }
}
