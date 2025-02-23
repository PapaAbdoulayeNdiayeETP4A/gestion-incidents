package com.gestionincidents.model;

public class Responsable extends Utilisateur {

    private String departement;

    public Responsable() {
        super();
    }

    public Responsable(String nom, String email, String role, String departement, String motDePasse) {
        super(nom, email, role, motDePasse);
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