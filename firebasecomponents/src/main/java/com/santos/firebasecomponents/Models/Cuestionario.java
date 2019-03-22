package com.santos.firebasecomponents.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class Cuestionario implements Parcelable {
    private String id_cuestionario;
    private String id_nota;
    private String pregunta;
    private String respuesta_txt;
    private @ServerTimestamp Date timestamp;
    private String id_curso;

    public Cuestionario() {
    }

    public Cuestionario(String id_cuestionario, String id_nota, String pregunta, String respuesta_txt, Date timestamp, String id_curso) {
        this.id_cuestionario = id_cuestionario;
        this.id_nota = id_nota;
        this.pregunta = pregunta;
        this.respuesta_txt = respuesta_txt;
        this.timestamp = timestamp;
        this.id_curso = id_curso;
    }

    protected Cuestionario(Parcel in) {
        id_cuestionario = in.readString();
        id_nota = in.readString();
        pregunta = in.readString();
        respuesta_txt = in.readString();
        id_curso = in.readString();
    }

    public static final Creator<Cuestionario> CREATOR = new Creator<Cuestionario>() {
        @Override
        public Cuestionario createFromParcel(Parcel in) {
            return new Cuestionario(in);
        }

        @Override
        public Cuestionario[] newArray(int size) {
            return new Cuestionario[size];
        }
    };

    public String getId_cuestionario() {
        return id_cuestionario;
    }

    public void setId_cuestionario(String id_cuestionario) {
        this.id_cuestionario = id_cuestionario;
    }

    public String getId_nota() {
        return id_nota;
    }

    public void setId_nota(String id_nota) {
        this.id_nota = id_nota;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getRespuesta_txt() {
        return respuesta_txt;
    }

    public void setRespuesta_txt(String respuesta_txt) {
        this.respuesta_txt = respuesta_txt;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getId_curso() {
        return id_curso;
    }

    public void setId_curso(String id_curso) {
        this.id_curso = id_curso;
    }

    @Override
    public String toString() {
        return "Cuestionario{" +
                "id_cuestionario='" + id_cuestionario + '\'' +
                ", id_nota='" + id_nota + '\'' +
                ", pregunta='" + pregunta + '\'' +
                ", respuesta_txt='" + respuesta_txt + '\'' +
                ", timestamp=" + timestamp +
                ", id_curso='" + id_curso + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_cuestionario);
        dest.writeString(id_nota);
        dest.writeString(pregunta);
        dest.writeString(respuesta_txt);
        dest.writeString(id_curso);
    }
}
