package com.santos.dev.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.santos.dev.Models.Cuestionario;
import com.santos.dev.R;

import java.util.ArrayList;

public class AdaptadorCuestionario extends RecyclerView.Adapter<AdaptadorCuestionario.ViewHolder> {
    private static final String TAG = "AdaptadorCuestionario";
    private Context context;
    private ArrayList<Cuestionario> cuestionarios = new ArrayList<>();

    public AdaptadorCuestionario(Context context, ArrayList<Cuestionario> cuestionarios) {
        this.context = context;
        this.cuestionarios = cuestionarios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cuestionario, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        if (viewHolder != null) {
            viewHolder.mTextViewPregunta.setText(cuestionarios.get(i).getPregunta());
            viewHolder.mEditTextRespuesta.setHint(cuestionarios.get(i).getRespuesta_txt());
        }
    }

    @Override
    public int getItemCount() {
        return cuestionarios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewPregunta;
        private EditText mEditTextRespuesta;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewPregunta = itemView.findViewById(R.id.tv_titulo);
            mEditTextRespuesta = itemView.findViewById(R.id.et_content);
        }
    }
}
