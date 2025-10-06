package com.adacar.central;
import com.adacar.central.model.PessoaFisica;
import com.adacar.central.model.PessoaJuridica;
import com.adacar.central.model.Veiculo;
import com.adacar.central.repository.impl.PessoaFisicaRepository;
import com.adacar.central.repository.impl.PessoaJuridicaRepository;
import com.adacar.central.repository.impl.VeiculoRepository;
import java.util.List;
import com.adacar.central.util.DadosDeTesteFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CentralApplication {

	public static void main(String[] args) {
		// Bloco para carga inicial de dados usando Suppliers.
		// Este bloco simula a necessidade de popular o sistema com dados iniciais.
		System.out.println("--- INICIANDO CARGA DE DADOS INICIAIS (SEEDING) ---");

		PessoaFisicaRepository pessoaFisicaRepository = new PessoaFisicaRepository();
		PessoaJuridicaRepository pessoaJuridicaRepository = new PessoaJuridicaRepository();
		VeiculoRepository veiculoRepository = new VeiculoRepository();

		try {
			// Adicionar Pessoa Física usando o Supplier
			PessoaFisica joao = DadosDeTesteFactory.geradorJoaoPF.get(); // <-- USO DO SUPPLIER
			List<PessoaFisica> clientes = pessoaFisicaRepository.findAll();
			if (clientes.stream().noneMatch(c -> c.getDocumento().equals(joao.getDocumento()))) {
				pessoaFisicaRepository.save(joao);
				System.out.println("Cliente PF criado via Supplier: " + joao.getNome());
			} else {
				System.out.println("Cliente PF " + joao.getNome() + " já existe.");
			}

			// Adicionar Pessoa Juridica usando o Supplier
			PessoaJuridica empresa = DadosDeTesteFactory.geradorEmpresaXYZ.get(); // <-- USO DO SUPPLIER
			List<PessoaJuridica> clientesPJ = pessoaJuridicaRepository.findAll();
			if (clientesPJ.stream().noneMatch(c -> c.getDocumento().equals(empresa.getDocumento()))) {
				pessoaJuridicaRepository.save(empresa);
				System.out.println("Cliente PJ criado via Supplier: " + empresa.getNome());
			} else {
				System.out.println("Cliente PJ " + empresa.getNome() + " já existe.");
			}

			// Adicionar Veiculo usando o Supplier
			Veiculo tesla = DadosDeTesteFactory.geradorTesla.get();
			List<Veiculo> veiculos = veiculoRepository.findAll();
			if (veiculos.stream().noneMatch(v -> v.getPlaca().equals(tesla.getPlaca()))) {
				veiculoRepository.save(tesla);
				System.out.println("Veículo criado via Supplier: " + tesla.getNome());
			} else {
				System.out.println("Veículo " + tesla.getNome() + " já existe.");
			}

			// Adicionando 2 clientes aleatórios para popular a lista
			System.out.println("\nAdicionando clientes aleatórios...");
			for (int i = 0; i < 2; i++) {
				PessoaFisica clienteAleatorio = DadosDeTesteFactory.geradorPfAleatorio.get(); // <-- USO DO SUPPLIER DINÂMICO
				pessoaFisicaRepository.save(clienteAleatorio);
				System.out.println("Cliente aleatório criado: " + clienteAleatorio.getNome());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("--- CARGA DE DADOS FINALIZADA ---\n");
	}
}
