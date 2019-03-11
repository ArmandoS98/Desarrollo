package com.santos.dev.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.santos.dev.Adapters.AdaptadorNotas;
import com.santos.dev.Interfaz.IMainMaestro;
import com.santos.firebasecomponents.Models.Notas;
import com.santos.dev.R;

import static android.app.Activity.RESULT_OK;

public class Dialog_FullScreen extends DialogFragment implements View.OnClickListener {
    private static final int GalleriaPick = 1;
    private Notas notas;
    private EditText mEditTextTitulo;
    private EditText mEditTextContent;
    private TextView mTextViewSave;
    private ImageView mImageView;
    private IMainMaestro iMainMaestro;
    private Uri mImageUri;

    public static Dialog_FullScreen newInstance(Notas notas) {
        Dialog_FullScreen dialog = new Dialog_FullScreen();

        Bundle args = new Bundle();
        args.putParcelable("notas", notas);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        notas = getArguments().getParcelable("notas");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fullscreen_dialong, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle("Editar");

        mEditTextTitulo = view.findViewById(R.id.et_titulo);
        mEditTextContent = view.findViewById(R.id.et_descripcion);
        mTextViewSave = view.findViewById(R.id.save);
        mImageView = view.findViewById(R.id.img_preview);

        mEditTextTitulo.setText(notas.getTituloNota());
        mEditTextContent.setText(notas.getDescripcionNota());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_close_black_24dp)
                .error(R.drawable.ic_close_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(this)
                .load(notas.getUrl_foto())
                .apply(options)
                .into(mImageView);

        mTextViewSave.setOnClickListener(this);
        mImageView.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        iMainMaestro = (IMainMaestro) getActivity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.save: {

                String title = mEditTextTitulo.getText().toString();
                String content = mEditTextContent.getText().toString();

                if (!title.equals("")) {

                    notas.setTituloNota(title);
                    notas.setDescripcionNota(content);

                    iMainMaestro.onNotaUpdate(notas);
                    getDialog().dismiss();
                } else {
                    Toast.makeText(getActivity(), "Enter a title", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.img_preview: {
                Intent galeriaIntent = new Intent();
                galeriaIntent.setAction(Intent.ACTION_GET_CONTENT);
                galeriaIntent.setType("image/*");
                startActivityForResult(galeriaIntent, GalleriaPick);
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleriaPick && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            mImageUri = data.getData();

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.ic_close_black_24dp)
                    .error(R.drawable.ic_close_black_24dp)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH);

            Glide.with(this)
                    .load(mImageUri)
                    .apply(options)
                    .into(mImageView);

        }
    }
}
