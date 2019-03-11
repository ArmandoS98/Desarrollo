package com.santos.firebasecomponents.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class Cursos implements Parcelable {
    private String nombre_curso;
    private String descripcion_curso;
    private @ServerTimestamp Date timestamp;
    private String url_foto;
    private String id_curso;
    private String key;
    private String id_user_settings;

    public Cursos() {
    }

    public Cursos(String nombre_curso, String descripcion_curso, Date timestamp, String url_foto, String id_curso, String key, String id_user_settings) {
        this.nombre_curso = nombre_curso;
        this.descripcion_curso = descripcion_curso;
        this.timestamp = timestamp;
        this.url_foto = url_foto;
        this.id_curso = id_curso;
        this.key = key;
        this.id_user_settings = id_user_settings;
    }

    protected Cursos(Parcel in) {
        nombre_curso = in.readString();
        descripcion_curso = in.readString();
        url_foto = in.readString();
        id_curso = in.readString();
        key = in.readString();
        id_user_settings = in.readString();
    }

    public static final Creator<Cursos> CREATOR = new Creator<Cursos>() {
        @Override
        public Cursos createFromParcel(Parcel in) {
            return new Cursos(in);
        }

        @Override
        public Cursos[] newArray(int size) {
            return new Cursos[size];
        }
    };

    public String getNombre_curso() {
        return nombre_curso;
    }

    public void setNombre_curso(String nombre_curso) {
        this.nombre_curso = nombre_curso;
    }

    public String getDescripcion_curso() {
        return descripcion_curso;
    }

    public void setDescripcion_curso(String descripcion_curso) {
        this.descripcion_curso = descripcion_curso;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl_foto() {
        return url_foto;
    }

    public void setUrl_foto(String url_foto) {
        this.url_foto = url_foto;
    }

    public String getId_curso() {
        return id_curso;
    }

    public void setId_curso(String id_curso) {
        this.id_curso = id_curso;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId_user_settings() {
        return id_user_settings;
    }

    public void setId_user_settings(String id_user_settings) {
        this.id_user_settings = id_user_settings;
    }

    @Override
    public String toString() {
        return "Cursos{" +
                "nombre_curso='" + nombre_curso + '\'' +
                ", descripcion_curso='" + descripcion_curso + '\'' +
                ", timestamp=" + timestamp +
                ", url_foto='" + url_foto + '\'' +
                ", id_curso='" + id_curso + '\'' +
                ", key='" + key + '\'' +
                ", id_user_settings='" + id_user_settings + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre_curso);
        dest.writeString(descripcion_curso);
        dest.writeString(url_foto);
        dest.writeString(id_curso);
        dest.writeString(key);
        dest.writeString(id_user_settings);
    }
}
