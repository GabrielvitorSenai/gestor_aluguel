package com.tecdes.aluguel.model;

public class AluguelCasa implements Aluguel {
    // Declaração da classe BoletoPagamento.
    // Ela IMPLEMENTA a interface Pagamento, ou seja, se compromete a definir o método "processar".
    //
    // Aqui vemos o uso do POLIMORFISMO — várias classes (como PixPagamento, CartaoPagamento, etc.)
    // podem implementar a mesma interface Pagamento de formas diferentes.
    //
    // Isso está diretamente ligado a dois princípios SOLID:
    // - **O** (Open/Closed): podemos criar novos tipos de pagamento sem modificar o código existente.
    // - **L** (Liskov Substitution): qualquer classe que implemente Pagamento pode ser usada no lugar da outra.

    @Override
    public String processar(double valor) {
        // Implementa o método da interface Pagamento.
        // O uso de @Override garante que estamos substituindo um método da interface corretamente.
        // Este método é responsável por realizar o "processamento" de um pagamento via boleto.

        return "Pagamento de R$ " + valor + " gerou um boleto bancário.\n";
        // Retorna uma mensagem indicando que o pagamento gerou um boleto bancário.
        //
        // Esse retorno é genérico, permitindo que o ControllerPagamento trate todos
        // os tipos de pagamento da mesma forma — sem precisar saber se é boleto, pix, cartão, etc.
    }

}