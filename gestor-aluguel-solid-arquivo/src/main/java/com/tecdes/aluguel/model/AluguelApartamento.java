package com.tecdes.aluguel.model;

public class AluguelApartamento implements Aluguel {
    @Override
    public String processar(double valor) {

        return "Pagamento de R$ " + valor + " processado via Cartão\n";
    }

}
