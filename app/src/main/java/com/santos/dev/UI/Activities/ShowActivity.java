package com.santos.dev.UI.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.santos.dev.Adapters.AdaptadorCuestionario;
import com.santos.dev.Adapters.AdapterArchivosAdicionales;
import com.santos.dev.Dialogs.Dialog_FullScreen;
import com.santos.dev.Dialogs.Dialog_FullScreen_Cuestionario;
import com.santos.dev.Interfaz.IMainMaestro;
import com.santos.firebasecomponents.Models.ArchivosAniadidos;
import com.santos.firebasecomponents.Models.Cuestionario;
import com.santos.firebasecomponents.Models.Cursos;
import com.santos.firebasecomponents.Models.Notas;
import com.santos.dev.R;
import com.santos.firebasecomponents.FirebaseMethods;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.annotation.Nullable;

import static com.santos.dev.MainActivity.KEY_NOTAS;
import static com.santos.firebasecomponents.Nodos.CONTENIDO_NOTA;
import static com.santos.firebasecomponents.Nodos.KEY;
import static com.santos.firebasecomponents.Nodos.NODO_CUESTIONARIO;
import static com.santos.firebasecomponents.Nodos.NODO_CURSOS;
import static com.santos.firebasecomponents.Nodos.NODO_IMAGENES_ANIADIDAS;
import static com.santos.firebasecomponents.Nodos.NODO_NOTAS;
import static com.santos.firebasecomponents.Nodos.PARAMETRO_ID_NOTA;
import static com.santos.firebasecomponents.Nodos.TITULO_NOTA;

public class ShowActivity extends AppCompatActivity implements IMainMaestro {
    private static final String TAG = "ShowActivity";
    private static final int GalleriaPick = 1;

    private FirebaseMethods firebaseMethods;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private DocumentSnapshot mLastQueriedDocument;
    private FirebaseAuth mAuth;
    private StorageReference mStorageReference;
    private FirebaseMethods mFirebaseMethods;

    private Dialog epicDialog;
    private TextView mTextViewDescripcion;
    private ImageButton mImageButtonArchivo;
    private TextView mTextViewFecha;
    private TextView mButtonEditarNota;
    private TextView mButtonElimnarNota;
    private FloatingActionButton mFloatingActionButton;
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerViewArchivos;
    private AdaptadorCuestionario mAdaptadorCuestionario;
    private AdapterArchivosAdicionales mAdapterArchivosAdicionales;
    private ArrayList<Cuestionario> cuestionarios;
    private ArrayList<ArchivosAniadidos> archivosAgregados;

    private Uri mImageUri;
    private String id_nota;
    public static String curso_id;
    private String nombre_nota;
    private Notas mNote = null;
    private String url_imagen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        mFirebaseMethods = new FirebaseMethods(this);

        epicDialog = new Dialog(this);

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

            toolbar.setNavigationOnClickListener(v -> finish());
            loadBackdrop(mNote.getUrl_foto());

            mTextViewDescripcion = findViewById(R.id.tv_descripcion_show);
            mTextViewFecha = findViewById(R.id.tv_fecha);
            mButtonEditarNota = findViewById(R.id.tv_editar_nota);
            mButtonElimnarNota = findViewById(R.id.tv_eliminar_nota);
            mFloatingActionButton = findViewById(R.id.fab);
            mRecyclerView = findViewById(R.id.recyclergenerico);
            mRecyclerViewArchivos = findViewById(R.id.recyclergenerico_arcivos_agregados);
            mImageButtonArchivo = findViewById(R.id.img_archivo);

            String date = getIntent().getStringExtra("date");
            curso_id = getIntent().getStringExtra(KEY);
            mTextViewFecha.setText(date);
            id_nota = mNote.getIdNota();

            mRecyclerView.setHasFixedSize(true);
            mRecyclerViewArchivos.setHasFixedSize(true);

            cuestionarios = new ArrayList<>();
            archivosAgregados = new ArrayList<>();

            getAlumnos(id_nota);
            getImagenAniadidas(id_nota);
            initRecyclerView();

            if (mNote.getId_user_settings().equals(mAuth.getUid())) {
                mButtonEditarNota.setVisibility(View.VISIBLE);
                mButtonElimnarNota.setVisibility(View.VISIBLE);
            } else {
                mButtonEditarNota.setVisibility(View.GONE);
                mButtonElimnarNota.setVisibility(View.GONE);
            }

