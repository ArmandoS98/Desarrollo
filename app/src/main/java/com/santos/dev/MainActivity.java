package com.santos.dev;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.santos.dev.Interfaz.IMainMaestro;
import com.santos.dev.Models.Notas;
import com.santos.dev.Opciones.ConversionesFragment;
import com.santos.dev.Opciones.FormulasFragment;
import com.santos.dev.Utils.FirebaseMethods;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IMainMaestro,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MainActivity";
    private Fragment fragmentoGenerico = null;
    //FirebaseMethods
    private FirebaseMethods firebaseMethods;
    private Dialog epicDialog;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        epicDialog = new Dialog(this);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //TODO: Verificacion si el usuario esta logeado.
        if (firebaseUser != null) {

            //TODO: ESTO PERMITE CAMBIARLE EL TEXTO EN EL ENCABEZADO

            View hView = navigationView.getHeaderView(0);
            final TextView nav_user = hView.findViewById(R.id.tv_nombre);
            final CircleImageView profile = hView.findViewById(R.id.imageView);

            nav_user.setText(firebaseUser.getDisplayName());

            if (firebaseUser.getPhotoUrl() != null) {
                Glide.with(this).load(firebaseUser.getPhotoUrl()).into(profile);
            }
        } else {
            firebaseUser = null;
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        //TODO: AQUI MANDAMOS A MOSTRAR EL FRAGMENTO CUANDO SE INICIA LA ACTIVIDAD
        if (navigationView != null) {
            //    prepararDrawer(navigationView);
            //Seleccionar item por defecto
            setTitle(navigationView.getMenu().getItem(0).getTitle());
            fragmentoGenerico = new FormulasFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.contenedor, fragmentoGenerico).commit();
        }
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
           /* MediaFragment mediaFragment = new MediaFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.contenedorDeCosas,mediaFragment).commit();*/
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (fragmentoGenerico instanceof FormulasFragment) {
                super.onBackPressed();
            } else {
                showHome();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showTheNewDialog(R.style.DialogScale);
            //mAdaptadorMaestrosCompleto.notifyDataSetChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_camera) {
            fragmentoGenerico = new FormulasFragment();
        } else if (id == R.id.nav_gallery) {
            fragmentoGenerico = new ConversionesFragment();
        } else if (id == R.id.nav_slideshow) {

        }

        if (fragmentoGenerico != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.contenedor, fragmentoGenerico)
                    .commit();
        }

        setTitle(item.getTitle());


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showHome() {
        fragmentoGenerico = new FormulasFragment();
        if (fragmentoGenerico != null) {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);// Colocas el id de tu NavigationView
            setTitle(navigationView.getMenu().getItem(0).getTitle());
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.contenedor, fragmentoGenerico, fragmentoGenerico.getTag()).commit();
        }
    }

    private void showTheNewDialog(int type) {

        //Inicializacion de nuestros metodos
        firebaseMethods = new FirebaseMethods(this, "Notas");
        epicDialog.setContentView(R.layout.popup_alumnos);
        epicDialog.getWindow().getAttributes().windowAnimations = type;

        //Widgets
        final ImageView closePopupPositiveImg = epicDialog.findViewById(R.id.closePopupPositive);
        final Button aceptar = epicDialog.findViewById(R.id.btn_acept);
        final EditText mEditTextTitulo = epicDialog.findViewById(R.id.note_title);
        final EditText mEditTextApellidos = epicDialog.findViewById(R.id.tidt_apellido);
        final EditText mEditTextEdad = epicDialog.findViewById(R.id.tiet_edad);

        closePopupPositiveImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                epicDialog.dismiss();
            }
        });


        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo: obtenemos los valores de las vistas corresponidnetes
                String nombre = mEditTextTitulo.getText().toString();
                String apellidos = mEditTextApellidos.getText().toString();
                String edad = mEditTextEdad.getText().toString();

                if (checkInputs(nombre, apellidos, edad)) {
                    crearNuevoAlumno(nombre, apellidos, edad);
                    epicDialog.dismiss();
                }
            }
        });

        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.show();
    }

    private boolean checkInputs(String nombres, String apellidos, String edad) {
        if (nombres.equals("") || apellidos.equals("") || edad.equals("")) {
            Toast.makeText(this, "Todos los compos son obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void crearNuevoAlumno(String nombre, String apellidos, String edad) {
        //firebaseMethods.registrarNuevoEmail(correo,"123456789");
        firebaseMethods.nuevaNota(
                nombre,
                apellidos,
                edad);
    }

    @Override
    public void onNotaSeleccionada(Notas notas) {
        Log.d(TAG, "onNotaSeleccionada: Nota" + notas);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}
