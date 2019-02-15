package com.santos.dev;

public class Conversiones {
    private String titulo;
    private String ejercicio;
    private int imagen;

    public Conversiones() {
    }

    public Conversiones(String titulo, String ejercicio, int imagen) {
        this.titulo = titulo;
        this.ejercicio = ejercicio;
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "Conversiones{" +
                "titulo='" + titulo + '\'' +
                ", ejercicio='" + ejercicio + '\'' +
                ", imagen=" + imagen +
                '}';
    }
}
