package com.santos.firebasecomponents.Models;

public class Semana {
    private String dia;
    private String curso;
    private String catedratico;
    private String salon;
    private String hora_de;
    private String hora_a;
    private int id;

    public Semana() {
    }

    public Semana(String dia, String curso, String catedratico, String salon, String hora_de, String hora_a, int id) {
        this.dia = dia;
        this.curso = curso;
        this.catedratico = catedratico;
        this.salon = salon;
        this.hora_de = hora_de;
        this.hora_a = hora_a;
        this.id = id;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getCatedratico() {
        return catedratico;
    }

    public void setCatedratico(String catedratico) {
        this.catedratico = catedratico;
    }

    public String getSalon() {
        return salon;
    }

    public void setSalon(String salon) {
        this.salon = salon;
    }

    public String getHora_de() {
        return hora_de;
    }

    public void setHora_de(String hora_de) {
        this.hora_de = hora_de;
    }

    public String getHora_a() {
        return hora_a;
    }

    public void setHora_a(String hora_a) {
        this.hora_a = hora_a;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Semana{" +
                "dia='" + dia + '\'' +
                ", curso='" + curso + '\'' +
                ", catedratico='" + catedratico + '\'' +
                ", salon='" + salon + '\'' +
                ", hora_de='" + hora_de + '\'' +
                ", hora_a='" + hora_a + '\'' +
                ", id=" + id +
                '}';
    }
}
