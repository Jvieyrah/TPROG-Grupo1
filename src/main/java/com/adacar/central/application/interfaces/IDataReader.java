package com.adacar.central.application.interfaces;
import java.io.File;
import java.io.IOException;

/**
 * Interface genérica para ler um único objeto de um arquivo.
 * @param <T> O tipo do objeto a ser lido.
 */
public interface IDataReader<T> {

  /**
   * Lê um único objeto de um arquivo.
   *
   * @param file O arquivo a ser lido.
   * @param type A classe do objeto a ser lido.
   * @return O objeto lido do arquivo.
   * @throws IOException Se ocorrer um erro de I/O durante a leitura do arquivo.
   */
  T read(File file, Class<T> type) throws IOException;
}
