package com.gestionincidents.controller;

import com.gestionincidents.model.dao.EquipeDAO;
import com.gestionincidents.model.Equipe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class EquipeController {

    private static final Logger logger = LogManager.getLogger(EquipeController.class);
    private EquipeDAO equipeDAO;

    public EquipeController() throws SQLException, IOException {
        this.equipeDAO = new EquipeDAO();
    }

    public List<Equipe> getEquipes() {
        try {
            return equipeDAO.getEquipes();
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la récupération des équipes : " + e.getMessage());
            return null;
        }
    }

    public Equipe getEquipe(int id) {
        try {
            return equipeDAO.getEquipeById(id);
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la récupération de l'équipe " + id + " : " + e.getMessage());
            return null;
        }
    }

    public void createEquipe(Equipe equipe) {
        try {
            equipeDAO.createEquipe(equipe);
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la création de l'équipe : " + e.getMessage());
        }
    }

    public void updateEquipe(Equipe equipe) {
        try {
            equipeDAO.updateEquipe(equipe);
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la mise à jour de l'équipe " + equipe.getId() + " : " + e.getMessage());
        }
    }

    public void deleteEquipe(int id) {
        try {
            equipeDAO.deleteEquipe(id);
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la suppression de l'équipe " + id + " : " + e.getMessage());
        }
    }

    public Equipe getEquipeByResponsableId(int responsableId) throws SQLException, IOException {
        EquipeDAO equipeDAO = new EquipeDAO();
        return equipeDAO.getEquipeByResponsableId(responsableId);
    }
}