            final Notas finalMNote = mNote;
            mButtonEditarNota.setOnClickListener(v -> {
                Dialog_FullScreen dialog_fullScreen = Dialog_FullScreen.newInstance(finalMNote);
                dialog_fullScreen.setCancelable(false);
                dialog_fullScreen.show(getSupportFragmentManager(), "Editar");
               /* Dialog_FullScreen dialog_fullScreen = new Dialog_FullScreen();

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                dialog_fullScreen.show(ft, dialog_fullScreen.TAG);*/
            });

            mButtonElimnarNota.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowActivity.this);

                builder.setTitle("Confirmar");
                builder.setMessage("Esta seguro de eliminar esta nota?");

                builder.setPositiveButton("SI", (dialog, which) -> {
                    // Do nothing but close the dialog
                    db = FirebaseFirestore.getInstance();

                    DocumentReference noteRef = db
                            .collection(NODO_CURSOS)
                            .document(curso_id)
                            .collection(NODO_NOTAS)
                            .document(finalMNote.getIdNota());

                    noteRef.delete().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ShowActivity.this, "Nota Eliminada", Toast.LENGTH_SHORT).show();
                            //mNoteRecyclerViewAdapter.removeNote(note);
                        } else {
                            Toast.makeText(ShowActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.dismiss();
                    ShowActivity.this.finish();
                });

                builder.setNegativeButton("NO", (dialog, which) -> {

                    // Do nothing
                    dialog.dismiss();
                });

                AlertDialog alert = builder.create();
                alert.show();
            });

            mFloatingActionButton.setOnClickListener(v -> Toast.makeText(ShowActivity.this, "Ups. Esta opcion esta en desarrollo", Toast.LENGTH_SHORT).show());

            mImageButtonArchivo.setOnClickListener(v -> {
                Intent galeriaIntent = new Intent();
                galeriaIntent.setAction(Intent.ACTION_GET_CONTENT);
                galeriaIntent.setType("image/*");
                startActivityForResult(galeriaIntent, GalleriaPick);
            });

            mTextViewDescripcion.setText(mNote.getDescripcionNota());

        }
    }

    private void showTheNewDialog(int type, final Uri mImageUri) {

        //Inicializacion de nuestros metodos
        firebaseMethods = new FirebaseMethods(this);
        epicDialog.setContentView(R.layout.nuevo_archivo_imagen);
        epicDialog.getWindow().getAttributes().windowAnimations = type;
        epicDialog.setCancelable(false);

        //Widgets
        final ImageView closePopupPositiveImg = epicDialog.findViewById(R.id.closePopupPositive);
        final ImageView imagenPreview = epicDialog.findViewById(R.id.img_preview);
        final Button aceptar = epicDialog.findViewById(R.id.btn_acept);
        final EditText mEditTextTitulo = epicDialog.findViewById(R.id.note_title);

        closePopupPositiveImg.setOnClickListener(v -> epicDialog.dismiss());

        mStorageReference = FirebaseStorage.getInstance().getReference("Imagenes");

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_close_black_24dp)
                .error(R.drawable.ic_close_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(this)
                .load(mImageUri)
                .apply(options)
                .into(imagenPreview);

        aceptar.setOnClickListener(v -> {
            //Todo: obtenemos los valores de las vistas corresponidnetes
            String nombre = mEditTextTitulo.getText().toString();

            if (checkInputs(nombre)) {
                saveNuevoArchivo(nombre, mImageUri);
                //crearNuevoAlumno(nombre, apellidos, edad);
                epicDialog.dismiss();
            }
        });

        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.show();
    }

    private boolean checkInputs(String nombres) {
        if (nombres.equals("")) {
            Toast.makeText(this, "Todos los compos son obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveNuevoArchivo(final String nombre, Uri mImageUri) {
        final StorageReference fileReference = mStorageReference.child(/*System.currentTimeMillis()*/ "acpu" + firebaseUser.getUid() + getDate() + ".jpg" /*+ getFileExtencion(mImageUri)*/);

        Task<Uri> urlTask = fileReference.putFile(this.mImageUri).continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            // Continue with the task to get the download URL
            return fileReference.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                url_imagen = downloadUri.toString();

                firebaseMethods.nuevoArchivo(
                        id_nota,
                        url_imagen,
                        nombre,
                        curso_id);
            } else {
                // Handle failures
                // ...
            }
        });


        /*fileReference.putFile(this.mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                url_imagen = fileReference.getDownloadUrl();
                url_imagen = taskSnapshot.getDownloadUrl().toString();
                firebaseMethods.nuevoArchivo(
                        id_nota,
                        url_imagen,
                        nombre,
                        curso_id);
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Upload is paused");
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                System.out.println("Upload is " + progress + "% done");
            }
        });*/
    }

    //Este metodo inicia el recycler view con sus componentes
    private void initRecyclerView() {
        if (mAdaptadorCuestionario == null) {
            mAdaptadorCuestionario = new AdaptadorCuestionario(this, cuestionarios);
        }

        if (mAdapterArchivosAdicionales == null) {
            mAdapterArchivosAdicionales = new AdapterArchivosAdicionales(this, archivosAgregados);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewArchivos.setLayoutManager(linearLayoutManager);

        mRecyclerView.setAdapter(mAdaptadorCuestionario);
        mRecyclerViewArchivos.setAdapter(mAdapterArchivosAdicionales);
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


        notesQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                cuestionarios.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Cuestionario _cuestionario = document.toObject(Cuestionario.class);
                    cuestionarios.add(_cuestionario);
                }

                if (cuestionarios.size() == 0) {
                    //mTextViewNoDatos.setVisibility(View.VISIBLE);
                }

                if (task.getResult().size() != 0) {
                    mLastQueriedDocument = task.getResult().getDocuments().get(task.getResult().size() - 1);
                }

                mAdaptadorCuestionario.notifyDataSetChanged();
            } else {
                Toast.makeText(ShowActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getImagenAniadidas(String id_notas) {
        db = FirebaseFirestore.getInstance();

        CollectionReference notesCollectionRef = db
                .collection(NODO_CURSOS)
                .document(curso_id)
                .collection(NODO_NOTAS)
                .document(id_nota)
                .collection(NODO_IMAGENES_ANIADIDAS);

        Query notesQuery = null;
        if (mLastQueriedDocument != null) {
            notesQuery = notesCollectionRef
                    .whereEqualTo("id_nota", id_nota)
                    .startAfter(mLastQueriedDocument);
        } else {
            notesQuery = notesCollectionRef
                    .whereEqualTo("id_nota", id_nota);
        }


        notesQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                archivosAgregados.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    ArchivosAniadidos archivosAniadidos = document.toObject(ArchivosAniadidos.class);
                    archivosAgregados.add(archivosAniadidos);
                }

                if (archivosAgregados.size() == 0) {
                    //mTextViewNoDatos.setVisibility(View.VISIBLE);
                    Toast.makeText(ShowActivity.this, "No hay ningun archivo!", Toast.LENGTH_SHORT).show();
                }

                if (task.getResult().size() != 0) {
                    mLastQueriedDocument = task.getResult().getDocuments().get(task.getResult().size() - 1);
                }

                mAdapterArchivosAdicionales.notifyDataSetChanged();
            } else {
                Toast.makeText(ShowActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
        ).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ShowActivity.this, "Informacion Actualizada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ShowActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
        getUpdateArchivoRefresh();
    }

    private void getUpdateCuestionarioRefresh() {
        db.collection(NODO_CURSOS).document(curso_id).collection(NODO_NOTAS).document(id_nota).collection(NODO_CUESTIONARIO)
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (cuestionarios.size() == 0) {
                        cuestionarios.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            Cuestionario cuestionario = doc.toObject(Cuestionario.class);
                            cuestionarios.add(cuestionario);
                        }
                    } else {
                        cuestionarios.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            Cuestionario cuestionario = doc.toObject(Cuestionario.class);
                            cuestionarios.add(cuestionario);
                        }
                    }
                    mAdaptadorCuestionario.notifyDataSetChanged();
                });
    }

    private void getUpdateArchivoRefresh() {
        db.collection(NODO_CURSOS)
                .document(curso_id)
                .collection(NODO_NOTAS)
                .document(id_nota)
                .collection(NODO_IMAGENES_ANIADIDAS)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if (archivosAgregados.size() == 0) {
                            archivosAgregados.clear();
                            for (QueryDocumentSnapshot doc : value) {
                                ArchivosAniadidos archivosAniadidos = doc.toObject(ArchivosAniadidos.class);
                                archivosAgregados.add(archivosAniadidos);
                            }
                        } else {
                            archivosAgregados.clear();
                            for (QueryDocumentSnapshot doc : value) {
                                ArchivosAniadidos archivosAniadidos = doc.toObject(ArchivosAniadidos.class);
                                archivosAgregados.add(archivosAniadidos);
                            }
                        }
                        mAdapterArchivosAdicionales.notifyDataSetChanged();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @android.support.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleriaPick && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            mImageUri = data.getData();

            showTheNewDialog(R.style.DialogScale, mImageUri);

        }
    }

    private String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd MMMM yyyy  HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}
