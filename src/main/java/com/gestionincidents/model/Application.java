package com.gestionincidents.model;

import java.util.ArrayList;
import java.util.List;

public class Application {

    private String nom; // Clé primaire
    private String description;
    private String version;
    private Equipe equipeResponsable; // Relation avec Equipe
    private List<Incident> incidents; // Relation avec Incident

    // Constructeurs

    public Application() {}

    public Application(String nom, String description, String version, Equipe equipeResponsable) {
        this.nom = nom;
        this.description = description;
        this.version = version;
        this.equipeResponsable = equipeResponsable;
        this.incidents = new ArrayList<>(); // Initialisation de la liste des incidents
    }

    // Getters et setters

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Equipe getEquipeResponsable() {
        return equipeResponsable;
    }

    public void setEquipeResponsable(Equipe equipeResponsable) {
        this.equipeResponsable = equipeResponsable;
    }

    public List<Incident> getIncidents() {
        return incidents;
    }

    public void setIncidents(List<Incident> incidents) {
        this.incidents = incidents;
    }

    // Méthodes pour les incidents

    public void ajouterIncident(Incident incident) {
        this.incidents.add(incident);
    }

    public void supprimerIncident(Incident incident) {
        this.incidents.remove(incident);
    }

}