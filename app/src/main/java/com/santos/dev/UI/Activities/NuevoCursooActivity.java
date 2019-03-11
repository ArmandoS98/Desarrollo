package com.santos.dev.UI.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.santos.dev.R;
import com.santos.firebasecomponents.FirebaseMethods;

import static com.santos.firebasecomponents.Nodos.NODO_CURSOS;


public class NuevoCursooActivity extends AppCompatActivity {

    private EditText mEditTextTitulo;
    private EditText mEditTextContenido;
    private TextView mButtonGuardar;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_cursoo);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

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
        firebaseMethods.nuevoCurso(args[0], args[1],"foto","1",mFirebaseUser.getUid());
        finish();
    }
}
