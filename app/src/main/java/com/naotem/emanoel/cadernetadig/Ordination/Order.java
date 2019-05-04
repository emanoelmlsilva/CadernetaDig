package com.naotem.emanoel.cadernetadig.Ordination;

import com.naotem.emanoel.cadernetadig.model.Geek;

import java.util.List;

public class Order {

    private static Order instance = null;

    private Order(){    }

    public static Order getInstance(){
        if(instance == null){
            instance = new Order();
        }
        return instance;
    }

    public static List<Geek> seleciton(List<Geek> list){
        int min;
        for(int i = 0;i < list.size();i++){
            min = i;
            for(int j = i + 1; j < list.size();j++){
                if(toCompare(list.get(j).getAtual() ,list.get(min).getAtual())){
                    min = j;
                }
            }
            Geek aux = list.get(i);
            list.set(i,list.get(min));
            list.set(min,aux);
        }
        reverse(list);
        return list;
    }

    private static boolean toCompare(Comparable geekA,Comparable geekB){
        return geekA.compareTo(geekB) < 0;
    }

    private static void reverse(List<Geek> listReverse){
        int hi = listReverse.size() - 1;
        for(int i = 0;i < listReverse.size() / 2;i++){
            Geek aux = listReverse.get(i);
            listReverse.set(i,listReverse.get(hi));
            listReverse.set(hi--,aux);
        }
    }

//    Ordenar por String

    public List<Geek> selecitonString(List<Geek> list){
        int min;
        for(int i = 0;i < list.size();i++){
            min = i;
            for(int j = i + 1; j < list.size();j++){
                if(toCompare(list.get(j).getNome().toLowerCase() ,list.get(min).getNome().toLowerCase())){
                    min = j;
                }
            }
            Geek aux = list.get(i);
            list.set(i,list.get(min));
            list.set(min,aux);
        }
//        reverse(list);
        return list;
    }


////    MERGE NAO ESTA FUNCIONADO
////    public void changeOptionist(List<Geek> changeNewList){
////        listItem = changeNewList;
////    }
//
//    //    lo = inicio, mid = meio, hi = fim
//    private static void merge(List<Geek> listOriginal,List<Geek>listAux,int lo,int mid,int hi){
//
////        copiar o array original para o auxiliar
//        listAux.addAll(listOriginal);
//
//        int i = lo, j = mid + 1;
//
//        for(int k = lo;k <= hi; k++){
//
//            if(i > mid){
//                listOriginal.set(k,listAux.get(j++));
//            }else if(j > hi){
//                listOriginal.set(k,listAux.get(i++));
//            }else if(comparar(listAux.get(j).getAtual(),listAux.get(i).getAtual())){
//                listOriginal.set(k,listAux.get(j++));
//            }else{
//                listOriginal.set(k,listAux.get(i++));
//            }
//        }
//
//    }
//
//    private static void sort(List<Geek> listOriginal,List<Geek> listAux,int lo,int hi){
//        if(hi <= lo){
//            return;
//        }
//        int mid = lo + ((hi - lo) / 2);
//        sort(listOriginal,listAux,lo,mid);
//        sort(listOriginal,listAux,mid + 1,hi);
//        merge(listOriginal,listAux,lo,mid,hi);
//    }
//
//    public static List<Geek> sort(List<Geek> list){
//        List<Geek> listAux = new ArrayList<>();
//        listAux.addAll(list);
//        sort(list,listAux,0,list.size()-1);
//        return list;
//    }
//
////    public static void show(List<Geek> list){
////        for(Geek g : list){
////            System.out.println("ordenou "+g.getAtual());//nome "+g.getNome()+"\n atual "+g.getAtual()+"\n total "+g.getTotal());
////        }
////    }
//    private static boolean comparar(Comparable item1,Comparable item2){
//        return item1.compareTo(item2) < 0; //retonar true se item1 for menor que item2
//    }
//
//
//
////    public void mostrar(){
////        for(int i = 0;i< listItem.size();i++){
////            System.out.println("comparando "+i+" = "+comparar(listItem.get(i),listItem.get(i+1)));
////        }
////    }
//
//
////    @Override
////    public int compareTo(@NonNull Item o) {
////        return
////        return 0;
////    }
}
