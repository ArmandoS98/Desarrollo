package com.santos.dev.Interfaz;


import android.widget.ImageView;

import com.santos.dev.Models.Cuestionario;
import com.santos.dev.Models.Cursos;
import com.santos.dev.Models.Notas;

public interface IMainMaestro {
    void onNotaSeleccionada(Notas notas);
    void onNotaUpdate(Notas notas);
    void onCursotoNotaa(Cursos cursos);

    void onNuevoCuestionario(String titulo, String content);
}
