package com.naotem.emanoel.cadernetadig.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.naotem.emanoel.cadernetadig.Controller.AnimeController;
import com.naotem.emanoel.cadernetadig.Controller.HqController;
import com.naotem.emanoel.cadernetadig.Controller.MangaController;
import com.naotem.emanoel.cadernetadig.Ordination.Order;
import com.naotem.emanoel.cadernetadig.R;
import com.naotem.emanoel.cadernetadig.model.Geek;
import com.naotem.emanoel.cadernetadig.model.Manga;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        MangaController.getInstancia(getApplicationContext());
        AnimeController.getInstancia(getApplicationContext());
        HqController.getInstance(getApplicationContext());
//          new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent start = new Intent(StartActivity.this,Tela_ListaActivity.class);
//                startActivity(start);
//                finish();
//            }
//        },SPLASH_TIME_OUT);
        Intent start = new Intent(StartActivity.this,Tela_ListaActivity.class);
        startActivity(start);
        finish();
    }


}
