package com.tecdes.aluguel.controller;

import java.util.List;

import com.tecdes.aluguel.model.Aluguel;
import com.tecdes.aluguel.repository.AluguelRepository;




public class AluguelController {

    private AluguelRepository repository;

    public AluguelController(){
        repository = new AluguelRepository();
    }

    public String realizarPagamento(Aluguel metodo, double valor){

        String resultado = metodo.processar(valor);
        

        repository.salvar(resultado);

        System.out.println("Pagamento registrado: "+ resultado);

        return resultado;
    }

    public List<String> listarPagamentos(){

        List<String> lista = repository.listar();

        System.out.println("Lista de pagamentos: " + lista);

        return lista;
    }
}
