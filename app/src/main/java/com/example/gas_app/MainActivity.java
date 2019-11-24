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
                Intent tela = new Intent(
                        MainActivity.this,
                        Calculo.class);

                startActivity(tela);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        buscarHistorico();
    }

    private void buscarHistorico() {
        String data = "";

        if (rbtData.isChecked())
            data = edtBuscar.getText().toString();

        TbHistorico tabela = new TbHistorico(this);

        listaHistorico = tabela.buscar(data);

        ListAdapter adapter = new SimpleAdapter(this,
                listaHistorico, R.layout.listview_historico,
                new String[] {"id", "data", "gasolina", "etanol", "comparacao"},
                new int[] {R.id.lblId, R.id.lblData, R.id.lblGasolina, R.id.lblEtanol, R.id.lblComparacao});

        setListAdapter(adapter);
    }

    private void verificarPermissao(String nomePermissao) {
        if (ContextCompat.checkSelfPermission(this,
                nomePermissao) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[] {nomePermissao}, 0);
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent tela = new Intent(MainActivity.this,
                Calculo.class);

        HashMap<String,String> historico = listaHistorico.get(position);

        Bundle parans = new Bundle();

        parans.putString("id", historico.get("id"));
        parans.putString("data", historico.get("data"));
        parans.putString("gasolina", historico.get("gasolina"));
        parans.putString("etanol", historico.get("etanol"));
        parans.putString("comparacao", historico.get("comparacao"));

        tela.putExtras(parans);

        startActivity(tela);
    }
}
