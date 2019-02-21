package com.santos.dev;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.santos.dev.Models.Notas;

import java.text.SimpleDateFormat;

import static com.santos.dev.MainActivity.KEY_NOTAS;

public class ShowActivity extends AppCompatActivity {
    private static final String TAG = "ShowActivity";

    private TextView mTextViewDescripcion;
    private TextView mTextViewFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);


        Notas mNote = null;
        Intent i = getIntent();
        mNote = i.getParcelableExtra(KEY_NOTAS);

        if (mNote != null) {
            Log.d(TAG, "onCreate: Valor: " + mNote);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
            collapsingToolbar.setTitle(mNote.getTituloNota());

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            loadBackdrop(mNote.getUrl_foto());

            mTextViewDescripcion = findViewById(R.id.tv_descripcion_show);
            mTextViewFecha = findViewById(R.id.tv_fecha);

            mTextViewDescripcion.setText(mNote.getDescripcionNota());

            String date = getIntent().getStringExtra("date");
            mTextViewFecha.setText(date);
        }
    }

    private void loadBackdrop(String url_foto) {
        final ImageView imageView = findViewById(R.id.backdrop);
        Glide.with(this).load(url_foto).apply(RequestOptions.centerCropTransform()).into(imageView);
    }

}
