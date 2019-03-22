package com.santos.dev.UI.Calendario;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.santos.dev.Adapters.AdapterHorarios;
import com.santos.dev.R;
import com.santos.dev.Utils.SQLiteFukes.DatabaseHelper;


public class LunesFragment extends Fragment {
    private static final String TAG = "LunesFragment";
    DatabaseHelper databaseHelper;
    private RecyclerView mRecyclerView;
    private AdapterHorarios mAdapterHorarios;


    public LunesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lunes, container, false);
        databaseHelper = new DatabaseHelper(getContext());
        getDataSQLite(view);
        return view;
    }

    private void getDataSQLite(View view) {
        Log.d(TAG, "getDataSQLite: ");

        mRecyclerView = view.findViewById(R.id.recyclergenerico);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mAdapterHorarios = new AdapterHorarios(getContext(), databaseHelper.getSemana("0"));
        mRecyclerView.setAdapter(mAdapterHorarios);

    }

}
