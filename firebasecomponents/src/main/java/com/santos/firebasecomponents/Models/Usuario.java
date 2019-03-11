package com.santos.firebasecomponents.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class Usuario implements Parcelable {
    private String id_usuario;
    private String nombre;
    private String telefono;
    private String url_perfil;
    private String correo;
    private @ServerTimestamp Date timestamp;

    public Usuario() {
    }

    public Usuario(String id_usuario, String nombre, String telefono, String url_perfil, String correo, Date timestamp) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.telefono = telefono;
        this.url_perfil = url_perfil;
        this.correo = correo;
        this.timestamp = timestamp;
    }

    protected Usuario(Parcel in) {
        id_usuario = in.readString();
        nombre = in.readString();
        telefono = in.readString();
        url_perfil = in.readString();
        correo = in.readString();
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUrl_perfil() {
        return url_perfil;
    }

    public void setUrl_perfil(String url_perfil) {
        this.url_perfil = url_perfil;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id_usuario='" + id_usuario + '\'' +
                ", nombre='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' +
                ", url_perfil='" + url_perfil + '\'' +
                ", correo='" + correo + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_usuario);
        dest.writeString(nombre);
        dest.writeString(telefono);
        dest.writeString(url_perfil);
        dest.writeString(correo);
    }
}
