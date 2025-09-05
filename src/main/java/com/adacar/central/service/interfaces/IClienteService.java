package com.adacar.central.service.interfaces;

import com.adacar.central.model.Cliente;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IClienteService {
  String cadastrar(String nome, String Documento) throws IOException;

  String alterar(String nome, String Documento);

  Optional<List<Cliente>> listarTodos();

  Optional<Cliente> buscarPorDocumento(String documento);

  Optional<List<Cliente>> buscarPorParteNome(String nome);

}
