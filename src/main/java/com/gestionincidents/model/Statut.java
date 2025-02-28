package com.gestionincidents.model;

public enum Statut {

    NOUVEAU("Nouveau"),
    ASSIGNE("Assigné"),
    EN_ATTENTE("En attente"),
    RESOLU("Résolu"),
	RE_OUVERT("Réouvert"),
	CLOTURE("Cloturé");

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