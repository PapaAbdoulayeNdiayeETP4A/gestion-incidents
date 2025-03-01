package com.gestionincidents.model;

import java.time.LocalDateTime;

public class Commentaire {

    private int id;
    private Integer commentaireParentId;
    private String contenu;
    private LocalDateTime date; // LocalDateTime pour correspondre à DATETIME
    private Utilisateur auteur;
    private Incident incident;

    public Commentaire() {
    }

    public Commentaire(String contenu, LocalDateTime date, Utilisateur auteur, Incident incident) {
        this.contenu = contenu;
        this.date = date;
        this.auteur = auteur;
        this.incident = incident;
    }

    // Getters et setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getCommentaireParentId() {
        return commentaireParentId;
    }

    public void setCommentaireParentId(Integer commentaireParentId) {
        this.commentaireParentId = commentaireParentId;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Utilisateur getAuteur() {
        return auteur;
    }

    public void setAuteur(Utilisateur auteur) {
        this.auteur = auteur;
    }

    public Incident getIncident() {
        return incident;
    }

    public void setIncident(Incident incident) {
        this.incident = incident;
    }
}