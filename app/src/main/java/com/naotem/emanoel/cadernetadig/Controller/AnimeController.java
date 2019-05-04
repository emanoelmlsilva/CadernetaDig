package com.naotem.emanoel.cadernetadig.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.naotem.emanoel.cadernetadig.BancoDadosSQLite.DataBaseGeek;
import com.naotem.emanoel.cadernetadig.model.Anime;

public class AnimeController extends DataBaseGeek {

    private static AnimeController instance = null;

    private AnimeController(@Nullable Context context) {
        super(context);
        createTable();
    }

    public static AnimeController getInstancia(Context context){
        if(instance == null){
            instance = new AnimeController(context);
        }
        return instance;
    }

    @Override
    protected ContentValues createContentValues(Object obj) {

        ContentValues cv = new ContentValues();
        Anime anime = (Anime) obj;
        cv.put("nome",anime.getNome());
        cv.put("ep_Atual",anime.getAtual());
        cv.put("ep_Total",anime.getTotal());
        cv.put("season",anime.getSeason());
        cv.put("site",anime.getSite());

        return cv;
    }

    @Override
    protected String getTable() {
        return "animes";
    }

    @Override
    protected String getCreateTableDescriptions() {

        return "id INTEGER PRIMARY KEY AUTOINCREMENT, "+ "nome TEXT, "+ "ep_Atual TEXT, "+ "ep_Total TEXT, "+ "site TEXT, "+ "season TEXT";
    }

    @Override
    protected Object createRegisterObject(Cursor cursor) {

        int id = cursor.getInt(cursor.getColumnIndex("id"));
        String nome = cursor.getString(cursor.getColumnIndex("nome"));
        double ep_Atual = cursor.getDouble(cursor.getColumnIndex("ep_Atual"));
        double ep_Total = cursor.getDouble(cursor.getColumnIndex("ep_Total"));
        String season = cursor.getString(cursor.getColumnIndex("season"));
        String site = cursor.getString(cursor.getColumnIndex("site"));

        Anime anime = new Anime(id,nome,ep_Atual,ep_Total,site,season);
        return anime;
    }

    @Override
    protected String getUpdate(Object obj) {

        Anime anime = (Anime)obj;
        return "nome = '"+anime.getNome() + "', ep_Atual = '" + anime.getAtual() + "', ep_Total = '" + anime.getTotal() + "', site = '" + anime.getSite() + "', season = '" + anime.getSeason() + "' WHERE id = '" + anime.getId() + "'";
    }
}
