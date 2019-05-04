package com.naotem.emanoel.cadernetadig.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.naotem.emanoel.cadernetadig.Adapter.AdapterRecyclerAnime;
import com.naotem.emanoel.cadernetadig.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class animeRecyclerFragment extends Fragment {

    private View view;
    private RecyclerView myRecycler;
    private static AdapterRecyclerAnime adapterAnime;

    public animeRecyclerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_anime_recycler, container, false);
        myRecycler = view.findViewById(R.id.anime_recycler);
        adapterAnime = AdapterRecyclerAnime.getInstance();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        myRecycler.setLayoutManager(layoutManager);
        myRecycler.setHasFixedSize(true);
        myRecycler.setAdapter(adapterAnime);
        return view;
    }

}
