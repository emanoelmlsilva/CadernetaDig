package com.naotem.emanoel.cadernetadig.Fragments;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.naotem.emanoel.cadernetadig.Activity.Tela_ListaActivity;
import com.naotem.emanoel.cadernetadig.Adapter.AdapterRecyclerAnime;
import com.naotem.emanoel.cadernetadig.BancoDadosSQLite.DataBaseGeek;
import com.naotem.emanoel.cadernetadig.Controller.AnimeController;
import com.naotem.emanoel.cadernetadig.R;
import com.naotem.emanoel.cadernetadig.exception.NomeExistenteException;
import com.naotem.emanoel.cadernetadig.exception.NumeroInvalidoException;
import com.naotem.emanoel.cadernetadig.exception.NumeroVazioException;
import com.naotem.emanoel.cadernetadig.exception.StringVaziaException;
import com.naotem.emanoel.cadernetadig.exception.VerificarMaiorExcpetion;
import com.naotem.emanoel.cadernetadig.model.Anime;
import com.naotem.emanoel.cadernetadig.model.verificarCondicoesNameNumber;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class registerAnimeFragment extends Fragment implements verificarCondicoesNameNumber<Anime> {

    private View view;
    private Spinner spinner;
    private TextInputLayout textoNome,textoEpisodioAtual,textoEpisodioTotal,textoSite;
    private String nome,site,season;
    private Double ep_Total,ep_Atual;
    private int cont;
    private String[] seasonNum;
    private List<Anime> animes;
    private DataBaseGeek<Anime> dbAnime;
    private Button btn_Voltar,btn_Add;
    private ArrayAdapter<String> adapter;
    private Tela_ListaActivity telaLista;
    private boolean nomeEquals,valorMaior;

    public registerAnimeFragment() {
        // Required empty public constructor
        telaLista = new Tela_ListaActivity();
        dbAnime = AnimeController.getInstancia(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_register_anime, container, false);
        initAll();
        actionButtonAll();

        return view;

    }

    public void initAll(){
        animes = dbAnime.allDataList();
        seasonNum = new String[101];
        fill_Array_of_numbers();
        btn_Voltar = view.findViewById(R.id.btn_voltarAnime);
        btn_Add = view.findViewById(R.id.btn_addAnime);
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,seasonNum);
        spinner = view.findViewById(R.id.adapterSeason);
        spinner.setAdapter(adapter);
        textoNome = view.findViewById(R.id.textoNome);
        textoEpisodioAtual = view.findViewById(R.id.textoEpisodioAtual);
        textoEpisodioTotal = view.findViewById(R.id.textoEpisodioTotal);
        textoSite = view.findViewById(R.id.textoSite);

    }

    public void actionButtonAll(){

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                season = seasonNum[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_Voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                telaLista.setVisibilityFab();
            }
        });

        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cont = 0;

                try{
                    checkInformaçoesNome(textoNome);
                    nome = textoNome.getEditText().getText().toString();
                    nomeEquals = checkNameEquals(nome,animes);
                    textoNome.setError(null);
                    cont++;
                } catch (StringVaziaException e) {
                    textoNome.setError("campo vazio");
                } catch (NomeExistenteException e) {
                    textoNome.setError("nome ja existente");
                }

                try{
                    checkInformationNumber(textoEpisodioAtual);
                    ep_Atual = Double.parseDouble(textoEpisodioAtual.getEditText().getText().toString());
                    textoEpisodioAtual.setError(null);
                    cont++;
                }catch (NumeroVazioException e){
                    textoEpisodioAtual.setError("campo vazio");
                }catch (NumeroInvalidoException e){
                    textoEpisodioAtual.setError("campo invalido, apenas numeros validos");
                }

                try{
                    checkInformationNumber(textoEpisodioTotal);
                    ep_Total = Double.parseDouble(textoEpisodioTotal.getEditText().getText().toString());
                    textoEpisodioTotal.setError(null);
                    cont++;
                }catch (NumeroVazioException e){
                    textoEpisodioTotal.setError("campo vazio");
                }catch (NumeroInvalidoException e){
                    textoEpisodioTotal.setError("campo invalido, apenas numeros validos");
                }

                if(cont == 3) {

                    valorMaior = false;
                    try {
                        valorMaior = checkValorBigger(ep_Atual, ep_Total);
                    } catch (VerificarMaiorExcpetion excpetion) {
                        textoEpisodioTotal.setError("campo invalido, quantidade Total menor que o capitulo Atual");
                    }

                    site = textoSite.getEditText().getText().toString();

                    if (site.equals("")) {
                        site = "N/A";
                    }

                    if (valorMaior && nomeEquals) {
                        Anime novo = new Anime(nome, ep_Atual, ep_Total, site, season);
                        Log.i("anime","refatorando = "+novo.getAtual()+" = "+novo.getTotal());

                        dbAnime.addGeek(novo);
                        animes = dbAnime.allDataList();
                        Snackbar.make(view, "SUCCESS", Snackbar.LENGTH_SHORT).show();
                        AdapterRecyclerAnime.getInstance().upDateAdapter(animes);
                        telaLista.updateAdapterRecycler();
                        textoNome.getEditText().setText("");
                        textoEpisodioAtual.getEditText().setText("");
                        textoEpisodioTotal.getEditText().setText("");
                        textoSite.getEditText().setText("");
                        spinner.setAdapter(adapter);
                        getActivity().getSupportFragmentManager().popBackStack();
                        telaLista.setVisibilityFab();
                    }
                }

            }
        });

    }

    @Override
    public void checkInformaçoesNome(TextInputLayout input) throws StringVaziaException {
        if(input.getEditText().getText().toString().isEmpty()){
            throw new StringVaziaException();
        }
    }

    @Override
    public void checkInformationNumber(TextInputLayout input) throws NumeroVazioException, NumeroInvalidoException {
        if(input.getEditText().getText().toString().isEmpty()){
            throw new NumeroVazioException();
        }else if(input.getEditText().getText().toString().matches("^[0-9].*") == false){
            throw new NumeroInvalidoException();
        }
    }

    @Override
    public boolean checkValorBigger(double valor1, double valor2) throws VerificarMaiorExcpetion {
        if(valor1 > valor2){
            throw new VerificarMaiorExcpetion();
        }
        return true;
    }

    @Override
    public boolean checkNameEquals(String nome, List<Anime> lista) throws NomeExistenteException {
        for(Anime nomes:lista){
            if(nomes.getNome().equals(nome)){
                throw new NomeExistenteException();
            }
        }

        return true;
    }

    private void fill_Array_of_numbers(){
        seasonNum[0] = "default";
        for(int i = 1;i < 101;i++){
            seasonNum[i] = String.valueOf(i);
        }
    }

}
