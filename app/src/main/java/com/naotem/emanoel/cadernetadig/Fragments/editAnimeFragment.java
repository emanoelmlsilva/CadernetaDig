package com.naotem.emanoel.cadernetadig.Fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
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

public class editAnimeFragment extends Fragment implements verificarCondicoesNameNumber<Anime> {

    private View view;
    private int posicaoItem;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private String[] seasonNum;
    private Button back,save,maiorS,menorS,maiorEpA,menorEpA,maiorEpT,menorEpT;
    private TextInputLayout inputNome,inputEpAtual,inputEpTotal,inputSite;
    private Anime animeEdit;
    private List<Anime> lista;
    private Tela_ListaActivity telaLista;
    private DataBaseGeek<Anime> dbAnime;
    private boolean equalsValue = false,nameEquals = false;
    private int cont;

    public editAnimeFragment() {
        // Required empty public constructor
        telaLista = new Tela_ListaActivity();
        dbAnime = AnimeController.getInstancia(getContext());
        posicaoItem = telaLista.pegarPosition("anime");
        lista = dbAnime.allDataList();
        animeEdit = lista.get(posicaoItem);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_anime, container, false);
        findView();
        retrieveInformation();
        actionButtons();
        return view;
    }

    public void findView(){

        back = view.findViewById(R.id.btn_back_Edit);
        save = view.findViewById(R.id.btn_saved_Edit);
        maiorS = view.findViewById(R.id.btn_Season_maior);
        menorS = view.findViewById(R.id.btn_Season_menor);
        maiorEpA = view.findViewById(R.id.btn_maiorEpA);
        menorEpA = view.findViewById(R.id.btn_menorEpA);
        maiorEpT = view.findViewById(R.id.btn_maiorEpT);
        menorEpT = view.findViewById(R.id.btn_menorEpT);
        inputNome = view.findViewById(R.id.textInputNome);
        seasonNum = new String[101];
        fill_Array_of_numbers();
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,seasonNum);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = view.findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        pegarValorSpinner();
        inputEpAtual = view.findViewById(R.id.textInputEpAtual);
        inputEpTotal = view.findViewById(R.id.textInputEpTotal);
        inputSite = view.findViewById(R.id.textInputSite);

    }

    
    public void retrieveInformation(){

        inputNome.getEditText().setText(animeEdit.getNome());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spinner.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        inputEpAtual.getEditText().setText(Double.toString(animeEdit.getAtual()));
        inputEpTotal.getEditText().setText(Double.toString(animeEdit.getTotal()));
        inputSite.getEditText().setText(animeEdit.getSite());

    }

    public void actionButtons(){

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().popBackStack();
                telaLista.setVisibilityFab();

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cont = 0;

                try{
                    checkInformaçoesNome(inputNome);
                    nameEquals = checkNameEquals(inputNome.getEditText().getText().toString(),lista);
                    inputNome.setError(null);
                } catch (NomeExistenteException e) {
                    inputNome.setError("nome java existe");
                } catch (StringVaziaException e) {
                    inputNome.setError("nome invalido, adicione um nome");
                }

                verificarTextInputLayout();

                if(nameEquals  && cont == 2) {

                    try{
                        equalsValue = checkValorBigger(Double.parseDouble(inputEpAtual.getEditText().getText().toString()),Double.parseDouble(inputEpTotal.getEditText().getText().toString()));
                        inputEpTotal.setError(null);
                    } catch (VerificarMaiorExcpetion e) {
                        inputEpTotal.setError("invalido, ep_atual maior que ep_total");
                    }

                    if(equalsValue){
                        animeEdit.setNome(inputNome.getEditText().getText().toString());
                        animeEdit.setSeason((String) spinner.getSelectedItem());
                        animeEdit.setAtual(Double.parseDouble(inputEpAtual.getEditText().getText().toString()));
                        animeEdit.setTotal(Double.parseDouble(inputEpTotal.getEditText().getText().toString()));
                        if(inputSite.getEditText().getText().toString().isEmpty()){
                            animeEdit.setSite("N/A");
                        }else{
                            animeEdit.setSite(inputSite.getEditText().getText().toString());
                        }

                        dbAnime.updateGeek(animeEdit);
                        lista = dbAnime.allDataList();
                        AdapterRecyclerAnime.getInstance().upDateAdapter(lista);
                        telaLista.updateAdapterRecycler();
                        Snackbar.make(view,"saved",Snackbar.LENGTH_SHORT).show();

                    }

                }

            }
        });

//        Buttons
        maiorS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int valor = 1;
                checkInfoValueSpinner(valor);

            }
        });

        menorS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int valor = -1;
                checkInfoValueSpinner(valor);

            }
        });

        maiorEpA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int valor = 1;
                checkInfoValue(inputEpAtual,valor);

            }
        });

        menorEpA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int valor = -1;
                checkInfoValue(inputEpAtual,valor);

            }
        });

        maiorEpT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int valor = 1;
                checkInfoValue(inputEpTotal,valor);

            }
        });

        menorEpT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int valor = -1;
                checkInfoValue(inputEpTotal,valor);

            }
        });


    }

    public void verificarTextInputLayout(){

                try{
                    checkInformationNumber(inputEpAtual);
                    inputEpAtual.setError(null);
                    cont++;
                } catch (NumeroInvalidoException e) {
                    inputEpAtual.setError("campo invalido");
                } catch (NumeroVazioException e) {
                    inputEpAtual.setError("campo vazio");
                }

                try{
                    checkInformationNumber(inputEpTotal);
                    inputEpTotal.setError(null);
                    cont++;
                } catch (NumeroInvalidoException e) {
                    inputEpTotal.setError("campo invalido");
                } catch (NumeroVazioException e) {
                    inputEpTotal.setError("campo vazio");
                }

    }


    public void checkInfoNull(TextInputLayout edit){
        if(edit.getEditText().getText().toString().isEmpty()){
            edit.getEditText().setText("0");
        }
    }

    public void checkInfoValue(TextInputLayout edit, int adicionado){

        checkInfoNull(edit);
        double valor = Double.parseDouble(edit.getEditText().getText().toString());
        if(valor >= 1){
            valor += adicionado;
        }

        if(valor == 0){
            valor = 1;
        }
        edit.getEditText().setText(Double.toString(valor));

    }

    public void checkInfoValueSpinner(int valor){

        if(spinner.getSelectedItem().equals("default") && valor == -1){
            spinner.setSelection(0);
        }else{
            spinner.setSelection(spinner.getSelectedItemPosition()+valor);
        }

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

        for(int i = 0;i <lista.size();i++){
            if(lista.get(i).getNome().equals(inputNome.getEditText().getText().toString().trim()) && i != posicaoItem){
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

    private void pegarValorSpinner(){
            int index;
            if(animeEdit.getSeason().equals("default")){
                index = 0;
            }else{
                index = Integer.parseInt(animeEdit.getSeason());
            }
            spinner.setSelection(index);
    }

}
