package com.gestionincidents.controller;

import com.gestionincidents.model.dao.UtilisateurDAO;
import com.gestionincidents.model.Utilisateur;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UtilisateurController {

    private static final Logger logger = LogManager.getLogger(UtilisateurController.class);
    private UtilisateurDAO utilisateurDAO;

    public UtilisateurController() {
        this.utilisateurDAO = new UtilisateurDAO();
    }

    public List<Utilisateur> getUtilisateurs() {
        try {
            return utilisateurDAO.getUtilisateurs();
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
            return null;
        }
    }

    public Utilisateur getUtilisateur(int id) {
        try {
            return utilisateurDAO.getUtilisateur(id);
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la récupération de l'utilisateur " + id + " : " + e.getMessage());
            return null;
        }
    }

    public void createUtilisateur(Utilisateur utilisateur) {
        try {
            utilisateurDAO.createUtilisateur(utilisateur);
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la création de l'utilisateur : " + e.getMessage());
        }
    }
    
    public void createDeveloppeur(int utilisateurId, String specialisation, String niveau, int anciennete, int equipeId, int responsableId) throws SQLException, IOException {
        utilisateurDAO.createDeveloppeur(utilisateurId, specialisation, niveau, anciennete, equipeId, responsableId);
    }

    public void createRapporteur(int utilisateurId, String service, String numMatricule) throws SQLException, IOException {
        utilisateurDAO.createRapporteur(utilisateurId, service, numMatricule);
    }

    public void createResponsable(int utilisateurId, String departement) throws SQLException, IOException {
        utilisateurDAO.createResponsable(utilisateurId, departement);
    }

    public void updateUtilisateur(Utilisateur utilisateur) {
        try {
            utilisateurDAO.updateUtilisateur(utilisateur);
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la mise à jour de l'utilisateur " + utilisateur.getId() + " : " + e.getMessage());
        }
    }

    public void deleteUtilisateur(int id) {
        try {
            utilisateurDAO.deleteUtilisateur(id);
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la suppression de l'utilisateur " + id + " : " + e.getMessage());
        }
    }

    public Utilisateur verifierConnexion(String email, String motDePasse) {
        try {
            Utilisateur utilisateur = utilisateurDAO.getUtilisateurParEmail(email);
            if (utilisateur != null && utilisateur.getMotDePasse().equals(motDePasse)) {
                return utilisateur;
            } else {
                return null;
            }
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la vérification de la connexion : " + e.getMessage());
            return null;
        }
    }
}