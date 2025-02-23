package com.gestionincidents.model;

import java.util.Date;

public class FichierImage extends Fichier {

    private byte[] contenu;
    private int largeur;
    private int hauteur;
    private String format;

    public FichierImage() {
        super();
    }

    public FichierImage(String nom, Date dateUpload, Utilisateur uploader, Incident incident, byte[] contenu, int largeur, int hauteur, String format) {
        super(nom, dateUpload, uploader, incident);
        this.contenu = contenu;
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.format = format;
    }

    // Getters et setters pour les attributs spécifiques

    public byte[] getContenu() {
        return contenu;
    }

    public void setContenu(byte[] contenu) {
        this.contenu = contenu;
    }

    public int getLargeur() {
        return largeur;
    }

    public void setLargeur(int largeur) {
        this.largeur = largeur;
    }

    public int getHauteur() {
        return hauteur;
    }

    public void setHauteur(int hauteur) {
        this.hauteur = hauteur;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

}