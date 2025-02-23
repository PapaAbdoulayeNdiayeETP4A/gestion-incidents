package com.gestionincidents.model.dao;

import com.gestionincidents.model.Application;
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

public class ApplicationDAO {

    private static final Logger logger = LogManager.getLogger(ApplicationDAO.class);

    public List<Application> getApplications() throws SQLException, IOException {
        List<Application> applications = new ArrayList<>();
        Connection connexion = null;

        try {
            connexion = ConnexionBD.getConnection();
            String sql = "SELECT a.nom, a.description, a.version, a.equipe_responsable_id, e.nom AS equipe_nom " +
                    "FROM application a " +
                    "INNER JOIN equipe e ON a.equipe_responsable_id = e.id";
            PreparedStatement statement = connexion.prepareStatement(sql);
            ResultSet resultat = statement.executeQuery();

            while (resultat.next()) {
                Application application = new Application();
                application.setNom(resultat.getString("nom"));
                application.setDescription(resultat.getString("description"));
                application.setVersion(resultat.getString("version"));

                Equipe equipe = new Equipe();
                equipe.setId(resultat.getInt("equipe_responsable_id"));
                equipe.setNom(resultat.getString("equipe_nom"));
                application.setEquipeResponsable(equipe);

                applications.add(application);
            }

            logger.info("Récupération des applications depuis la base de données.");

        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la récupération des applications : " + e.getMessage());
            throw e;

        }

        return applications;
    }

    public Application getApplicationParNom(String nom) throws SQLException, IOException {
        Connection connexion = null;
        try {
            connexion = ConnexionBD.getConnection();
            String sql = "SELECT a.nom, a.description, a.version, a.equipe_responsable_id, e.nom AS equipe_nom " +
                    "FROM application a " +
                    "INNER JOIN equipe e ON a.equipe_responsable_id = e.id " +
                    "WHERE a.nom = ?";
            PreparedStatement statement = connexion.prepareStatement(sql);
            statement.setString(1, nom);
            ResultSet resultat = statement.executeQuery();

            if (resultat.next()) {
                Application application = new Application();
                application.setNom(resultat.getString("nom"));
                application.setDescription(resultat.getString("description"));
                application.setVersion(resultat.getString("version"));

                Equipe equipe = new Equipe();
                equipe.setId(resultat.getInt("equipe_responsable_id"));
                equipe.setNom(resultat.getString("equipe_nom"));
                application.setEquipeResponsable(equipe);

                return application;
            } else {
                return null;
            }
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la récupération de l'application " + nom + " : " + e.getMessage());
            throw e;
        }
    }

    public void createApplication(Application application) throws SQLException, IOException {
        Connection connexion = null;
        try {
            connexion = ConnexionBD.getConnection();
            String sql = "INSERT INTO application (nom, description, version, equipe_responsable_id) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connexion.prepareStatement(sql);
            statement.setString(1, application.getNom());
            statement.setString(2, application.getDescription());
            statement.setString(3, application.getVersion());
            statement.setInt(4, application.getEquipeResponsable().getId());
            statement.executeUpdate();
            logger.info("Application créée : " + application.getNom());
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la création de l'application : " + e.getMessage());
            throw e;
        }
    }

    public void updateApplication(Application application) throws SQLException, IOException {
        Connection connexion = null;
        try {
            connexion = ConnexionBD.getConnection();
            String sql = "UPDATE application SET description = ?, version = ?, equipe_responsable_id = ? WHERE nom = ?";
            PreparedStatement statement = connexion.prepareStatement(sql);
            statement.setString(1, application.getDescription());
            statement.setString(2, application.getVersion());
            statement.setInt(3, application.getEquipeResponsable().getId());
            statement.setString(4, application.getNom());
            statement.executeUpdate();
            logger.info("Application mise à jour : " + application.getNom());
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la mise à jour de l'application " + application.getNom() + " : " + e.getMessage());
            throw e;
        }
    }

    public void deleteApplication(String nom) throws SQLException, IOException {
        Connection connexion = null;
        try {
            connexion = ConnexionBD.getConnection();
            String sql = "DELETE FROM application WHERE nom = ?";
            PreparedStatement statement = connexion.prepareStatement(sql);
            statement.setString(1, nom);
            statement.executeUpdate();
            logger.info("Application supprimée : " + nom);
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la suppression de l'application " + nom + " : " + e.getMessage());
            throw e;
        }
    }
}