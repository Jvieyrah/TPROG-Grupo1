package com.adacar.central.repository.interfaces;

import java.io.IOException;
import java.util.List;

public interface IRepository<T> {
  List<T> findAll() throws IOException;
  T findById(String id) throws IOException;
  T findById(String id1, String id2) throws IOException; // Sobrecarga para dois argumentos
  void save(T entity) throws IOException;
  void update(T entity) throws IOException;
  void delete(String id) throws IOException;
  void delete(String id1, String id2) throws IOException; // Sobrecarga para dois argumentos
}