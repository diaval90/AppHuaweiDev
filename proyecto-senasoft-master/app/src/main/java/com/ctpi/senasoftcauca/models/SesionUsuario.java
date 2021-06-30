package com.ctpi.senasoftcauca.models;

public class SesionUsuario {
    private static SesionUsuario instance;
    public Usuario usuario = new Usuario();

    public SesionUsuario() {
    }

    public static synchronized SesionUsuario getInstance() {
        if (instance == null) {
            instance = new SesionUsuario();
        }
        return instance;
    }
}
