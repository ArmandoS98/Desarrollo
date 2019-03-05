package com.santos.dev.UI.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.santos.dev.Adapters.AdaptadorCuestionario;
import com.santos.dev.Adapters.AdaptadorNotas;
import com.santos.dev.Dialogs.Dialog_FullScreen;
import com.santos.dev.Dialogs.Dialog_FullScreen_Cuestionario;
import com.santos.dev.Interfaz.IMainMaestro;
import com.santos.dev.Models.Cuestionario;
import com.santos.dev.Models.Cursos;
import com.santos.dev.Models.Notas;
import com.santos.dev.R;
import com.santos.dev.Utils.FirebaseMethods;

import java.util.ArrayList;

import javax.annotation.Nullable;

import static com.santos.dev.MainActivity.KEY_NOTAS;
import static com.santos.dev.UI.Activities.TabActivity.id_docuento;
import static com.santos.dev.Utils.Nodos.CONTENIDO_NOTA;
import static com.santos.dev.Utils.Nodos.KEY;
import static com.santos.dev.Utils.Nodos.NODO_CUESTIONARIO;
import static com.santos.dev.Utils.Nodos.NODO_CURSOS;
import static com.santos.dev.Utils.Nodos.NODO_NOTAS;
import static com.santos.dev.Utils.Nodos.PARAMETRO_ID_NOTA;
import static com.santos.dev.Utils.Nodos.TITULO_NOTA;

public class ShowActivity extends AppCompatActivity implements IMainMaestro {
    private static final String TAG = "ShowActivity";

    private FirebaseFirestore db;
    private DocumentSnapshot mLastQueriedDocument;
    private FirebaseAuth mAuth;
    private StorageReference mStorageReference;
    private FirebaseMethods mFirebaseMethods;

    private TextView mTextViewDescripcion;
    private TextView mTextViewFecha;
    private TextView mButtonEditarNota;
    private TextView mButtonElimnarNota;
    private FloatingActionButton mFloatingActionButton;
    private RecyclerView mRecyclerView;
    private AdaptadorCuestionario mAdaptadorCuestionario;
    private ArrayList<Cuestionario> cuestionarios;

    private Uri mImageUri;
    private String id_nota;
    public static String curso_id;
    private String nombre_nota;
    Notas mNote = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mFirebaseMethods = new FirebaseMethods(this);

        Intent i = getIntent();
        mNote = i.getParcelableExtra(KEY_NOTAS);

