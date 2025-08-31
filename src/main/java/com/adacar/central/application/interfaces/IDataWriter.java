package com.adacar.central.application.interfaces;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface IDataWriter<T> {

  /**
   * Escreve um único objeto em um arquivo.
   *
   * @param file O arquivo onde o objeto será escrito.
   * @param data O objeto a ser escrito.
   * @throws IOException Se ocorrer um erro de I/O durante a escrita.
   */
  //void write(File file, T data) throws IOException;

  /**
   * Escreve uma lista de objetos em um arquivo.
   *
   * @param file O arquivo onde a lista será escrita.
   * @param data A lista de objetos a ser escrita.
   * @throws IOException Se ocorrer um erro de I/O durante a escrita.
   */
  void writeList(File file, List<T> data) throws IOException;
}