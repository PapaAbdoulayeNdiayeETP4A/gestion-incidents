package com.gestionincidents.model;

import java.time.LocalDateTime;

public abstract class Fichier {

    private int id;
    private String nom;
    private LocalDateTime dateUpload; // LocalDateTime pour correspondre à DATETIME
    private Utilisateur uploader;
    private Incident incident;

    public Fichier() {
    }

    public Fichier(String nom, LocalDateTime dateUpload, Utilisateur uploader, Incident incident) {
        this.nom = nom;
        this.dateUpload = dateUpload;
        this.uploader = uploader;
        this.incident = incident;
    }

    // Getters et setters pour les attributs communs

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

    public LocalDateTime getDateUpload() {
        return dateUpload;
    }

    public void setDateUpload(LocalDateTime dateUpload) {
        this.dateUpload = dateUpload;
    }

    public Utilisateur getUploader() {
        return uploader;
    }

    public void setUploader(Utilisateur uploader) {
        this.uploader = uploader;
    }

    public Incident getIncident() {
        return incident;
    }

    public void setIncident(Incident incident) {
        this.incident = incident;
    }

    // Méthodes abstraites (si nécessaire)

}