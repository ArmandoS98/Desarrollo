package com.santos.dev.Opciones;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.santos.dev.Adapters.AdaptadorFormulasG;
import com.santos.dev.Adapters.AdapterFormulasN;
import com.santos.dev.Conversiones;
import com.santos.dev.FormulaG;
import com.santos.dev.Operaciones.Fraccion;
import com.santos.dev.R;

import java.util.ArrayList;


public class FormulasFragment extends Fragment {
    private RecyclerView mRecyclerViewHoirzaontal;
    private RecyclerView mRecyclerViewConverciones;
    private ArrayList<FormulaG> mFormulaGS;
    private ArrayList<Conversiones> mConversiones;

    public FormulasFragment() {
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
        View view = inflater.inflate(R.layout.fragment_formulas, container, false);
        Fraccion fraccion = new Fraccion(30, 1);
        Fraccion fraccion1 = new Fraccion(1, 180);
        TextView textView = view.findViewById(R.id.textview_title);
        //textView.setText(fraccion.Grados_a_gon().toString(2));
        mRecyclerViewHoirzaontal = view.findViewById(R.id.recycler_formulas_generales);
        mRecyclerViewConverciones = view.findViewById(R.id.recycler);
        mRecyclerViewHoirzaontal.setHasFixedSize(true);
        mRecyclerViewConverciones.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewHoirzaontal.setLayoutManager(linearLayoutManager);
        mRecyclerViewConverciones.setLayoutManager(new LinearLayoutManager(getContext()));
        getData();
        getInfo();
        AdaptadorFormulasG adaptadorFormulasG = new AdaptadorFormulasG(getContext(), mFormulaGS);
        AdapterFormulasN adapterFormulasN = new AdapterFormulasN(getContext(), mConversiones);
        mRecyclerViewHoirzaontal.setAdapter(adaptadorFormulasG);
        mRecyclerViewConverciones.setAdapter(adapterFormulasN);
        return view;
    }

    private void getInfo() {
        mConversiones = new ArrayList<>();
        mConversiones.add(new Conversiones("π radianes", "180°", R.drawable.radianes));
        mConversiones.add(new Conversiones("Revoluciones", "180°", R.drawable.rev));
        mConversiones.add(new Conversiones("Gon", "180°", R.drawable.gons));

    }

    private void getData() {
        mFormulaGS = new ArrayList<>();
        mFormulaGS.add(new FormulaG("π radianes", "π radianes"));
        mFormulaGS.add(new FormulaG("Centigrados", "180°"));
        mFormulaGS.add(new FormulaG("GON", "200"));
    }
}
