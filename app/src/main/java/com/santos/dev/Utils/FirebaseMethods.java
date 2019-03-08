package com.santos.dev.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.santos.dev.Models.ArchivosAniadidos;
import com.santos.dev.Models.Cuestionario;
import com.santos.dev.Models.Cursos;
import com.santos.dev.Models.Notas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.santos.dev.Utils.Nodos.NODO_CUESTIONARIO;
import static com.santos.dev.Utils.Nodos.NODO_CURSOS;
import static com.santos.dev.Utils.Nodos.NODO_IMAGENES_ANIADIDAS;
import static com.santos.dev.Utils.Nodos.NODO_NOTAS;

public class FirebaseMethods {
    private static final String TAG = "FirebaseMethods";
    public static final String FOTO = "https://firebasestorage.googleapis.com/v0/b/school-it-fd8a7.appspot.com/o/fotos%20perfil%2Ffacebook-avatar.jpg?alt=media&token=af814dda-f2c9-438a-8a73-bc1c77e1142a";
    public static final String FRASE = "No importa cuántas veces falles, sólo debes de estar en lo correcto una vez. Entonces todos te llamarán un éxito de la noche a la mañana y te dirán lo afortunado que eres";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore db; /*= FirebaseFirestore.getInstance();*/
    private DocumentReference newNoteRef; /*= db.collection("Alumnos").document();*/
    private DocumentSnapshot mLastQueriedDocument;

    private String userID;
    private Context mContext;

