package com.gestionincidents.controller;

import com.gestionincidents.model.dao.ApplicationDAO;
import com.gestionincidents.model.Application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ApplicationController {

    private static final Logger logger = LogManager.getLogger(ApplicationController.class);
    private ApplicationDAO applicationDAO;

    public ApplicationController() throws SQLException, IOException {
        this.applicationDAO = new ApplicationDAO();
    }

    public List<Application> getApplications() {
        try {
            return applicationDAO.getApplications();
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la récupération des applications : " + e.getMessage());
            return null;
        }
    }

    public Application getApplication(String nom) {
        try {
            return applicationDAO.getApplicationParNom(nom);
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la récupération de l'application " + nom + " : " + e.getMessage());
            return null;
        }
    }

    public void createApplication(Application application) {
        try {
            applicationDAO.createApplication(application);
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la création de l'application : " + e.getMessage());
        }
    }

    public void updateApplication(Application application) {
        try {
            applicationDAO.updateApplication(application);
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la mise à jour de l'application " + application.getNom() + " : " + e.getMessage());
        }
    }

    public void deleteApplication(String nom) {
        try {
            applicationDAO.deleteApplication(nom);
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la suppression de l'application " + nom + " : " + e.getMessage());
        }
    }

    public Application getApplicationsByEquipeId(int equipeId) throws SQLException, IOException {
        ApplicationDAO applicationDAO = new ApplicationDAO();
        return applicationDAO.getApplicationsByEquipeId(equipeId);
    }

}