package com.santos.firebasecomponents.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class ArchivosAniadidos implements Parcelable {
    private String url;
    private String id_image;
    private String id_nota;
    private String descripcion;
    private @ServerTimestamp Date timestamp;

    public ArchivosAniadidos() {
    }

    public ArchivosAniadidos(String url, String id_image, String id_nota, String descripcion, Date timestamp) {
        this.url = url;
        this.id_image = id_image;
        this.id_nota = id_nota;
        this.descripcion = descripcion;
        this.timestamp = timestamp;
    }

    protected ArchivosAniadidos(Parcel in) {
        url = in.readString();
        id_image = in.readString();
        id_nota = in.readString();
        descripcion = in.readString();
    }

    public static final Creator<ArchivosAniadidos> CREATOR = new Creator<ArchivosAniadidos>() {
        @Override
        public ArchivosAniadidos createFromParcel(Parcel in) {
            return new ArchivosAniadidos(in);
        }

        @Override
        public ArchivosAniadidos[] newArray(int size) {
            return new ArchivosAniadidos[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId_image() {
        return id_image;
    }

    public void setId_image(String id_image) {
        this.id_image = id_image;
    }

    public String getId_nota() {
        return id_nota;
    }

    public void setId_nota(String id_nota) {
        this.id_nota = id_nota;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ArchivosAniadidos{" +
                "url='" + url + '\'' +
                ", id_image='" + id_image + '\'' +
                ", id_nota='" + id_nota + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(id_image);
        dest.writeString(id_nota);
        dest.writeString(descripcion);
    }
}
