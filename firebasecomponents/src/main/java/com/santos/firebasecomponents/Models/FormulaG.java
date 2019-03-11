package com.santos.firebasecomponents.Models;

public class FormulaG {
    private String titulo;
    private String imagen;

    public FormulaG(String titulo, String imagen) {
        this.titulo = titulo;
        this.imagen = imagen;
    }

    public FormulaG() {

    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen( String imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "FormulaG{" +
                "titulo='" + titulo + '\'' +
                ", imagen=" + imagen +
                '}';
    }
}
