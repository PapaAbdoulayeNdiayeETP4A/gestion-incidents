package com.gestionincidents.model;

public class Rapporteur extends Utilisateur {

    private String service;
    private String numMatricule;

    public Rapporteur() {
        super();
    }

    public Rapporteur(int id, String nom, String email, String motDePasse, String service, String numMatricule, String role) {
        super(id, nom, email, motDePasse, role);
        this.service = service;
        this.numMatricule = numMatricule;
    }

    // Getters et setters pour les attributs spécifiques

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getNumMatricule() {
        return numMatricule;
    }

    public void setNumMatricule(String numMatricule) {
        this.numMatricule = numMatricule;
    }
}