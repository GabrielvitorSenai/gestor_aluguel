package com.tecdes.aluguel.repository;



import java.util.ArrayList;
import java.util.List;

public class AluguelRepository {
    
    private List<String> registros = new ArrayList<>();
    

    public void salvar(String registro){
        
        registros.add(registro);
    

        System.out.println("Registro salvo com sucesso: " + registros);
    }

    public List<String> listar (){

        return new ArrayList<>(registros);
    }
}
