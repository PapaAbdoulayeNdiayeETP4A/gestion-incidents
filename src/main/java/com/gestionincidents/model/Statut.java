package com.gestionincidents.model;

public enum Statut {

    OUVERT("Ouvert"),
    EN_COURS("En cours"),
    RESOLU("Résolu"),
    CLOS("Clos");

    private String libelle;

    Statut(String libelle) {
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