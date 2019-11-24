package com.example.gas_app;

public class Operacao {

    float a = 0;
    float b = 0;
    float resultado;

    public float getA() {
        return a;
    }

    public void setA(float a) {
        this.a = a;
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = b;
    }

    public float getResultado() {
        return resultado;
    }

    public void setResultado(float resultado) {
        this.resultado = resultado;
    }

    public float divide(){
        resultado = getA() / getB();
        return resultado;
    }

}

