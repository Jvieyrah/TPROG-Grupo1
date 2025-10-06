package com.adacar.central;

import com.adacar.central.enums.StatusLocacao;
import com.adacar.central.enums.TipoVeiculo;
import com.adacar.central.model.*;
import com.adacar.central.repository.impl.AluguelRepository;
import com.adacar.central.repository.impl.VeiculoRepository;
import com.adacar.central.service.impl.*;
import com.adacar.central.service.interfaces.IDesconto;
import com.adacar.central.service.interfaces.IPagamento;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;

public class MenuApplication {

    private static final ClienteService clienteService = new ClienteService();
    private static final VeiculoService veiculoService = new VeiculoService();
    private static final AluguelRepository aluguelRepository = new AluguelRepository();
    private static final RelatorioService relatorioService = new RelatorioService(aluguelRepository);


    private static final List<Aluguel> alugueisAtivos = new ArrayList<>();

   //consumer
    private static final Consumer<Cliente> impressoraDeCliente = cliente -> {
        System.out.printf("| Nome: %-30s | Documento: %-18s |\n",
                cliente.getNome(),
                cliente.getDocumento());
    };

    //consumerr
    private static final Consumer<Veiculo> impressoraDeVeiculo = veiculo -> {
        System.out.printf("| Placa: %-10s | Modelo: %-20s | Tipo: %-8s | Diária: R$ %-8.2f | Status: %s\n",
                veiculo.getPlaca(),
                veiculo.getNome(),
                veiculo.getTipo(),
                veiculo.getValorDiaria(),
                veiculo.getStatus());
    };


