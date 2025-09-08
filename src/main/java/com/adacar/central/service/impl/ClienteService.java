package com.adacar.central.service.impl;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.InvalidStateException;

import com.adacar.central.model.Cliente;
import com.adacar.central.model.PessoaFisica;
import com.adacar.central.model.PessoaJuridica;
import com.adacar.central.repository.impl.PessoaFisicaRepository;
import com.adacar.central.repository.impl.PessoaJuridicaRepository;
import com.adacar.central.service.interfaces.IClienteService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteService  implements IClienteService {
  private PessoaFisicaRepository pessoaFisicaRepository;
  private PessoaJuridicaRepository pessoaJuridicaRepository;

  private  CPFValidator cpfValidator = new CPFValidator();
  private  CNPJValidator cnpjValidator = new CNPJValidator();

  public ClienteService() {
    this.pessoaFisicaRepository = new PessoaFisicaRepository();
    this.pessoaJuridicaRepository = new PessoaJuridicaRepository();
  }


  @Override
  public String cadastrar(String nome, String Documento) throws IOException {
    try{
    Cliente clienteExistente = buscarPorDocumento(Documento).orElse(null);
    if (clienteExistente != null) {
      return "Cliente com documento " + Documento + " já existe: " + clienteExistente.getNome();
    }
    String documentoLimpo = limparDocumento(Documento);
    if (documentoLimpo.length() == 11) {
      if (isCPFValido(documentoLimpo)) {
        PessoaFisica novaPessoaFisica = new PessoaFisica(nome, documentoLimpo);
        pessoaFisicaRepository.save(novaPessoaFisica);
        return "Pessoa Física cadastrada com sucesso: " + nome + " - " + documentoLimpo;
      } else {
        return "CPF inválido: " + Documento;
      }
    } else if (documentoLimpo.length() == 14) {
      if (isCNPJValido(documentoLimpo)) {
        PessoaJuridica novaPessoaJuridica = new PessoaJuridica(nome, documentoLimpo);
        pessoaJuridicaRepository.save(novaPessoaJuridica);
        return "Pessoa Jurídica cadastrada com sucesso: " + nome + " - " + documentoLimpo;
      } else {
        return "CNPJ inválido: " + Documento;
      }
    } else {
      return "Documento deve ser CPF (11 dígitos) ou CNPJ (14 dígitos): " + Documento;
    }
    } catch (Exception e) {
      e.printStackTrace();
      return "Erro ao cadastrar cliente: " + e.getMessage();
    }

  }


  private String limparDocumento(String documento) {
    return documento.replaceAll("[^0-9]", "");
  }

  private boolean isCPFValido(String cpf) {
    try {
      cpfValidator.assertValid(cpf);
      return true;
    } catch (InvalidStateException e) {
      return false;
    }
  }

  private boolean isCNPJValido(String cnpj) {
    try {
      cnpjValidator.assertValid(cnpj);
      return true;
    } catch (InvalidStateException e) {
      return false;
    }
  }

  @Override
  public String alterar(String nome, String Documento) {
    try {
      Cliente clienteExistente = buscarPorDocumento(Documento).orElse(null);
      if (clienteExistente == null) {
        return "Cliente com documento " + Documento + " não encontrado.";
      }
      String documentoLimpo = limparDocumento(Documento);
      if (documentoLimpo.length() == 11) {
          PessoaFisica pessoaFisica = (PessoaFisica) clienteExistente;
          pessoaFisica.setNome(nome);
          pessoaFisicaRepository.update(pessoaFisica);
          return "Pessoa Física atualizada com sucesso: " + nome + " - " + documentoLimpo;
      } else if (documentoLimpo.length() == 14) {
          PessoaJuridica pessoaJuridica = (PessoaJuridica) clienteExistente;
          pessoaJuridica.setNome(nome);
          pessoaJuridicaRepository.update(pessoaJuridica);
          return "Pessoa Jurídica atualizada com sucesso: " + nome + " - " + documentoLimpo;
      } else {
        return "Documento deve ser CPF (11 dígitos) ou CNPJ (14 dígitos): " + Documento;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return "Erro ao atualizar cliente: " + e.getMessage();
    }
  }

  @Override
  public Optional<List<Cliente>> listarTodos() {
    try {
      List<PessoaFisica> pessoasFisicas = listarTodasPessoasFisicas().get();
      List<PessoaJuridica> pessoasJuridicas = listarTodasPessoasJuridicas().get();

      List<Cliente> clientes = new ArrayList<>(); // Use the supertype Cliente
      clientes.addAll(pessoasJuridicas);
      clientes.addAll(pessoasFisicas);

      return Optional.of(clientes);
    } catch (Exception e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }
  private Optional<List<PessoaFisica>> listarTodasPessoasFisicas() {
    try {
    return Optional.of(pessoaFisicaRepository.findAll());
    } catch (Exception e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  private Optional<List<PessoaJuridica>> listarTodasPessoasJuridicas() {
    try {
      return Optional.of(pessoaJuridicaRepository.findAll());
    } catch (Exception e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  @Override
  public Optional<Cliente> buscarPorDocumento(String documento) {
    return listarTodos().flatMap(clientes ->
        clientes.stream()
            .filter(cliente -> cliente.getDocumento().equals(documento))
            .findFirst()
    );
  }

  @Override
  public Optional<List<Cliente>> buscarPorParteNome(String nome) {
    return  listarTodos().map(clientes ->
        clientes.stream()
            .filter(cliente -> cliente.getNome().toLowerCase().contains(nome.toLowerCase()))
            .toList()
    );
  }

//        exclusivo para mocks de teste
  public void setPessoaFisicaRepository(PessoaFisicaRepository pessoaFisicaRepository) {
    this.pessoaFisicaRepository = pessoaFisicaRepository;
  }

  public void setPessoaJuridicaRepository(PessoaJuridicaRepository pessoaJuridicaRepository) {
    this.pessoaJuridicaRepository = pessoaJuridicaRepository;
  }

  public void setCpfValidator(CPFValidator cpfValidator) {
    this.cpfValidator = cpfValidator;
  }

  public void setCnpjValidator(CNPJValidator cnpjValidator) {
    this.cnpjValidator = cnpjValidator;
  }

}
