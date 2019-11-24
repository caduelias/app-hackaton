package com.example.gas_app;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BancoDeDados {

    private static final BancoDeDados bancoDeDados = new BancoDeDados();
    public static BancoDeDados getInstance() {
        return bancoDeDados;
    }

    private SQLiteDatabase bancoDados;
    public SQLiteDatabase getBancoDados() {
        return bancoDados;
    }

    public void abrirBanco(Context context) {

        if (bancoDados != null) {
            if (bancoDados.isOpen()) {
                return;
            }
        }

        bancoDados = context.openOrCreateDatabase("dbHackaton.db",
                Context.MODE_PRIVATE, null);
    }

    public void fecharBanco() {

        if (bancoDados == null) {
            return;
        }

        bancoDados.close();
    }

    public void executarSQL(String sql) {
        try {
            bancoDados.execSQL(sql);
        }
        catch (Exception e) {
            System.out.println("Erro SQL: " + e.getMessage());
        }
    }

    public void adicionarNovaColuna(String tabela, String coluna,
                                    String tipo, String valorPadrao) {
        if (existeColunaTabela(tabela, coluna))
            return;

        String padrao = "";

        if (valorPadrao.isEmpty() == false)
            padrao = " DEFAULT " + valorPadrao;

        executarSQL("ALTER TABLE " + tabela + " ADD COLUMN " +
                coluna + " " + tipo + " " + padrao);
    }

    private boolean existeColunaTabela(String tabela, String coluna) {
        try {

            Cursor cursor = bancoDados.rawQuery("SELECT * FROM " + tabela +
                    " LIMIT 0", null);

            return (cursor.getColumnIndex(coluna) != -1);

        }
        catch (Exception e) {
            return false;
        }
    }
}

