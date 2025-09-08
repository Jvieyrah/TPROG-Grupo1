
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

---

## Conceitos Aplicados: Facilidades e Desafios

Durante o desenvolvimento do projeto, foram aplicados diversos conceitos de Programação Orientada a Objetos. Abaixo estão os conceitos que apresentaram maior facilidade e os que representaram maiores desafios na implementação:

### Conceitos de Fácil Aplicação

1. **Herança e Polimorfismo**
   - A implementação da hierarquia de classes, com `Cliente` como classe abstrata e `PessoaFisica`/`PessoaJuridica` como classes concretas, foi relativamente direta.
   - O uso de métodos abstratos como `getDocumento()` permitiu implementar comportamentos específicos para cada tipo de cliente de forma organizada.

2. **Encapsulamento**
   - A definição clara de atributos privados/protegidos e a utilização de getters e setters facilitaram o controle de acesso aos dados.
   - A separação entre as camadas de modelo, repositório e serviço manteve o código organizado e coeso.

3. **Tratamento de Exceções**
   - A implementação de validações e tratamento de erros para os dados de entrada tornou o sistema mais robusto e previsível.

### Conceitos que Apresentaram Desafios

1. **Persistência de Dados com JSON**
   - A implementação da serialização/desserialização de objetos para JSON, especialmente com herança, exigiu o uso de anotações específicas como `@JsonTypeInfo` e `@JsonSubTypes`.
   - A manutenção da integridade dos dados entre as sessões da aplicação foi um desafio que exigiu atenção especial.

2. **Gerenciamento de Estados**
   - Controlar o estado dos veículos (disponível/alugado) e garantir a consistência dessas informações em diferentes partes do sistema foi mais complexo do que o inicialmente previsto.
   - A implementação de regras de negócio que envolviam múltiplas entidades (como verificar disponibilidade de veículo antes do aluguel) exigiu um planejamento cuidadoso.

3. **Design de Interfaces**
   - A definição clara das interfaces de serviço e repositório exigiu várias iterações para encontrar o equilíbrio entre flexibilidade e simplicidade.
   - A decisão sobre quais métodos deveriam estar em quais interfaces foi um processo que evoluiu ao longo do desenvolvimento.

4. **Acoplamento entre Componentes**
   - Manter um baixo acoplamento entre as classes de serviço e repositório, especialmente em operações que envolvem múltiplas entidades, foi um desafio constante.
   - A injeção de dependências entre os serviços precisou ser cuidadosamente planejada para evitar dependências circulares.

Estes desafios foram importantes para o aprendizado e resultaram em um código mais robusto e manutenível, demonstrando a importância de um bom design de software na resolução de problemas complexos.

* **RN2: Tipos de Veículos**: A aplicação considera três tipos de veículos: **PEQUENO**, **MEDIO** e **SUV**.

* **RN3: Registro de Aluguéis**: Todos os aluguéis e devoluções registram o **local, data e horário**.

* **RN4: Cálculo de Diárias**: A cobrança é feita por diárias completas. Horas adicionais são consideradas uma nova diária. Por exemplo, um aluguel de 25 de janeiro, às 15h30, até 26 de janeiro, às 16h30, será cobrado como duas diárias.

* **RN5: Disponibilidade de Veículos**: Um veículo só pode ser alugado se estiver disponível. Veículos que já estão alugados não podem ser alugados novamente.

* **RN6: Unicidade de Clientes**: Clientes são identificados de forma única por seu documento: **CPF** para Pessoas Físicas e **CNPJ** para Pessoas Jurídicas. Não é possível cadastrar clientes com o mesmo documento.

* **RN7: Descontos de Devolução**: Descontos são aplicados no momento da devolução, conforme a duração do aluguel:
    * **Pessoa Física**: 5% de desconto para aluguéis que durarem mais de 5 diárias.
    * **Pessoa Jurídica**: 10% de desconto para aluguéis que durarem mais de 3 diárias.
