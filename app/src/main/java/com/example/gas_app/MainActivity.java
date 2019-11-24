package com.example.gas_app;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends ListActivity {

    Button btnCalcular, btnBuscar;
    EditText edtBuscar;
    RadioButton rbtData;
    ArrayList<HashMap<String,String>> listaHistorico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verificarPermissao(
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        btnCalcular = findViewById(R.id.btnCalcula);
        btnBuscar = findViewById(R.id.btnBuscar);
        edtBuscar = findViewById(R.id.edtBuscar);
        rbtData = findViewById(R.id.rbtData);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarHistorico();
            }
        });

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //criando o caminho para a tela de cadastro
                Intent tela = new Intent(
                        MainActivity.this,
                        Calculo.class);

                //abrindo a tela de cadastro
                startActivity(tela);
            }
        });
    }

    //onResumo é executado toda vez que a
    //   tela aparece (fica visivel)
    @Override
    protected void onResume() {
        super.onResume();

        //buscando todos os alunos cadastrados
        buscarHistorico();
    }

    private void buscarHistorico() {
        //criando as variaveis que receberao o valor da busca
        String data = "";

        //verifica qual item esta marcado para buscar
        if (rbtData.isChecked())
            data = edtBuscar.getText().toString();

        //criando a classe responsavel pela tabela aluno
        TbHistorico tabela = new TbHistorico(this);

        //buscando os alunos, passando o filtro da busca
        listaHistorico = tabela.buscar(data);

        //criando o adaptador de dados para exibir os dados dos
        //  alunos na listagem, usando o modelo
        ListAdapter adapter = new SimpleAdapter(this,
                listaHistorico, R.layout.listview_historico,
                new String[] {"id", "data", "gasolina", "etanol", "comparacao"},
                new int[] {R.id.lblId, R.id.lblData, R.id.lblGasolina, R.id.lblEtanol, R.id.lblComparacao});

        //adicionando na lista da tela (listView) o adaptador criador
        setListAdapter(adapter);
    }

    private void verificarPermissao(String nomePermissao) {
        //verificando se o usuario ja deu ou nao a permissao
        if (ContextCompat.checkSelfPermission(this,
                nomePermissao) != PackageManager.PERMISSION_GRANTED) {

            //verifica se o usuario ja negou a permissao
            //  e marcou a opcao de nao ver novamento
            //if (ActivityCompat.shouldShowRequestPermissionRationale(
            //                                      this, nomePermissao))

            //solicitando a permissao
            ActivityCompat.requestPermissions(this,
                    new String[] {nomePermissao}, 0);
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        //criando o caminho para abrir a tela de cadastro
        Intent tela = new Intent(MainActivity.this,
                Calculo.class);

        //carregando os dados do aluno selecionado
        HashMap<String,String> historico = listaHistorico.get(position);

        //criando a classe de parametros
        Bundle parans = new Bundle();

        //adicionando os dados do aluno selecionado nos parans
        parans.putString("id", historico.get("id"));
        parans.putString("data", historico.get("data"));
        parans.putString("gasolina", historico.get("gasolina"));
        parans.putString("etanol", historico.get("etanol"));
        parans.putString("comparacao", historico.get("comparacao"));

        //adicionando os parametros no caminho
        tela.putExtras(parans);

        //abrindo a tela com os parametros
        startActivity(tela);
    }
}
