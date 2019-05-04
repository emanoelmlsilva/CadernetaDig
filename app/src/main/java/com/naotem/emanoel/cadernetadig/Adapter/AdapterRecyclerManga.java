package com.naotem.emanoel.cadernetadig.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.naotem.emanoel.cadernetadig.Activity.Tela_ListaActivity;
import com.naotem.emanoel.cadernetadig.BancoDadosSQLite.DataBaseGeek;
import com.naotem.emanoel.cadernetadig.Controller.MangaController;
import com.naotem.emanoel.cadernetadig.Fragments.editMangaFragment;
import com.naotem.emanoel.cadernetadig.Fragments.infoMangaFragment;
import com.naotem.emanoel.cadernetadig.R;
import com.naotem.emanoel.cadernetadig.exception.NumeroInvalidoException;
import com.naotem.emanoel.cadernetadig.exception.NumeroVazioException;
import com.naotem.emanoel.cadernetadig.exception.VerificarMaiorExcpetion;
import com.naotem.emanoel.cadernetadig.model.Manga;
import com.naotem.emanoel.cadernetadig.model.verificarCondicaoNumber;

import java.util.List;

public class AdapterRecyclerManga extends RecyclerView.Adapter<AdapterRecyclerManga.MyViewHorlder> implements verificarCondicaoNumber {

    private Tela_ListaActivity telaLista;
    private MyViewHorlder myHolder;
    private int positionItem;
    private Context mContext;
    private DataBaseGeek<Manga> db;
    private List<Manga> mangas;
    private static AdapterRecyclerManga instance = null;
    private ConstraintLayout selectionConstraint;


    public static AdapterRecyclerManga getInstance(){
        if(instance == null){
            instance = new AdapterRecyclerManga();
        }
        return instance;
    }

    private AdapterRecyclerManga(){

        db = MangaController.getInstancia(null);
        mangas = db.allDataList();

    }

    @NonNull
    @Override
    public MyViewHorlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapterlistmanga,parent,false);
        mContext = itemLista.getContext();
        myHolder = new MyViewHorlder(itemLista);
        telaLista = new Tela_ListaActivity();
        db = MangaController.getInstancia(null);
        mangas = db.allDataList();
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHorlder holder, final int position) {

        final Manga manga =  mangas.get(position);
        ConstraintLayout myConstraint = holder.constraintLayout;
        holder.nome.setText(manga.getNome());
        holder.capitulo.setText("Capitulo Atual: "+Double.toString(manga.getAtual()));
        holder.quantidade.setText("Quantidade Total: "+Double.toString(manga.getTotal()));

        clickImagemMenu(holder,manga,position);
        clickGeek(myConstraint,holder,position);
        addValue(holder,manga,db);

    }

//    adiciona um a mais no capitulo ou no total
    private void addValue(MyViewHorlder myHolder, final Manga myManga, final DataBaseGeek<Manga> dbManga){

        final double capitulo = myManga.getAtual(), total = myManga.getTotal();

        myHolder.addCapitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkChapter(Double.toString(capitulo),capitulo,total)){
                    myManga.setAtual(capitulo + 1);
                    clickButtons(myManga,dbManga);
                }
            }
        });

        myHolder.addTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAmountTotal(Double.toString(total),capitulo,total)){
                    myManga.setTotal(total + 1);
                    clickButtons(myManga,dbManga);
                }
            }
        });

    }

//    salvar alteração do butao
    private void clickButtons(Manga manga,DataBaseGeek<Manga> dbManga){
        dbManga.updateGeek(manga);
        AdapterRecyclerManga.getInstance().upDateAdapter(dbManga.allDataList());
        telaLista.updateAdapterRecycler();
    }

