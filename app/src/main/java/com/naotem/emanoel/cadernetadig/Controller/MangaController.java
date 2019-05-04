package com.naotem.emanoel.cadernetadig.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.naotem.emanoel.cadernetadig.Adapter.AdapterRecyclerManga;
import com.naotem.emanoel.cadernetadig.BancoDadosSQLite.DataBaseGeek;
import com.naotem.emanoel.cadernetadig.model.Manga;

public class MangaController extends DataBaseGeek {

    public static MangaController instancia = null;

    private MangaController(Context context){
        super(context);
        createTable();

    }

    public static MangaController getInstancia(Context context){
        if(instancia == null){
            instancia = new MangaController(context);
        }
        return instancia;

    }
    @Override
    protected ContentValues createContentValues(Object obj) {

        ContentValues values = new ContentValues();

        Manga manga = (Manga)obj;
        values.put("nome",manga.getNome());
        values.put("capitulo",manga.getAtual());
        values.put("quantidade",manga.getTotal());
        values.put("site",manga.getSite());
        return values;
    }

    @Override
    protected String getTable() {
        return "mangas";
    }

    @Override
    protected String getCreateTableDescriptions() {
        return "id INTEGER PRIMARY KEY AUTOINCREMENT, "+"nome varchar(30) NOT NULL, "+"capitulo float NOT NULL, "+"quantidade float NOT NULL, "+"site varchar(30) NOT NULL";
    }

    @Override
    protected Object createRegisterObject(Cursor cursor) {

        int id = cursor.getInt(cursor.getColumnIndex("id"));
        String nome = cursor.getString(cursor.getColumnIndex("nome"));
        double quantidade = cursor.getDouble(cursor.getColumnIndex("quantidade"));
        double capitulo = cursor.getDouble(cursor.getColumnIndex("capitulo"));
        String site = cursor.getString(cursor.getColumnIndex("site"));

        Manga manga = new Manga(id,nome,capitulo,quantidade,site);

        return manga;
    }

    @Override
    protected String getUpdate(Object obj) {
        Manga novo = (Manga) obj;
        return "nome = '" + novo.getNome() + "', capitulo = '" + novo.getAtual() + "', quantidade = '" + novo.getTotal() + "', site = '" + novo.getSite() + "' WHERE id = '" + novo.getId() + "'";
    }

}