    public static void main(String[] args) {

        rodarTestesDeDesconto();
        Scanner scanner = new Scanner(System.in);
        int option;

        while (true) {
            displayMenu();
            try {
                option = Integer.parseInt(scanner.nextLine());

                switch (option) {
                    case 1 -> cadastrarCliente(scanner);
                    case 2 -> alterarCliente(scanner);
                    case 3 -> buscarClientePorDocumento(scanner);
                    case 4 -> listarTodosClientes();
                    case 5 -> cadastrarVeiculo(scanner);
                    case 6 -> alterarVeiculo(scanner);
                    case 7 -> buscarVeiculoPorPlaca(scanner);
                    case 8 -> listarVeiculosDisponiveis();
                    case 9 -> removerVeiculo(scanner);
                    case 10 -> alugarVeiculo(scanner);
                    case 11 -> devolverVeiculo(scanner);
                    case 12 -> gerarRelatorioFaturamento(scanner);
                    case 13 -> gerarRelatorioUso();
                    case 14 -> gerarRecibo(scanner);
                    case 0 -> {
                        System.out.println("Encerrando o sistema. Até logo! 👋");
                        scanner.close();
                        return;
                    }
                    default -> System.out.println("Opção inválida! Por favor, tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Erro: Por favor, digite um número válido.");
            } catch (Exception e) {
                System.err.println("Ocorreu um erro inesperado: " + e.getMessage());
                e.printStackTrace();
            }

            System.out.println("\nPressione Enter para continuar...");
            scanner.nextLine();
        }
    }

    private static void displayMenu() {
        System.out.println("\n--- ADA CAR RENTAL SYSTEM ---");
        System.out.println("======= GESTÃO DE CLIENTES =======");
        System.out.println("1. Cadastrar Cliente");
        System.out.println("2. Alterar Cliente");
        System.out.println("3. Buscar Cliente por Documento");
        System.out.println("4. Listar Todos os Clientes");
        System.out.println("======== GESTÃO DE VEÍCULOS ========");
        System.out.println("5. Cadastrar Veículo");
        System.out.println("6. Alterar Veículo");
        System.out.println("7. Buscar Veículo por Placa");
        System.out.println("8. Listar Veículos Disponíveis");
        System.out.println("9. Remover Veículo");
        System.out.println("=========== OPERAÇÕES ===========");
        System.out.println("10. Alugar Veículo");
        System.out.println("11. Devolver Veículo");
        System.out.println("========== RELATÓRIOS ===========");
        System.out.println("12. Gerar Relatório de Faturamento");
        System.out.println("13. Gerar Relatórios de Uso (Veículos e Clientes)");
        System.out.println("14. Gerar Recibo de Aluguel/Devolução");
        System.out.println("=================================");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void cadastrarCliente(Scanner scanner) throws IOException {
        System.out.print("Digite o nome do cliente: ");
        String nome = scanner.nextLine();
        System.out.print("Digite o documento (CPF ou CNPJ): ");
        String documento = scanner.nextLine();
        String resultado = clienteService.cadastrar(nome, documento);
        System.out.println(resultado);
    }

    private static void alterarCliente(Scanner scanner) {
        System.out.print("Digite o documento do cliente que deseja alterar: ");
        String documento = scanner.nextLine();
        System.out.print("Digite o NOVO nome do cliente: ");
        String nome = scanner.nextLine();
        String resultado = clienteService.alterar(nome, documento);
        System.out.println(resultado);
    }

    private static void buscarClientePorDocumento(Scanner scanner) {
        System.out.print("Digite o documento do cliente: ");
        String documento = scanner.nextLine();
        System.out.println("\n--- RESULTADO DA BUSCA ---");
        clienteService.buscarPorDocumento(documento)
                .ifPresentOrElse(
                        // MODIFICADO: Usando o Consumer para imprimir
                        impressoraDeCliente,
                        () -> System.out.println("Cliente não encontrado.")
                );
    }

    private static void listarTodosClientes() {
        int skip = 0;
        int limit = 10;
        Boolean repeat = true;
        while (repeat) {
            System.out.println("\n--- LISTA DE TODOS OS CLIENTES ---");
            System.out.println("\n--- pagina  " + (skip / limit + 1) + "  (mostrando " + limit
                + " por pagina)---");
            Optional<List<Cliente>> clientesOpt = clienteService.listarTodosComPaginacao(skip,
                limit);
            if (clientesOpt.isEmpty() || clientesOpt.get().isEmpty()) {
                System.out.println("Você esgotou a lista.");
            }
            clientesOpt.get().forEach(System.out::println);

            System.out.println(
                "\nOpções: [N] Próxima página | [P] Página anterior | [S] Sair da listagem");
            String escolha = new Scanner(System.in).nextLine().trim().toUpperCase();
            switch (escolha) {
                case "N" -> skip += limit;
                case "P" -> skip -= limit;
                case "S" -> repeat = false;
                default -> System.out.println("Opção inválida! Por favor, tente novamente.");
            }
        }
    }



    private static void cadastrarVeiculo(Scanner scanner) {
        try {
            System.out.print("Digite a placa do veículo (ex: ABC1D23): ");
            String placa = scanner.nextLine();
            System.out.print("Digite o nome/modelo do veículo (ex: Fiat Mobi): ");
            String nome = scanner.nextLine();
            System.out.print("Digite o tipo do veículo (PEQUENO, MEDIO, SUV): ");
            TipoVeiculo tipo = TipoVeiculo.valueOf(scanner.nextLine().toUpperCase());

            Veiculo veiculo = new Veiculo(placa, nome, tipo);
            String resultado = veiculoService.cadastrar(veiculo);
            System.out.println(resultado);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: Tipo de veículo inválido. Use PEQUENO, MEDIO ou SUV.");
        }
    }

    private static void alterarVeiculo(Scanner scanner) {
        System.out.print("Digite a placa do veículo que deseja alterar: ");
        String placa = scanner.nextLine();

        Veiculo veiculo = veiculoService.buscarPorPlaca(placa);
        if (veiculo == null) {
            System.out.println("Veículo não encontrado.");
            return;
        }

        try {
            System.out.print("Digite o NOVO nome/modelo do veículo: ");
            String novoNome = scanner.nextLine();
            System.out.print("Digite o NOVO tipo do veículo (PEQUENO, MEDIO, SUV): ");
            TipoVeiculo novoTipo = TipoVeiculo.valueOf(scanner.nextLine().toUpperCase());

            veiculo.setNome(novoNome);
            veiculo.setTipo(novoTipo);

            String resultado = veiculoService.alterar(veiculo);
            System.out.println(resultado);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: Tipo de veículo inválido. Use PEQUENO, MEDIO ou SUV.");
        }
    }

    private static void buscarVeiculoPorPlaca(Scanner scanner) {
        System.out.print("Digite a placa do veículo: ");
        String placa = scanner.nextLine();
        Veiculo veiculo = veiculoService.buscarPorPlaca(placa);
        System.out.println("\n--- RESULTADO DA BUSCA ---");
        if (veiculo != null) {
            // MODIFICADO: Usando o Consumer para imprimir
            impressoraDeVeiculo.accept(veiculo);
        } else {
            System.out.println("Veículo não encontrado.");
        }
    }

    private static void listarVeiculosDisponiveis() {
        int skip = 0;
        int limit = 10;
        Boolean repeat = true;
        while (repeat) {
            System.out.println("\n--- LISTA DE VEÍCULOS DISPONÍVEIS ---");
            System.out.println("\n--- pagina  " + (skip / limit + 1) + "  (mostrando " + limit
                + " por pagina)---");
            Optional<List<Veiculo>> veiculosOpt = veiculoService
                .listarTodosDisponiveisPaginacao(skip, limit);
            if (veiculosOpt.isEmpty() || veiculosOpt.get().isEmpty()) {
                System.out.println("Você esgotou a lista.");
            }
            veiculosOpt.get().forEach(System.out::println);

            System.out.println(
                "\nOpções: [N] Próxima página | [P] Página anterior | [S] Sair da listagem");
            String escolha = new Scanner(System.in).nextLine().trim().toUpperCase();
            switch (escolha) {
                case "N" -> skip += limit;
                case "P" -> skip -= limit;
                case "S" -> repeat = false;
                default -> System.out.println("Opção inválida! Por favor, tente novamente.");
            }
        }
    }

    private static void removerVeiculo(Scanner scanner) {
        System.out.print("Digite a placa do veículo que deseja remover: ");
        String placa = scanner.nextLine();

        boolean temAluguelAtivo = alugueisAtivos.stream()
                .anyMatch(aluguel -> aluguel.getVeiculo().getPlaca().equalsIgnoreCase(placa));

        if(temAluguelAtivo) {
            System.out.println("Erro: Veículo não pode ser removido pois está atualmente alugado.");
            return;
        }

        String resultado = veiculoService.remover(placa);
        if("ok".equalsIgnoreCase(resultado)) {
            System.out.println("Veículo removido com sucesso!");
        } else {
            System.out.println("Erro ao remover veículo: " + resultado);
        }
    }

    private static void alugarVeiculo(Scanner scanner) throws IOException {
        System.out.print("Digite o documento do cliente: ");
        String docCliente = scanner.nextLine();
        Optional<Cliente> clienteOpt = clienteService.buscarPorDocumento(docCliente);

        if (clienteOpt.isEmpty()) {
            System.out.println("Cliente não encontrado. Cadastre o cliente primeiro.");
            return;
        }

        listarVeiculosDisponiveis();
        System.out.print("\nDigite a placa do veículo a ser alugado: ");
        String placaVeiculo = scanner.nextLine();
        Veiculo veiculo = veiculoService.buscarPorPlaca(placaVeiculo);

        if (veiculo == null || !veiculo.podeSerAlugado()) {
            System.out.println("Veículo não encontrado ou não está disponível.");
            return;
        }

        System.out.print("Digite o local de retirada: ");
        String localRetirada = scanner.nextLine();

        IPagamento pagamentoService = new PagamentoService();
        AluguelService aluguelService = new AluguelService(new AluguelRepository(), new VeiculoRepository(), pagamentoService);

        // Usa a variável dataRetirada na chamada do serviço
        Aluguel novoAluguel = aluguelService.alugar(clienteOpt.get(), veiculo, localRetirada, dataRetirada);
        alugueisAtivos.add(novoAluguel);

        // Exibe a data/hora na mensagem de sucesso
        System.out.println("\n✅ Veículo alugado com sucesso!");
        System.out.println("   Data/Hora de Retirada: " + dataRetirada.toLocalTime().toString());
        System.out.println("   Data: " + dataRetirada.toLocalDate().toString());
    }

    private static void devolverVeiculo(Scanner scanner) throws IOException {
        System.out.print("Digite a placa do veículo a ser devolvido: ");
        String placa = scanner.nextLine();

        Optional<Aluguel> aluguelOpt = alugueisAtivos.stream()
                .filter(a -> a.getVeiculo().getPlaca().equalsIgnoreCase(placa))
                .findFirst();

        if (aluguelOpt.isEmpty()) {
            System.out.println("Nenhum aluguel ativo encontrado para esta placa nesta sessão.");
            return;
        }

        Aluguel aluguelParaDevolver = aluguelOpt.get();

        System.out.print("Digite o local de devolução: ");
        String localDevolucao = scanner.nextLine();

        IPagamento pagamentoService = new PagamentoService();
        AluguelService aluguelService = new AluguelService(new AluguelRepository(), new VeiculoRepository(), pagamentoService);

        LocalDateTime dataDevolucao = LocalDateTime.now().plusDays(5);

        double valorTotal = aluguelService.devolver(aluguelParaDevolver, localDevolucao, dataDevolucao);

        aluguelParaDevolver.setValorTotal(valorTotal);

        aluguelService.devolver(aluguelParaDevolver, localDevolucao, dataDevolucao);

        alugueisAtivos.remove(aluguelParaDevolver);

        System.out.println("\n✅ Veículo devolvido com sucesso!");
        System.out.println("   Local de Devolução: " + localDevolucao);
        System.out.println("   Data/Hora de Devolução: " + dataDevolucao.toLocalDate().toString() + " às " + dataDevolucao.toLocalTime().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
        System.out.printf("Valor total a pagar: R$ %.2f\n", valorTotal);
    }

    //Teste para testar os descontos (REMOVER DEPOIS)

    private static void rodarTestesDeDesconto() {
        System.out.println("\n--- INICIANDO TESTES AUTOMATIZADOS DE DESCONTO ---");
        Cliente clientePF = new PessoaFisica("Cliente PF Teste", "12345678901");
        Cliente clientePJ = new PessoaJuridica("Cliente PJ Teste", "12345678901234");
        Veiculo veiculo = new Veiculo("TST-0001", "Veículo de Teste", TipoVeiculo.MEDIO);
        veiculo.setValorDiaria(100.00);


        IPagamento pagamentoService = new PagamentoService();

        testarCenario("PF com 3 dias (sem desconto)", pagamentoService, clientePF, veiculo, 3, 300.00);

        testarCenario("PF com 6 dias (com 5% de desconto)", pagamentoService, clientePF, veiculo, 6, 570.00);

        testarCenario("PJ com 2 dias (sem desconto)", pagamentoService, clientePJ, veiculo, 2, 200.00);

        testarCenario("PJ com 4 dias (com 10% de desconto)", pagamentoService, clientePJ, veiculo, 4, 360.00);

        System.out.println("--- TESTES AUTOMATIZADOS FINALIZADOS ---\n");
    }

    private static void testarCenario(String descricao, IPagamento pagamentoService, Cliente cliente, Veiculo veiculo, int dias, double valorEsperado) {
        LocalDateTime dataRetirada = LocalDateTime.now();
        LocalDateTime dataDevolucao = dataRetirada.plusDays(dias);
        Aluguel aluguelTeste = new Aluguel(cliente, veiculo, "Filial Teste", dataRetirada);
        aluguelTeste.setDataHoraDevolucao(dataDevolucao);


        Function<Integer, Double> politicaDesconto = (cliente instanceof PessoaFisica)
                ? VeiculoService.POLITICA_DESCONTO_PF
                : VeiculoService.POLITICA_DESCONTO_PJ;

        double valorCalculado = pagamentoService.calcularValorTotal(aluguelTeste, politicaDesconto);

        boolean passou = Math.abs(valorEsperado - valorCalculado) < 0.01;

        System.out.printf("Cenário: %-35s | Esperado: R$ %.2f | Calculado: R$ %.2f | Resultado: %s\n",
                descricao, valorEsperado, valorCalculado, (passou ? "✅ PASSOU" : "❌ FALHOU"));
    }
}