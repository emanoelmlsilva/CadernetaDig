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
public class editMangaFragment extends Fragment implements verificarCondicoesNameNumber<Manga> {

    private int posicaoItem;
    private Button back, saved,maiorC,maiorQ,menorC,menorQ;
    private View view;
    private TextInputLayout inputNome,inputSite, inputCapitulo, inputQuantidade;
    private List<Manga> lista;
    private Tela_ListaActivity telaLista;
    private Manga mangaEdit;
    private DataBaseGeek<Manga> dbManga;
    private int cont;
    private boolean equalsValue = false,nameEquals = false;

    public editMangaFragment() {
        // Required empty public constructor
        telaLista = new Tela_ListaActivity();
        dbManga = MangaController.getInstancia(getContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_manga, container, false);
        posicaoItem = telaLista.pegarPosition("manga");
        lista = dbManga.allDataList();
        mangaEdit = lista.get(posicaoItem);

        findView();
        retrieveInformation();
        buttonsEdit();

        return view;
    }

    public void onDestroy(){
        super.onDestroy();
    }

    public void buttonsEdit(){

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().popBackStack();
                telaLista.setVisibilityFab();

            }
        });

        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cont = 0;

                try {
                    checkInformaçoesNome(inputNome);
                    nameEquals = checkNameEquals(inputNome.getEditText().getText().toString(), lista);
                    inputNome.setError(null);
                } catch (NomeExistenteException e) {
                    inputNome.setError("nome java existe");
                } catch (StringVaziaException e) {
                    inputNome.setError("nome invalido, adicione um nome");
                }

                verificarTextInputLayout();

                if (nameEquals && cont == 2) {

                    try {
                        equalsValue = checkValorBigger(Double.parseDouble(inputCapitulo.getEditText().getText().toString()), Double.parseDouble(inputQuantidade.getEditText().getText().toString()));
                        inputQuantidade.setError(null);
                    } catch (VerificarMaiorExcpetion e) {
                        inputQuantidade.setError("invalido, ep_atual maior que ep_total");
                    }


                    if (equalsValue) {
                        mangaEdit.setNome(inputNome.getEditText().getText().toString());
                        mangaEdit.setAtual(Double.parseDouble(inputCapitulo.getEditText().getText().toString()));
                        mangaEdit.setTotal(Double.parseDouble(inputQuantidade.getEditText().getText().toString()));
                        if (inputSite.getEditText().getText().toString().isEmpty()) {
                            mangaEdit.setSite("N/A");
                        } else {
                            mangaEdit.setSite(inputSite.getEditText().getText().toString());
                        }
                        dbManga.updateGeek(mangaEdit);
                        lista = dbManga.allDataList();
                        AdapterRecyclerManga.getInstance().upDateAdapter(lista);
                        telaLista.updateAdapterRecycler();
                        Snackbar.make(view, "saved", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });

//buttons
        menorC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int valor = -1;
                checkInfoValue(inputCapitulo,valor);

            }
        });

        maiorC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int valor = 1;
                checkInfoValue(inputCapitulo,valor);
            }
        });

        menorQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int valor = -1;
                checkInfoValue(inputQuantidade,valor);

            }
        });

        maiorQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int valor = 1;
                checkInfoValue(inputQuantidade,valor);

            }
        });


    }


    public void findView(){

        inputNome = view.findViewById(R.id.textInputNome);
        inputSite = view.findViewById(R.id.textInputSite);
        inputCapitulo = view.findViewById(R.id.editCapitulo);
        inputQuantidade = view.findViewById(R.id.editQuantidade);
        back = view.findViewById(R.id.btn_back);
        saved = view.findViewById(R.id.btn_saved);
        menorC = view.findViewById(R.id.btn_menorC);
        maiorC = view.findViewById(R.id.btn_maiorC);
        menorQ = view.findViewById(R.id.btn_menorQ);
        maiorQ = view.findViewById(R.id.btn_maiorQ);

    }

    public void retrieveInformation(){

        inputNome.getEditText().setText(mangaEdit.getNome());
        inputCapitulo.getEditText().setText(Double.toString(mangaEdit.getAtual()));
        inputQuantidade.getEditText().setText(Double.toString(mangaEdit.getTotal()));
        inputSite.getEditText().setText(mangaEdit.getSite());

    }

    public void verificarTextInputLayout(){

        try{
            checkInformationNumber(inputCapitulo);
            inputCapitulo.setError(null);
            cont++;
        } catch (NumeroInvalidoException e) {
            inputCapitulo.setError("campo invalido");
        } catch (NumeroVazioException e) {
            inputCapitulo.setError("campo vazio");
        }

        try{
            checkInformationNumber(inputQuantidade);
            inputQuantidade.setError(null);
            cont++;
        } catch (NumeroInvalidoException e) {
            inputQuantidade.setError("campo invalido");
        } catch (NumeroVazioException e) {
            inputQuantidade.setError("campo vazio");
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

    public void checkInfoValueInt(TextInputLayout edit, int adicionado){

        checkInfoNull(edit);
        int valor = Integer.parseInt(edit.getEditText().getText().toString());
        if(valor >= 1){
            valor += adicionado;
        }

        if(valor == 0){
            valor = 1;
        }
        edit.getEditText().setText(Integer.toString(valor));

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

        for(int i = 0;i <lista.size();i++){
            if(lista.get(i).getNome().equals(inputNome.getEditText().getText().toString().trim()) && i != posicaoItem){
                throw new NomeExistenteException();
            }
        }

        return true;
    }



}
