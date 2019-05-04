package com.naotem.emanoel.cadernetadig.BancoDadosSQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.naotem.emanoel.cadernetadig.Ordination.Order;
import com.naotem.emanoel.cadernetadig.SeachBinary.SeachBinary;
import com.naotem.emanoel.cadernetadig.model.Geek;

import java.util.ArrayList;
import java.util.List;

public abstract class DataBaseGeek<Item> extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    protected static final String DATABASE_NAME = "GeekList.DB";

    public DataBaseGeek(@Nullable Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "DROP TABLE "+ getTable();
        db.execSQL(sql);
        onCreate(db);

    }

    protected void createTable(){
        executeSql("CREATE TABLE IF NOT EXISTS "+ getTable() + " ( "+ getCreateTableDescriptions() + " ) ");

    }

    protected void executeSql(String SQL){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL);
        db.close();
    }

    public void addGeek(Item obj){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = createContentValues(obj);
        db.insert(getTable(),null,contentValues);
        db.close();
    }

    public List<Item> allDataList(){

        List<Item> listGeek = new ArrayList<>();

        String sql = "SELECT * FROM "+ getTable();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.moveToFirst()){

            do{
                listGeek.add(createRegisterObject(cursor));
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return (List<Item>) Order.getInstance().selecitonString((List<Geek>) listGeek);
    }

   public void updateGeek(Item obj) {
        String sql = "UPDATE " +  getTable() + " SET " + getUpdate(obj);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
        db.close();
   }

   public void delete(int id){

       String sql = "DELETE FROM '" + getTable() + "' WHERE " + "id = '"+id+"'";
       SQLiteDatabase db = getWritableDatabase();
       db.execSQL(sql);
       db.close();


   }

    public int searchString(String nome){
        List<Geek> listGeek = Order.getInstance().selecitonString((List<Geek>) allDataList());
        return SeachBinary.getInstance().seach(listGeek,nome);
    }

    protected abstract ContentValues createContentValues(Item obj);

    protected abstract String getTable();

    protected abstract String getCreateTableDescriptions();

    protected abstract Item  createRegisterObject(Cursor cursor);

    protected abstract String getUpdate(Item obj);
}
