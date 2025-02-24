package com.gestionincidents.model;

public class Application {

    private int id; // Ajout de l'ID comme clé primaire
    private String nom; // Clé primaire (dans la base de données)
    private String description;
    private String version;
    private Equipe equipeResponsable; // Relation avec Equipe

    // Constructeurs

    public Application() {}

    public Application(int id, String nom, String description, String version, Equipe equipeResponsable) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.version = version;
        this.equipeResponsable = equipeResponsable;
    }

    // Getters et setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    // Méthodes pour les incidents (commentées car nous ne voulons pas les gérer dans Application)

    // public List<Incident> getIncidents() {
    //     return incidents;
    // }

    // public void setIncidents(List<Incident> incidents) {
    //     this.incidents = incidents;
    // }

    // public void ajouterIncident(Incident incident) {
    //     this.incidents.add(incident);
    // }

    // public void supprimerIncident(Incident incident) {
    //     this.incidents.remove(incident);
    // }

}