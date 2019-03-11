package com.santos.dev.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.santos.dev.Interfaz.IMainMaestro;
import com.santos.firebasecomponents.Models.Cursos;
import com.santos.dev.R;

import java.util.ArrayList;

public class AdaptadorCursos extends RecyclerView.Adapter<AdaptadorCursos.ViewHolder> {
    private static final String TAG = "AdaptadorCursos";
    private Context context;
    private ArrayList<Cursos> cursos = new ArrayList<>();
    private IMainMaestro mIMainMaestro;
    private int mSelectedNoteIndex;

    public AdaptadorCursos(Context context, ArrayList<Cursos> cursos) {
        this.context = context;
        this.cursos = cursos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cursos, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.mTextViewTexto.setText(cursos.get(i).getNombre_curso());
    }

    @Override
    public int getItemCount() {
        return cursos.size();
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mIMainMaestro = (IMainMaestro) context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextViewTexto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTexto = itemView.findViewById(R.id.tv_titulo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mSelectedNoteIndex = getAdapterPosition();
            mIMainMaestro.onCursotoNotaa(cursos.get(mSelectedNoteIndex));
        }
    }
}
