package com.santos.dev.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.santos.dev.Conversiones;
import com.santos.dev.R;

import java.util.ArrayList;

public class AdapterFormulasN extends RecyclerView.Adapter<AdapterFormulasN.ViewHolder> {
    private Context context;
    private ArrayList<Conversiones> conversiones = new ArrayList<>();

    public AdapterFormulasN(Context context, ArrayList<Conversiones> conversiones) {
        this.context = context;
        this.conversiones = conversiones;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.cartas, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (viewHolder != null) {
            viewHolder.mTextViewTitutlo.setText(conversiones.get(i).getTitulo());
            viewHolder.mTextViewProblema.setText("Convertir " + conversiones.get(i).getEjercicio());
            viewHolder.mImageViewResolve.setImageResource(conversiones.get(i).getImagen());
        }
    }

    @Override
    public int getItemCount() {
        return conversiones.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewTitutlo;
        private TextView mTextViewProblema;
        private ImageView mImageViewResolve;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTitutlo = itemView.findViewById(R.id.tv_titulo);
            mTextViewProblema = itemView.findViewById(R.id.tv_ejemplo);
            mImageViewResolve = itemView.findViewById(R.id.img_resove);

        }
    }
}
