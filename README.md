
# Projeto ADA LocateCar - Locadora de Veículos

Bem-vindo ao projeto **ADA LocateCar**, uma aplicação desenvolvida para gerenciar o aluguel de veículos, seguindo os princípios de design de software e as regras de negócio especificadas. Este README detalha a estrutura do projeto, os relacionamentos entre as classes e as regras de negócio implementadas.

---

## Estrutura e Relacionamentos

A arquitetura da aplicação foi desenhada com base em princípios de **coerência** e **baixo acoplamento**, utilizando interfaces e herança para garantir a flexibilidade e a extensibilidade. A seguir, os principais relacionamentos entre as classes, conforme o diagrama UML:

### Agregação e Composição

* **`Aluguel`** "1" \*-- "1" **`Veiculo`**: Cada aluguel é composto por um único veículo.

* **`Aluguel`** "1" \*-- "1" **`Cliente`**: Cada aluguel está associado a um único cliente.

### Generalização (Herança)

* **`Cliente`** `<|--` **`PessoaFisica`**: A classe `PessoaFisica` herda as características da classe `Cliente`.

* **`Cliente`** `<|--` **`PessoaJuridica`**: A classe `PessoaJuridica` herda as características da classe `Cliente`.

### Dependência e Abstração

* **`VeiculoService`** "1" --> "1" **`VeiculoRepository`**: O `VeiculoService` depende do `VeiculoRepository` para a persistência de dados.

* **`ClienteService`** "1" --> "1" **`ClienteRepository`**: O `ClienteService` depende do `ClienteRepository` para a persistência de dados.

* **`AluguelService`** "1" --> "1" **`VeiculoService`** e **`ClienteService`**: O `AluguelService` depende dos outros serviços para gerenciar as operações de aluguel e devolução.

* **`IRepository`** `<|--` **`VeiculoRepository`** e **`ClienteRepository`**: As classes de repositório implementam a interface `IRepository`, garantindo um contrato de persistência.

* **`IVeiculoService`**, **`IClienteService`** e **`IAluguelService`**: Interfaces que definem os contratos para as classes de serviço.

### Relacionamentos entre Entidades

* **`VeiculoRepository`** "1" \*-- "1..*" **`Veiculo`**: O repositório de veículos contém um ou mais veículos.

* **`ClienteRepository`** "1" \*-- "1..*" **`Cliente`**: O repositório de clientes contém um ou mais clientes.

* **`Veiculo`** "1" \*-- "1" **`TipoVeiculo`**: Cada veículo possui um único tipo.

---

## Regras de Negócio Implementadas

A aplicação foi projetada para cumprir as seguintes regras de negócio, garantindo a integridade e a lógica das operações de aluguel.

* **RN1: Unicidade de Veículos**: Cada **veículo** é identificado de forma única pela sua **placa**. Não é possível cadastrar veículos com a mesma placa.

* **RN2: Tipos de Veículos**: A aplicação considera três tipos de veículos: **PEQUENO**, **MEDIO** e **SUV**.

* **RN3: Registro de Aluguéis**: Todos os aluguéis e devoluções registram o **local, data e horário**.

* **RN4: Cálculo de Diárias**: A cobrança é feita por diárias completas. Horas adicionais são consideradas uma nova diária. Por exemplo, um aluguel de 25 de janeiro, às 15h30, até 26 de janeiro, às 16h30, será cobrado como duas diárias.

* **RN5: Disponibilidade de Veículos**: Um veículo só pode ser alugado se estiver disponível. Veículos que já estão alugados não podem ser alugados novamente.

* **RN6: Unicidade de Clientes**: Clientes são identificados de forma única por seu documento: **CPF** para Pessoas Físicas e **CNPJ** para Pessoas Jurídicas. Não é possível cadastrar clientes com o mesmo documento.

* **RN7: Descontos de Devolução**: Descontos são aplicados no momento da devolução, conforme a duração do aluguel:
    * **Pessoa Física**: 5% de desconto para aluguéis que durarem mais de 5 diárias.
    * **Pessoa Jurídica**: 10% de desconto para aluguéis que durarem mais de 3 diárias.
