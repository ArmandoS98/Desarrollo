package com.santos.dev.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.santos.dev.Interfaz.IMainMaestro;
import com.santos.firebasecomponents.Models.Cuestionario;
import com.santos.firebasecomponents.Models.Notas;
import com.santos.dev.R;

public class Dialog_FullScreen_Cuestionario extends DialogFragment implements View.OnClickListener {

    private Notas notas;
    private Cuestionario cuestionario;
    private EditText mEditTextTitulo;
    private EditText mEditTextContent;
    private TextView mTextViewSave;
    private IMainMaestro iMainMaestro;

    public static Dialog_FullScreen_Cuestionario newInstance(Notas notas) {
        Dialog_FullScreen_Cuestionario dialog = new Dialog_FullScreen_Cuestionario();

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
        View view = inflater.inflate(R.layout.fullscreen_dialong_cuestrionario, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        toolbar.setTitle("Cuestionario");

        mTextViewSave = view.findViewById(R.id.save);

        mEditTextTitulo = view.findViewById(R.id.et_titulo);
        mEditTextContent = view.findViewById(R.id.et_descripcion);

        mTextViewSave.setOnClickListener(this);

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
                    iMainMaestro.onNuevoCuestionario(title, content);
                    getDialog().dismiss();
                } else {
                    Toast.makeText(getActivity(), "Enter a title", Toast.LENGTH_SHORT).show();
                }
                break;
            }

        }
    }
}
