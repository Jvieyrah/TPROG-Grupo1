package com.adacar.central.service.impl;

import com.adacar.central.enums.StatusLocacao;
import com.adacar.central.enums.StatusVeiculo;
import com.adacar.central.model.Aluguel;
import com.adacar.central.model.Cliente;
import com.adacar.central.model.Veiculo;
import com.adacar.central.repository.interfaces.IRepository;
import com.adacar.central.service.interfaces.IPagamento;
import java.io.IOException;
import java.time.LocalDateTime;

public class AluguelService {
    private final IRepository<Aluguel> aluguelRepository;
    private final IRepository<Veiculo> veiculoRepository;
    private final IPagamento pagamento;

    public AluguelService(IRepository<Aluguel> aluguelRepository, IRepository<Veiculo> veiculoRepository, IPagamento pagamento) {
        this.aluguelRepository = aluguelRepository;
        this.veiculoRepository = veiculoRepository;
        this.pagamento = pagamento;
    }

    public Aluguel alugar(Cliente cliente, Veiculo veiculo, String filialRetirada, LocalDateTime dataRetirada) throws IOException {
        if (!veiculo.getStatus().equals(StatusVeiculo.DISPONIVEL)) {
            throw new IllegalStateException("Veículo não está disponível para locação.");
        }
        Aluguel aluguel = new Aluguel(cliente, veiculo, filialRetirada, dataRetirada);
        veiculo.setStatus(StatusVeiculo.ALUGADO);

        this.aluguelRepository.save(aluguel);
        this.veiculoRepository.update(veiculo);
        return aluguel;
    }

    public void devolver(Aluguel aluguel, String filialDevolucao, LocalDateTime dataDevolucao) throws IOException {
        aluguel.setDataHoraDevolucao(dataDevolucao);
        aluguel.setStatus(StatusLocacao.FINALIZADA);

        this.pagamento.calcularValorTotal(aluguel);

        aluguel.getVeiculo().setStatus(StatusVeiculo.DISPONIVEL);
        this.aluguelRepository.update(aluguel);
        this.veiculoRepository.update(aluguel.getVeiculo());
    }
}