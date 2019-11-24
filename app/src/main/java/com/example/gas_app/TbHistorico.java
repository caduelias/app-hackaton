package com.example.gas_app;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;

public class TbHistorico {

    public TbHistorico(Context context) {
        BancoDeDados.getInstance().abrirBanco(context);

        String sql = "CREATE TABLE IF NOT EXISTS tbHistorico (id INTEGER PRIMARY KEY " +
                " AUTOINCREMENT, data TEXT, gasolina TEXT, etanol TEXT, comparacao TEXT)";

        BancoDeDados.getInstance().executarSQL(sql);

    }

    private String addAspas(String texto) {
        return "'" + texto + "'";
    }

    public void salvar(String data, String gasolina, String etanol, String comparacao) {

            inserir(data, gasolina, etanol, comparacao);

    }

    private void inserir(String data, String gasolina, String etanol, String comparacao) {
        String sql = "INSERT INTO tbHistorico (data, gasolina, etanol, comparacao) VALUES (" +
                addAspas(data) + ", " + addAspas(gasolina) + ", " + addAspas(etanol) + ", " + addAspas(comparacao) + ")";

        BancoDeDados.getInstance().executarSQL(sql);
    }

    public void excluir(String id) {
        String sql = "DELETE FROM tbHistorico WHERE id = " + id;

        BancoDeDados.getInstance().executarSQL(sql);
    }

    public ArrayList<HashMap<String, String>> buscar(String data) {
        try {
            String condicaoSQL = "";
            String operadorSQL = "";

            if (data.isEmpty() == false) {
                condicaoSQL = "UPPER(data) LIKE UPPER(" +
                        addAspas("%" + data + "%") + ")";
                operadorSQL = " AND ";
            }
            else if (data.isEmpty() == false) {
                condicaoSQL += operadorSQL + "data = " + addAspas(data);
                operadorSQL = " AND ";
            }

            Cursor cursor = BancoDeDados.getInstance().getBancoDados().query(
                    "tbHistorico", //nome da tabela
                    new String[] {"id", "data", "gasolina", "etanol", "comparacao"}, //campos retornados na busca
                    condicaoSQL, //condicao do WHERE da busca
                    null, //argumentos do WHERE, caso exista
                    null, //clausula GROUP BY
                    null, //clausula HAVING
                    "data", //ORDER BY, ordenacao da busca
                    null //limite de registros
            );

            return retornarLista(cursor);
        }
        catch (Exception e) {
            System.out.println("Erro ao buscar: " + e.getMessage());
            return new ArrayList<HashMap<String, String>>();
        }
    }

    private ArrayList<HashMap<String, String>> retornarLista(Cursor cursor) {
        try {
            ArrayList<HashMap<String, String>> listaRetorno =
                    new ArrayList<HashMap<String, String>>();

            int campoId = cursor.getColumnIndex("id");
            int campoData = cursor.getColumnIndex("data");
            int campoGasolina = cursor.getColumnIndex("gasolina");
            int campoEtanol = cursor.getColumnIndex("etanol");
            int campoComparacao = cursor.getColumnIndex("comparacao");

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                for (int i = 0; i < cursor.getCount(); i++) {
                    HashMap<String,String> item = new HashMap<>();
                    //adicionando o valor do banco (cursor) no item HASH
                    item.put("id", cursor.getString(campoId));
                    item.put("data", cursor.getString(campoData));
                    item.put("gasolina", cursor.getString(campoGasolina));
                    item.put("etanol", cursor.getString(campoEtanol));
                    item.put("comparacao", cursor.getString(campoComparacao));

                    listaRetorno.add(item);

                    cursor.moveToNext();
                }
            }

            return listaRetorno;
        }
        catch (Exception e) {
            System.out.println("Erro ao montar lista: " + e.getMessage());
            //caso de erro, retorna uma lista vazia
            return new ArrayList<HashMap<String, String>>();
        }
    }

}
