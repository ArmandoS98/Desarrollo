package com.santos.dev.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.santos.dev.R;
import com.santos.firebasecomponents.Models.Semana;

import java.util.ArrayList;

public class AdapterHorarios extends RecyclerView.Adapter<AdapterHorarios.ViewHolder> {
    private Context mContext;
    private ArrayList<Semana> titulo = new ArrayList<>();

    public AdapterHorarios(Context mContext, ArrayList<Semana> titulo) {
        this.mContext = mContext;
        this.titulo = titulo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_horario, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (viewHolder != null) {
            viewHolder.mTextViewCurso.setText("Curso: " + titulo.get(i).getCurso());
            viewHolder.mtexTextViewCatedratico.setText("Catedratico: " + titulo.get(i).getCatedratico());
            viewHolder.mteTextViewSalon.setText("Salon: " + titulo.get(i).getSalon());
            viewHolder.mTextViewHora.setText("De " + titulo.get(i).getHora_de() + " a " + titulo.get(i).getHora_a());
        }
    }

    @Override
    public int getItemCount() {
        return titulo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextViewCurso;
        private TextView mtexTextViewCatedratico;
        private TextView mteTextViewSalon;
        private TextView mTextViewHora;
        private ImageView mImageViewPopup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewCurso = itemView.findViewById(R.id.subject);
            mtexTextViewCatedratico = itemView.findViewById(R.id.teacher);
            mteTextViewSalon = itemView.findViewById(R.id.room);
            mTextViewHora = itemView.findViewById(R.id.time);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
