package com.gestionincidents.model.dao;

import com.gestionincidents.model.Utilisateur;
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

public class UtilisateurDAO {

    private static final Logger logger = LogManager.getLogger(UtilisateurDAO.class);

    public List<Utilisateur> getUtilisateurs() throws SQLException, IOException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement statement = connexion.prepareStatement("SELECT * FROM utilisateur");
             ResultSet resultat = statement.executeQuery()) {

            while (resultat.next()) {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(resultat.getInt("id"));
                utilisateur.setNom(resultat.getString("nom"));
                utilisateur.setEmail(resultat.getString("email"));
                utilisateur.setMotDePasse(resultat.getString("mot_de_passe"));
                utilisateur.setRole(resultat.getString("role")); // Récupérer le rôle ici
                utilisateurs.add(utilisateur);
            }
            logger.info("Récupération des utilisateurs depuis la base de données.");
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
            throw e;
        }
        return utilisateurs;
    }

    public Utilisateur getUtilisateur(int id) throws SQLException, IOException {
        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement statement = connexion.prepareStatement("SELECT * FROM utilisateur WHERE id = ?")) {

            statement.setInt(1, id);
            try (ResultSet resultat = statement.executeQuery()) {
                if (resultat.next()) {
                    Utilisateur utilisateur = new Utilisateur();
                    utilisateur.setId(resultat.getInt("id"));
                    utilisateur.setNom(resultat.getString("nom"));
                    utilisateur.setEmail(resultat.getString("email"));
                    utilisateur.setMotDePasse(resultat.getString("mot_de_passe"));
                    utilisateur.setRole(resultat.getString("role")); // Récupérer le rôle ici
                    return utilisateur;
                } else {
                    return null;
                }
            }
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la récupération de l'utilisateur " + id + " : " + e.getMessage());
            throw e;
        }
    }

    public Utilisateur getUtilisateurParEmail(String email) throws SQLException, IOException {
        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement statement = connexion.prepareStatement("SELECT * FROM utilisateur WHERE email = ?")) {

            statement.setString(1, email);
            try (ResultSet resultat = statement.executeQuery()) {
                if (resultat.next()) {
                    Utilisateur utilisateur = new Utilisateur();
                    utilisateur.setId(resultat.getInt("id"));
                    utilisateur.setNom(resultat.getString("nom"));
                    utilisateur.setEmail(resultat.getString("email"));
                    utilisateur.setMotDePasse(resultat.getString("mot_de_passe"));
                    utilisateur.setRole(resultat.getString("role")); // Récupérer le rôle ici
                    return utilisateur;
                } else {
                    return null;
                }
            }
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la récupération de l'utilisateur par email : " + e.getMessage());
            throw e;
        }
    }

    public void createUtilisateur(Utilisateur utilisateur) throws SQLException, IOException {
        Connection connexion = null;
        try {
            connexion = ConnexionBD.getConnection();
            String sql = "INSERT INTO utilisateur (nom, email, mot_de_passe, role) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connexion.prepareStatement(sql);
            statement.setString(1, utilisateur.getNom());
            statement.setString(2, utilisateur.getEmail());
            statement.setString(3, utilisateur.getMotDePasse());
            statement.setString(4, utilisateur.getRole());
            statement.executeUpdate();
            logger.info("Utilisateur créé : " + utilisateur.getNom());
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la création de l'utilisateur : " + e.getMessage());
            throw e;
        }
    }

    public void updateUtilisateur(Utilisateur utilisateur) throws SQLException, IOException {
        Connection connexion = null;
        try {
            connexion = ConnexionBD.getConnection();
            String sql = "UPDATE utilisateur SET nom = ?, email = ?, mot_de_passe = ?, role = ? WHERE id = ?";
            PreparedStatement statement = connexion.prepareStatement(sql);
            statement.setString(1, utilisateur.getNom());
            statement.setString(2, utilisateur.getEmail());
            statement.setString(3, utilisateur.getMotDePasse());
            statement.setString(4, utilisateur.getRole());
            statement.setInt(5, utilisateur.getId());
            statement.executeUpdate();
            logger.info("Utilisateur mis à jour : " + utilisateur.getNom());
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la mise à jour de l'utilisateur " + utilisateur.getId() + " : " + e.getMessage());
            throw e;
        }
    }

    public void deleteUtilisateur(int id) throws SQLException, IOException {
        Connection connexion = null;
        try {
            connexion = ConnexionBD.getConnection();
            String sql = "DELETE FROM utilisateur WHERE id = ?";
            PreparedStatement statement = connexion.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
            logger.info("Utilisateur supprimé : " + id);
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la suppression de l'utilisateur " + id + " : " + e.getMessage());
            throw e;
        }
    }
}