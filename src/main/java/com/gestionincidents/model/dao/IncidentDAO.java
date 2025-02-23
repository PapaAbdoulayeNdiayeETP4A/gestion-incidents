package com.gestionincidents.model.dao;

import com.gestionincidents.model.*;
import com.gestionincidents.utils.ConnexionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IncidentDAO {

    private static final Logger logger = LogManager.getLogger(IncidentDAO.class);
    private Connection connection;

    public IncidentDAO() throws SQLException, IOException {
        try {
            connection = ConnexionBD.getConnection();
        } catch (SQLException e) {
            logger.error("Erreur lors de la connexion à la base de données : " + e.getMessage());
            throw e;
        }
    }

    public List<Incident> getAllIncidents() throws SQLException {
        List<Incident> incidents = new ArrayList<>();
        String sql = "SELECT i.id, i.description, i.date_signalement, i.priorite, i.statut, " +
                "a.nom AS application_nom, " +
                "u_rapporteur.id AS rapporteur_id, u_rapporteur.nom AS rapporteur_nom, u_rapporteur.email AS rapporteur_email, u_rapporteur.role AS rapporteur_role, " +
                "u_assigne.id AS assigne_id, u_assigne.nom AS assigne_nom, u_assigne.email AS assigne_email, u_assigne.role AS assigne_role " +
                "FROM incident i " +
                "INNER JOIN application a ON i.application_concernee_nom = a.nom " +
                "INNER JOIN utilisateur u_rapporteur ON i.rapporteur_id = u_rapporteur.id " +
                "LEFT JOIN utilisateur u_assigne ON i.assigne_a_id = u_assigne.id"; // LEFT JOIN pour gérer les incidents non assignés

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Incident incident = new Incident();
                incident.setId(resultSet.getInt("id"));
                incident.setDescription(resultSet.getString("description"));
                incident.setDateCreation(resultSet.getTimestamp("date_signalement"));

                String prioriteStr = resultSet.getString("priorite");
                incident.setPriorite(Priorite.valueOf(prioriteStr));

                String statutStr = resultSet.getString("statut");
                incident.setStatut(Statut.valueOf(statutStr));

                Application application = new Application();
                application.setNom(resultSet.getString("application_nom"));
                incident.setApplicationConcernee(application);

                Utilisateur rapporteur = new Utilisateur();
                rapporteur.setId(resultSet.getInt("rapporteur_id"));
                rapporteur.setNom(resultSet.getString("rapporteur_nom"));
                rapporteur.setEmail(resultSet.getString("rapporteur_email"));
                rapporteur.setRole(resultSet.getString("rapporteur_role"));
                incident.setRapporteur(rapporteur);

                int assigneId = resultSet.getInt("assigne_id");
                if (!resultSet.wasNull()) { // Vérifier si assigne_id est NULL
                    Utilisateur assigneA = new Utilisateur();
                    assigneA.setId(assigneId);
                    assigneA.setNom(resultSet.getString("assigne_nom"));
                    assigneA.setEmail(resultSet.getString("assigne_email"));
                    assigneA.setRole(resultSet.getString("assigne_role"));
                    incident.setAssigneA(assigneA);
                }

                incidents.add(incident);
            }
        }
        return incidents;
    }

    public Incident getIncidentById(int id) throws SQLException {
        String sql = "SELECT i.id, i.description, i.date_signalement, i.priorite, i.statut, " +
                "a.nom AS application_nom, " +
                "u_rapporteur.id AS rapporteur_id, u_rapporteur.nom AS rapporteur_nom, u_rapporteur.email AS rapporteur_email, u_rapporteur.role AS rapporteur_role, " +
                "u_assigne.id AS assigne_id, u_assigne.nom AS assigne_nom, u_assigne.email AS assigne_email, u_assigne.role AS assigne_role " +
                "FROM incident i " +
                "INNER JOIN application a ON i.application_concernee_nom = a.nom " +
                "INNER JOIN utilisateur u_rapporteur ON i.rapporteur_id = u_rapporteur.id " +
                "LEFT JOIN utilisateur u_assigne ON i.assigne_a_id = u_assigne.id " +
                "WHERE i.id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Incident incident = new Incident();
                    incident.setId(resultSet.getInt("id"));
                    incident.setDescription(resultSet.getString("description"));
                    incident.setDateCreation(resultSet.getTimestamp("date_signalement"));
                    incident.setPriorite(Priorite.valueOf(resultSet.getString("priorite")));
                    incident.setStatut(Statut.valueOf(resultSet.getString("statut")));

                    Application application = new Application();
                    application.setNom(resultSet.getString("application_nom"));
                    incident.setApplicationConcernee(application);

                    Utilisateur rapporteur = new Utilisateur();
                    rapporteur.setId(resultSet.getInt("rapporteur_id"));
                    rapporteur.setNom(resultSet.getString("rapporteur_nom"));
                    rapporteur.setEmail(resultSet.getString("rapporteur_email"));
                    rapporteur.setRole(resultSet.getString("rapporteur_role"));
                    incident.setRapporteur(rapporteur);

                    int assigneId = resultSet.getInt("assigne_id");
                    if (!resultSet.wasNull()) {
                        Utilisateur assigneA = new Utilisateur();
                        assigneA.setId(assigneId);
                        assigneA.setNom(resultSet.getString("assigne_nom"));
                        assigneA.setEmail(resultSet.getString("assigne_email"));
                        assigneA.setRole(resultSet.getString("assigne_role"));
                        incident.setAssigneA(assigneA);
                    }
                    return incident;
                }
            }
        }
        return null;
    }

    public void createIncident(Incident incident) throws SQLException {
        String sql = "INSERT INTO incident (description, date_signalement, priorite, statut, application_concernee_nom, rapporteur_id, assigne_a_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, incident.getDescription());
            statement.setTimestamp(2, new Timestamp(incident.getDateCreation().getTime()));
            statement.setString(3, incident.getPriorite().name());
            statement.setString(4, incident.getStatut().name());
            statement.setString(5, incident.getApplicationConcernee().getNom());
            statement.setInt(6, incident.getRapporteur().getId());
            if (incident.getAssigneA() != null) {
                statement.setInt(7, incident.getAssigneA().getId());
            } else {
                statement.setNull(7, Types.INTEGER);
            }
            statement.executeUpdate();
        }
    }

    public void updateIncident(Incident incident) throws SQLException {
        String sql = "UPDATE incident SET description = ?, priorite = ?, statut = ?, application_concernee_nom = ?, rapporteur_id = ?, assigne_a_id = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, incident.getDescription());
            statement.setString(2, incident.getPriorite().name());
            statement.setString(3, incident.getStatut().name());
            statement.setString(4, incident.getApplicationConcernee().getNom());
            statement.setInt(5, incident.getRapporteur().getId());
            if (incident.getAssigneA() != null) {
                statement.setInt(6, incident.getAssigneA().getId());
            } else {
                statement.setNull(6, Types.INTEGER);
            }
            statement.setInt(7, incident.getId());
            statement.executeUpdate();
        }
    }

    public void deleteIncident(int id) throws SQLException {
        String sql = "DELETE FROM incident WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public void assignerIncident(int incidentId, int developpeurId) throws SQLException {
        String sql = "UPDATE incident SET assigne_a_id = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, developpeurId);
            statement.setInt(2, incidentId);
            statement.executeUpdate();
        }
    }

    public void changerStatutIncident(int incidentId, Statut nouveauStatut) throws SQLException {
        String sql = "UPDATE incident SET statut = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nouveauStatut.name());
            statement.setInt(2, incidentId);
            statement.executeUpdate();
        }
    }

    // ... (autres méthodes)
}