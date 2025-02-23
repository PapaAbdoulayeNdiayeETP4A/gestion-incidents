package com.gestionincidents.model;

import java.util.List;

public class Equipe {

    private int id;
    private String nom;
    private List<Developpeur> membres; // Ajout de la liste des membres (Developpeurs)
    private List<Application> applications; // Ajout de la liste des applications

    public Equipe() {
    }

    public Equipe(String nom) {
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

    public List<Developpeur> getMembres() {
        return membres;
    }

    public void setMembres(List<Developpeur> membres) {
        this.membres = membres;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

}