        if (mNote != null) {
            Log.d(TAG, "onCreate: Valor: " + mNote);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            nombre_nota = mNote.getTituloNota();
            CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
            collapsingToolbar.setTitle(nombre_nota);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            loadBackdrop(mNote.getUrl_foto());

            mTextViewDescripcion = findViewById(R.id.tv_descripcion_show);
            mTextViewFecha = findViewById(R.id.tv_fecha);
            mButtonEditarNota = findViewById(R.id.tv_editar_nota);
            mButtonElimnarNota = findViewById(R.id.tv_eliminar_nota);
            mFloatingActionButton = findViewById(R.id.fab);

            String date = getIntent().getStringExtra("date");
            curso_id = getIntent().getStringExtra(KEY);
            mTextViewFecha.setText(date);
            id_nota = mNote.getIdNota();

            mRecyclerView = findViewById(R.id.recyclergenerico);
            mRecyclerView.setHasFixedSize(true);
            cuestionarios = new ArrayList<>();
            getAlumnos(id_nota);
            initRecyclerView();

            if (mNote.getId_user_settings().equals(mAuth.getUid())) {
                mButtonEditarNota.setVisibility(View.VISIBLE);
                mButtonElimnarNota.setVisibility(View.VISIBLE);
            } else {
                mButtonEditarNota.setVisibility(View.GONE);
                mButtonElimnarNota.setVisibility(View.GONE);
            }

            final Notas finalMNote = mNote;
            mButtonEditarNota.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog_FullScreen dialog_fullScreen = Dialog_FullScreen.newInstance(finalMNote);
                    dialog_fullScreen.setCancelable(false);
                    dialog_fullScreen.show(getSupportFragmentManager(), "Editar");
                   /* Dialog_FullScreen dialog_fullScreen = new Dialog_FullScreen();

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    dialog_fullScreen.show(ft, dialog_fullScreen.TAG);*/
                }
            });

            mButtonElimnarNota.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ShowActivity.this);

                    builder.setTitle("Confirmar");
                    builder.setMessage("Esta seguro de eliminar esta nota?");

                    builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            db = FirebaseFirestore.getInstance();

                            DocumentReference noteRef = db
                                    .collection(NODO_CURSOS)
                                    .document(curso_id)
                                    .collection(NODO_NOTAS)
                                    .document(finalMNote.getIdNota());

                            noteRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ShowActivity.this, "Nota Eliminada", Toast.LENGTH_SHORT).show();
                                        //mNoteRecyclerViewAdapter.removeNote(note);
                                    } else {
                                        Toast.makeText(ShowActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            dialog.dismiss();
                            ShowActivity.this.finish();
                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

            mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ShowActivity.this, "Ups. Esta opcion esta en desarrollo", Toast.LENGTH_SHORT).show();
                }
            });

            mTextViewDescripcion.setText(mNote.getDescripcionNota());

        }
    }

    //Este metodo inicia el recycler view con sus componentes
    private void initRecyclerView() {
        if (mAdaptadorCuestionario == null) {
            mAdaptadorCuestionario = new AdaptadorCuestionario(this, cuestionarios);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
        mRecyclerView.setAdapter(mAdaptadorCuestionario);
    }

    private void getAlumnos(String id_notas) {
        db = FirebaseFirestore.getInstance();

        CollectionReference notesCollectionRef = db
                .collection(NODO_CURSOS)
                .document(curso_id)
                .collection(NODO_NOTAS)
                .document(id_nota).collection(NODO_CUESTIONARIO);

        Query notesQuery = null;
        if (mLastQueriedDocument != null) {
            notesQuery = notesCollectionRef
                    .whereEqualTo("id_nota", id_nota)
                    .startAfter(mLastQueriedDocument);
        } else {
            notesQuery = notesCollectionRef
                    .whereEqualTo("id_nota", id_nota);
        }


        notesQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    //mImageViewNoHayTasks.setVisibility(View.GONE);
                    //mTextViewNoHayTasks.setVisibility(View.GONE);

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Cuestionario _cuestionario = document.toObject(Cuestionario.class);
                        cuestionarios.add(_cuestionario);
                    }

                    /*if (alumnos.size() == 0) {
                        mTextViewNoDatos.setVisibility(View.VISIBLE);
                    }*/

                    if (task.getResult().size() != 0) {
                        mLastQueriedDocument = task.getResult().getDocuments().get(task.getResult().size() - 1);
                    }

                    //imagenanimada.setVisibility(View.GONE);

                    //mRotateLoading.stop();
                    //imagenanimada.pauseAnimation();
                    //imagenanimada.setVisibility(View.GONE);
                    mAdaptadorCuestionario.notifyDataSetChanged();
                    //runAnimation(mRecyclerView,0);
                } else {
                    Toast.makeText(ShowActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void loadBackdrop(String url_foto) {
        final ImageView imageView = findViewById(R.id.backdrop);
        Glide.with(this).load(url_foto).apply(RequestOptions.centerCropTransform()).into(imageView);
    }

    @Override
    public void onNotaSeleccionada(Notas notas) {
        //Esta opcion no se tuliza
    }

    @Override
    public void onNotaUpdate(Notas notas) {
        DocumentReference noteref = db.collection(NODO_CURSOS).document(curso_id).collection(NODO_NOTAS).document(notas.getIdNota());
        noteref.update(TITULO_NOTA, notas.getTituloNota(),
                CONTENIDO_NOTA, notas.getDescripcionNota()
        ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ShowActivity.this, "Informacion Actualizada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ShowActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onCursotoNotaa(Cursos cursos) {

    }

    @Override
    public void onNuevoCuestionario(String titulo, String content) {
        mFirebaseMethods.nuevoCuestionario(curso_id, id_nota, titulo, content);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUpdateNotasRefresh();
        getUpdateCuestionarioRefresh();
    }

    private void getUpdateCuestionarioRefresh() {
        db.collection(NODO_CURSOS).document(curso_id).collection(NODO_NOTAS).document(id_nota).collection(NODO_CUESTIONARIO)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if (cuestionarios.size() == 0) {
                            cuestionarios.clear();
                        } else {
                            cuestionarios.clear();
                            for (QueryDocumentSnapshot doc : value) {
                                Cuestionario cuestionario = doc.toObject(Cuestionario.class);
                                cuestionarios.add(cuestionario);
                            }
                        }
                        mAdaptadorCuestionario.notifyDataSetChanged();
                    }
                });
    }

    private void getUpdateNotasRefresh() {
        db.collection(NODO_CURSOS).document(curso_id).collection(NODO_NOTAS)
                .whereEqualTo(PARAMETRO_ID_NOTA, id_nota)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        for (QueryDocumentSnapshot doc : value) {
                            Notas notas = doc.toObject(Notas.class);


                            mTextViewDescripcion.setText(notas.getDescripcionNota());
                            nombre_nota = notas.getTituloNota();

                            /*Owner owner = doc.toObject(Owner.class);

                            user_ID = owner.getOwner_id();
                            tvcorre.setText(owner.getCorreo());
                            tvtelefono.setText(owner.getTelefono());
                            tvedad.setText(owner.getEdad());
                            tvnombres.setText(owner.getNombres());
                            mEditTextApellidos.setText(owner.getApellidos());
                            if (owner.getUrl_foto() != null) {
                                Glide.with(getApplicationContext()).load(owner.getUrl_foto()).into(imgpefil);
                            }*/
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.show, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.shared) {
            Toast.makeText(this, "Opcion en desarrollo", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, mTextViewDescripcion.getText().toString());
            startActivity(Intent.createChooser(intent, "Share with"));
            return true;
        } else if (id == R.id.preguntas) {
            Dialog_FullScreen_Cuestionario dialog_fullScreen = Dialog_FullScreen_Cuestionario.newInstance(mNote);
            dialog_fullScreen.setCancelable(false);
            dialog_fullScreen.show(getSupportFragmentManager(), "Cuestionario");
        }
        return super.onOptionsItemSelected(item);
    }
}
