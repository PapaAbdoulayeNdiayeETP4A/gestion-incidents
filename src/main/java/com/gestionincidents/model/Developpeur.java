package com.gestionincidents.model;

public class Developpeur extends Utilisateur {

    private String specialisation;
    private String niveau;
    private int anciennete;
    private Equipe equipe; // Relation avec Equipe

    public Developpeur() {
        super();
    }

    public Developpeur(int id, String nom, String email, String motDePasse, String specialisation, String niveau, int anciennete, Equipe equipe, String role) {
        super(id, nom, email, motDePasse, role);
        this.specialisation = specialisation;
        this.niveau = niveau;
        this.anciennete = anciennete;
        this.equipe = equipe;
    }

    // Getters et setters pour les attributs spécifiques

    public String getSpecialisation() {
        return specialisation;
    }

    public void setSpecialisation(String specialisation) {
        this.specialisation = specialisation;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public int getAnciennete() {
        return anciennete;
    }

    public void setAnciennete(int anciennete) {
        this.anciennete = anciennete;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }
}