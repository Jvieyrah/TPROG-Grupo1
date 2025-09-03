package com.adacar.central.repository.impl;

import com.adacar.central.application.Impl.JsonDataReader;
import com.adacar.central.application.Impl.JsonDataWriter;
import com.adacar.central.model.PessoaFisica;
import com.adacar.central.model.PessoaJuridica;
import com.adacar.central.repository.interfaces.IRepository;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class PessoaFisicaRepository implements IRepository<PessoaFisica> {
  private JsonDataWriter<PessoaFisica> jsonDataWriter = new JsonDataWriter<>();
  private JsonDataReader<PessoaFisica> jsonDataReader = new JsonDataReader<>();

  private File file = new File("src/main/resources/files/clients/clientesPF.json");

  @Override
  public List<PessoaFisica> findAll() throws IOException {
    return  jsonDataReader.readList(file, PessoaFisica.class);
  }

  @Override
  public PessoaFisica findById(String documento) throws IOException {
    return findAll().stream()
        .filter(pessoaFisica -> pessoaFisica.getDocumento().equals(documento))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Pessoa Física com documento " + documento + " não encontrada."));
  }

  @Override
  public PessoaFisica findById(String id1, String id2) throws IOException {
    throw new UnsupportedOperationException("Use findById(String documento) para buscar uma pessoa física.");
  }

  @Override
  public void save(PessoaFisica entity) {
    try {
      List<PessoaFisica> pessoasFisicas = new ArrayList<>(findAll());
for (PessoaFisica pessoaFisica : pessoasFisicas){
        if (pessoaFisica.getDocumento().equals(entity.getDocumento())){
          throw new RuntimeException("Pessoa Física com documento " + entity.getDocumento() + " já existe.");
        }
      }
      pessoasFisicas.add(entity);
      jsonDataWriter.writeList(file, pessoasFisicas);
    } catch (IOException e) {
      throw new RuntimeException("Erro ao salvar Pessoa Física: " + e.getMessage(), e);
    }
  }

  @Override
  public void update(PessoaFisica entity) throws IOException {
     try {
        PessoaFisica pessoasFisicaJson = findById(entity.getDocumento());
        List<PessoaFisica> pessoasFisicas = new ArrayList<>(findAll());
        pessoasFisicas.remove(pessoasFisicaJson);
        pessoasFisicas.add(entity);
        jsonDataWriter.writeList(file, pessoasFisicas);
      } catch (IOException e) {
        throw new RuntimeException("Erro ao atualizar Pessoa Física: " + e.getMessage(), e);
     }
  }

  @Override
  public void delete(String id)  throws IOException  {
    try {
      PessoaFisica pessoasFisicaJson = findById(id);
      List<PessoaFisica> pessoasFisicas = new ArrayList<>(findAll());
      pessoasFisicas.remove(pessoasFisicaJson);
      jsonDataWriter.writeList(file, pessoasFisicas);
    } catch (IOException e) {
      throw new RuntimeException("Erro ao deletar Pessoa Física: " + e.getMessage(), e);
    }
  }

  @Override
  public void delete(String id1, String id2) throws IOException {
    throw new UnsupportedOperationException("Use delete(String id) para deletar uma pessoa física.");
  }

  //classes abaixo exclusivos para testes
  // Métodos setters para injeção de dependência via Mockito
  public void setJsonDataReader(JsonDataReader<PessoaFisica> jsonDataReader) {
    this.jsonDataReader = jsonDataReader;
  }

  public void setJsonDataWriter(JsonDataWriter<PessoaFisica> jsonDataWriter) {
    this.jsonDataWriter = jsonDataWriter;
  }

  public void setFile(File file) {
    this.file = file;
  }

}
