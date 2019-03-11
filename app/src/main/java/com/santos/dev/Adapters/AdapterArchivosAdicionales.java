package com.santos.dev.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.santos.firebasecomponents.Models.ArchivosAniadidos;
import com.santos.dev.R;

import java.util.ArrayList;

public class AdapterArchivosAdicionales extends RecyclerView.Adapter<AdapterArchivosAdicionales.ViewHolder> {
    private Context context;
    private ArrayList<ArchivosAniadidos> archivosAniadidos = new ArrayList<>();

    public AdapterArchivosAdicionales(Context context, ArrayList<ArchivosAniadidos> archivosAniadidos) {
        this.context = context;
        this.archivosAniadidos = archivosAniadidos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_archivos_card, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (viewHolder != null) {
            //obtenemos la foto de perfil
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.ic_close_black_24dp)
                    .error(R.drawable.ic_close_black_24dp)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH);

            Glide.with(context)
                    .load(archivosAniadidos.get(i).getUrl())
                    .apply(options)
                    .into((viewHolder).mImageView);
        }
    }

    @Override
    public int getItemCount() {
        return archivosAniadidos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.img_archivo);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Hola que hace?", Toast.LENGTH_SHORT).show();
        }
    }
}
