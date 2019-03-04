package com.santos.dev.UI;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.santos.dev.Adapters.AdaptadorNotas;
import com.santos.dev.Models.Conversiones;
import com.santos.dev.Models.FormulaG;
import com.santos.dev.Models.Notas;
import com.santos.dev.R;
import com.santos.dev.Utils.FirebaseMethods;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

import javax.annotation.Nullable;

import static com.santos.dev.UI.Activities.TabActivity.id_docuento;
import static com.santos.dev.Utils.Nodos.NODO_CURSOS;
import static com.santos.dev.Utils.Nodos.NODO_NOTAS;
import static com.santos.dev.Utils.Nodos.PARAMETRO_KEY;
import static com.santos.dev.Utils.Nodos.PARAMETRO_VALOR;


public class FormulasFragment extends Fragment {
    private static final String TAG = "FormulasFragment";
    public static final String NOTAS_DATABASE = "Notas";
    private RecyclerView mRecyclerViewHoirzaontal;
    private RecyclerView mRecyclerViewConverciones;
    private ArrayList<FormulaG> mFormulaGS;
    private ArrayList<Conversiones> mConversiones;
    //FirebaseMethods
    private RecyclerView recyclergenerico;

    //Variables
    private ArrayList<Notas> alumnos = new ArrayList<>();
    private DocumentSnapshot mLastQueriedDocument;
    private FirebaseMethods firebaseMethods;
    private AdaptadorNotas mAdaptadorNotas;
    private RotateLoading mRotateLoading;


    public FormulasFragment() {
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
        View view = inflater.inflate(R.layout.fragment_formulas, container, false);
        recyclergenerico = view.findViewById(R.id.recyclergenerico);
        mRotateLoading = view.findViewById(R.id.rotateloading);

        mRotateLoading.start();

        getAlumnos();
        initRecyclerView();
        /*Fraccion fraccion = new Fraccion(30, 1);
        Fraccion fraccion1 = new Fraccion(1, 180);
        TextView textView = view.findViewById(R.id.textview_title);
        //textView.setText(fraccion.Grados_a_gon().toString(2));
        mRecyclerViewHoirzaontal = view.findViewById(R.id.recycler_formulas_generales);
        mRecyclerViewConverciones = view.findViewById(R.id.recycler);
        mRecyclerViewHoirzaontal.setHasFixedSize(true);
        mRecyclerViewConverciones.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewHoirzaontal.setLayoutManager(linearLayoutManager);
        mRecyclerViewConverciones.setLayoutManager(new LinearLayoutManager(getContext()));
        getData();
        getInfo();
        AdaptadorFormulasG adaptadorFormulasG = new AdaptadorFormulasG(getContext(), mFormulaGS);
        AdapterFormulasN adapterFormulasN = new AdapterFormulasN(getContext(), mConversiones);
        mRecyclerViewHoirzaontal.setAdapter(adaptadorFormulasG);
        mRecyclerViewConverciones.setAdapter(adapterFormulasN);*/
        return view;
    }

    FirebaseFirestore db;
    CollectionReference notesCollectionRef;

    private void getAlumnos() {
        db = FirebaseFirestore.getInstance();

        notesCollectionRef = db.collection(NODO_CURSOS).document(id_docuento).collection(NODO_NOTAS);

        Query notesQuery = null;
        if (mLastQueriedDocument != null) {
            notesQuery = notesCollectionRef
                    .whereEqualTo("key", "1")
                    .startAfter(mLastQueriedDocument);
        } else {
            notesQuery = notesCollectionRef
                    .whereEqualTo("key", "1");
        }


        notesQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    //mImageViewNoHayTasks.setVisibility(View.GONE);
                    //mTextViewNoHayTasks.setVisibility(View.GONE);

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Notas alumno = document.toObject(Notas.class);
                        alumnos.add(alumno);
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
            mAdaptadorNotas = new AdaptadorNotas(getContext(), alumnos);
        }

        recyclergenerico.setLayoutManager(new LinearLayoutManager(getContext()));


        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);

        recyclergenerico.setAdapter(mAdaptadorNotas);
    }

    private void getInfo() {
        mConversiones = new ArrayList<>();
        mConversiones.add(new Conversiones("π radianes", "180°", R.drawable.radianes));
        mConversiones.add(new Conversiones("Revoluciones", "180°", R.drawable.rev));
        mConversiones.add(new Conversiones("Gon", "180°", R.drawable.gons));

    }

    private void getData() {
        mFormulaGS = new ArrayList<>();
        mFormulaGS.add(new FormulaG("π radianes", "π radianes"));
        mFormulaGS.add(new FormulaG("Centigrados", "180°"));
        mFormulaGS.add(new FormulaG("GON", "200"));
    }

    @Override
    public void onResume() {
        super.onResume();
        db.collection(NODO_CURSOS).document(id_docuento).collection(NODO_NOTAS)
                .whereEqualTo(PARAMETRO_KEY, PARAMETRO_VALOR)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d(TAG, "onEvent: llego");
                            return;
                        }


                        if (alumnos.size() == 0) {
                            alumnos.clear();
                        } else {
                            alumnos.clear();
                            for (QueryDocumentSnapshot doc : value) {
                                Notas grado = doc.toObject(Notas.class);
                                alumnos.add(grado);
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
