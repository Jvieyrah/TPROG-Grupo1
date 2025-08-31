package com.adacar.central;

import com.adacar.central.application.Impl.JsonDataReader;
import com.adacar.central.model.Aluguel;
import com.adacar.central.model.Cliente;
import com.adacar.central.model.PessoaFisica;
import com.adacar.central.model.PessoaJuridica;
import com.adacar.central.model.Veiculo;
import java.io.File;
import java.util.List;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CentralApplication {

	public static void main(String[] args) {
		//SpringApplication.run(CentralApplication.class, args);
		JsonDataReader<PessoaFisica> jsonReaderPF = new JsonDataReader<>();
		JsonDataReader<PessoaJuridica> jsonReaderPJ = new JsonDataReader<>();
		JsonDataReader<Veiculo> jsonReaderVeiculo = new JsonDataReader<>();
		JsonDataReader<Aluguel> jsonReaderAluguel = new JsonDataReader<>();

		File filePF = new File("src/main/resources/files/clients/clientesPF.json");
		File filePJ = new File("src/main/resources/files/clients/clientesPJ.json");
		File fileVeiculo = new File("src/main/resources/files/vehicles/veiculos.json");
		File fileAluguel = new File("src/main/resources/files/rentals/alugueis.json");

		try {
			List<PessoaFisica> clientes = jsonReaderPF.readList(filePF, PessoaFisica.class);
			for (Cliente cliente : clientes){
				System.out.println("cliente lido do arquivo: " + cliente.getNome() + " - " + cliente.getDocumento());
			}
			List<PessoaJuridica> clientesPJ = jsonReaderPJ.readList(filePJ, PessoaJuridica.class);
			for (Cliente cliente : clientesPJ){
				System.out.println("cliente lido do arquivo: " + cliente.getNome() + " - " + cliente.getDocumento());
			}
			List<Veiculo> veiculos = jsonReaderVeiculo.readList(fileVeiculo, Veiculo.class);
			for (Veiculo veiculo : veiculos){
				System.out.println("veiculo lido do arquivo: " + veiculo.getNome() + " - " + veiculo.getPlaca());
			}
			List<Aluguel> alugueis = jsonReaderAluguel.readList(fileAluguel, Aluguel.class);
			for (Aluguel aluguel : alugueis){
				System.out.println("aluguel lido do arquivo: " + aluguel);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}


	}

}
