package com.gestionincidents.model.dao;

import com.gestionincidents.model.Commentaire;
import com.gestionincidents.model.Utilisateur;
import com.gestionincidents.utils.ConnexionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CommentaireDAO {

    private static final Logger logger = LogManager.getLogger(CommentaireDAO.class);

    public List<Commentaire> getCommentaires(int incidentId) throws SQLException, IOException {
        List<Commentaire> commentaires = new ArrayList<>();
        Connection connexion = null;

        try {
            connexion = ConnexionBD.getConnection();
            String sql = "SELECT c.id, c.contenu, c.date, c.auteur_id, u.nom AS auteur_nom " +
                    "FROM commentaire c " +
                    "INNER JOIN utilisateur u ON c.auteur_id = u.id " +
                    "WHERE c.incident_id = ?";
            PreparedStatement statement = connexion.prepareStatement(sql);
            statement.setInt(1, incidentId);
            ResultSet resultat = statement.executeQuery();

            while (resultat.next()) {
                Commentaire commentaire = new Commentaire();
                commentaire.setId(resultat.getInt("id"));
                commentaire.setContenu(resultat.getString("contenu"));
                Timestamp timestamp = resultat.getTimestamp("date");
                if (timestamp != null) {
                    commentaire.setDate(timestamp.toLocalDateTime());
                }

                Utilisateur auteur = new Utilisateur();
                auteur.setId(resultat.getInt("auteur_id"));
                auteur.setNom(resultat.getString("auteur_nom"));
                commentaire.setAuteur(auteur);

                commentaires.add(commentaire);
            }

            logger.info("Récupération des commentaires pour l'incident " + incidentId + " depuis la base de données.");

        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la récupération des commentaires : " + e.getMessage());
            throw e;
        }

        return commentaires;
    }

    public Commentaire getCommentaireById(int commentaireId) throws SQLException, IOException {
        Connection connexion = null;
        try {
            connexion = ConnexionBD.getConnection();
            String sql = "SELECT c.id, c.contenu, c.date, c.auteur_id, u.nom AS auteur_nom " +
                    "FROM commentaire c " +
                    "INNER JOIN utilisateur u ON c.auteur_id = u.id " +
                    "WHERE c.id = ?";
            PreparedStatement statement = connexion.prepareStatement(sql);
            statement.setInt(1, commentaireId);
            ResultSet resultat = statement.executeQuery();

            if (resultat.next()) {
                Commentaire commentaire = new Commentaire();
                commentaire.setId(resultat.getInt("id"));
                commentaire.setContenu(resultat.getString("contenu"));
                Timestamp timestamp = resultat.getTimestamp("date");
                if (timestamp != null) {
                    commentaire.setDate(timestamp.toLocalDateTime());
                }

                Utilisateur auteur = new Utilisateur();
                auteur.setId(resultat.getInt("auteur_id"));
                auteur.setNom(resultat.getString("auteur_nom"));
                commentaire.setAuteur(auteur);

                return commentaire;
            } else {
                return null;
            }
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la récupération du commentaire " + commentaireId + " : " + e.getMessage());
            throw e;
        }
    }

    public void createCommentaire(Commentaire commentaire, int incidentId) throws SQLException, IOException {
        Connection connexion = null;
        try {
            connexion = ConnexionBD.getConnection();
            String sql = "INSERT INTO commentaire (contenu, date, auteur_id, incident_id) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connexion.prepareStatement(sql);
            statement.setString(1, commentaire.getContenu());
            statement.setTimestamp(2, Timestamp.valueOf(commentaire.getDate()));
            statement.setInt(3, commentaire.getAuteur().getId());
            statement.setInt(4, incidentId);
            statement.executeUpdate();
            logger.info("Commentaire créé pour l'incident " + incidentId);
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la création du commentaire : " + e.getMessage());
            throw e;
        }
    }

    public void createReponseCommentaire(Commentaire commentaire, int incidentId, int commentaireParentId) throws SQLException, IOException {
        Connection connexion = null;
        try {
            connexion = ConnexionBD.getConnection();
            String sql = "INSERT INTO commentaire (contenu, date, auteur_id, incident_id) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connexion.prepareStatement(sql);
            statement.setString(1, commentaire.getContenu());
            statement.setTimestamp(2, Timestamp.valueOf(commentaire.getDate()));
            statement.setInt(3, commentaire.getAuteur().getId());
            statement.setInt(4, incidentId);
            statement.executeUpdate();
            logger.info("Réponse au commentaire créée pour l'incident " + incidentId);
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la création de la réponse au commentaire : " + e.getMessage());
            throw e;
        }
    }

    public void updateCommentaire(Commentaire commentaire) throws SQLException, IOException {
        Connection connexion = null;
        try {
            connexion = ConnexionBD.getConnection();
            String sql = "UPDATE commentaire SET contenu = ?, date = ?, auteur_id = ? WHERE id = ?";
            PreparedStatement statement = connexion.prepareStatement(sql);
            statement.setString(1, commentaire.getContenu());
            statement.setTimestamp(2, Timestamp.valueOf(commentaire.getDate()));
            statement.setInt(3, commentaire.getAuteur().getId());
            statement.setInt(4, commentaire.getId());
            statement.executeUpdate();
            logger.info("Commentaire mis à jour : " + commentaire.getId());
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la mise à jour du commentaire " + commentaire.getId() + " : " + e.getMessage());
            throw e;
        }
    }

    public void deleteCommentaire(int commentaireId) throws SQLException, IOException {
        Connection connexion = null;
        try {
            connexion = ConnexionBD.getConnection();
            String sql = "DELETE FROM commentaire WHERE id = ?";
            PreparedStatement statement = connexion.prepareStatement(sql);
            statement.setInt(1, commentaireId);
            statement.executeUpdate();
            logger.info("Commentaire supprimé : " + commentaireId);
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la suppression du commentaire " + commentaireId + " : " + e.getMessage());
            throw e;
        }
    }
}