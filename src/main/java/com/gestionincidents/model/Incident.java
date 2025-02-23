package com.gestionincidents.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Incident {

    private int id;
    private Application applicationConcernee;
    private String description;
    private Date dateCreation;
    private Date dateModification;
    private Priorite priorite;
    private Statut statut;
    private Utilisateur rapporteur;
    private Utilisateur assigneA;
    private Date dateCloture;
    private String solution;
    private List<Commentaire> commentaires;

    public Incident() {
        this.commentaires = new ArrayList<>();
    }

    public Incident(Application applicationConcernee, String description, Date dateCreation, Date dateModification, Priorite priorite, Statut statut, Utilisateur rapporteur) {
        this.applicationConcernee = applicationConcernee;
        this.description = description;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
        this.priorite = priorite;
        this.statut = statut;
        this.rapporteur = rapporteur;
        this.commentaires = new ArrayList<>();
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

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateModification() {
        return dateModification;
    }

    public void setDateModification(Date dateModification) {
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

    public Date getDateCloture() {
        return dateCloture;
    }

    public void setDateCloture(Date dateCloture) {
        this.dateCloture = dateCloture;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public List<Commentaire> getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(List<Commentaire> commentaires) {
        this.commentaires = commentaires;
    }

    public void ajouterCommentaire(Commentaire commentaire) {
        this.commentaires.add(commentaire);
    }

    public void supprimerCommentaire(Commentaire commentaire) {
        this.commentaires.remove(commentaire);
    }
}