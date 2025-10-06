package com.adacar.central.service.interfaces;

import com.adacar.central.model.Veiculo;

import java.util.List;
import java.util.Optional;

public interface IVeiculoService {

    String cadastrar(Veiculo veiculo);
    String alterar(Veiculo veiculo);
    Veiculo buscarPorPlaca(String placa);
    Veiculo buscarPorParteNome(String nome);
    Optional<List<Veiculo>> listarTodos();
    Optional<List<Veiculo>> listarTodosDisponiveisPaginacao(int skip, int limit);
    String remover(String placa);
    List<Veiculo> listarDisponiveis();
}
