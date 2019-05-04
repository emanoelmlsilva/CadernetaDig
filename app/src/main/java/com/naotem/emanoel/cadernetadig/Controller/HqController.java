package com.naotem.emanoel.cadernetadig.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.naotem.emanoel.cadernetadig.BancoDadosSQLite.DataBaseGeek;
import com.naotem.emanoel.cadernetadig.model.Hq;

public class HqController extends DataBaseGeek {

    public static HqController instance = null;

    private HqController(Context context) {
        super(context);
        createTable();
    }

    public static HqController getInstance(Context context){
        if(instance == null){
            instance = new HqController(context);
        }
        return instance;
    }

    @Override
    protected ContentValues createContentValues(Object obj) {

        ContentValues contentValues = new ContentValues();
        Hq hq = (Hq)obj;
        contentValues.put("nome",hq.getNome());
        contentValues.put("capitulo",hq.getAtual());
        contentValues.put("quantidade",hq.getTotal());
        contentValues.put("site",hq.getSite());
        return contentValues;
    }

    @Override
    protected String getTable() {
        return "hqs";
    }

    @Override
    protected String getCreateTableDescriptions() {

        return "id INTEGER PRIMARY KEY AUTOINCREMENT, "+"nome varchar(30) NOT NULL, "+"capitulo double NOT NULL, "+"quantidade double NOT NULL, "+"site varchar(30) NOT NULL";
    }

    @Override
    protected Object createRegisterObject(Cursor cursor) {

        int id = cursor.getInt(cursor.getColumnIndex("id"));
        String nome = cursor.getString(cursor.getColumnIndex("nome"));
        double capitulo = cursor.getDouble(cursor.getColumnIndex("capitulo"));
        double quantidade = cursor.getDouble(cursor.getColumnIndex("quantidade"));
        String site = cursor.getString(cursor.getColumnIndex("site"));

        Hq hq = new Hq(id,nome,capitulo,quantidade,site);
        return hq;
    }

    @Override
    protected String getUpdate(Object obj) {

        Hq novo = (Hq)obj;
        return "nome = '" + novo.getNome() + "', capitulo = '" + novo.getAtual() + "', quantidade = '" + novo.getTotal() + "', site = '" + novo.getSite() + "' WHERE id = '" + novo.getId() + "'";
    }
}
