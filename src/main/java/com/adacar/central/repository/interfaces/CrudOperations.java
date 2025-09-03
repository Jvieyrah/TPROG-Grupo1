package com.adacar.central.repository.interfaces;

import java.util.List;
import java.util.Optional;

public interface CrudOperations<T, ID> {
    T create(T entity);

    Optional<T> findById(ID id);

    List<T> findAll();

    Optional<T> update(ID id, T entity);

    void deleteById(ID id);
}