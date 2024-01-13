package com.example.myapplication;

public class Usuario {

    private String usuario;
    private String contrasena;

    private double saldo;

    public Usuario() {
        // Constructor vacío requerido para Firebase
    }

    public Usuario( String usuario, String contrasena, double saldo) {

        this.usuario = usuario;
        this.contrasena = contrasena;
        this.saldo = saldo;
    }

    // Métodos getter y setter para acceder a los campos privados


    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contrasena;
    }

    public void setContraseña(String contrasena) {
        this.contrasena = contrasena;
    }


    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
