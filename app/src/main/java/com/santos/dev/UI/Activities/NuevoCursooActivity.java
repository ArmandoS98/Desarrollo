package com.santos.dev.UI.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.santos.dev.R;
import com.santos.dev.Utils.FirebaseMethods;

import static com.santos.dev.Utils.Nodos.NODO_CURSOS;

public class NuevoCursooActivity extends AppCompatActivity {

    private EditText mEditTextTitulo;
    private EditText mEditTextContenido;
    private TextView mButtonGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_cursoo);

        mEditTextTitulo = findViewById(R.id.et_titulo);
        mEditTextContenido = findViewById(R.id.et_descripcion);
        mButtonGuardar = findViewById(R.id.btn_guardar);

        mButtonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSaveCurso(mEditTextTitulo.getText().toString(),mEditTextContenido.getText().toString());
            }
        });
    }

    private void getSaveCurso(String... args) {
        FirebaseMethods firebaseMethods = new FirebaseMethods(this,NODO_CURSOS);
        firebaseMethods.nuevoCurso(args[0], args[1],"foto","1","yo");
        finish();
    }
}
