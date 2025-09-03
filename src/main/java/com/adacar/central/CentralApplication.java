package com.adacar.central;

import com.adacar.central.application.Impl.JsonDataReader;
import com.adacar.central.application.Impl.JsonDataWriter;
import com.adacar.central.enums.TipoVeiculo;
import com.adacar.central.model.Aluguel;
import com.adacar.central.model.Cliente;
import com.adacar.central.model.PessoaFisica;
import com.adacar.central.model.PessoaJuridica;
import com.adacar.central.model.Veiculo;
import com.adacar.central.repository.impl.AluguelRepository;
import com.adacar.central.repository.impl.PessoaFisicaRepository;
import com.adacar.central.repository.impl.PessoaJuridicaRepository;
import com.adacar.central.repository.impl.VeiculoRepository;
import java.io.File;
import java.util.List;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CentralApplication {

	public static void main(String[] args) {
		//SpringApplication.run(CentralApplication.class, args);
		PessoaFisicaRepository pessoaFisicaRepository = new PessoaFisicaRepository();
		PessoaJuridicaRepository pessoaJuridicaRepository = new PessoaJuridicaRepository();
		VeiculoRepository veiculoRepository = new VeiculoRepository();
		AluguelRepository aluguelRepository = new AluguelRepository();

		try {
			// Pessoa Física
			List<PessoaFisica> clientes = pessoaFisicaRepository.findAll();
			for (Cliente cliente : clientes) {
				System.out.println("cliente lido do arquivo: " + cliente.getNome() + " - " + cliente.getDocumento());
			}

			// Pessoa Jurídica
			List<PessoaJuridica> clientesPJ = pessoaJuridicaRepository.findAll();
			for (Cliente cliente : clientesPJ) {
				System.out.println("cliente lido do arquivo: " + cliente.getNome() + " - " + cliente.getDocumento());
			}

			// Veículos
			List<Veiculo> veiculos = veiculoRepository.findAll();
			for (Veiculo veiculo : veiculos) {
				System.out.println("veiculo lido do arquivo: " + veiculo.getNome() + " - " + veiculo.getPlaca());
			}

			// Aluguéis
			List<Aluguel> alugueis = aluguelRepository.findAll();
			for (Aluguel aluguel : alugueis) {
				System.out.println("aluguel lido do arquivo: " + aluguel);
			}

			System.out.println("agora serão escritas novas classes nas listas trazidas pelos repositórios e incluídas nos arquivos json");

			// Adicionar Pessoa Física
			PessoaFisica joao = new PessoaFisica("João Vieira", "123.456.789-00");
			if (clientes.stream().anyMatch(cliente -> cliente.getDocumento().equals("123.456.789-00"))) {
				System.out.println("Cliente com CPF 123.456.789-00 já existe: " + joao.getNome() + " - " + joao.getDocumento());
				return;
			}
			pessoaFisicaRepository.save(joao);
			System.out.println("cliente criado: " + joao + " e adicionado ao arquivo clientesPF.json");

			// Adicionar Pessoa Jurídica
			PessoaJuridica empresa = new PessoaJuridica("Empresa XYZ", "12.345.678/0001-00");
			if (clientesPJ.stream().anyMatch(cliente -> cliente.getDocumento().equals("12.345.678/0001-00"))) {
				System.out.println("Cliente com CNPJ 12.345.678/0001-00 já existe: " + empresa.getNome() + " - " + empresa.getDocumento());
				return;
			}
			pessoaJuridicaRepository.save(empresa);
			System.out.println("cliente criado: " + empresa + " e adicionado ao arquivo clientesPJ.json");

			// Adicionar Veículo
			Veiculo tesla = new Veiculo("ABC-1234", "Tesla Model X", TipoVeiculo.SUV);
			if (veiculos.stream().anyMatch(veiculo -> veiculo.getPlaca().equals("ABC-1234"))) {
				System.out.println("Veículo com placa ABC-1234 já existe: " + tesla.getNome() + " - " + tesla.getPlaca());
				return;
			}
			veiculoRepository.save(tesla);
			System.out.println("veiculo criado: " + tesla + " e adicionado ao arquivo veiculos.json");

			// Adicionar Aluguel
			Aluguel aluguel1 = new Aluguel(joao, tesla, "Loja Centro", java.time.LocalDateTime.now());
			if (alugueis.stream().anyMatch(aluguel ->
					aluguel.getCliente().getDocumento().equals("123.456.789-00") &&
							aluguel.getVeiculo().getPlaca().equals("ABC-1234") &&
							aluguel.getDataHoraRetirada().equals(aluguel1.getDataHoraRetirada()))) {
				System.out.println("Aluguel já existe para o cliente " + aluguel1.getCliente().getNome() +
						" com o veículo " + aluguel1.getVeiculo().getNome() +
						" na data/hora " + aluguel1.getDataHoraRetirada());
				return;
			}
			//tesla.setIsAlugado(true);
			veiculoRepository.update(tesla);
			aluguelRepository.save(aluguel1);
			System.out.println("aluguel criado: " + aluguel1 + " e adicionado ao arquivo alugueis.json");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
