package com.gestionincidents.model;

import java.util.List;

public class Equipe {

    private int id;
    private String nom;
    private String description;
    private int responsableId;
    private List<Utilisateur> membres;

    public Equipe() {
    }

    public Equipe(int id, String nom, String description, int responsableId) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.responsableId = responsableId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getResponsableId() {
        return responsableId;
    }

    public void setResponsableId(int responsableId) {
        this.responsableId = responsableId;
    }

    public List<Utilisateur> getMembres() {
        return membres;
    }

    public void setMembres(List<Utilisateur> membres) {
        this.membres = membres;
    }

}
