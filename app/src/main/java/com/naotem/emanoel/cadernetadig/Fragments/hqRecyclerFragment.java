package com.naotem.emanoel.cadernetadig.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.naotem.emanoel.cadernetadig.Adapter.AdapterRecyclerHq;
import com.naotem.emanoel.cadernetadig.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class hqRecyclerFragment extends Fragment {

    private View view;
    private RecyclerView myRecycler;
    private static AdapterRecyclerHq adapterHQ;

    public hqRecyclerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_hq_recycler, container, false);
        myRecycler = view.findViewById(R.id.hq_recycler);
        adapterHQ = AdapterRecyclerHq.getInstance();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        myRecycler.setLayoutManager(layoutManager);
        myRecycler.setHasFixedSize(true);
        myRecycler.setAdapter(adapterHQ);
        return view;
    }

}
