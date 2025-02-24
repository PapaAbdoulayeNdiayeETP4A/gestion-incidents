package com.gestionincidents.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Incident {

    private int id;
    private Application applicationConcernee;
    private String description;
    private LocalDate dateSignalement; // LocalDate pour correspondre à DATE
    private LocalDateTime dateCreation; // LocalDateTime pour correspondre à DATETIME
    private LocalDateTime dateModification; // LocalDateTime pour correspondre à DATETIME
    private Priorite priorite; // String pour correspondre à VARCHAR
    private Statut statut; // String pour correspondre à VARCHAR
    private Utilisateur rapporteur;
    private Utilisateur assigneA;
    private LocalDate dateCloture; // LocalDate pour correspondre à DATE
    private String solution;

    public Incident() {
    }

    public Incident(Application applicationConcernee, String description, LocalDate dateSignalement, LocalDateTime dateCreation, LocalDateTime dateModification, Priorite priorite, Statut statut, Utilisateur rapporteur) {
        this.applicationConcernee = applicationConcernee;
        this.description = description;
        this.dateSignalement = dateSignalement;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
        this.priorite = priorite;
        this.statut = statut;
        this.rapporteur = rapporteur;
    }

    // Getters et setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Application getApplicationConcernee() {
        return applicationConcernee;
    }

    public void setApplicationConcernee(Application applicationConcernee) {
        this.applicationConcernee = applicationConcernee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateSignalement() {
        return dateSignalement;
    }

    public void setDateSignalement(LocalDate dateSignalement) {
        this.dateSignalement = dateSignalement;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }

    public Priorite getPriorite() {
        return priorite;
    }

    public void setPriorite(Priorite priorite) {
        this.priorite = priorite;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public Utilisateur getRapporteur() {
        return rapporteur;
    }

    public void setRapporteur(Utilisateur rapporteur) {
        this.rapporteur = rapporteur;
    }

    public Utilisateur getAssigneA() {
        return assigneA;
    }

    public void setAssigneA(Utilisateur assigneA) {
        this.assigneA = assigneA;
    }

    public LocalDate getDateCloture() {
        return dateCloture;
    }

    public void setDateCloture(LocalDate dateCloture) {
        this.dateCloture = dateCloture;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    // List de commentaires commentée car géré dans le DAO/Controller.
    // public List<Commentaire> getCommentaires() {
    //     return commentaires;
    // }
    //
    // public void setCommentaires(List<Commentaire> commentaires) {
    //     this.commentaires = commentaires;
    // }
    //
    // public void ajouterCommentaire(Commentaire commentaire) {
    //     this.commentaires.add(commentaire);
    // }
    //
    // public void supprimerCommentaire(Commentaire commentaire) {
    //     this.commentaires.remove(commentaire);
    // }
}