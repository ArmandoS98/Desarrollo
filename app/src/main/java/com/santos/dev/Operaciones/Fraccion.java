package com.santos.dev.Operaciones;

public class Fraccion {
    private int numerador;
    private int denominador;

    public Fraccion() {

    }

    public Fraccion(int numerador, int denominador) {
        this.numerador = numerador;
        this.denominador = denominador;
    }

    public int getNumerador() {
        return numerador;
    }

    public void setNumerador(int numerador) {
        this.numerador = numerador;
    }

    public int getDenominador() {
        return denominador;
    }

    public void setDenominador(int denominador) {
        this.denominador = denominador;
    }

    public Fraccion sumar(Fraccion fraccion) {
        Fraccion aux = new Fraccion(numerador * fraccion.denominador + denominador * fraccion.numerador, fraccion.denominador * denominador);
        //aux.simplificar();
        return aux;
    }

    //Grados
    public Fraccion Grados_a_Radianes(int numerador) {
        this.numerador = numerador;
        Fraccion aux = new Fraccion(numerador * 1, 180 * 1);
        aux.mostrarFraccionSimplificada();
        return aux;
    }

    public Fraccion Grados_a_revoluciones(int numerador) {
        this.numerador = numerador;
        Fraccion aux = new Fraccion(numerador * 1, 360 * 1);
        aux.mostrarFraccionSimplificada();
        return aux;
    }

    public Fraccion Grados_a_gon(int numerador) {
        this.numerador = numerador;
        Fraccion aux = new Fraccion(numerador * 200, 180 * 1);
        aux.mostrarFraccionSimplificada();
        return aux;
    }

    //Radianes
    public Fraccion radianes_a_Grados(int numerador, int denominador) {
        this.numerador = numerador;
        this.denominador = denominador;
        Fraccion aux = new Fraccion(numerador * 180, denominador * 1);
        aux.mostrarFraccionSimplificada();
        return aux;
       /* if (denominador != 0) {

        } else {
            Fraccion aux = new Fraccion(numerador * 180, 1 * 1);
            aux.mostrarFraccionSimplificada();
            return aux;
        }*/

    }
    /*
    *     public Fraccion Grados_a_gon(int numerador, int denominador) {
        this.numerador = numerador;
        this.denominador = denominador;
        if (denominador != 0) {
            Fraccion aux = new Fraccion(numerador * 200, 180 * denominador);
            aux.mostrarFraccionSimplificada();
            return aux;
        } else {
            Fraccion aux = new Fraccion(numerador * 200, 180 * 1);
            aux.mostrarFraccionSimplificada();
            return aux;
        }
    }

    * */

    void mostrarFraccionSimplificada() {
        System.out.println("Simplificando...");
        int cont = 2;
        while (cont <= Math.abs(numerador) && cont <= Math.abs(denominador)) {
            if (numerador % cont == 0 && denominador % cont == 0) {
                numerador = numerador / cont;
                denominador = denominador / cont;
                System.out.println(toString());
            } else
                cont++;

            System.out.println("Procesos: " + cont);
        }
        System.out.println("Fin");
    }

    public String toString(int i) {
        String cadena = "nulo";
        switch (i) {
            case 0:
                cadena = numerador + "/" + denominador + " π radianes";
                break;
            case 1:
                cadena = numerador + "/" + denominador + " rev.";
                break;
            case 2:
                cadena = numerador / denominador + " gon.";
                break;
            case 3:
                cadena = numerador / denominador + " grados.";
                break;
        }
        return cadena;
    }
}
