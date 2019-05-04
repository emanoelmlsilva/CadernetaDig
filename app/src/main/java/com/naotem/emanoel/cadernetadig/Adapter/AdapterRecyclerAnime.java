package com.naotem.emanoel.cadernetadig.Adapter;

import android.annotation.SuppressLint;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.naotem.emanoel.cadernetadig.Activity.Tela_ListaActivity;
import com.naotem.emanoel.cadernetadig.BancoDadosSQLite.DataBaseGeek;
import com.naotem.emanoel.cadernetadig.Controller.AnimeController;
import com.naotem.emanoel.cadernetadig.Fragments.editAnimeFragment;
import com.naotem.emanoel.cadernetadig.Fragments.infoAnimeFragment;
import com.naotem.emanoel.cadernetadig.R;
import com.naotem.emanoel.cadernetadig.exception.NumeroInvalidoException;
import com.naotem.emanoel.cadernetadig.exception.NumeroVazioException;
import com.naotem.emanoel.cadernetadig.exception.VerificarMaiorExcpetion;
import com.naotem.emanoel.cadernetadig.model.Anime;
import com.naotem.emanoel.cadernetadig.model.verificarCondicaoNumber;

import java.util.List;

public class AdapterRecyclerAnime extends RecyclerView.Adapter<AdapterRecyclerAnime.MyViewHorlder> implements verificarCondicaoNumber {

    private Tela_ListaActivity telaLista;
    private AdapterRecyclerAnime.MyViewHorlder myHolder;
    private int positionItem;
    private Context mContext;
    private AnimeController db;
    private List<Anime> animes;
    private static AdapterRecyclerAnime instance = null;
    private ConstraintLayout selectionConstraint;

    public static AdapterRecyclerAnime getInstance(){
        if(instance == null){
            instance = new AdapterRecyclerAnime();
        }
        return instance;
    }

    private AdapterRecyclerAnime(){

        db = AnimeController.getInstancia(null);
        animes = db.allDataList();
    }

    @NonNull
    @Override
    public AdapterRecyclerAnime.MyViewHorlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapterlistanime,parent,false);
        mContext = itemLista.getContext();
        myHolder = new AdapterRecyclerAnime.MyViewHorlder(itemLista);
        telaLista = new Tela_ListaActivity();
        db = AnimeController.getInstancia(null);
        animes = db.allDataList();
        return myHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHorlder holder, final int position) {

        final Anime anime =  animes.get(position);
        ConstraintLayout myConstraint = holder.constraintLayout;
        holder.nome.setText(anime.getNome());
        holder.ep_Atual.setText("Ep_Atual: " + Double.toString(anime.getAtual()));
        holder.ep_Total.setText("Ep_Total: " + Double.toString(anime.getTotal()));
        holder.season.setText("Temporada: " + anime.getSeason());

        clickImagemMenu(holder,anime,position);
        clickGeek(myConstraint,holder);
        addValue(holder,anime,db);

    }

    //    adiciona um a mais no capitulo ou no total
    private void addValue(MyViewHorlder myHolder, final Anime myAnime, final DataBaseGeek<Anime> dbAnime){

        final double atual = myAnime.getAtual(), total = myAnime.getTotal();

        myHolder.addAtual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkEpAtual(Double.toString(atual),atual,total)){
                    myAnime.setAtual(atual + 1);
                    clickButtons(myAnime,dbAnime);
                }
            }
        });

        myHolder.addTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkEpTotal(Double.toString(total))){
                    myAnime.setTotal(total + 1);
                    clickButtons(myAnime,dbAnime);
                }
            }
        });

    }

    //    mostrar os botoes para adicionar mais um (1)
    private void clickGeek(final ConstraintLayout actalConstraint, final MyViewHorlder myHolder){

        myHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectionConstraint != null){
                    selectionConstraint.findViewById(R.id.constraintAnime).setVisibility(ConstraintLayout.GONE);
                    Log.i("teste","null!=");
                }

                if(actalConstraint.equals(selectionConstraint)){
                    selectionConstraint.findViewById(R.id.constraintAnime).setVisibility(ConstraintLayout.GONE);
                    selectionConstraint = null;
                    Log.i("teste","equal");
                }else {
                    actalConstraint.findViewById(R.id.constraintAnime).setVisibility(ConstraintLayout.VISIBLE);
                    selectionConstraint = actalConstraint;
                    Log.i("teste","else");
                }
            }
        });

    }


    //    salvar alteração do butao
    private void clickButtons(Anime anime,DataBaseGeek<Anime> dbAnime){
        dbAnime.updateGeek(anime);
        AdapterRecyclerAnime.getInstance().upDateAdapter(dbAnime.allDataList());
        telaLista.updateAdapterRecycler();
    }

    public int posicaoItem(){
        return positionItem;
    }

    public void upDateAdapter(List<Anime> listAnime){
        this.animes = listAnime;
    }
    @Override
    public int getItemCount() {
        return  animes.size();
    }

    private void clickImagemMenu(final MyViewHorlder holder, final Anime anime, final int position){
        holder.imagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                positionItem = position;

                PopupMenu menu = new PopupMenu(mContext,holder.imagem);
                menu.inflate(R.menu.menu_opcoes_recycler);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch(item.getItemId()){
                            case R.id.remover:
                                removerItem(anime);
                                break;
                            case R.id.editar:
                                telaLista.mudarFragment(new editAnimeFragment());
                                break;
                            case R.id.informacao:
                                telaLista.mudarFragment(new infoAnimeFragment());
                                break;
                        }

                        return false;
                    }
                });


                menu.show();
            }
        });
    }

    //    remover item da lista
    private void removerItem(final Anime anime){

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
                db.delete(anime.getId());
                animes = db.allDataList();
                AdapterRecyclerAnime.getInstance().upDateAdapter(animes);
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

    //    verifica se as condiçoes dos numeros estao corretas episodio atual
    private boolean checkEpAtual(String input,double capitulo,double total){
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

    //    verifica se as condiçoes dos numeros estao corretas episodio total
    private boolean checkEpTotal(String input){
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
        }else if(!input.matches("^[0-9].*")){
            throw new NumeroInvalidoException();
        }
    }

    @Override
    public void checkValorBigger(double valor1, double valor2) throws VerificarMaiorExcpetion {
        if(valor1 > valor2) throw new VerificarMaiorExcpetion();
    }

    class MyViewHorlder extends RecyclerView.ViewHolder{

        TextView nome;
        TextView season;
        TextView ep_Atual;
        TextView ep_Total;
        ConstraintLayout constraintLayout;
        ImageView imagem;
        ImageButton addAtual,addTotal;

        MyViewHorlder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.listViewNome);
            season = itemView.findViewById(R.id.adapterSeason);
            ep_Atual = itemView.findViewById(R.id.listViewEpisodioAtual);
            ep_Total = itemView.findViewById(R.id.listViewEpisodioTotal);
            constraintLayout = itemView.findViewById(R.id.adapterlistanime);
            imagem = itemView.findViewById(R.id.listViewMenuAnime);
            addAtual = itemView.findViewById(R.id.addAtual);
            addTotal = itemView.findViewById(R.id.addTotal);
        }
    }



}
