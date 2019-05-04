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
import com.naotem.emanoel.cadernetadig.Adapter.AdapterRecyclerHq;
import com.naotem.emanoel.cadernetadig.BancoDadosSQLite.DataBaseGeek;
import com.naotem.emanoel.cadernetadig.Controller.HqController;
import com.naotem.emanoel.cadernetadig.R;
import com.naotem.emanoel.cadernetadig.exception.NomeExistenteException;
import com.naotem.emanoel.cadernetadig.exception.NumeroInvalidoException;
import com.naotem.emanoel.cadernetadig.exception.NumeroVazioException;
import com.naotem.emanoel.cadernetadig.exception.StringVaziaException;
import com.naotem.emanoel.cadernetadig.exception.VerificarMaiorExcpetion;
import com.naotem.emanoel.cadernetadig.model.Hq;
import com.naotem.emanoel.cadernetadig.model.verificarCondicoesNameNumber;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class registerHqFragment extends Fragment implements verificarCondicoesNameNumber<Hq> {


    private Button btnVoltar,btnAdd;
    private View view;
    private String nome,site;
    private double capitulo,quantidade;
    private int cont;
    private List<Hq> hqs;
    private TextInputLayout inputLayoutNome,inputLayoutCapitulo,inputLayoutQuantidade,inputLayoutSite;
    private Tela_ListaActivity telaLista;
    private DataBaseGeek<Hq> dbHq;
    private boolean nomeEquals,valorMaior;

    public registerHqFragment() {

        telaLista = new Tela_ListaActivity();
        dbHq = HqController.getInstance(getContext());
    }

    private void init(){
        hqs = dbHq.allDataList();
        btnVoltar = view.findViewById(R.id.btn_voltar);
        btnAdd = view.findViewById(R.id.btn_add);
        inputLayoutNome = view.findViewById(R.id.contentNome);
        inputLayoutNome.setError(null);
        inputLayoutCapitulo = view.findViewById(R.id.contentCapiitulo);
        inputLayoutCapitulo.setError(null);
        inputLayoutQuantidade = view.findViewById(R.id.contentQuantidade);
        inputLayoutQuantidade.setError(null);
        inputLayoutSite = view.findViewById(R.id.contentSite);
        inputLayoutSite.setError(null);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_registrer_hq, container, false);
        init();
        actionButtons();

        return view;
    }

    private void actionButtons(){

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                telaLista.setVisibilityFab();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                site = inputLayoutSite.getEditText().getText().toString();

                cont = 0;

                try{
                    checkInformaçoesNome(inputLayoutNome);
                    nome = inputLayoutNome.getEditText().getText().toString();
                    nomeEquals = checkNameEquals(nome,hqs);
                    inputLayoutNome.setError(null);
                    cont++;
                }catch (StringVaziaException erro){
                    inputLayoutNome.setError("campo vazio");
                }catch (NomeExistenteException erro){
                    inputLayoutNome.setError("nome ja existente");
                }

                try{
                    checkInformationNumber(inputLayoutCapitulo);
                    capitulo = Double.parseDouble(inputLayoutCapitulo.getEditText().getText().toString());
                    inputLayoutCapitulo.setError(null);
                    cont++;
                }catch (NumeroVazioException e){
                    inputLayoutCapitulo.setError("campo vazio");
                }catch (NumeroInvalidoException e){
                    inputLayoutCapitulo.setError("campo invalido, apenas numeros validos");
                }

                try{
                    checkInformationNumber(inputLayoutQuantidade);
                    quantidade = Double.parseDouble(inputLayoutQuantidade.getEditText().getText().toString());
                    inputLayoutQuantidade.setError(null);
                    cont++;
                }catch (NumeroVazioException e){
                    inputLayoutQuantidade.setError("campo vazio");
                }catch (NumeroInvalidoException e){
                    inputLayoutQuantidade.setError("campo invalido, apenas numeros validos");
                }

                if(cont == 3){

                    valorMaior = false;
                    try{
                        valorMaior = checkValorBigger(capitulo,quantidade);
                    }catch(VerificarMaiorExcpetion excpetion){
                        inputLayoutQuantidade.setError("campo invalido, quantidade Total menor que o capitulo Atual");
                    }

                    site = inputLayoutSite.getEditText().getText().toString();

                    if(site.equals("")){
                        site = "N/A";
                    }

                    if(valorMaior &&  nomeEquals) {

                        Hq novo = new Hq(nome, capitulo, quantidade, site);
                        dbHq.addGeek(novo);
                        hqs = dbHq.allDataList();
                        Snackbar.make(view, "SUCCESS", Snackbar.LENGTH_SHORT).show();
                        AdapterRecyclerHq.getInstance().upDateAdapter(hqs);
                        telaLista.updateAdapterRecycler();
                        inputLayoutNome.getEditText().setText("");
                        inputLayoutCapitulo.getEditText().setText("");
                        inputLayoutQuantidade.getEditText().setText("");
                        inputLayoutSite.getEditText().setText("");
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
    public boolean checkNameEquals(String nome, List<Hq> lista) throws NomeExistenteException {
        for(Hq nomes:lista){
            if(nomes.getNome().equals(nome)){
                throw new NomeExistenteException();
            }
        }

        return true;
    }
}
