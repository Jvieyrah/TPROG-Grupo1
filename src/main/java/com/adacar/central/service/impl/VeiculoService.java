package com.adacar.central.service.impl;

import com.adacar.central.enums.StatusVeiculo;
import com.adacar.central.model.Veiculo;
import com.adacar.central.repository.impl.VeiculoRepository;
import com.adacar.central.service.interfaces.IVeiculoService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class VeiculoService implements IVeiculoService {

    private VeiculoRepository veiculoRepository;
    
    // Regex para validar placa no formato brasileiro (AAA-0000 ou AAA0A00)
    private static final String PLACA_PATTERN = "^[A-Z]{3}[0-9][0-9A-Z][0-9]{2}$";
    private static final Pattern pattern = Pattern.compile(PLACA_PATTERN);

    public VeiculoService() {
        this.veiculoRepository = new VeiculoRepository();
    }

    @Override
    public String cadastrar(Veiculo veiculo) {
        try {
            validarVeiculo(veiculo);
            veiculoRepository.save(veiculo);
            return "Veiculo inserido com sucesso!";
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return "Erro ao cadastrar veículo";
        }
    }

    @Override
    public String alterar(Veiculo veiculo) {
        try {
            validarVeiculo(veiculo);
            veiculoRepository.update(veiculo);
            return "Veiculo alterado com sucesso!";
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return "Erro ao alterar veículo" ;
        }
    }

    @Override
    public Veiculo buscarPorPlaca(String placa) {
        try {
            validarPlaca(placa);
            return veiculoRepository.findById(placa);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Veiculo buscarPorParteNome(String nome) {
        if (nome == null || nome.isEmpty()) {
            throw new RuntimeException("O nome do veículo é obrigatório");
        }
        try {
            return veiculoRepository.findById(nome);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao buscar veículo: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<List<Veiculo>> listarTodos() {
        try {
            return Optional.of(veiculoRepository.findAll());
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public String remover(String placa) {
        try {
            validarPlaca(placa);
            veiculoRepository.delete(placa);
            return "ok";
        } catch (IOException e) {
            return "Erro ao remover veículo";
        }
    }

    @Override
    public List<Veiculo> listarDisponiveis() {
        try {
            Optional<List<Veiculo>> veiculos = Optional.of(new ArrayList<>(veiculoRepository.findAll()));
            if (veiculos.get().isEmpty()) {
                return new ArrayList<>();
            }
            return veiculos.get()
                    .stream()
                    .filter(veiculo -> veiculo.getStatus().equals(StatusVeiculo.DISPONIVEL)).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao listar veículos: " + e.getMessage(), e);
        }
    }
    
    private void validarVeiculo(Veiculo veiculo) {
        if (veiculo == null) {
            throw new RuntimeException("O veículo não pode ser nulo");
        }
        
        if (veiculo.getPlaca() == null || veiculo.getPlaca().trim().isEmpty()) {
            throw new RuntimeException("A placa do veículo é obrigatória");
        }
        
        validarPlaca(veiculo.getPlaca());
        
        if (veiculo.getNome() == null || veiculo.getNome().trim().isEmpty()) {
            throw new RuntimeException("O nome do veículo é obrigatório");
        }
        
        if (veiculo.getTipo() == null) {
            throw new RuntimeException("O tipo do veículo é obrigatório");
        }
        
        if (veiculo.getValorDiaria() <= 0) {
            throw new RuntimeException("O valor da diária deve ser maior que zero");
        }
    }
    
    private void validarPlaca(String placa) {
        // Remove espaços em branco e converte para maiúsculas
        String placaFormatada = placa.trim().toUpperCase();
        
        // Verifica se a placa está no formato correto
        if (!pattern.matcher(placaFormatada).matches()) {
            throw new RuntimeException("Formato de placa inválido. Use o formato AAA-0000 ou AAA0A00");
        }
    }

    public void setVeiculoRepository(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }
}
