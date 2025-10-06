    package com.adacar.central.service.impl;

    import com.adacar.central.model.Aluguel;
    import com.adacar.central.model.PessoaFisica;
    import com.adacar.central.model.PessoaJuridica;
    import com.adacar.central.service.interfaces.IDesconto;
    import com.adacar.central.service.interfaces.IPagamento;
    import java.time.Duration;

    public class PagamentoService implements IPagamento {
        private final IDesconto desconto;

        public PagamentoService(IDesconto desconto) {
            this.desconto = desconto;
        }

        private long calcularDiarias(Aluguel aluguel) {
            if (aluguel.getDataHoraRetirada() == null || aluguel.getDataHoraDevolucao() == null) {
                throw new IllegalArgumentException("Datas de retirada e devolução devem ser informadas.");
            }
            Duration duracao = Duration.between(aluguel.getDataHoraRetirada(), aluguel.getDataHoraDevolucao());
            long horas = duracao.toHours();
            return (horas / 24) + (horas % 24 > 0 ? 1 : 0);
        }

        @Override
        public double calcularValorTotal(Aluguel aluguel) {
            long diarias = calcularDiarias(aluguel);
            double valorBase = aluguel.getVeiculo().getValorDiaria() * diarias;
            double valorDesconto = 0.0;

            valorDesconto = this.desconto.calcular((int) diarias);

            return valorBase - (valorBase * valorDesconto);
        }
    }
