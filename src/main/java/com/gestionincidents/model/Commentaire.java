package com.gestionincidents.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Commentaire {

    private int id;
    private Integer commentaireParentId; // Ajout de l'attribut
    private String contenu;
    private Date date;
    private Utilisateur auteur; // Relation avec Utilisateur
    private Incident incident; // Relation avec Incident
    private List<Fichier> fichiers; // Ajout de la liste des fichiers

    // Constructeurs

    public Commentaire() {
        this.fichiers = new ArrayList<>(); // Initialisation de la liste des fichiers
    }

    public Commentaire(String contenu, Date date, Utilisateur auteur, Incident incident) {
        this.contenu = contenu;
        this.date = date;
        this.auteur = auteur;
        this.incident = incident;
        this.fichiers = new ArrayList<>(); // Initialisation de la liste des fichiers
    }

    // Getters et setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    public List<Fichier> getFichiers() {
        return fichiers;
    }

    public void setFichiers(List<Fichier> fichiers) {
        this.fichiers = fichiers;
    }

    // Méthodes pour les fichiers

    public void ajouterFichier(Fichier fichier) {
        this.fichiers.add(fichier);
    }

    public void supprimerFichier(Fichier fichier) {
        this.fichiers.remove(fichier);
    }
    
    public Integer getCommentaireParentId() {
        return commentaireParentId;
    }

    public void setCommentaireParentId(Integer commentaireParentId) {
        this.commentaireParentId = commentaireParentId;
    }

}