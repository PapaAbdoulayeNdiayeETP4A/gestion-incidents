package com.gestionincidents.model;

import java.time.LocalDateTime;

public class FichierTrace extends Fichier {

    private String contenu;
    private String logicielSource;

    public FichierTrace() {
        super();
    }

    public FichierTrace(String nom, LocalDateTime dateUpload, Utilisateur uploader, Incident incident, String contenu, String logicielSource) {
        super(nom, dateUpload, uploader, incident);
        this.contenu = contenu;
        this.logicielSource = logicielSource;
    }

    // Getters et setters pour les attributs spécifiques

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getLogicielSource() {
        return logicielSource;
    }

    public void setLogicielSource(String logicielSource) {
        this.logicielSource = logicielSource;
    }
}