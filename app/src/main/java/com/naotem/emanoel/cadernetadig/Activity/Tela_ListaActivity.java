package com.naotem.emanoel.cadernetadig.Activity;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.naotem.emanoel.cadernetadig.Adapter.AdapterRecyclerAnime;
import com.naotem.emanoel.cadernetadig.Adapter.AdapterRecyclerHq;
import com.naotem.emanoel.cadernetadig.Adapter.AdapterRecyclerManga;
import com.naotem.emanoel.cadernetadig.Fragments.animeRecyclerFragment;
import com.naotem.emanoel.cadernetadig.Fragments.hqRecyclerFragment;
import com.naotem.emanoel.cadernetadig.Fragments.mangaRecyclerFragment;
import com.naotem.emanoel.cadernetadig.Fragments.registerAnimeFragment;
import com.naotem.emanoel.cadernetadig.Fragments.registerHqFragment;
import com.naotem.emanoel.cadernetadig.Fragments.registerMangaFragment;
import com.naotem.emanoel.cadernetadig.R;
import com.naotem.emanoel.cadernetadig.ViewPager.ViewPagerAdapter;

public class Tela_ListaActivity<Item> extends AppCompatActivity {

    public static AdapterRecyclerManga adapterManga;
    public static AdapterRecyclerAnime adapterAnime;
    public static AdapterRecyclerHq adapterHq;
    private static FragmentManager fragmentManager;
    private static FragmentTransaction transaction;
    private static TabLayout tabLayout;
    private static FloatingActionButton fab;
    private registerMangaFragment registerManga;
    private registerAnimeFragment registerAnime;
    private registerHqFragment registerHq;
    private static ViewPager viewPager;
    private static ViewPagerAdapter viewPagerAdapter;
    private mangaRecyclerFragment recyclerManga;
    private animeRecyclerFragment recyclerAnime;
    private static hqRecyclerFragment recyclerHq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela__lista);

        init();
        buttonsAction();

    }

    public void init(){

        //inicializa
        adapterManga = AdapterRecyclerManga.getInstance();
        adapterAnime = AdapterRecyclerAnime.getInstance();
        adapterHq = AdapterRecyclerHq.getInstance();
        registerManga = new registerMangaFragment();
        registerAnime = new registerAnimeFragment();
        registerHq = new registerHqFragment();

        recyclerManga = mangaRecyclerFragment.getInstance();
        recyclerAnime = new animeRecyclerFragment();
        recyclerHq = new hqRecyclerFragment();

        fragmentManager = getSupportFragmentManager();
        tabLayout = findViewById(R.id.myTabLayout);

//        viewpager
        viewPager = findViewById(R.id.viewPagerLista);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

//        add fragment
        addAllFragments();
        tabLayout.setupWithViewPager(viewPager);

        fab = findViewById(R.id.fab);
    }

    public int pegarPosition(String choice){
        if(choice.equals("manga")){
            return adapterManga.posicaoItem();
        }else if(choice.equals("anime")){
            return adapterAnime.posicaoItem();
        }else{
            return  adapterHq.posicaoItem();
        }
    }

    public void addAllFragments(){
        viewPagerAdapter.addFragment(recyclerManga,"Manga");
        viewPagerAdapter.addFragment(recyclerAnime,"Anime");
        viewPagerAdapter.addFragment(recyclerHq,"HQ");
        viewPager.setAdapter(viewPagerAdapter);

    }

        //mudar de fragmento
    public void mudarFragment(Fragment fragment){
        setVisibilityFab();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.constraiLista,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void buttonsAction(){

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               switch(tabLayout.getSelectedTabPosition()){
                   case 0:
                       mudarFragment(registerManga);
                       break;
                   case 1:
                       mudarFragment(registerAnime);
                       break;
                   case 2:
                        mudarFragment(registerHq);
                       break;
               }
            }
        });
    }

    public void updateAdapterRecycler(){
        int posiçao = viewPager.getCurrentItem();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(posiçao);
    }

    public void setVisibilityFab(){
        if(fab.getVisibility() == View.INVISIBLE){
            fab.setVisibility(View.VISIBLE);
        }else{
            fab.setVisibility(View.INVISIBLE);
        }
    }
}