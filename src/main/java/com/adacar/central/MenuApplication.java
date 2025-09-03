package com.adacar.central;

import com.adacar.central.model.Aluguel;
import com.adacar.central.model.Veiculo;
import com.adacar.central.repository.impl.AluguelRepository;
import com.adacar.central.repository.impl.VeiculoRepository;
import com.adacar.central.service.impl.ClienteService;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for running the interactive command-line menu.
 * This class is the primary entry point for user interaction with the application.
 */
public class MenuApplication {

    public static void main(String[] args) {
        // Instantiate services and repositories needed for the menu's operations.
        ClienteService clienteService = new ClienteService();
        VeiculoRepository veiculoRepository = new VeiculoRepository();
        AluguelRepository aluguelRepository = new AluguelRepository();

        Scanner scanner = new Scanner(System.in);
        int option = -1;

        // The main loop continues until the user chooses option 0 to exit.
        while (option != 0) {
            System.out.println("\n============== MENU ADA CAR ==============");
            System.out.println("--- GESTÃO DE CLIENTES ---");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Alterar Nome do Cliente");
            System.out.println("3. Listar todos os Clientes");
            System.out.println("4. Buscar Cliente por Documento");
            System.out.println("5. Buscar Clientes por Nome");
            System.out.println("--- GESTÃO DE VEÍCULOS E ALUGUÉIS ---");
            System.out.println("6. Listar todos os Veículos");
            System.out.println("7. Listar todos os Aluguéis");
            System.out.println("0. Sair do Sistema");
            System.out.println("========================================");
            System.out.print("➡️  Escolha uma opção: ");

            try {
                option = scanner.nextInt();
                // This nextLine() is crucial to consume the newline character left by nextInt(),
                // preventing issues with subsequent nextLine() calls.
                scanner.nextLine();

                switch (option) {
                    case 1:
                        System.out.println("\n--- Cadastrar Novo Cliente ---");
                        System.out.print("Digite o nome: ");
                        String nomeCadastro = scanner.nextLine();
                        System.out.print("Digite o documento (CPF ou CNPJ): ");
                        String docCadastro = scanner.nextLine();
                        String resultadoCadastro = clienteService.cadastrar(nomeCadastro, docCadastro);
                        System.out.println("✅ " + resultadoCadastro);
                        break;

                    case 2:
                        System.out.println("\n--- Alterar Nome do Cliente ---");
                        System.out.print("Digite o documento do cliente que deseja alterar: ");
                        String docAlterar = scanner.nextLine();
                        System.out.print("Digite o novo nome: ");
                        String novoNome = scanner.nextLine();
                        String resultadoAlteracao = clienteService.alterar(novoNome, docAlterar);
                        System.out.println("🔄 " + resultadoAlteracao);
                        break;

                    case 3:
                        System.out.println("\n--- Lista de Todos os Clientes ---");
                        clienteService.listarTodos().ifPresentOrElse(
                                listaClientes -> {
                                    if (listaClientes.isEmpty()) {
                                        System.out.println("ℹ️  Nenhum cliente cadastrado.");
                                    } else {
                                        listaClientes.forEach(System.out::println);
                                    }
                                },
                                () -> System.out.println("❌ Erro ao listar clientes.")
                        );
                        break;

                    case 4:
                        System.out.println("\n--- Buscar Cliente por Documento ---");
                        System.out.print("Digite o documento a ser buscado: ");
                        String docBusca = scanner.nextLine();
                        clienteService.buscarPorDocumento(docBusca).ifPresentOrElse(
                                cliente -> System.out.println("✔️  Cliente encontrado: " + cliente),
                                () -> System.out.println("❌ Cliente não encontrado.")
                        );
                        break;

                    case 5:
                        System.out.println("\n--- Buscar Cliente por Parte do Nome ---");
                        System.out.print("Digite parte do nome a ser buscado: ");
                        String nomeBusca = scanner.nextLine();
                        System.out.println("\n--- Resultado da Busca ---");
                        clienteService.buscarPorParteNome(nomeBusca).ifPresentOrElse(
                                listaClientes -> {
                                    if (listaClientes.isEmpty()) {
                                        System.out.println("ℹ️  Nenhum cliente encontrado com o termo: " + nomeBusca);
                                    } else {
                                        listaClientes.forEach(System.out::println);
                                    }
                                },
                                () -> System.out.println("❌ Erro ao buscar clientes.")
                        );
                        break;

                    case 6:
                        System.out.println("\n--- Lista de Todos os Veículos ---");
                        List<Veiculo> todosVeiculos = veiculoRepository.findAll();
                        if (todosVeiculos.isEmpty()) {
                            System.out.println("ℹ️  Nenhum veículo cadastrado.");
                        } else {
                            todosVeiculos.forEach(System.out::println);
                        }
                        break;

                    case 7:
                        System.out.println("\n--- Lista de Todos os Aluguéis ---");
                        List<Aluguel> todosAlugueis = aluguelRepository.findAll();
                        if (todosAlugueis.isEmpty()) {
                            System.out.println("ℹ️  Nenhum aluguel registrado.");
                        } else {
                            todosAlugueis.forEach(System.out::println);
                        }
                        break;

                    case 0:
                        System.out.println("\n👋 Saindo do sistema... Até logo!");
                        break;

                    default:
                        System.out.println("⚠️ Opção inválida. Por favor, tente novamente.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("❌ Erro: Por favor, digite um número válido para a opção.");
                scanner.nextLine(); // Clears the invalid input from the scanner.
                option = -1; // Resets option to ensure the loop continues.
            } catch (Exception e) {
                System.out.println("🚨 Ocorreu um erro inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Close the scanner to release system resources.
        scanner.close();
    }
}