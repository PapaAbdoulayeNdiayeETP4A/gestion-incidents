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

    public int createUtilisateur(Utilisateur utilisateur) throws SQLException, IOException {
        return utilisateurDAO.createUtilisateur(utilisateur);
    }
    
    public void createDeveloppeur(int utilisateurId, String specialisation, String niveau, int anciennete, int equipeId, int responsableId) throws SQLException, IOException {
        if (equipeId > 0 && responsableId > 0) {
            utilisateurDAO.createDeveloppeur(utilisateurId, specialisation, niveau, anciennete, equipeId);
        } else {
            logger.error("Équipe ou responsable invalide pour le développeur.");
        }
    }

    public void createRapporteur(int utilisateurId, String service, String numMatricule) throws SQLException, IOException {
        if (service != null && numMatricule != null) {
            utilisateurDAO.createRapporteur(utilisateurId, service, numMatricule);
        } else {
            logger.error("Service ou numéro matricule invalide pour le rapporteur.");
        }
    }

    public void createResponsable(int utilisateurId, String departement, int equipeId) throws SQLException, IOException {
        if (departement != null) {
            utilisateurDAO.createResponsable(utilisateurId, departement, equipeId);
        } else {
            logger.error("Département invalide pour le responsable.");
        }
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
            utilisateurDAO.supprimerUtilisateur(id);
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la suppression de l'utilisateur " + id + " : " + e.getMessage());
        }
    }

    public Utilisateur rechercherUtilisateurParId(int id) throws SQLException, IOException {
        return utilisateurDAO.rechercherUtilisateurParId(id);
    }

    public List<Utilisateur> rechercherUtilisateursParNom(String nom) throws SQLException, IOException {
        return utilisateurDAO.rechercherUtilisateursParNom(nom);
    }

    public int verifierConnexion(String email, String motDePasse) {
        try {
            Utilisateur utilisateur = utilisateurDAO.getUtilisateurParEmail(email);
            if (utilisateur != null && utilisateur.getMotDePasse().equals(motDePasse)) {
                if (utilisateur.isEstSupprime()) {
                    return -1; // Utilisateur supprimé
                }
                return 1; // Connexion réussie;
            } else {
                return 0;
            }
        } catch (SQLException | IOException ex) {
            logger.error("Erreur lors de la vérification de la connexion : " + ex.getMessage());
        }
        return 0;
    }

    public void supprimerUtilisateur(int id) throws SQLException, IOException {
        try {
            utilisateurDAO.supprimerUtilisateur(id);
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la suppression de l'utilisateur " + id + " : " + e.getMessage());
        }
    }

	public List<Utilisateur> getDeveloppeursParEquipe(int id) throws SQLException, IOException {
		return utilisateurDAO.getDeveloppeursParEquipe(id);
	}
    
    
}
