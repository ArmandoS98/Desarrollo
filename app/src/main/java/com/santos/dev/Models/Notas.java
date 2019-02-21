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
    private String idNota;
    private String key;
    private String userName;
    private String userPhoto;
    private String userEmail;
    private String id_user_settings;

    public Notas() {
    }

    public Notas(String tituloNota, String descripcionNota, Date timestamp, String nombreTemaNota, String url_foto, String idNota, String key, String userName, String userPhoto, String userEmail, String id_user_settings) {
        this.tituloNota = tituloNota;
        this.descripcionNota = descripcionNota;
        this.timestamp = timestamp;
        this.nombreTemaNota = nombreTemaNota;
        this.url_foto = url_foto;
        this.idNota = idNota;
        this.key = key;
        this.userName = userName;
        this.userPhoto = userPhoto;
        this.userEmail = userEmail;
        this.id_user_settings = id_user_settings;
    }

    protected Notas(Parcel in) {
        tituloNota = in.readString();
        descripcionNota = in.readString();
        nombreTemaNota = in.readString();
        url_foto = in.readString();
        idNota = in.readString();
        key = in.readString();
        userName = in.readString();
        userPhoto = in.readString();
        userEmail = in.readString();
        id_user_settings = in.readString();
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

    public String getIdNota() {
        return idNota;
    }

    public void setIdNota(String idNota) {
        this.idNota = idNota;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getId_user_settings() {
        return id_user_settings;
    }

    public void setId_user_settings(String id_user_settings) {
        this.id_user_settings = id_user_settings;
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
        dest.writeString(idNota);
        dest.writeString(key);
        dest.writeString(userName);
        dest.writeString(userPhoto);
        dest.writeString(userEmail);
        dest.writeString(id_user_settings);
    }

    @Override
    public String toString() {
        return "Notas{" +
                "tituloNota='" + tituloNota + '\'' +
                ", descripcionNota='" + descripcionNota + '\'' +
                ", timestamp=" + timestamp +
                ", nombreTemaNota='" + nombreTemaNota + '\'' +
                ", url_foto='" + url_foto + '\'' +
                ", idNota='" + idNota + '\'' +
                ", key='" + key + '\'' +
                ", userName='" + userName + '\'' +
                ", userPhoto='" + userPhoto + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", id_user_settings='" + id_user_settings + '\'' +
                '}';
    }
}
