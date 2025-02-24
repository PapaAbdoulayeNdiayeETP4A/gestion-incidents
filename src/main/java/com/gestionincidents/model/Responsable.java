package com.gestionincidents.model;

public class Responsable extends Utilisateur {

    private String departement;

    public Responsable() {
        super();
    }

    public Responsable(int id, String nom, String email, String motDePasse, String departement, String role) {
        super(id, nom, email, motDePasse, role);
        this.departement = departement;
    }

    // Getters et setters pour les attributs spécifiques

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }
}