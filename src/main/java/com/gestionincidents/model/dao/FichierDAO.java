package com.gestionincidents.model.dao;

import com.gestionincidents.model.Fichier;
import com.gestionincidents.model.FichierTrace;
import com.gestionincidents.model.FichierImage;
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

public class FichierDAO {

    private static final Logger logger = LogManager.getLogger(FichierDAO.class);

    public Fichier getFichier(int id) throws SQLException, IOException {
        Fichier fichier = null;
        Connection connexion = null;

        try {
            connexion = ConnexionBD.getConnection();
            String sql = "SELECT f.id, f.nom, f.date_upload, f.uploader_id, u.nom AS uploader_nom, f.type, f.contenu " +
                    "FROM fichier f " +
                    "INNER JOIN utilisateur u ON f.uploader_id = u.id " +
                    "WHERE f.id = ?";
            PreparedStatement statement = connexion.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultat = statement.executeQuery();

            if (resultat.next()) {
                String type = resultat.getString("type");
                if (type.equals("trace")) {
                    fichier = new FichierTrace();
                    ((FichierTrace) fichier).setContenu(resultat.getString("contenu"));
                } else if (type.equals("image")) {
                    fichier = new FichierImage();
                    ((FichierImage) fichier).setContenu(resultat.getBytes("contenu"));
                }

                if (fichier != null) {
                    fichier.setId(resultat.getInt("id"));
                    fichier.setNom(resultat.getString("nom"));
                    fichier.setDateUpload(resultat.getTimestamp("date_upload").toLocalDateTime()); // Conversion en LocalDateTime

                    Utilisateur uploader = new Utilisateur();
                    uploader.setId(resultat.getInt("uploader_id"));
                    uploader.setNom(resultat.getString("uploader_nom"));
                    fichier.setUploader(uploader);
                }
            }

            logger.info("Récupération du fichier " + id + " depuis la base de données.");

        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la récupération du fichier : " + e.getMessage());
            throw e;
        }

        return fichier;
    }

    public void createFichier(Fichier fichier) throws SQLException, IOException {
        Connection connexion = null;
        try {
            connexion = ConnexionBD.getConnection();
            String sql;
            PreparedStatement statement;

            if (fichier instanceof FichierTrace) {
                sql = "INSERT INTO fichier (nom, date_upload, uploader_id, type, contenu) VALUES (?, ?, ?, ?, ?)";
                statement = connexion.prepareStatement(sql);
                statement.setString(5, ((FichierTrace) fichier).getContenu());
            } else if (fichier instanceof FichierImage) {
                sql = "INSERT INTO fichier (nom, date_upload, uploader_id, type, contenu) VALUES (?, ?, ?, ?, ?)";
                statement = connexion.prepareStatement(sql);
                statement.setBytes(5, ((FichierImage) fichier).getContenu());
            } else {
                throw new IllegalArgumentException("Type de fichier non supporté.");
            }

            statement.setString(1, fichier.getNom());
            statement.setTimestamp(2, Timestamp.valueOf(fichier.getDateUpload())); // Conversion en Timestamp
            statement.setInt(3, fichier.getUploader().getId());
            statement.setString(4, fichier.getClass().getSimpleName().replace("Fichier", "").toLowerCase()); // Récupère le type (trace ou image)

            statement.executeUpdate();
            logger.info("Fichier créé : " + fichier.getNom());

        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la création du fichier : " + e.getMessage());
            throw e;
        }
    }

    public void updateFichier(Fichier fichier) throws SQLException, IOException {
        Connection connexion = null;
        try {
            connexion = ConnexionBD.getConnection();
            String sql;
            PreparedStatement statement;

            if (fichier instanceof FichierTrace) {
                sql = "UPDATE fichier SET nom = ?, date_upload = ?, uploader_id = ?, contenu = ? WHERE id = ?";
                statement = connexion.prepareStatement(sql);
                statement.setString(4, ((FichierTrace) fichier).getContenu());
            } else if (fichier instanceof FichierImage) {
                sql = "UPDATE fichier SET nom = ?, date_upload = ?, uploader_id = ?, contenu = ? WHERE id = ?";
                statement = connexion.prepareStatement(sql);
                statement.setBytes(4, ((FichierImage) fichier).getContenu());
            } else {
                throw new IllegalArgumentException("Type de fichier non supporté.");
            }

            statement.setString(1, fichier.getNom());
            statement.setTimestamp(2, Timestamp.valueOf(fichier.getDateUpload())); // Conversion en Timestamp
            statement.setInt(3, fichier.getUploader().getId());
            statement.setInt(5, fichier.getId());

            statement.executeUpdate();
            logger.info("Fichier mis à jour : " + fichier.getNom());

        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la mise à jour du fichier : " + e.getMessage());
            throw e;
        }
    }

    public void deleteFichier(int fichierId) throws SQLException, IOException {
        Connection connexion = null;
        try {
            connexion = ConnexionBD.getConnection();
            String sql = "DELETE FROM fichier WHERE id = ?";
            PreparedStatement statement = connexion.prepareStatement(sql);
            statement.setInt(1, fichierId);
            statement.executeUpdate();
            logger.info("Fichier supprimé : " + fichierId);

        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la suppression du fichier : " + e.getMessage());
            throw e;
        }
    }
}