//    menu de opcao
    private void clickImagemMenu(final MyViewHorlder myHolder, final Manga manga, final int position){

        myHolder.imagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positionItem = position;

                PopupMenu menu = new PopupMenu(mContext,myHolder.imagem);
                menu.inflate(R.menu.menu_opcoes_recycler);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch(item.getItemId()){
                            case R.id.remover:
                                removerItem(manga);
                                break;
                            case R.id.editar:
                                telaLista.mudarFragment(new editMangaFragment());
                                break;
                            case R.id.informacao:
                                telaLista.mudarFragment(new infoMangaFragment());
                                break;
                        }

                        return false;
                    }
                });

                menu.show();
            }
        });
    }

//    mostrar os botoes para adicionar mais um (1)
    private void clickGeek(final ConstraintLayout actalConstraint, final MyViewHorlder myHolder, final int position){

        myHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectionConstraint != null){
                    selectionConstraint.findViewById(R.id.constraintButton).setVisibility(ConstraintLayout.GONE);
                }

                if(actalConstraint.equals(selectionConstraint)){
                    selectionConstraint.findViewById(R.id.constraintButton).setVisibility(ConstraintLayout.GONE);
                    selectionConstraint = null;
                }else {
                    actalConstraint.findViewById(R.id.constraintButton).setVisibility(ConstraintLayout.VISIBLE);
                    selectionConstraint = actalConstraint;
                }
            }
        });

    }

//    remover item da lista
    private void removerItem(final Manga manga){

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.DialogStyle);

        builder.setTitle("Confirmar");

//                configura cancelamento
        builder.setCancelable(false);

//                Configura icone
        builder.setIcon(android.R.drawable.ic_delete);

//                Configurar opções de butoes sim ou nao
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.delete(manga.getId());
                mangas = db.allDataList();
                AdapterRecyclerManga.getInstance().upDateAdapter(mangas);
                telaLista.updateAdapterRecycler();
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                                       nao faz nada
            }
        });

//              Criar e exibir dialog
        builder.create();
        builder.show();

    }

//    atualiza a lista do adapter
    public void upDateAdapter(List<Manga> listManga){
        this.mangas = listManga;
    }

    @Override
    public int getItemCount() {
        return  mangas.size();
    }

    public int posicaoItem(){
        return positionItem;
    }

//    verifica se as condiçoes dos numeros estao corretas de capitulo
    private boolean checkChapter(String input, double capitulo, double total){
        try{
            checkInformationNumber(input);
            checkValorBigger(capitulo,total);
            return true;
        }catch(NumeroInvalidoException | NumeroVazioException erro){
            Log.i("erro","não é um numero");
            return false;
        }catch (VerificarMaiorExcpetion erro){
            Log.i("erro","capitulo maior");
            return false;
        }

    }

    //    verifica se as condiçoes dos numeros estao corretas de quantidade
    private boolean checkAmountTotal(String input, double capitulo, double total){
        try{
            checkInformationNumber(input);
            return true;
        }catch(NumeroInvalidoException | NumeroVazioException erro){
            Log.i("erro","não é um numero");
            return false;
        }

    }


    @Override
    public void checkInformationNumber(String input) throws NumeroVazioException, NumeroInvalidoException {
        if(input.isEmpty()){
            throw new NumeroVazioException();
        }else if(input.matches("^[0-9].*") == false){
            throw new NumeroInvalidoException();
        }

    }

    @Override
    public void checkValorBigger(double valor1, double valor2) throws VerificarMaiorExcpetion {

        if(valor1 > valor2) throw new VerificarMaiorExcpetion();

    }


    class MyViewHorlder extends RecyclerView.ViewHolder{

        TextView nome;
        TextView capitulo;
        TextView quantidade;
        ImageView imagem;
        ConstraintLayout constraintLayout;
        ImageButton addCapitulo,addTotal;

        MyViewHorlder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.listViewNome);
            capitulo = itemView.findViewById(R.id.listViewCapitulo);
            quantidade = itemView.findViewById(R.id.listViewQuantidade);
            imagem = itemView.findViewById(R.id.listViewMenuManga);
            constraintLayout = itemView.findViewById(R.id.constrainRecy);
            addCapitulo = itemView.findViewById(R.id.btnAddAdapterCapitulo);
            addTotal = itemView.findViewById(R.id.btnAddAdapterTotal);
        }
    }


}