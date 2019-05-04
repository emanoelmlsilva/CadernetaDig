package com.naotem.emanoel.cadernetadig.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.naotem.emanoel.cadernetadig.Activity.Tela_ListaActivity;
import com.naotem.emanoel.cadernetadig.BancoDadosSQLite.DataBaseGeek;
import com.naotem.emanoel.cadernetadig.Controller.AnimeController;
import com.naotem.emanoel.cadernetadig.R;
import com.naotem.emanoel.cadernetadig.model.Anime;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class infoAnimeFragment extends Fragment {

    private View view;
    private Tela_ListaActivity tela;
    private List<Anime> lista;
    private Anime animeInfo;
    private DataBaseGeek<Anime> dbAnime;
    private Button back;
    private TextView viewNome,viewSeason,viewAtual,viewTotal,viewSite;


    public infoAnimeFragment() {
        tela = new Tela_ListaActivity();
        dbAnime = AnimeController.getInstancia(getContext());
        lista = dbAnime.allDataList();
        animeInfo = lista.get(tela.pegarPosition("anime"));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_info_anime, container, false);
        init();
        fillView();
        actionButtons();

        return view;
    }

    private void init(){
        viewNome = view.findViewById(R.id.textoViewNomeInfo);
        viewSeason = view.findViewById(R.id.textoViewSeasonInfo);
        viewAtual = view.findViewById(R.id.textoViewEpAtualInfo);
        viewTotal = view.findViewById(R.id.textoViewEpTotalInfo);
        viewSite = view.findViewById(R.id.textoViewSiteInfo);
        back = view.findViewById(R.id.buttonBack);
    }

    private void fillView(){
        viewNome.setText(animeInfo.getNome());
        viewSeason.setText(animeInfo.getSeason());
        viewAtual.setText(Double.toString(animeInfo.getAtual()));
        viewTotal.setText(Double.toString(animeInfo.getTotal()));
        viewSite.setText(animeInfo.getSite());
    }

    private void actionButtons(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                tela.setVisibilityFab();
            }
        });

        viewSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent internet = new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+animeInfo.getSite()));
                startActivity(internet);
            }
        });
    }

}
