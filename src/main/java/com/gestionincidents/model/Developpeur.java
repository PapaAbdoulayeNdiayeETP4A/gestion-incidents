package com.gestionincidents.model;

public class Developpeur extends Utilisateur {

    private String specialisation;
    private String niveau;
    private int anciennete;
    private int equipeId; // Modification: Utilisation d'un identifiant d'équipe au lieu de l'objet direct

    public Developpeur() {
        super();
    }

    public Developpeur(int id, String nom, String email, String motDePasse, String specialisation, String niveau, int anciennete, int equipeId, String role) {
        super(id, nom, email, motDePasse, role);
        this.specialisation = specialisation;
        this.niveau = niveau;
        this.anciennete = anciennete;
        this.equipeId = equipeId;
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

    public int getEquipeId() {
        return equipeId; // Retourne l'ID de l'équipe au lieu de l'objet équipe
    }

    public void setEquipeId(int equipeId) {
        this.equipeId = equipeId; // Définit l'ID de l'équipe au lieu de l'objet équipe
    }
}
