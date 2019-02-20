package com.santos.dev.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class Notas implements Parcelable {

    private String tituloNota;
    private String descripcionNota;
    private @ServerTimestamp Date timestamp;
    private String nombreTemaNota;
    private String url_foto;

    public Notas() {
    }

    public Notas(String tituloNota, String descripcionNota, Date timestamp, String nombreTemaNota, String url_foto) {
        this.tituloNota = tituloNota;
        this.descripcionNota = descripcionNota;
        this.timestamp = timestamp;
        this.nombreTemaNota = nombreTemaNota;
        this.url_foto = url_foto;
    }

    protected Notas(Parcel in) {
        tituloNota = in.readString();
        descripcionNota = in.readString();
        nombreTemaNota = in.readString();
        url_foto = in.readString();
    }

    public static final Creator<Notas> CREATOR = new Creator<Notas>() {
        @Override
        public Notas createFromParcel(Parcel in) {
            return new Notas(in);
        }

        @Override
        public Notas[] newArray(int size) {
            return new Notas[size];
        }
    };

    public String getTituloNota() {
        return tituloNota;
    }

    public void setTituloNota(String tituloNota) {
        this.tituloNota = tituloNota;
    }

    public String getDescripcionNota() {
        return descripcionNota;
    }

    public void setDescripcionNota(String descripcionNota) {
        this.descripcionNota = descripcionNota;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getNombreTemaNota() {
        return nombreTemaNota;
    }

    public void setNombreTemaNota(String nombreTemaNota) {
        this.nombreTemaNota = nombreTemaNota;
    }

    public String getUrl_foto() {
        return url_foto;
    }

    public void setUrl_foto(String url_foto) {
        this.url_foto = url_foto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tituloNota);
        dest.writeString(descripcionNota);
        dest.writeString(nombreTemaNota);
        dest.writeString(url_foto);
    }

    @Override
    public String toString() {
        return "Notas{" +
                "tituloNota='" + tituloNota + '\'' +
                ", descripcionNota='" + descripcionNota + '\'' +
                ", timestamp=" + timestamp +
                ", nombreTemaNota='" + nombreTemaNota + '\'' +
                ", url_foto='" + url_foto + '\'' +
                '}';
    }
}
