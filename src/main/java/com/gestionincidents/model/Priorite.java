package com.gestionincidents.model;

public enum Priorite {

    FAIBLE("Faible"),
    MOYENNE("Moyenne"),
    ELEVEE("Élevée"),
    URGENTE("Urgente");

    private String libelle;

    Priorite(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    @Override
    public String toString() {
        return libelle; // Pour afficher le libellé dans l'interface utilisateur
    }

}