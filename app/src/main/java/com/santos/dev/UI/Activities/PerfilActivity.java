package com.santos.dev.UI.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.santos.dev.MainActivity;
import com.santos.dev.R;

public class PerfilActivity extends AppCompatActivity implements View.OnClickListener {

    //private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        Button btn = findViewById(R.id.btn_hola);
        btn.setOnClickListener(this);
        //mAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_hola:
                FirebaseAuth.getInstance().signOut();

                startActivity(new Intent(PerfilActivity.this, LoginUActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PerfilActivity.this, MainActivity.class));
        finish();
    }
}
