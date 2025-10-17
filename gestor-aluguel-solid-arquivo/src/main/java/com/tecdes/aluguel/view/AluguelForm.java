package com.tecdes.aluguel.view;

// Pacote (organização em camadas: view para interface gráfica/Swing).

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.tecdes.aluguel.controller.AluguelController;
import com.tecdes.aluguel.model.Aluguel;
import com.tecdes.aluguel.model.AluguelApartamento;
import com.tecdes.aluguel.model.AluguelCasa;
import com.tecdes.aluguel.model.AluguelComercial;

public class AluguelForm extends JFrame{
    // A classe de UI herda de JFrame (janela principal).

    private JTextField txtValor;               // Campo de entrada do valor (R$).
    private JTextField txtMes;
    private JComboBox<String> cmbMetodo;       // Seleção do método (Boleto/Cartão/PIX).
    private JTextArea txtResultado;            // Área para mostrar resultados/histórico.
    private AluguelController controller;    // Controller que orquestra regra + repositório.
    JLabel lblTaxaCond;                      // Rótulo: tipo do cartão (débito/crédito).
    JTextField cmbTaxaCond;           // Combobox para tipo de cartão.
    
    public AluguelForm(){
        // Configurações básicas da janela.
        setSize(420,520);
        setTitle("Gestor de Aluguel - v1.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);          // Layout absoluto (posicionamento manual via setBounds).
        setResizable(false);      // Janela fixa.

        controller = new AluguelController();
        // Instancia o controller (SRP: UI não processa pagamentos, apenas delega).

        initComponents();
        // Inicializa e posiciona os componentes da tela.
    }

    private void initComponents(){

        //------------Entrada de dados-------------------------------------------- 

        JLabel lblValor = new JLabel("Valor (R$):");
        lblValor.setBounds(40, 70, 100, 25);
        add(lblValor);

        txtValor = new JTextField();
        txtValor.setBounds(150, 70, 200, 25);
        add(txtValor);

        JLabel lblMes = new JLabel("Meses:");
        lblMes.setBounds(40, 120, 100, 25);
        add(lblMes);

        txtMes = new JTextField();
        txtMes.setBounds(150, 120, 200, 25);
        add(txtMes);

        JLabel lblMetodo = new JLabel("Tipo de locação:");
        lblMetodo.setBounds(40, 30, 150, 25);
        add(lblMetodo);

        cmbMetodo = new JComboBox<>(new String[]{"Casa", "Apartamento", "Comercial"});
        // Combobox com três opções.
        // OBS: aqui é "Cartão" (com acento).
        cmbMetodo.setBounds(190, 30, 160, 25);
        add(cmbMetodo);

        lblTaxaCond =new JLabel("Taxa do condominio R$: ");
        lblTaxaCond.setBounds(40, 170, 150, 25);
        lblTaxaCond.setVisible(false); // Só aparece quando método = Cartão.
        add(lblTaxaCond);

        cmbTaxaCond = new JTextField("80.00");
        cmbTaxaCond.setBounds(190, 170, 160, 25);
        cmbTaxaCond.setVisible(false); // Só aparece quando método = Cartão.
        add(cmbTaxaCond);


      //------------Botões-------------------------------------------- 
        JButton btnProcessar = new JButton("Processar Pagamento");
        btnProcessar.setBounds(120, 200, 180, 35);
        add(btnProcessar);

        JButton btnSalvarHistorico = new JButton("Salvar Histórico");
        btnSalvarHistorico.setBounds(120, 240, 180, 35);
        add(btnSalvarHistorico);

        //---------Saida de Dados----------
        txtResultado = new JTextArea();
        txtResultado.setEditable(false);   // Somente leitura (UI não edita o resultado).
        txtResultado.setLineWrap(true);
        txtResultado.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(txtResultado);
        scroll.setBounds(40, 290, 320, 100);
        add(scroll);

        //---------------Eventos (Listener)---------------------
        btnProcessar.addActionListener(e -> processar());
        // Ao clicar em "Processar", chama o método processar().

        cmbMetodo.addActionListener(e -> atualizarCampos());
        // Trocar método mostra/esconde campos de cartão.

        cmbTaxaCond.addActionListener(e -> atualizarCampos());
        // Trocar tipo (débito/crédito) mostra/esconde parcelas.

        btnSalvarHistorico.addActionListener(e -> salvarhistoricoEmArquivo());
        // Salva o histórico retornado pelo controller em arquivo.
        
    }

    private void atualizarCampos(){
        String metodo = (String) cmbMetodo.getSelectedItem();
        boolean isApartamento = metodo.equals("Apartamento");
        // Mostra controles de cartão somente quando método = Cartão.

        lblTaxaCond.setVisible(isApartamento);
        cmbTaxaCond.setVisible(isApartamento);

       
    }


    private void processar(){
        try {
            double valor = Double.parseDouble(txtValor.getText());
            // Converte o conteúdo do campo em double (pode lançar exceção).
            double mes = Double.parseDouble(txtMes.getText());

            String metodo = cmbMetodo.getSelectedItem().toString();
            // Lê o método selecionado (Boleto/Cartão/PIX).

            System.out.println(metodo);
            System.out.println(cmbMetodo.getSelectedItem());
            // Logs no console (debug).

            Aluguel pagamento = obterMetodo(metodo);
            // Escolhe a implementação concreta de Pagamento conforme o método selecionado.
            // POLIMORFISMO: o restante do fluxo só conhece a interface Pagamento.

            String resultado = controller.realizarPagamento(pagamento, valor);
            // Chama o controller (DIP no controller: depende de abstr. Pagamento).
            // SRP: UI apenas delega; não processa pagamento.

            txtResultado.append(resultado + "\n");
            // Mostra o resultado na área de texto (histórico visual).
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Valor iválido...");
            // Exceção genérica (NumberFormatException etc.). Mensagem com pequeno typo em "iválido".
        }
    }

    private Aluguel obterMetodo(String metodo){
        // Fábrica simples (hardcoded) que decide qual classe concreta instanciar.
        // Aqui a UI está acoplada às implementações (violação parcial de DIP na camada de view).
        if(metodo.equals("Casa")){
            return new AluguelCasa();
        }
        if(metodo.equals("Apartamento")){
            return new AluguelApartamento();
            // ATENÇÃO: aqui compara "Cartao" (sem acento),
            // enquanto no combo é "Cartão" (com acento). Nunca vai cair neste if.
            // Resultado: Cartão selecionado cairá no retorno padrão (Pix) abaixo.
        }
        return new AluguelComercial();
        // Caso padrão: retorna PIX.
    }

    private void salvarhistoricoEmArquivo(){
        List<String> pagamentos = controller.listarPagamentos();
        // Pede ao controller o histórico armazenado no repositório.

        if(pagamentos.isEmpty()){
            JOptionPane.showMessageDialog(null, "Não ha pagamentos no historico");
            // Mensagem com acentuação faltando ("há", "histórico"). Apenas observação.
            return;
        }

        try(FileWriter writer = new FileWriter("historico_pagamento.txt")){
            // Cria/abre arquivo texto no diretório de execução da aplicação.

            for(String registro : pagamentos){
                writer.write(registro);
                // Escreve cada registro (sem separador/linha extra além do que já veio na String).
            }
            JOptionPane.showMessageDialog(null, "Gravação do arquivo com sucesso.");
            // Confirmação ao usuário.

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro na tentativa de salvar o arquivo: "+e.getMessage());
            // Trata problemas de IO (permissão, path, disco, etc.).
        }
    }

}
