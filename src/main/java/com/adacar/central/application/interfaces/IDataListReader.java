package com.adacar.central.application.interfaces;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Interface genérica para ler uma lista de objetos de um arquivo.
 * @param <T> O tipo dos objetos na lista.
 */
public interface IDataListReader<T> {

  /**
   * Lê uma lista de objetos de um arquivo.
   *
   * @param file O arquivo a ser lido.
   * @param type A classe do objeto a ser lido.
   * @return Uma lista de objetos lida do arquivo.
   * @throws IOException Se ocorrer um erro de I/O durante a leitura do arquivo.
   */
  List<T> readList(File file, Class<T> type) throws IOException;
}
