package com.example.gas_app;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;

public class TbHistorico {

    public TbHistorico(Context context) {
        //abrindo a conexao com o banco de dados
        BancoDeDados.getInstance().abrirBanco(context);

        //SQL para criar a tabela no banco de dados, caso ela ainda nao exista
        String sql = "CREATE TABLE IF NOT EXISTS tbHistorico (id INTEGER PRIMARY KEY " +
                " AUTOINCREMENT, data TEXT, gasolina TEXT, etanol TEXT, comparacao TEXT)";

        //executando o SQL no banco de dados
        BancoDeDados.getInstance().executarSQL(sql);

    }

    private String addAspas(String texto) {
        //adicionando Aspas Simples antes e depois do texto
        return "'" + texto + "'";
    }

    public void salvar(String data, String gasolina, String etanol, String comparacao) {

            inserir(data, gasolina, etanol, comparacao);

    }

    private void inserir(String data, String gasolina, String etanol, String comparacao) {
        //montando SQL para inserir o aluno na tabela
        String sql = "INSERT INTO tbHistorico (data, gasolina, etanol, comparacao) VALUES (" +
                addAspas(data) + ", " + addAspas(gasolina) + ", " + addAspas(etanol) + ", " + addAspas(comparacao) + ")";

        //executando o SQL de INSERT no banco de dados
        BancoDeDados.getInstance().executarSQL(sql);
    }

    public void excluir(String id) {
        //montando SQL para excluir o aluno na tabela
        String sql = "DELETE FROM tbHistorico WHERE id = " + id;

        //executando o SQL de DELETE no banco de dados
        BancoDeDados.getInstance().executarSQL(sql);
    }

    public ArrayList<HashMap<String, String>> buscar(String data) {
        try {
            String condicaoSQL = "";
            String operadorSQL = "";

            //montando a condicao do SQL de acordo com os parametros recebidos
            if (data.isEmpty() == false) {
                condicaoSQL = "UPPER(data) LIKE UPPER(" +
                        addAspas("%" + data + "%") + ")";
                operadorSQL = " AND ";
            }
            else if (data.isEmpty() == false) {
                condicaoSQL += operadorSQL + "data = " + addAspas(data);
                operadorSQL = " AND ";
            }

            //criando uma busca usando o metodo QUERY do banco de dados
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

            //retornando a lista (array) com os registros do banco de dados
            return retornarLista(cursor);
        }
        catch (Exception e) {
            System.out.println("Erro ao buscar: " + e.getMessage());
            //caso de erro, retorna uma lista vazia
            return new ArrayList<HashMap<String, String>>();
        }
    }

    private ArrayList<HashMap<String, String>> retornarLista(Cursor cursor) {
        try {
            //criando a lista que sera retornada pelo metodo
            ArrayList<HashMap<String, String>> listaRetorno =
                    new ArrayList<HashMap<String, String>>();

            //pegando o indice de cada coluna do banco
            int campoId = cursor.getColumnIndex("id");
            int campoData = cursor.getColumnIndex("data");
            int campoGasolina = cursor.getColumnIndex("gasolina");
            int campoEtanol = cursor.getColumnIndex("etanol");
            int campoComparacao = cursor.getColumnIndex("comparacao");

            //se existem dados retornados do banco de dados
            if (cursor.getCount() > 0) {
                //move o cursor para a primeira posicao
                cursor.moveToFirst();

                //FOR para rodas todos os itens do cursor (registros do banco)
                for (int i = 0; i < cursor.getCount(); i++) {
                    HashMap<String,String> item = new HashMap<>();
                    //adicionando o valor do banco (cursor) no item HASH
                    item.put("id", cursor.getString(campoId));
                    item.put("data", cursor.getString(campoData));
                    item.put("gasolina", cursor.getString(campoGasolina));
                    item.put("etanol", cursor.getString(campoEtanol));
                    item.put("comparacao", cursor.getString(campoComparacao));

                    //adicionando o item na lista de retorno
                    listaRetorno.add(item);

                    //movendo o cursor para o proximo registro
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
