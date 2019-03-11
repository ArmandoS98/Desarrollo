package com.santos.dev.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.santos.firebasecomponents.Models.FormulaG;
import com.santos.dev.R;

import java.util.ArrayList;

public class AdaptadorFormulasG extends RecyclerView.Adapter<AdaptadorFormulasG.ViewHolder> {
    private Context context;
    private ArrayList<FormulaG> formulaGS = new ArrayList<>();

    public AdaptadorFormulasG(Context context, ArrayList<FormulaG> formulaGS) {
        this.context = context;
        this.formulaGS = formulaGS;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.formulas_gnerales, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (viewHolder != null) {
            viewHolder.mTextViewTitulo.setText(formulaGS.get(i).getTitulo());
            viewHolder.mTextViewValor.setText(formulaGS.get(i).getImagen());
        }
    }

    @Override
    public int getItemCount() {
        return formulaGS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewTitulo;
        private TextView mTextViewValor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTitulo = itemView.findViewById(R.id.tv_titulo);
            mTextViewValor = itemView.findViewById(R.id.tv_valor);
        }
    }
}
