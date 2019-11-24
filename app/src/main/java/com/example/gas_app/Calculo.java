package com.example.gas_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Calculo extends AppCompatActivity {

    Operacao operacao;
    EditText edtGasolina, edtEtanol, edtComparacao, edtData, edtId;
    Button btnVoltar, btnSalvar, btnCalcula, btnExcluir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculo);

        operacao = new Operacao();

        edtGasolina = findViewById(R.id.edtGasolina);
        edtEtanol = findViewById(R.id.edtEtanol);
        edtComparacao = findViewById(R.id.edtComparacao);
        edtData = findViewById(R.id.edtData);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnVoltar = findViewById(R.id.btnVoltar);
        btnCalcula = findViewById(R.id.btnCalcula);
        edtId = findViewById(R.id.edtId);
        btnExcluir = findViewById(R.id.btnExcluir);
        edtId.setEnabled(false);
        edtData.setEnabled(false);
        edtComparacao.setEnabled(false);

        //capturando o caminho que foi utilizado para abrir a tela
        Intent caminhoTela = getIntent();
        if (caminhoTela != null) {
            //capturando os parametros enviados para esta tela
            Bundle parans = caminhoTela.getExtras();

            //verifica se existem parametros enviados
            if (parans != null) {
                //populando os campos da tela com os recebidos da principal
                edtId.setText(parans.getString("id"));
                edtGasolina.setText(parans.getString("gasolina"));
                edtEtanol.setText(parans.getString("etanol"));
                edtComparacao.setText(parans.getString("comparacao"));
                edtData.setText(parans.getString("data"));

                //desabilitando o RA, pois é nossa chave no banco
                edtGasolina.setEnabled(false);
                edtEtanol.setEnabled(false);
                btnSalvar.setEnabled(false);
            }
        }

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ao cancelar, chama o evento de voltar do
                //  do android para fechar a tela
                onBackPressed();
            }
        });

        btnCalcula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");

                Date data = new Date();

                String dataformatada = formataData.format(data);

                edtData.setText(dataformatada);

                String retorno = "";
                Float resultado;

                //validando o Nome, RA e Cidade, para nao salvar vazio
                if (edtGasolina.getText().toString().isEmpty()) {
                    mostrarMensagem( "Hackaton", "O campo Gasolina é obrigatório!");
                    return;
                }

                if (edtEtanol.getText().toString().isEmpty()) {
                    mostrarMensagem("Hackaton", "O campo Etanol é obrigatório!");
                    return;
                }

                obterDados();

                retorno = (String.valueOf(operacao.divide()));

                edtComparacao.setText(retorno);

                resultado = operacao.divide();

                if (resultado < 0.7) {
                    mostrarMensagem("Resultado: ", "Abastecer com Etanol!");
                } else if ( resultado >= 0.7) {
                    mostrarMensagem("Resultado", "Abastecer com Gasolina !");
                }

            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtComparacao.getText().toString().isEmpty()) {
                    mostrarMensagem("Alerta:", "Realize o cálculo para salvar!");
                    return;
                }

                try {
                    TbHistorico tabela = new TbHistorico(
                            Calculo.this);

                    //salvado os dados do aluno
                    tabela.salvar(
                            edtData.getText().toString(),
                            edtGasolina.getText().toString(),
                            edtEtanol.getText().toString(),
                            edtComparacao.getText().toString()
                    );

                    //fechando a tela ao salvar
                    onBackPressed();
                }
                catch (Exception e) { }

            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //criando a mensagem de alerta
                AlertDialog.Builder alerta =
                        new AlertDialog.Builder(Calculo.this);

                //adicionando um titulo a mensagem
                alerta.setTitle("Gas-app");

                //adicionando a pergunta de validacao na mensagem
                alerta.setMessage("Deseja realmente excluir este item?");

                //criando a opcao negativa, que so fecha a mensagem
                alerta.setNegativeButton("Não", null);

                //criando a opcao positiva, que ira excluir o aluno
                alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (edtId.getText().toString().isEmpty()) {
                            mostrarMensagem("Alerta:", "Item é inválido ou inexistente!");
                            return;
                        }

                        TbHistorico tabela = new TbHistorico(Calculo.this);

                        //excluindo o aluno do banco de dados pelo RA
                        tabela.excluir(edtId.getText().toString());

                        //fechando a tela ao excluir
                        onBackPressed();
                    }
                });

                //exibindo a mensagem na tela
                alerta.show();
            }
        });

    }

    private void mostrarMensagem(String titulo, String mensagem) {
        //criando a classe da mensagem de alerta
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);

        //adicionando um titulo para a mensagem
        alerta.setTitle(titulo);

        //adicionando o texto recebido pelo metodo na mensagem/alerta
        alerta.setMessage(mensagem);

        //adicionando um botao de OK para fechar a mensagem
        alerta.setNeutralButton("OK", null);

        //exibindo a mensagem criada na tela
        alerta.show();
    }

    private void obterDados() {
        operacao.setA(Float.parseFloat(edtEtanol.getText().toString()));
        operacao.setB(Float.parseFloat(edtGasolina.getText().toString()));
    }

}