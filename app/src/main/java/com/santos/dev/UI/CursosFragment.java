package com.santos.dev.UI;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.santos.dev.Adapters.AdaptadorCursos;
import com.santos.dev.Models.Cursos;
import com.santos.dev.R;
import com.santos.dev.Utils.FirebaseMethods;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

import javax.annotation.Nullable;

import static com.santos.dev.Utils.Nodos.NODO_CURSOS;
import static com.santos.dev.Utils.Nodos.PARAMETRO_KEY;
import static com.santos.dev.Utils.Nodos.PARAMETRO_VALOR;

public class CursosFragment extends Fragment {
    private static final String TAG = "CursosFragment";
    private RecyclerView mRecyclerViewConverciones;
    private ArrayList<Cursos> mCursos = new ArrayList<>();

    //Variables
    private DocumentSnapshot mLastQueriedDocument;
    private FirebaseMethods firebaseMethods;
    private RotateLoading mRotateLoading;
    private AdaptadorCursos mAdaptadorNotas;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;

    public CursosFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cursos, container, false);
        mRecyclerViewConverciones = view.findViewById(R.id.recyclergenerico);
        mRotateLoading = view.findViewById(R.id.rotateloading);

        mRotateLoading.start();

        getAlumnos();
        initRecyclerView();
        return view;
    }

    FirebaseFirestore db;
    CollectionReference notesCollectionRef;

    private void getAlumnos() {
        db = FirebaseFirestore.getInstance();

        notesCollectionRef = db.collection(NODO_CURSOS);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        Query notesQuery = null;
        if (mLastQueriedDocument != null) {
            notesQuery = notesCollectionRef
                    .whereEqualTo("id_user_settings", mFirebaseUser.getUid())
                    .startAfter(mLastQueriedDocument);
        } else {
            notesQuery = notesCollectionRef
                    .whereEqualTo("id_user_settings", mFirebaseUser.getUid());
        }


        notesQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    //mImageViewNoHayTasks.setVisibility(View.GONE);
                    //mTextViewNoHayTasks.setVisibility(View.GONE);

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Cursos cursos = document.toObject(Cursos.class);
                        mCursos.add(cursos);
                    }

                    /*if (alumnos.size() == 0) {
                        mTextViewNoDatos.setVisibility(View.VISIBLE);
                    }*/

                    if (task.getResult().size() != 0) {
                        mLastQueriedDocument = task.getResult().getDocuments().get(task.getResult().size() - 1);
                    }

                    //imagenanimada.setVisibility(View.GONE);

                    mRotateLoading.stop();
                    //imagenanimada.pauseAnimation();
                    //imagenanimada.setVisibility(View.GONE);
                    mAdaptadorNotas.notifyDataSetChanged();
                    //runAnimation(mRecyclerView,0);
                } else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //Este metodo inicia el recycler view con sus componentes
    private void initRecyclerView() {
        if (mAdaptadorNotas == null) {
            mAdaptadorNotas = new AdaptadorCursos(getContext(), mCursos);
        }

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
        mRecyclerViewConverciones.setLayoutManager(layoutManager);
        mRecyclerViewConverciones.setAdapter(mAdaptadorNotas);
    }

    @Override
    public void onResume() {
        super.onResume();
        db.collection(NODO_CURSOS)
                .whereEqualTo("id_user_settings", mFirebaseUser.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d(TAG, "onEvent: llego");
                            return;
                        }


                        if (mCursos.size() == 0) {
                            mCursos.clear();
                        } else {
                            mCursos.clear();
                            for (QueryDocumentSnapshot doc : value) {
                                Cursos cursos = doc.toObject(Cursos.class);
                                mCursos.add(cursos);
                            }

                            /*if (alumnos.size() == 0) {
                                mTextViewNoDatos.setVisibility(View.VISIBLE);
                            } else {
                                mTextViewNoDatos.setVisibility(View.GONE);
                            }*/

                        }

                        mAdaptadorNotas.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onStart() {

        super.onStart();
    }

    @Override
    public void onStop() {
       /* if (listenerRegistration != null) {
            listenerRegistration.remove();
        }*/
        super.onStop();
    }
}
