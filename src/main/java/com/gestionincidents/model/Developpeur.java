package com.gestionincidents.model;

public class Developpeur extends Utilisateur {

    private String specialisation;
    private String niveau;
    private int anciennete;
    private Equipe equipe; // Ajout de l'attribut equipe

    public Developpeur() {
        super();
    }

    public Developpeur(String nom, String email, String role, String specialisation, String niveau, int anciennete, Equipe equipe, String motDePasse) {
        super(nom, email, role, motDePasse);
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