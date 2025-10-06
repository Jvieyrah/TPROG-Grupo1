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
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;

public class PessoaFisicaRepository implements IRepository<PessoaFisica> {
  private JsonDataWriter<PessoaFisica> jsonDataWriter = new JsonDataWriter<>();
  private JsonDataReader<PessoaFisica> jsonDataReader = new JsonDataReader<>();

  private File file = new File("src/main/resources/files/clients/clientesPF.json");

  @Override
  public List<PessoaFisica> findAll() throws IOException {
    // Create a defensive copy of the list to avoid concurrent modification issues
    return new ArrayList<>(jsonDataReader.readList(file, PessoaFisica.class));
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
      
      // Verifica se já existe uma pessoa com o mesmo documento usando Stream
      boolean documentoExistente = pessoasFisicas.stream()
          .map(PessoaFisica::getDocumento)
          .anyMatch(doc -> doc.equals(entity.getDocumento()));
          
      if (documentoExistente) {
        throw new RuntimeException("Pessoa Física com documento " + entity.getDocumento() + " já existe.");
      }
      
      pessoasFisicas.add(entity);
      jsonDataWriter.writeList(file, pessoasFisicas);
    } catch (IOException e) {
      throw new RuntimeException("Erro ao salvar Pessoa Física: " + e.getMessage(), e);
    }
  }

    @Override
    public void update(PessoaFisica entity) throws IOException {
        if (entity == null || entity.getDocumento() == null) {
            throw new IllegalArgumentException("Pessoa Física ou documento não pode ser nulo");
        }
        
        List<PessoaFisica> pessoasFisicas = jsonDataReader.readList(file, PessoaFisica.class);
        
        // Encontra o índice da pessoa usando Stream
        int index = IntStream.range(0, pessoasFisicas.size())
            .filter(i -> pessoasFisicas.get(i).getDocumento().equals(entity.getDocumento()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Pessoa Física não encontrada para atualização: " + entity.getDocumento()));
        
        // Atualiza a pessoa na lista
        pessoasFisicas.set(index, entity);
        
        try {
            jsonDataWriter.writeList(file, pessoasFisicas);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao atualizar Pessoa Física: " + e.getMessage(), e);
        }
    }

  @Override
  public void delete(String id) throws IOException {
    try {
      List<PessoaFisica> pessoasFisicas = new ArrayList<>(findAll());
      
      // Remove a pessoa usando Stream e filter
      List<PessoaFisica> pessoasAtualizadas = pessoasFisicas.stream()
          .filter(p -> !p.getDocumento().equals(id))
          .collect(java.util.stream.Collectors.toList());
      
      // Verifica se algum item foi removido
      if (pessoasAtualizadas.size() == pessoasFisicas.size()) {
        throw new RuntimeException("Pessoa Física com documento " + id + " não encontrada para exclusão.");
      }
      
      jsonDataWriter.writeList(file, pessoasAtualizadas);
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
