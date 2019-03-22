package com.santos.dev.Utils.SQLiteFukes;

import android.os.Parcel;
import android.os.Parcelable;

public class Horario implements Parcelable {
    private String dia;
    private String nombre;
    private String descripcion;
    private String de_a;

    public Horario() {
    }

    public Horario(String dia, String nombre, String descripcion, String de_a) {
        this.dia = dia;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.de_a = de_a;
    }

    protected Horario(Parcel in) {
        dia = in.readString();
        nombre = in.readString();
        descripcion = in.readString();
        de_a = in.readString();
    }

    public static final Creator<Horario> CREATOR = new Creator<Horario>() {
        @Override
        public Horario createFromParcel(Parcel in) {
            return new Horario(in);
        }

        @Override
        public Horario[] newArray(int size) {
            return new Horario[size];
        }
    };

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDe_a() {
        return de_a;
    }

    public void setDe_a(String de_a) {
        this.de_a = de_a;
    }

    @Override
    public String toString() {
        return "Horario{" +
                "dia='" + dia + '\'' +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", de_a='" + de_a + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dia);
        dest.writeString(nombre);
        dest.writeString(descripcion);
        dest.writeString(de_a);
    }
}
