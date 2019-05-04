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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.naotem.emanoel.cadernetadig.Activity.Tela_ListaActivity;
import com.naotem.emanoel.cadernetadig.BancoDadosSQLite.DataBaseGeek;
import com.naotem.emanoel.cadernetadig.Controller.HqController;
import com.naotem.emanoel.cadernetadig.Fragments.editarHqFragment;
import com.naotem.emanoel.cadernetadig.Fragments.infoHqFragment;
import com.naotem.emanoel.cadernetadig.R;
import com.naotem.emanoel.cadernetadig.exception.NumeroInvalidoException;
import com.naotem.emanoel.cadernetadig.exception.NumeroVazioException;
import com.naotem.emanoel.cadernetadig.exception.VerificarMaiorExcpetion;
import com.naotem.emanoel.cadernetadig.model.Hq;
import com.naotem.emanoel.cadernetadig.model.verificarCondicaoNumber;

import java.util.List;

public class AdapterRecyclerHq extends RecyclerView.Adapter<AdapterRecyclerHq.MyViewHorlder> implements verificarCondicaoNumber {

    private Tela_ListaActivity telaLista;
    private MyViewHorlder myHolder;
    private int positionItem;
    private Context mContext;
    private DataBaseGeek<Hq> db;
    private List<Hq> hqs;
    private static AdapterRecyclerHq instance = null;
    private ConstraintLayout selectionConstraint;

    public static AdapterRecyclerHq getInstance(){
        if(instance == null){
            instance = new AdapterRecyclerHq();
        }
        return instance;
    }

    private AdapterRecyclerHq(){

        db = HqController.getInstance(null);
        hqs = db.allDataList();

    }


    @NonNull
    @Override
    public AdapterRecyclerHq.MyViewHorlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapterlistahq,parent,false);
        mContext = itemLista.getContext();
        myHolder = new MyViewHorlder(itemLista);
        telaLista = new Tela_ListaActivity();
        db = HqController.getInstance(null);
        hqs = db.allDataList();
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHorlder holder, final int position) {

        final Hq hq = hqs.get(position);
        ConstraintLayout myConstraint = holder.constrainHolder;
        holder.nome.setText(hq.getNome());
        holder.capitulo.setText("Capitulo Atual: "+Double.toString(hq.getAtual()));
        holder.quantidade.setText("Quantidade Total: "+Double.toString(hq.getTotal()));
        clickImagemMenu(holder,hq,position);
        clickGeek(myConstraint,holder);
        addValue(holder,hq,db);

    }

    private void clickImagemMenu(final MyViewHorlder holder, final Hq hq, final int position){
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
                                removerItem(hq);
                                break;
                            case R.id.editar:
                                telaLista.mudarFragment(new editarHqFragment());
                                break;
                            case R.id.informacao:
                                telaLista.mudarFragment(new infoHqFragment());
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
    private void removerItem(final Hq hq){

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
                db.delete(hq.getId());
                hqs = db.allDataList();
                AdapterRecyclerHq.getInstance().upDateAdapter(hqs);
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

    //    adiciona um a mais no capitulo ou no total
    private void addValue(MyViewHorlder myHolder, final Hq myHq, final DataBaseGeek<Hq> dbHq){

        final double atual = myHq.getAtual(), total = myHq.getTotal();

        myHolder.addCapitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkEpAtual(Double.toString(atual),atual,total)){
                    myHq.setAtual(atual + 1);
                    clickButtons(myHq,dbHq);
                }
            }
        });

        myHolder.addTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkEpTotal(Double.toString(total),total,atual)){
                    myHq.setTotal(total + 1);
                    clickButtons(myHq,dbHq);
                }
            }
        });

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
    private boolean checkEpTotal(String input,double capitulo,double total){
        try{
            checkInformationNumber(input);
            return true;
        }catch(NumeroInvalidoException | NumeroVazioException erro){
            Log.i("erro","não é um numero");
            return false;
        }

    }

    //    mostrar os botoes para adicionar mais um (1)
    private void clickGeek(final ConstraintLayout actalConstraint, final MyViewHorlder myHolder){

        myHolder.constrainHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectionConstraint != null){
                    selectionConstraint.findViewById(R.id.constraintHq).setVisibility(ConstraintLayout.GONE);
                    Log.i("teste","null!=");
                }

                if(actalConstraint.equals(selectionConstraint)){
                    selectionConstraint.findViewById(R.id.constraintHq).setVisibility(ConstraintLayout.GONE);
                    selectionConstraint = null;
                    Log.i("teste","equal");
                }else {
                    actalConstraint.findViewById(R.id.constraintHq).setVisibility(ConstraintLayout.VISIBLE);
                    selectionConstraint = actalConstraint;
                    Log.i("teste","else");
                }
            }
        });

    }

    //    salvar alteração do butao
    private void clickButtons(Hq hq,DataBaseGeek<Hq> dbHq){
        dbHq.updateGeek(hq);
        AdapterRecyclerHq.getInstance().upDateAdapter(dbHq.allDataList());
        telaLista.updateAdapterRecycler();
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


    public void upDateAdapter(List<Hq> listHq){
        this.hqs = listHq;
    }

    public int posicaoItem(){
        return positionItem;
    }

    @Override
    public int getItemCount() {
        return hqs.size();
    }

    class MyViewHorlder extends RecyclerView.ViewHolder{

        TextView nome;
        TextView capitulo;
        TextView quantidade;
        ConstraintLayout constrainHolder;
        ImageView imagem;
        ImageButton addCapitulo,addTotal;

        public MyViewHorlder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.adapterNome);
            capitulo = itemView.findViewById(R.id.adapterCapitulo);
            quantidade = itemView.findViewById(R.id.adapterQuantidade);
            constrainHolder = itemView.findViewById(R.id.adapterHq);
            imagem = itemView.findViewById(R.id.adapterMenu);
            addCapitulo = itemView.findViewById(R.id.btnAddAdapterCapHq);
            addTotal = itemView.findViewById(R.id.btnAddAdapterTotHq);
        }
    }
}
