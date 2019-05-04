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
import com.naotem.emanoel.cadernetadig.Controller.HqController;
import com.naotem.emanoel.cadernetadig.R;
import com.naotem.emanoel.cadernetadig.model.Hq;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class infoHqFragment extends Fragment {

    private View view;
    private Tela_ListaActivity tela;
    private List<Hq> lista;
    private Hq hqInfo;
    private DataBaseGeek<Hq> dbHq;
    private Button back;
    private TextView viewNome,viewAtual,viewTotal,viewSite;
    private Tela_ListaActivity telaLista;

    public infoHqFragment() {
        // Required empty public constructor
        tela = new Tela_ListaActivity();
        dbHq = HqController.getInstance(getContext());
        lista = dbHq.allDataList();
        hqInfo = lista.get(tela.pegarPosition("hq"));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_info_hq, container, false);
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

    private void fillView(){
        viewNome.setText(hqInfo.getNome());
        viewAtual.setText(Double.toString(hqInfo.getAtual()));
        viewTotal.setText(Double.toString(hqInfo.getTotal()));
        viewSite.setText(hqInfo.getSite());
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
                Intent internet = new Intent(Intent.ACTION_VIEW,Uri.parse("https://"+hqInfo.getSite()));
                startActivity(internet);
            }
        });
    }

}
