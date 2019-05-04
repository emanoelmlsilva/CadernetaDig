package com.naotem.emanoel.cadernetadig.SeachBinary;

import android.util.Log;

import com.naotem.emanoel.cadernetadig.model.Geek;

import java.util.List;

public class SeachBinary {

        private static SeachBinary instance = null;
        private List<Geek> list;

        private SeachBinary(){
        }

        public static SeachBinary getInstance(){
            if(instance == null){
                instance = new SeachBinary();
            }
            return instance;
        }

        public int seach(List<Geek> list,Comparable chave){
            int lengthSeach = list.size();
            int lo = 1;
            int hi = lengthSeach;
            while(lo <= hi){
                int mid = lo + (hi - lo) / 2;
                if(toCompareMenor(chave, (Comparable) list.get(mid).getNome())){
                    hi = mid - 1;
                }else if(toCompareMaior(chave, (Comparable) list.get(mid).getNome())){
                    lo = mid + 1;
                }else{
                    return mid;
                }
            }
            return -1;
        }

        private boolean toCompareMenor(Comparable chave,Comparable list){
            return chave.compareTo(list) < 0;
        }

        private boolean toCompareMaior(Comparable chave,Comparable list){
            return chave.compareTo(list) > 0;
        }
}
