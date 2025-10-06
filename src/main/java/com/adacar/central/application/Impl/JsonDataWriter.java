package com.adacar.central.application.Impl;
import com.adacar.central.application.interfaces.IDataWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonDataWriter<T> implements IDataWriter<T> {

  private final ObjectMapper objectMapper;

  public JsonDataWriter() {
    this.objectMapper = new ObjectMapper();
    // Permite indentação para o JSON ficar legível
    this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    // Registra o módulo para suportar os tipos de data e hora do Java 8
    this.objectMapper.registerModule(new JavaTimeModule());
  }

//  @Override
//  public void write(File file, T data) throws IOException {
//    objectMapper.writeValue(file, data);
//  }

  @Override
  public void writeList(File file, List<T> data) throws IOException {
    // Usa FileOutputStream + OutputStreamWriter + BufferedWriter para escrita textual buffered.
    try (java.io.OutputStream fos = new java.io.FileOutputStream(file);
         java.io.Writer osw = new java.io.OutputStreamWriter(fos, java.nio.charset.StandardCharsets.UTF_8);
         java.io.BufferedWriter writer = new java.io.BufferedWriter(osw)) {
      objectMapper.writeValue(writer, data);
      writer.flush();
    }
  }
}