package com.gestionincidents.model.dao;

import com.gestionincidents.model.Equipe;
import com.gestionincidents.utils.ConnexionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EquipeDAO {

    private static final Logger logger = LogManager.getLogger(EquipeDAO.class);

    public List<Equipe> getEquipes() throws SQLException, IOException {
        List<Equipe> equipes = new ArrayList<>();
        Connection connexion = null;

        try {
            connexion = ConnexionBD.getConnection();
            String sql = "SELECT e.id, e.nom FROM equipe e";
            PreparedStatement statement = connexion.prepareStatement(sql);
            ResultSet resultat = statement.executeQuery();

            while (resultat.next()) {
                Equipe equipe = new Equipe();
                equipe.setId(resultat.getInt("id"));
                equipe.setNom(resultat.getString("nom"));

                // Gestion des membres et applications déplacée vers DAO/Controller.
                // List<Developpeur> developpeurs = getDeveloppeurs(connexion, equipe.getId());
                // equipe.setMembres(developpeurs);

                // List<Application> applications = getApplications(connexion, equipe.getId());
                // equipe.setApplications(applications);

                equipes.add(equipe);
            }

            logger.info("Récupération des équipes depuis la base de données.");

        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la récupération des équipes : " + e.getMessage());
            throw e;
        }

        return equipes;
    }

    public Equipe getEquipeById(int equipeId) throws SQLException, IOException {
        Connection connexion = null;
        try {
            connexion = ConnexionBD.getConnection();
            String sql = "SELECT e.id, e.nom FROM equipe e WHERE e.id = ?";
            PreparedStatement statement = connexion.prepareStatement(sql);
            statement.setInt(1, equipeId);
            ResultSet resultat = statement.executeQuery();

            if (resultat.next()) {
                Equipe equipe = new Equipe();
                equipe.setId(resultat.getInt("id"));
                equipe.setNom(resultat.getString("nom"));

                // Gestion des membres et applications déplacée vers DAO/Controller.
                // List<Developpeur> developpeurs = getDeveloppeurs(connexion, equipe.getId());
                // equipe.setMembres(developpeurs);

                // List<Application> applications = getApplications(connexion, equipe.getId());
                // equipe.setApplications(applications);

                return equipe;
            } else {
                return null;
            }
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la récupération de l'équipe " + equipeId + " : " + e.getMessage());
            throw e;
        }
    }

    public void createEquipe(Equipe equipe) throws SQLException, IOException {
        Connection connexion = null;
        try {
            connexion = ConnexionBD.getConnection();
            String sql = "INSERT INTO equipe (nom) VALUES (?)";
            PreparedStatement statement = connexion.prepareStatement(sql);
            statement.setString(1, equipe.getNom());
            statement.executeUpdate();
            logger.info("Équipe créée : " + equipe.getNom());
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la création de l'équipe : " + e.getMessage());
            throw e;
        }
    }

    public void updateEquipe(Equipe equipe) throws SQLException, IOException {
        Connection connexion = null;
        try {
            connexion = ConnexionBD.getConnection();
            String sql = "UPDATE equipe SET nom = ? WHERE id = ?";
            PreparedStatement statement = connexion.prepareStatement(sql);
            statement.setString(1, equipe.getNom());
            statement.setInt(2, equipe.getId());
            statement.executeUpdate();
            logger.info("Équipe mise à jour : " + equipe.getNom());
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la mise à jour de l'équipe " + equipe.getNom() + " : " + e.getMessage());
            throw e;
        }
    }

    public void deleteEquipe(int equipeId) throws SQLException, IOException {
        Connection connexion = null;
        try {
            connexion = ConnexionBD.getConnection();
            String sql = "DELETE FROM equipe WHERE id = ?";
            PreparedStatement statement = connexion.prepareStatement(sql);
            statement.setInt(1, equipeId);
            statement.executeUpdate();
            logger.info("Équipe supprimée : " + equipeId);
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la suppression de l'équipe " + equipeId + " : " + e.getMessage());
            throw e;
        }
    }

    // Gestion des membres et applications déplacée vers DAO/Controller.
    // private List<Developpeur> getDeveloppeurs(Connection connexion, int equipeId) throws SQLException {
    //     List<Developpeur> developpeurs = new ArrayList<>();
    //     String sql = "SELECT u.id, u.nom, u.email FROM utilisateur u WHERE u.equipe_id = ?";
    //     PreparedStatement statement = connexion.prepareStatement(sql);
    //     statement.setInt(1, equipeId);
    //     ResultSet resultat = statement.executeQuery();
    //
    //     while (resultat.next()) {
    //         Developpeur developpeur = new Developpeur();
    //         developpeur.setId(resultat.getInt("id"));
    //         developpeur.setNom(resultat.getString("nom"));
    //         developpeur.setEmail(resultat.getString("email"));
    //         developpeurs.add(developpeur);
    //     }
    //
    //     return developpeurs;
    // }
    //
    // private List<Application> getApplications(Connection connexion, int equipeId) throws SQLException {
    //     List<Application> applications = new ArrayList<>();
    //     String sql = "SELECT a.nom, a.description, a.version FROM application a WHERE a.equipe_responsable_id = ?";
    //     PreparedStatement statement = connexion.prepareStatement(sql);
    //     statement.setInt(1, equipeId);
    //     ResultSet resultat = statement.executeQuery();
    //
    //     while (resultat.next()) {
    //         Application application = new Application();
    //         application.setNom(resultat.getString("nom"));
    //         application.setDescription(resultat.getString("description"));
    //         application.setVersion(resultat.getString("version"));
    //         applications.add(application);
    //     }
    //
    //     return applications;
    // }
}