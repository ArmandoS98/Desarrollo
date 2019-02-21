package com.santos.dev.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.santos.dev.Interfaz.IMainMaestro;
import com.santos.dev.Models.Notas;
import com.santos.dev.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorNotas extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "AdaptadorMaestrosComple";
    private Context mContext;
    private ArrayList<Notas> alumnos = new ArrayList<>();
    private IMainMaestro mIMainMaestro;
    private int mSelectedNoteIndex;

    public AdaptadorNotas(Context mContext, ArrayList<Notas> alumnos) {
        this.mContext = mContext;
        this.alumnos = alumnos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        int layout = 0;

        layout = R.layout.item_alumnos_layout;


        View itemView = LayoutInflater.from(mContext).inflate(layout, parent, false);
        holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            //obtenemos la foto de perfil
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.ic_close_black_24dp)
                    .error(R.drawable.ic_close_black_24dp)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH);

            Glide.with(mContext)
                    .load(alumnos.get(position).getUrl_foto())
                    .apply(options)
                    .into(((ViewHolder) holder).mImageView);

            //obtenemos su nombre
            ((ViewHolder) holder).mTextViewNombre.setText(alumnos.get(position).getTituloNota());
            ((ViewHolder) holder).mteTextViewDescripcion.setText(alumnos.get(position).getDescripcionNota());
        }
    }

    @Override
    public int getItemCount() {
        return alumnos.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mIMainMaestro = (IMainMaestro) mContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CircleImageView mCircleImageViewPerfil;
        private ImageView mImageView;
        private TextView mTextViewNombre;
        private TextView mTextViewNombreUser;
        private TextView mteTextViewDescripcion;
        private ActionMode maActionMode;
        private boolean multiSelect = false;
        private CardView frameLayout;
        int p;
        private ArrayList<Integer> selectedItems = new ArrayList<Integer>();

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.img_ejercicio);
            mCircleImageViewPerfil = itemView.findViewById(R.id.img_user);
            mTextViewNombre = itemView.findViewById(R.id.tv_nombre_usuario);
            mTextViewNombreUser = itemView.findViewById(R.id.tv_persona_name);
            mteTextViewDescripcion = itemView.findViewById(R.id.tv_descripcion);
            frameLayout = itemView.findViewById(R.id.cadr_elements);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            mSelectedNoteIndex = getAdapterPosition();
            mIMainMaestro.onNotaSeleccionada(alumnos.get(mSelectedNoteIndex));
        }
    }

    public void updateList(ArrayList<Notas> newLista) {
        alumnos = new ArrayList<>();
        alumnos.addAll(newLista);
        notifyDataSetChanged();
    }
}
