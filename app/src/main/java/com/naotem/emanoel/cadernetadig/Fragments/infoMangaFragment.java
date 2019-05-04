package com.naotem.emanoel.cadernetadig.Fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.naotem.emanoel.cadernetadig.Activity.Tela_ListaActivity;
import com.naotem.emanoel.cadernetadig.BancoDadosSQLite.DataBaseGeek;
import com.naotem.emanoel.cadernetadig.Controller.MangaController;
import com.naotem.emanoel.cadernetadig.R;
import com.naotem.emanoel.cadernetadig.model.Manga;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class infoMangaFragment extends Fragment {

    private View view;
    private Tela_ListaActivity tela;
    private List<Manga> lista;
    private Manga mangaInfo;
    private DataBaseGeek<Manga> dbManga;
    private Button back;
    private TextView viewNome,viewAtual,viewTotal,viewSite;

    public infoMangaFragment() {
        // Required empty public constructor
        tela = new Tela_ListaActivity();
        dbManga = MangaController.getInstancia(getContext());
        lista = dbManga.allDataList();
        mangaInfo = lista.get(tela.pegarPosition("manga"));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_info_manga, container, false);
        init();
        fillView();
        actionButtons();

        return view;
    }

    private void init(){
        viewNome = view.findViewById(R.id.textViewNomeInfo);
        viewAtual = view.findViewById(R.id.textViewAtualInfo);
        viewTotal = view.findViewById(R.id.textViewTotalInfo);
        viewSite = view.findViewById(R.id.textViewSiteInfo);
        back = view.findViewById(R.id.buttonBack);
    }

    @SuppressLint("SetTextI18n")
    private void fillView(){
        viewNome.setText(mangaInfo.getNome());
        viewAtual.setText(Double.toString(mangaInfo.getAtual()));
        viewTotal.setText(Double.toString(mangaInfo.getTotal()));
        viewSite.setText(mangaInfo.getSite());
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
                Intent internet = new Intent(Intent.ACTION_VIEW,Uri.parse("https://"+mangaInfo.getSite()));
                startActivity(internet);
            }
        });
    }

}
