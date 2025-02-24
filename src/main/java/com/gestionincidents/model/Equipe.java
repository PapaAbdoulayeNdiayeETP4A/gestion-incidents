package com.gestionincidents.model;

public class Equipe {

    private int id;
    private String nom;
    // Gestion des membres et applications commentée car gérée dans le DAO/Controller.
    // private List<Developpeur> membres;
    // private List<Application> applications;

    public Equipe() {
    }

    public Equipe(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    // Getters et setters pour les attributs

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    // Gestion des membres et applications commentée car gérée dans le DAO/Controller.
    // public List<Developpeur> getMembres() {
    //     return membres;
    // }
    //
    // public void setMembres(List<Developpeur> membres) {
    //     this.membres = membres;
    // }
    //
    // public List<Application> getApplications() {
    //     return applications;
    // }
    //
    // public void setApplications(List<Application> applications) {
    //     this.applications = applications;
    // }
}