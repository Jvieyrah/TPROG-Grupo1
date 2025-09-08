package com.adacar.central.application.Impl;

import com.adacar.central.application.interfaces.IDataListReader;
import com.adacar.central.application.interfaces.IDataReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Implementação genérica para leitura de arquivos JSON usando Jackson.
 * @param <T> O tipo do objeto que a classe irá ler.
 */
public class JsonDataReader<T> implements  IDataListReader<T> {
  private final ObjectMapper objectMapper;

  public JsonDataReader() {
    this.objectMapper = new ObjectMapper();
    // módulo sugerido pelo gemini para suportar os tipos de data não suportados pelo jackson
    this.objectMapper.registerModule(new JavaTimeModule());
  }

//  @Override
//  public T read(File file, Class<T> type) throws IOException {
//    return objectMapper.readValue(file, type);
//  }

  @Override
  public List<T> readList(File file, Class<T> type) throws IOException {
    return objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, type));
  }
}