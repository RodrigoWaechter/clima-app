package com.unisc.projeto.clima_app.domain;

public class Preferencia {

    private Integer idPreferencia;
    private String cidadePreferida;
    private Tema temaApp;


    public Preferencia() {
    }

    public Preferencia(Integer idPreferencia, String cidadePreferida, Tema temaApp) {
        this.idPreferencia = idPreferencia;
        this.cidadePreferida = cidadePreferida;
        this.temaApp = temaApp;
    }

    public Integer getIdPreferencia() {
        return idPreferencia;
    }

    public void setIdPreferencia(Integer idPreferencia) {
        this.idPreferencia = idPreferencia;
    }

    public String getCidadePreferida() {
        return cidadePreferida;
    }

    public void setCidadePreferida(String cidadePreferida) {
        this.cidadePreferida = cidadePreferida;
    }

    public Tema getTemaApp() {
        return temaApp;
    }

    public void setTemaApp(Tema temaApp) {
        this.temaApp = temaApp;
    }
}