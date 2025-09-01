package com.adacar.central;

import com.adacar.central.application.Impl.JsonDataReader;
import com.adacar.central.application.Impl.JsonDataWriter;
import com.adacar.central.enums.TipoVeiculo;
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

			System.out.println("agora serão escritas novas classes nas listas trazidas pelo JsonDataReader e incluidas nos arquivos json ");
			JsonDataWriter<PessoaFisica> pessoaFisicaWriter = new JsonDataWriter<>();
			JsonDataWriter<PessoaJuridica> pessoaJuridicaWriter = new JsonDataWriter<>();
			JsonDataWriter<Veiculo> veiculoWriter = new JsonDataWriter<>();
			JsonDataWriter<Aluguel> aluguelWriter = new JsonDataWriter<>();


			PessoaFisica Joao = new PessoaFisica("João Vieira", "123.456.789-00");
			for (Cliente cliente : clientes){
				if (cliente.getDocumento().equals("123.456.789-00")){
					System.out.println("Cliente com CPF 123.456.789-00 já existe: " + cliente.getNome() + " - " + cliente.getDocumento());
					return;
				}
			}
			clientes.add(Joao);
			System.out.println("cliente criado: " + Joao.toString() + " e adicionado a ultima posição da lista Clientes" );
			pessoaFisicaWriter.writeList(filePF, clientes);
			System.out.println("arquivo clientesPF.json atualizado com o novo cliente João Vieira");

			PessoaJuridica empresa = new PessoaJuridica("Empresa XYZ", "12.345.678/0001-00");
			for (Cliente cliente : clientesPJ){
				if (cliente.getDocumento().equals("12.345.678/0001-00")){
					System.out.println("Cliente com CNPJ 12.345.678/0001-00 já existe: " + cliente.getNome() + " - " + cliente.getDocumento());
					return;
				}
			}
			clientesPJ.add(empresa);
			System.out.println("cliente criado: " + empresa.toString() + " e adicionado a ultima posição da lista ClientesPJ" );
			pessoaJuridicaWriter.writeList(filePJ, clientesPJ);
			System.out.println("arquivo clientesPJ.json atualizado com o novo cliente Empresa XYZ");

			System.out.println("veiculo a ser criado e adicionado a ultima posição da lista Veiculos");
			Veiculo tesla = new Veiculo("ABC-1234", "Tesla Model X", TipoVeiculo.SUV);
			for (Veiculo veiculo : veiculos){
				if (veiculo.getPlaca().equals("ABC-1234")){
					System.out.println("Veículo com placa ABC-1234 já existe: " + veiculo.getNome() + " - " + veiculo.getPlaca());
					return;
				}
			}
			veiculos.add(tesla);
			System.out.println("veiculo criado: " + tesla.toString() + " e adicionado a ultima posição da lista Veiculos" );
			veiculoWriter.writeList(fileVeiculo, veiculos);
			System.out.println("arquivo veiculos.json atualizado com o novo veiculo Tesla Model X");
			System.out.println("aluguel a ser criado e adicionado a ultima posição da lista Alugueis");
			Aluguel aluguel1 = new Aluguel(Joao, tesla, "Loja Centro", java.time.LocalDateTime.now());

			// Encontra o veículo na lista e o marque como alugado
			for (Veiculo veiculo : veiculos) {
				if ("ABC-1234".equals(veiculo.getPlaca())) {
					veiculo.setIsAlugado(true);
					aluguel1.setVeiculo(veiculo); // garante que o aluguel referencia o veículo atualizado
					System.out.println("Veículo " + veiculo.getNome() + " (Placa: " + veiculo.getPlaca() + ") foi marcado como alugado." + veiculo.getIsAlugado());
					break; // Encerra o loop após encontrar o veículo
				}
			}
			for (Aluguel aluguel : alugueis){
				if (aluguel.getCliente().getDocumento().equals("123.456.789-00") &&
					aluguel.getVeiculo().getPlaca().equals("ABC-1234") &&
					aluguel.getDataHoraRetirada().equals(aluguel1.getDataHoraRetirada())) {
					System.out.println("Aluguel já existe para o cliente " + aluguel.getCliente().getNome() +
							" com o veículo " + aluguel.getVeiculo().getNome() +
							" na data/hora " + aluguel.getDataHoraRetirada());
					return;
				}
			}
			veiculoWriter.writeList(fileVeiculo, veiculos); // atualiza o arquivo veiculos.json com o veiculo Tesla agora marcado como alugado
			alugueis.add(aluguel1);
			System.out.println("aluguel criado: " + aluguel1.toString() + " e adicionado a ultima posição da lista Alugueis" );
			aluguelWriter.writeList(fileAluguel, alugueis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
