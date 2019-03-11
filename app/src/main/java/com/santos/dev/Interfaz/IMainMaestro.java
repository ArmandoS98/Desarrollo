package com.santos.dev.Interfaz;


import com.santos.firebasecomponents.Models.Cuestionario;
import com.santos.firebasecomponents.Models.Cursos;
import com.santos.firebasecomponents.Models.Notas;

public interface IMainMaestro {
    void onNotaSeleccionada(Notas notas);
    void onNotaUpdate(Notas notas);
    void onCursotoNotaa(Cursos cursos);

    void onNuevoCuestionario(String titulo, String content);
}