    public FirebaseMethods(Context context, String... args) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        //args(0) = nodo
        //args(1) = id documetno o para este caso nodo de cursos
        //args(2) = nodo de la subcollecion
        newNoteRef = db.collection(args[0]).document(args[1]).collection(args[2]).document();
        mContext = context;

        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }

    }

    public FirebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mContext = context;

        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    public FirebaseMethods(Context context, String nodo) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        newNoteRef = db.collection(nodo).document();
        mContext = context;

        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    /**
     * Con este metodo registramos a un nuevo maestro y/o alumno
     *
     * @param email
     * @param contrasenia
     */
    public void registrarNuevoEmail(String email, String contrasenia) {
        mAuth.createUserWithEmailAndPassword(email, contrasenia)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            userID = user.getUid();
                            //Toast.makeText(mContext, "Authstate change: " + userID, Toast.LENGTH_SHORT).show();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    //Todo: Incerciones en la DB

    public void nuevoCurso(String... datos) {
        Cursos cursos = new Cursos();
        cursos.setNombre_curso(datos[0]);
        cursos.setDescripcion_curso(datos[1]);
        cursos.setUrl_foto(datos[2]);
        cursos.setId_curso(newNoteRef.getId());
        cursos.setKey(datos[3]);
        cursos.setId_user_settings(datos[4]);

        newNoteRef.set(cursos).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Toast.makeText(mContext, "Curso Creado", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void nuevaNota(String... datos) {

        //newNoteRef = db.collection(nodo).document("hi").collection("maiz").document();

        Notas nota = new Notas();
        nota.setTituloNota(datos[0]);
        nota.setDescripcionNota(datos[1]);
        nota.setNombreTemaNota(datos[2]);
        nota.setUrl_foto(datos[6]);
        nota.setIdNota(newNoteRef.getId());
        nota.setKey("1");
        nota.setUserName(datos[3]);
        nota.setUserPhoto(datos[4]);
        nota.setUserEmail(datos[5]);
        nota.setId_user_settings(datos[7]);

        newNoteRef.set(nota).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(mContext, "Nota Creada", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(mContext, "Error al crear la njota", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void nuevoCuestionario(String... datos) {
        //datos(0) = id del curso
        //datos(1) = id de la tarea
        //datos(2) = pregunta
        //datos(3) = respuesta
        newNoteRef = db.collection(NODO_CURSOS).document(datos[0]).collection(NODO_NOTAS).document(datos[1]).collection(NODO_CUESTIONARIO).document();

        Cuestionario cuestionario = new Cuestionario();
        cuestionario.setId_cuestionario(newNoteRef.getId());
        cuestionario.setId_nota(datos[1]);
        cuestionario.setPregunta(datos[2]);
        cuestionario.setRespuesta_txt(datos[3]);

        newNoteRef.set(cuestionario).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(mContext, "Cuestionario Creado", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(mContext, "Error al crear la njota", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * @param datos
     */
    public void nuevoArchivo(String... datos) {
        //datos(0) = id del nota
        //datos(1) = url
        //datos(2) = descripcion
        //datos(3) = id curso
        newNoteRef = db
                .collection(NODO_CURSOS)
                .document(datos[3])
                .collection(NODO_NOTAS)
                .document(datos[0])
                .collection(NODO_IMAGENES_ANIADIDAS)
                .document();

        ArchivosAniadidos archivosAniadidos = new ArchivosAniadidos();
        archivosAniadidos.setId_image(newNoteRef.getId());
        archivosAniadidos.setId_nota(datos[0]);
        archivosAniadidos.setUrl(datos[1]);
        archivosAniadidos.setDescripcion(datos[2]);


        newNoteRef.set(archivosAniadidos).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(mContext, "Archivo creado", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(mContext, "Error al crear la njota", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*public void addNewMaestro(final String correo, String colegio_ID, String nombres, String apellidos, String edad, String DPI, String telefono, final String grado_asignado) {

        Usuario usuario = new Usuario(
                correo,
                FOTO, FRASE,
                colegio_ID,
                newNoteRef.getId(),
                userID,
                nombres,
                apellidos,
                edad,
                DPI,
                telefono,
                true,
                true,
                true);

        newNoteRef.set(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(mContext, "Maestro Agregado con exito!", Toast.LENGTH_SHORT).show();
                    DocumentReference nuevaAsignacion = db.collection("Asignacion").document();

                    Asignacion asignacion = new Asignacion();

                    asignacion.setId_asignacion(nuevaAsignacion.getId());
                    asignacion.setId_grado(grado_asignado);
                    asignacion.setId_maestro(newNoteRef.getId());

                    nuevaAsignacion.set(asignacion).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                            }
                        }
                    });
                } else
                    Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();


            }
        });
    }

    public void addNewGrado(String titulo, String seccion, String colegio_ID, List<String> materias) {
        Note grados = new Note();
        grados.setTitle(titulo);
        grados.setContent(seccion);
        grados.setMaterias(materias);
        grados.setGrado_id(newNoteRef.getId());
        grados.setId_colegio(colegio_ID);

        newNoteRef.set(grados).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(mContext, "Grado creado!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Error al momento de crear!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void addGrado(String grado_ID, String maestro_ID) {

        Asignacion asignacion = new Asignacion();

        asignacion.setId_asignacion(newNoteRef.getId());
        asignacion.setId_grado(grado_ID);
        asignacion.setId_maestro(maestro_ID);

        newNoteRef.set(asignacion).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(mContext, "Grado Asignado!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void addGradoAlumno(String alumno_ID, String grado_ID) {

        AsignacionGradoAlumno asignacionGradoAlumno = new AsignacionGradoAlumno();
        asignacionGradoAlumno.setId_asignacion(newNoteRef.getId());
        asignacionGradoAlumno.setId_alumno(alumno_ID);
        asignacionGradoAlumno.setId_grado(grado_ID);

        newNoteRef.set(asignacionGradoAlumno).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(mContext, "Grado Asignado!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void addAvisoTodosLosGrados(String colegio_ID, final String titutlo, final String contenido, final String usuario_ID, final String nombre_creador, final String fotografia) {

        CollectionReference notesCollectionRef = db.collection("Grados");

        Query notesQuery = null;
        if (mLastQueriedDocument != null) {
            notesQuery = notesCollectionRef
                    .whereEqualTo("id_colegio", colegio_ID)
                    .startAfter(mLastQueriedDocument);
        } else {
            notesQuery = notesCollectionRef
                    .whereEqualTo("id_colegio", colegio_ID);
        }

        notesQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                        Note grados = snapshot.toObject(Note.class);

                        newNoteRef = db.collection("Avisos").document();

                        Avisos avisosC = new Avisos();
                        avisosC.setTitulo(titutlo); //titulo
                        avisosC.setContent(contenido); //content
                        avisosC.setAviso_id(newNoteRef.getId()); //aviso_id
                        avisosC.setId_usuario(usuario_ID); //id_usuario
                        avisosC.setId_grado(grados.getGrado_id()); //id_grado
                        avisosC.setNombre_creador(nombre_creador); //nombre_creador
                        avisosC.setProfile_url(fotografia); //foto de perfil

                        newNoteRef.set(avisosC).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(mContext, "Aviso Creado", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, "Error al crear el Aviso!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }
            }
        });
    }

    public void addAviso(String titulo, String contenido, String usuario_ID, String grado_ID, String nombre_usuario, String foto_url_usuario) {

        final Avisos avisosC = new Avisos();
        avisosC.setTitulo(titulo); //titulo
        avisosC.setContent(contenido); //content
        avisosC.setAviso_id(newNoteRef.getId()); //aviso_id
        avisosC.setId_usuario(usuario_ID); //id_usuario
        avisosC.setId_grado(grado_ID); //id_grado
        avisosC.setNombre_creador(nombre_usuario); //nombre_creador
        avisosC.setProfile_url(foto_url_usuario); //foto de perfil

        newNoteRef.set(avisosC).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(mContext, "Aviso Creado!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "No se pudo crear", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addTarea(String titulo, String descripcion, String admin_ID, String grado_ID, String fecha_inicial, String materia) {
        final Tarea actividadesC = new Tarea();
        actividadesC.setTitle(titulo);
        actividadesC.setContent(descripcion);
        actividadesC.setTarea_id(newNoteRef.getId());
        actividadesC.setId_admin(admin_ID);
        actividadesC.setId_grado(grado_ID);
        actividadesC.setTimestamp_end(getDateFromString(fecha_inicial));
        actividadesC.setMateria(materia);

        /**
         * con el metodo onComplete verificamos que la informacion se haya enviada correctamente a la DB.
         */
       /* newNoteRef.set(actividadesC).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(mContext, "Tarea Creada!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Error al crear tarea!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //tODO: UPDATE
    public void updateUserProfile(String user_ID, String nombres, String apellidos, String edad, String telefono, String correo){
        newNoteRef = db.collection(Utilidades.OWNER)
                .document(user_ID);

        newNoteRef.update(
                "nombres", nombres,
                "apellidos", apellidos,
                "edad", edad,
                "telefono", telefono,
                "correo", correo
        ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(mContext, "Informacion Actualizada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Error al momento de Actualizar!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateUserProfileColegio(String colegio_ID, String nombres, String direccion, String horarios, String telefono, String mision, String vision){
        newNoteRef = db.collection(Utilidades.COLEGIO)
                .document(colegio_ID);

        newNoteRef.update(
                "nombre", nombres,
                "direccion", direccion,
                "horario", horarios,
                "telefono", telefono,
                "mision", mision,
                "vision", vision
        ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(mContext, "Informacion Actualizada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Error al momento de Actualizar!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Todo: Eliminacion de informacion de la DB
    /*
     * Con este metodo de nuestra interfaz se ejecuta elimina nuestra tarea por medio del ID de cada una
     */
  /*  public void deleteGrado(String coleccion, final String documento_ID) {
        newNoteRef = db.collection(coleccion)
                .document(documento_ID);

        newNoteRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    CollectionReference colec = db.collection("Asignacion");

                    Query notesQuery = null;
                    if (mLastQueriedDocument != null) {
                        notesQuery = colec
                                .whereEqualTo("id_grado", documento_ID)
                                .startAfter(mLastQueriedDocument);
                    } else {
                        notesQuery = colec
                                .whereEqualTo("id_grado", documento_ID);
                    }

                    notesQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                    Asignacion asignacion = snapshot.toObject(Asignacion.class);

                                    newNoteRef = db.collection("Asignacion")
                                            .document(asignacion.id_asignacion);

                                    newNoteRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(mContext, "Eliminado!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                                CollectionReference colec = db.collection("Asignacion_Grado_Alumno");

                                Query alumnosQuery = null;
                                if (mLastQueriedDocument != null) {
                                    alumnosQuery = colec
                                            .whereEqualTo("id_grado", documento_ID)
                                            .startAfter(mLastQueriedDocument);
                                } else {
                                    alumnosQuery = colec
                                            .whereEqualTo("id_grado", documento_ID);
                                }

                                alumnosQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                                AsignacionGradoAlumno asignacionGradoAlumno = snapshot.toObject(AsignacionGradoAlumno.class);

                                                newNoteRef = db.collection("Asignacion_Grado_Alumno")
                                                        .document(asignacionGradoAlumno.getId_asignacion());

                                                newNoteRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(mContext, "Eliminado!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            CollectionReference colec = db.collection(Utilidades.AVISO);

                                            Query alumnosQuery = null;
                                            if (mLastQueriedDocument != null) {
                                                alumnosQuery = colec
                                                        .whereEqualTo("id_grado", documento_ID)
                                                        .startAfter(mLastQueriedDocument);
                                            } else {
                                                alumnosQuery = colec
                                                        .whereEqualTo("id_grado", documento_ID);
                                            }

                                            alumnosQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                                            Avisos avisos = snapshot.toObject(Avisos.class);

                                                            newNoteRef = db.collection(Utilidades.AVISO)
                                                                    .document(avisos.getAviso_id());

                                                            newNoteRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    Toast.makeText(mContext, "Eliminado!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        }

                                                        //Tareas
                                                        CollectionReference colec = db.collection(Utilidades.TAREA);

                                                        Query alumnosQuery = null;
                                                        if (mLastQueriedDocument != null) {
                                                            alumnosQuery = colec
                                                                    .whereEqualTo("id_grado", documento_ID)
                                                                    .startAfter(mLastQueriedDocument);
                                                        } else {
                                                            alumnosQuery = colec
                                                                    .whereEqualTo("id_grado", documento_ID);
                                                        }

                                                        alumnosQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                                                        Tarea tarea = snapshot.toObject(Tarea.class);

                                                                        newNoteRef = db.collection(Utilidades.TAREA)
                                                                                .document(tarea.getTarea_id());

                                                                        newNoteRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                Toast.makeText(mContext, "Eliminado!", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                                    }

                                                                    //Diario
                                                                    CollectionReference colec = db.collection(Utilidades.DIARIO);

                                                                    Query alumnosQuery = null;
                                                                    if (mLastQueriedDocument != null) {
                                                                        alumnosQuery = colec
                                                                                .whereEqualTo(Utilidades.DIARIO_GRADO_ID, documento_ID)
                                                                                .startAfter(mLastQueriedDocument);
                                                                    } else {
                                                                        alumnosQuery = colec
                                                                                .whereEqualTo(Utilidades.DIARIO_GRADO_ID, documento_ID);
                                                                    }

                                                                    alumnosQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                                                                    Diario diario = snapshot.toObject(Diario.class);

                                                                                    newNoteRef = db.collection(Utilidades.DIARIO)
                                                                                            .document(diario.getId_diario());

                                                                                    newNoteRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            Toast.makeText(mContext, "Eliminado!", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    });
                                                                                }
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });

                                        }
                                    }
                                });

                            }
                        }
                    });
                    Toast.makeText(mContext, "Eliminando...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Error al Eliminar!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void deleteActividades(String coleccion, final String documento_ID) {
        newNoteRef = db.collection(coleccion)
                .document(documento_ID);

        newNoteRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(mContext, "Eliminado!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Error al Eliminar!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void deleteMaestro(String coleccion, final String documento_ID) {
        newNoteRef = db.collection(coleccion)
                .document(documento_ID);

        newNoteRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    CollectionReference colec = db.collection("Asignacion");

                    Query notesQuery = null;
                    if (mLastQueriedDocument != null) {
                        notesQuery = colec
                                .whereEqualTo("id_maestro", documento_ID)
                                .startAfter(mLastQueriedDocument);
                    } else {
                        notesQuery = colec
                                .whereEqualTo("id_maestro", documento_ID);
                    }

                    notesQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                    Asignacion asignacion = snapshot.toObject(Asignacion.class);

                                    newNoteRef = db.collection("Asignacion")
                                            .document(asignacion.id_asignacion);

                                    newNoteRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                //Todo: se eliminan las asignaciones
                                                Toast.makeText(mContext, "Eliminado!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                                CollectionReference referenceTarea = db.collection("Tarea");

                                Query tareaQuery = null;
                                if (mLastQueriedDocument != null) {
                                    tareaQuery = referenceTarea
                                            .whereEqualTo("id_admin", documento_ID)
                                            .startAfter(mLastQueriedDocument);
                                } else {
                                    tareaQuery = referenceTarea
                                            .whereEqualTo("id_admin", documento_ID);
                                }

                                tareaQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                                Tarea tarea = snapshot.toObject(Tarea.class);

                                                newNoteRef = db.collection("Tarea")
                                                        .document(tarea.getTarea_id());

                                                newNoteRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(mContext, "Tareas Eliminadas!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }

                                            //Todo: Eliminar Avisos
                                            CollectionReference referenceAvisos = db.collection("Avisos");

                                            Query avisos = null;
                                            if (mLastQueriedDocument != null) {
                                                avisos = referenceAvisos
                                                        .whereEqualTo("id_usuario", documento_ID)
                                                        .startAfter(mLastQueriedDocument);
                                            } else {
                                                avisos = referenceAvisos
                                                        .whereEqualTo("id_usuario", documento_ID);
                                            }

                                            avisos.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                                            Avisos avisos1 = snapshot.toObject(Avisos.class);

                                                            newNoteRef = db.collection("Avisos")
                                                                    .document(avisos1.getAviso_id());

                                                            newNoteRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    Toast.makeText(mContext, "Avisos Eliminados", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        }

                                                        //Diario
                                                        CollectionReference colec = db.collection(Utilidades.DIARIO);

                                                        Query alumnosQuery = null;
                                                        if (mLastQueriedDocument != null) {
                                                            alumnosQuery = colec
                                                                    .whereEqualTo("id_maestro", documento_ID)
                                                                    .startAfter(mLastQueriedDocument);
                                                        } else {
                                                            alumnosQuery = colec
                                                                    .whereEqualTo("id_maestro", documento_ID);
                                                        }

                                                        alumnosQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                                                        Diario diario = snapshot.toObject(Diario.class);

                                                                        newNoteRef = db.collection(Utilidades.DIARIO)
                                                                                .document(diario.getId_diario());

                                                                        newNoteRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                Toast.makeText(mContext, "Eliminado!", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });

                                        }

                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

    public void deleteAlumno(String coleccion, final String documento_ID) {
        newNoteRef = db.collection(coleccion)
                .document(documento_ID);

        newNoteRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(mContext, "Eliminando Avisos...", Toast.LENGTH_SHORT).show();
                    CollectionReference colec = db.collection("Avisos");

                    Query notesQuery = null;
                    if (mLastQueriedDocument != null) {
                        notesQuery = colec
                                .whereEqualTo("id_usuario", documento_ID)
                                .startAfter(mLastQueriedDocument);
                    } else {
                        notesQuery = colec
                                .whereEqualTo("id_usuario", documento_ID);
                    }

                    notesQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                    Avisos avisos = snapshot.toObject(Avisos.class);

                                    newNoteRef = db.collection("Avisos")
                                            .document(avisos.getAviso_id());

                                    newNoteRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                //Todo: se eliminan las asignaciones
                                                Toast.makeText(mContext, "Eliminado!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                                CollectionReference referenceTarea = db.collection("Asignacion_Grado_Alumno");

                                Query tareaQuery = null;
                                if (mLastQueriedDocument != null) {
                                    tareaQuery = referenceTarea
                                            .whereEqualTo("id_alumno", documento_ID)
                                            .startAfter(mLastQueriedDocument);
                                } else {
                                    tareaQuery = referenceTarea
                                            .whereEqualTo("id_alumno", documento_ID);
                                }

                                tareaQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                                AsignacionGradoAlumno asignacionGradoAlumno = snapshot.toObject(AsignacionGradoAlumno.class);

                                                newNoteRef = db.collection("Asignacion_Grado_Alumno")
                                                        .document(asignacionGradoAlumno.getId_asignacion());

                                                newNoteRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            //Todo: se eliminan las asignaciones
                                                            Toast.makeText(mContext, "Eliminado!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    });

                    Toast.makeText(mContext, "Eliminado con exito!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Error al Eliminar!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void deleteAsignacion(String documento_ID){
        CollectionReference referenceTarea = db.collection("Asignacion_Grado_Alumno");

        Query tareaQuery = null;
        if (mLastQueriedDocument != null) {
            tareaQuery = referenceTarea
                    .whereEqualTo("id_asignacion", documento_ID)
                    .startAfter(mLastQueriedDocument);
        } else {
            tareaQuery = referenceTarea
                    .whereEqualTo("id_asignacion", documento_ID);
        }

        tareaQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                        AsignacionGradoAlumno asignacionGradoAlumno = snapshot.toObject(AsignacionGradoAlumno.class);

                        newNoteRef = db.collection("Asignacion_Grado_Alumno")
                                .document(asignacionGradoAlumno.getId_asignacion());

                        newNoteRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                }
                            }
                        });
                    }
                }
            }
        });
    }

    //Todo: Actualizacion de la DB
    public void permisoTarea(boolean tareas, String identificador) {

        Usuario usuario = new Usuario();
        usuario.setPermiso_tarea(tareas);
        /*
        usuario.setPermiso_diario(diario);
        usuario.setPermiso_aviso(aviso);
*/
      /*  DocumentReference noteRef = db.collection("User")
                .document(identificador);

        noteRef.update(
                "permiso_tarea", usuario.getPermiso_tarea()
        ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                } else {
                    Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void permisoDiario(boolean diario, String identificador) {

        Usuario usuario = new Usuario();
        usuario.setPermiso_diario(diario);
        /*
        usuario.setPermiso_diario(diario);
        usuario.setPermiso_aviso(aviso);
*/
    /*    DocumentReference noteRef = db.collection("User")
                .document(identificador);

        noteRef.update(
                "permiso_diario", usuario.getPermiso_diario()
        ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                } else {
                    Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void permisoAviso(boolean aviso, String identificador) {

        Usuario usuario = new Usuario();
        usuario.setPermiso_aviso(aviso);
        /*
        usuario.setPermiso_diario(diario);
        usuario.setPermiso_aviso(aviso);
*/
   /*     DocumentReference noteRef = db.collection("User")
                .document(identificador);

        noteRef.update(
                "permiso_aviso", usuario.getPermiso_aviso()
        ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                } else {
                    Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*
     * Este metodo fue creada para poder manupular la fecha que se recoge del dataPicker del Dialog de crear
     * modificar tareas para su correcta incercion en la DB
     * su unico parametro es pasarle un dato tipo string de la fecha.
     */
    static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public Date getDateFromString(String datetoSaved) {

        try {
            Date date = format.parse(datetoSaved);
            return date;
        } catch (ParseException e) {
            return null;
        }

    }
}
