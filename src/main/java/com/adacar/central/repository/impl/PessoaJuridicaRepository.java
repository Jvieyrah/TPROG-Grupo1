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
      
      // Verifica se já existe uma pessoa jurídica com o mesmo documento usando Stream
      boolean documentoExistente = pessoasJuridicas.stream()
          .map(PessoaJuridica::getDocumento)
          .anyMatch(doc -> doc.equals(entity.getDocumento()));
          
      if (documentoExistente) {
        throw new RuntimeException("Pessoa Jurídica com documento " + entity.getDocumento() + " já existe.");
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
      List<PessoaJuridica> pessoasJuridicas = new ArrayList<>(findAll());
      
      // Encontra o índice da pessoa jurídica a ser atualizada usando Stream
      int index = -1;
      for (int i = 0; i < pessoasJuridicas.size(); i++) {
          if (pessoasJuridicas.get(i).getDocumento().equals(entity.getDocumento())) {
              index = i;
              break;
          }
      }
      
      if (index == -1) {
          throw new RuntimeException("Pessoa Jurídica com documento " + entity.getDocumento() + " não encontrada para atualização.");
      }
      
      // Atualiza a pessoa jurídica na posição encontrada
      pessoasJuridicas.set(index, entity);
      jsonDataWriter.writeList(file, pessoasJuridicas);
    } catch (IOException e) {
      throw new RuntimeException("Erro ao atualizar Pessoa Jurídica: " + e.getMessage(), e);
    }
  }

  @Override
  public void delete(String id) throws IOException {
    try {
      List<PessoaJuridica> pessoasJuridicas = new ArrayList<>(findAll());
      
      // Remove a pessoa jurídica usando Stream e filter
      List<PessoaJuridica> pessoasAtualizadas = pessoasJuridicas.stream()
          .filter(p -> !p.getDocumento().equals(id))
          .collect(java.util.stream.Collectors.toList());
      
      // Verifica se algum item foi removido
      if (pessoasAtualizadas.size() == pessoasJuridicas.size()) {
        throw new RuntimeException("Pessoa Jurídica com documento " + id + " não encontrada para exclusão.");
      }
      
      jsonDataWriter.writeList(file, pessoasAtualizadas);
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
