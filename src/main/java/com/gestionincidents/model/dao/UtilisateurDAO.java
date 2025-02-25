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
             PreparedStatement statement = connexion.prepareStatement("SELECT id, nom, email, mot_de_passe, role, est_supprime FROM utilisateur");
             ResultSet resultat = statement.executeQuery()) {

            while (resultat.next()) {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(resultat.getInt("id"));
                utilisateur.setNom(resultat.getString("nom"));
                utilisateur.setEmail(resultat.getString("email"));
                utilisateur.setMotDePasse(resultat.getString("mot_de_passe"));
                utilisateur.setRole(resultat.getString("role"));
                utilisateur.setEstSupprime(resultat.getBoolean("est_supprime")); // Récupérer est_supprime
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
                    utilisateur.setRole(resultat.getString("role"));
                    utilisateur.setEstSupprime(resultat.getBoolean("est_supprime")); // Récupérer est_supprime
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
    
    public List<Integer> getDeveloppeurIdsByResponsableId(int responsableId) throws SQLException, IOException {
        List<Integer> developpeurIds = new ArrayList<>();
        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement statement = connexion.prepareStatement("SELECT utilisateur_id FROM developpeur WHERE responsable_id = ?")) {
            statement.setInt(1, responsableId);
            try (ResultSet resultat = statement.executeQuery()) {
                while (resultat.next()) {
                    developpeurIds.add(resultat.getInt("utilisateur_id"));
                }
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la récupération des développeurs de l'équipe : " + e.getMessage());
            throw e;
        }
        return developpeurIds;
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
    
    public void createDeveloppeur(int utilisateurId, String specialisation, String niveau, int anciennete, int equipeId, int responsableId) throws SQLException, IOException {
        String sql = "INSERT INTO developpeur (utilisateur_id, specialisation, niveau, anciennete, equipe_id, responsable_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement statement = connexion.prepareStatement(sql)) {
            statement.setInt(1, utilisateurId);
            statement.setString(2, specialisation);
            statement.setString(3, niveau);
            statement.setInt(4, anciennete);
            statement.setInt(5, equipeId);
            statement.setInt(6, responsableId);
            statement.executeUpdate();
        }
    }

    public void createRapporteur(int utilisateurId, String service, String numMatricule) throws SQLException, IOException {
        String sql = "INSERT INTO rapporteur (utilisateur_id, service, num_matricule) VALUES (?, ?, ?)";
        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement statement = connexion.prepareStatement(sql)) {
            statement.setInt(1, utilisateurId);
            statement.setString(2, service);
            statement.setString(3, numMatricule);
            statement.executeUpdate();
        }
    }

    public void createResponsable(int utilisateurId, String departement) throws SQLException, IOException {
        String sql = "INSERT INTO responsable (utilisateur_id, departement) VALUES (?, ?)";
        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement statement = connexion.prepareStatement(sql)) {
            statement.setInt(1, utilisateurId);
            statement.setString(2, departement);
            statement.executeUpdate();
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
    
    
    public void supprimerDeveloppeur(int utilisateurId) throws SQLException, IOException {
        String sql = "DELETE FROM developpeur WHERE utilisateur_id = ?";
        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement statement = connexion.prepareStatement(sql)) {
            statement.setInt(1, utilisateurId);
            statement.executeUpdate();
        }
    }

    public void supprimerRapporteur(int utilisateurId) throws SQLException, IOException {
        String sql = "DELETE FROM rapporteur WHERE utilisateur_id = ?";
        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement statement = connexion.prepareStatement(sql)) {
            statement.setInt(1, utilisateurId);
            statement.executeUpdate();
        }
    }

    public void supprimerResponsable(int utilisateurId) throws SQLException, IOException {
        String sql = "DELETE FROM responsable WHERE utilisateur_id = ?";
        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement statement = connexion.prepareStatement(sql)) {
            statement.setInt(1, utilisateurId);
            statement.executeUpdate();
        }
    }

    public int getDernierIdUtilisateur() throws SQLException, IOException {
        String sql = "SELECT LAST_INSERT_ID()";
        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement statement = connexion.prepareStatement(sql);
             ResultSet resultat = statement.executeQuery()) {
            if (resultat.next()) {
                return resultat.getInt(1);
            }
            return -1; // Ou lancez une exception si aucun ID n'est trouvé
        }
    }
    
    public List<Utilisateur> getAllResponsables() throws SQLException, IOException {
        List<Utilisateur> responsables = new ArrayList<>();
        String sql = "SELECT u.id, u.nom, u.email, u.mot_de_passe, u.role FROM utilisateur u INNER JOIN responsable r ON u.id = r.utilisateur_id";
        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement statement = connexion.prepareStatement(sql);
             ResultSet resultat = statement.executeQuery()) {
            while (resultat.next()) {
                Utilisateur responsable = new Utilisateur();
                responsable.setId(resultat.getInt("id"));
                responsable.setNom(resultat.getString("nom"));
                responsable.setEmail(resultat.getString("email"));
                responsable.setMotDePasse(resultat.getString("mot_de_passe"));
                responsable.setRole(resultat.getString("role"));
                responsables.add(responsable);
            }
        }
        return responsables;
    }

    public int getResponsableIdByName(String nomResponsable) throws SQLException, IOException {
        String sql = "SELECT u.id FROM utilisateur u INNER JOIN responsable r ON u.id = r.utilisateur_id WHERE u.nom = ?";
        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement statement = connexion.prepareStatement(sql)) {
            statement.setString(1, nomResponsable);
            try (ResultSet resultat = statement.executeQuery()) {
                if (resultat.next()) {
                    return resultat.getInt("id");
                }
            }
        }
        return -1; // Ou lancez une exception si le responsable n'est pas trouvé
    }
    
    public void supprimerUtilisateur(int id) throws SQLException, IOException {
        String sql = "UPDATE utilisateur SET est_supprime = TRUE WHERE id = ?";
        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement statement = connexion.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public Utilisateur rechercherUtilisateurParId(int id) throws SQLException, IOException {
        String sql = "SELECT id, nom, email, mot_de_passe, role, est_supprime FROM utilisateur WHERE id = ?";
        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement statement = connexion.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultat = statement.executeQuery()) {
                if (resultat.next()) {
                    Utilisateur utilisateur = new Utilisateur();
                    utilisateur.setId(resultat.getInt("id"));
                    utilisateur.setNom(resultat.getString("nom"));
                    utilisateur.setEmail(resultat.getString("email"));
                    utilisateur.setMotDePasse(resultat.getString("mot_de_passe"));
                    utilisateur.setRole(resultat.getString("role"));
                    utilisateur.setEstSupprime(resultat.getBoolean("est_supprime"));
                    return utilisateur;
                }
                return null; // Utilisateur non trouvé
            }
        }
    }

    public List<Utilisateur> rechercherUtilisateursParNom(String nom) throws SQLException, IOException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT id, nom, email, mot_de_passe, role, est_supprime FROM utilisateur WHERE nom LIKE ?";
        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement statement = connexion.prepareStatement(sql)) {
            statement.setString(1, "%" + nom + "%"); // Recherche partielle
            try (ResultSet resultat = statement.executeQuery()) {
                while (resultat.next()) {
                    Utilisateur utilisateur = new Utilisateur();
                    utilisateur.setId(resultat.getInt("id"));
                    utilisateur.setNom(resultat.getString("nom"));
                    utilisateur.setEmail(resultat.getString("email"));
                    utilisateur.setMotDePasse(resultat.getString("mot_de_passe"));
                    utilisateur.setRole(resultat.getString("role"));
                    utilisateur.setEstSupprime(resultat.getBoolean("est_supprime"));
                    utilisateurs.add(utilisateur);
                }
                return utilisateurs;
            }
        }
    }
}