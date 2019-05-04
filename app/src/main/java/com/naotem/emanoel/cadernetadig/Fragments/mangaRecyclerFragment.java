package com.naotem.emanoel.cadernetadig.Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.naotem.emanoel.cadernetadig.Adapter.AdapterRecyclerManga;
import com.naotem.emanoel.cadernetadig.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class mangaRecyclerFragment extends Fragment {

    private View view;
    private static RecyclerView myRecycler;
    public static AdapterRecyclerManga adapterManga;
    private static mangaRecyclerFragment instance = null;

    public static mangaRecyclerFragment getInstance(){

        if(instance == null){
            instance = new mangaRecyclerFragment();
        }
        return instance;
    }

    @SuppressLint("ValidFragment")
    private mangaRecyclerFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_manga_recycler, container, false);
        init();
        return view;
    }

    private void init(){
        myRecycler = view.findViewById(R.id.manga_recycler);
        adapterManga = AdapterRecyclerManga.getInstance();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        myRecycler.setLayoutManager(layoutManager);
        myRecycler.setHasFixedSize(true);
        myRecycler.setAdapter(adapterManga);
    }

}
