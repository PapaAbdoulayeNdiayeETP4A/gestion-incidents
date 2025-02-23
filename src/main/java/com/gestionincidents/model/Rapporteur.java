package com.gestionincidents.model;

public class Rapporteur extends Utilisateur {

    private String service;
    private String num_matricule;

    public Rapporteur() {
        super();
    }

    public Rapporteur(String nom, String email, String role, String service, String num_matricule, String motDePasse) {
        super(nom, email, role, motDePasse);
        this.service = service;
        this.num_matricule = num_matricule;
    }

    // Getters et setters pour les attributs spécifiques

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getNumMatricule() {
        return num_matricule;
    }

    public void setNumMatricule(String num_matricule) {
        this.num_matricule = num_matricule;
    }

}