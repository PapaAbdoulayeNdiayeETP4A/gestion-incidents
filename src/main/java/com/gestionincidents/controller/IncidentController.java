package com.gestionincidents.controller;

import com.gestionincidents.model.dao.ApplicationDAO;
import com.gestionincidents.model.dao.IncidentDAO;
import com.gestionincidents.model.Application;
import com.gestionincidents.model.Incident;
import com.gestionincidents.model.Statut;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class IncidentController {

    private static final Logger logger = LogManager.getLogger(IncidentController.class);
    private IncidentDAO incidentDAO;
    private ApplicationDAO applicationDAO;

    public IncidentController() throws SQLException, IOException {
        this.incidentDAO = new IncidentDAO();
        this.applicationDAO = new ApplicationDAO();
    }

    public List<Incident> getIncidents() {
        try {
            return incidentDAO.getAllIncidents();
        } catch (SQLException e) {
            logger.error("Erreur lors de la récupération des incidents : " + e.getMessage());
            return null;
        }
    }

    public Incident getIncident(int id) {
        try {
            return incidentDAO.getIncidentById(id);
        } catch (SQLException e) {
            logger.error("Erreur lors de la récupération de l'incident " + id + " : " + e.getMessage());
            return null;
        }
    }

    public void createIncident(Incident incident) {
        try {
            incidentDAO.createIncident(incident);
        } catch (SQLException e) {
            logger.error("Erreur lors de la création de l'incident : " + e.getMessage());
        }
    }

    public void updateIncident(Incident incident) {
        try {
            incidentDAO.updateIncident(incident);
        } catch (SQLException e) {
            logger.error("Erreur lors de la mise à jour de l'incident " + incident.getId() + " : " + e.getMessage());
        }
    }

    public void deleteIncident(int id) {
        try {
            incidentDAO.deleteIncident(id);
        } catch (SQLException e) {
            logger.error("Erreur lors de la suppression de l'incident " + id + " : " + e.getMessage());
        }
    }

    public List<Application> getAllApplications() throws SQLException, IOException {
        return applicationDAO.getApplications();
    }

    public void changerStatutIncident(int incidentId, Statut nouveauStatut) throws SQLException {
        try {
            incidentDAO.changerStatutIncident(incidentId, nouveauStatut);
        } catch (SQLException e) {
            logger.error("Erreur lors du changement de statut de l'incident " + incidentId + " : " + e.getMessage());
            throw e;
        }
    }
    
    public List<Incident> getIncidentsOuverts() throws SQLException, IOException {
        return incidentDAO.getIncidentsOuverts();
    }

    public List<Incident> getIncidentsAssignesADeveloppeur(int developpeurId) throws SQLException, IOException {
        return incidentDAO.getIncidentsAssignesADeveloppeur(developpeurId);
    }

    public void assignerIncidentADeveloppeur(int incidentId, int developpeurId) throws SQLException, IOException {
        incidentDAO.assignerIncidentADeveloppeur(incidentId, developpeurId);
    }
}