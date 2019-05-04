package com.naotem.emanoel.cadernetadig.Fragments;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.naotem.emanoel.cadernetadig.Activity.Tela_ListaActivity;
import com.naotem.emanoel.cadernetadig.Adapter.AdapterRecyclerManga;
import com.naotem.emanoel.cadernetadig.BancoDadosSQLite.DataBaseGeek;
import com.naotem.emanoel.cadernetadig.Controller.MangaController;
import com.naotem.emanoel.cadernetadig.R;
import com.naotem.emanoel.cadernetadig.exception.NomeExistenteException;
import com.naotem.emanoel.cadernetadig.exception.NumeroInvalidoException;
import com.naotem.emanoel.cadernetadig.exception.NumeroVazioException;
import com.naotem.emanoel.cadernetadig.exception.StringVaziaException;
import com.naotem.emanoel.cadernetadig.exception.VerificarMaiorExcpetion;
import com.naotem.emanoel.cadernetadig.model.Manga;
import com.naotem.emanoel.cadernetadig.model.verificarCondicoesNameNumber;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class registerMangaFragment extends Fragment implements verificarCondicoesNameNumber<Manga> {



    private Button btnVoltar,addManga;
    private View view;
    private String nome,site;
    private double capitulo,quantidadeTotal;
    private List<Manga> mangas;
    private TextInputLayout inputEditNome,inputEditCapitulo,inputEditQuantidade,inputEditsite;
    private int cont;
    private DataBaseGeek<Manga> dbManga;
    private boolean nomeEquals,valorMaior;
    private Tela_ListaActivity<Manga> telaLista;

    public registerMangaFragment() {

        dbManga = MangaController.getInstancia(getContext());

    }


    public void init(){
        telaLista = new Tela_ListaActivity<>();
        mangas = dbManga.allDataList();
        btnVoltar = view.findViewById(R.id.btn_voltarAnime);
        addManga = view.findViewById(R.id.addManga);
        inputEditNome = view.findViewById(R.id.textoNome);
        inputEditNome.setError(null);
        inputEditCapitulo = view.findViewById(R.id.textoCapituloAtual);
        inputEditCapitulo.setError(null);
        inputEditQuantidade = view.findViewById(R.id.textoCapituloTotal);
        inputEditQuantidade.setError(null);
        inputEditsite = view.findViewById(R.id.textoSite);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_registermanga, container, false);
        init();
        actionButtons();

        return view;
    }

    public void actionButtons(){

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                telaLista.setVisibilityFab();
            }
        });

        addManga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                site = inputEditsite.getEditText().getText().toString();

                cont = 0;

                try{
                    checkInformaçoesNome(inputEditNome);
                    nome = inputEditNome.getEditText().getText().toString();
                    nomeEquals = checkNameEquals(nome,mangas);
                    inputEditNome.setError(null);
                    cont++;
                } catch (StringVaziaException e) {
                    inputEditNome.setError("campo vazio");
                } catch (NomeExistenteException e) {
                    inputEditNome.setError("nome ja existente");
                }

                try{
                    checkInformationNumber(inputEditCapitulo);
                    capitulo = Double.parseDouble(inputEditCapitulo.getEditText().getText().toString());
                    inputEditCapitulo.setError(null);
                    cont++;
                }catch (NumeroVazioException e){
                    inputEditCapitulo.setError("campo vazio");
                }catch (NumeroInvalidoException e){
                    inputEditCapitulo.setError("campo invalido, apenas numeros validos");
                }

                try{
                    checkInformationNumber(inputEditQuantidade);
                    quantidadeTotal = Double.parseDouble(inputEditQuantidade.getEditText().getText().toString());
                    inputEditQuantidade.setError(null);
                    cont++;
                }catch (NumeroVazioException e){
                    inputEditQuantidade.setError("campo vazio");
                }catch (NumeroInvalidoException e){
                    inputEditQuantidade.setError("campo invalido, apenas numeros validos");
                }

                if(cont == 3){

                    valorMaior = false;
                    try{
                        valorMaior = checkValorBigger(capitulo,quantidadeTotal);
                    }catch(VerificarMaiorExcpetion excpetion){
                        inputEditQuantidade.setError("campo invalido, quantidade Total menor que o capitulo Atual");
                    }

                    site = inputEditsite.getEditText().getText().toString();

                    if(site.equals("")){
                        site = "N/A";
                    }

                    if(valorMaior &&  nomeEquals) {

                        Manga novo = new Manga(nome, capitulo, quantidadeTotal, site);
                        dbManga.addGeek(novo);
                        mangas = dbManga.allDataList();
                        Snackbar.make(view, "SUCCESS", Snackbar.LENGTH_SHORT).show();
                        AdapterRecyclerManga.getInstance().upDateAdapter(mangas);
                        telaLista.updateAdapterRecycler();
                        inputEditNome.getEditText().setText("");
                        inputEditCapitulo.getEditText().setText("");
                        inputEditQuantidade.getEditText().setText("");
                        inputEditsite.getEditText().setText("");
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
    public boolean checkNameEquals(String nome, List<Manga> lista) throws NomeExistenteException {
        for(Manga nomes:lista){
            if(nomes.getNome().equals(nome)){
                throw new NomeExistenteException();
            }
        }

        return true;
    }

}

