package com.tecdes.aluguel.model;

public class AluguelComercial implements Aluguel {
    
    @Override
    public String processar(double valor) {
        
        return "Pagamento de R$ " + valor + " realizado via PIX\n";
        
    }

}
