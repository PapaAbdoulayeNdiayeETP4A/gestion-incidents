package com.gestionincidents.model.dao;

import com.gestionincidents.model.Utilisateur;
import com.gestionincidents.utils.ConnexionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
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

    public int createUtilisateur(Utilisateur utilisateur) throws SQLException, IOException {
        String sql = "INSERT INTO utilisateur (nom, email, mot_de_passe, role, est_supprime) VALUES (?, ?, ?, ?, ?)";
        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement statement = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { // Récupérer les clés générées
            statement.setString(1, utilisateur.getNom());
            statement.setString(2, utilisateur.getEmail());
            statement.setString(3, utilisateur.getMotDePasse());
            statement.setString(4, utilisateur.getRole());
            statement.setBoolean(5, utilisateur.isEstSupprime());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Retourner l'ID généré
                } else {
                    throw new SQLException("La création de l'utilisateur a échoué, aucun ID généré.");
                }
            }
        }
    }

    public void createResponsable(int utilisateurId, String departement, int equipeId) throws SQLException, IOException {
        // Création du responsable
        String sql = "INSERT INTO responsable (utilisateur_id, departement) VALUES (?, ?)";
        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement statement = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, utilisateurId);
            statement.setString(2, departement);
            statement.executeUpdate();

            // Récupérer l'ID du responsable créé
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int responsableId = generatedKeys.getInt(1);

                    // Associer le responsable à l'équipe
                    associerResponsableAEquipe(responsableId, equipeId);
                } else {
                    throw new SQLException("Échec de la récupération de l'ID du responsable.");
                }
            }
        }
    }

    public void associerResponsableAEquipe(int responsableId, int equipeId) throws SQLException, IOException {
        String sql = "UPDATE equipe SET responsable_id = ? WHERE id = ?";
        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement statement = connexion.prepareStatement(sql)) {
            statement.setInt(1, responsableId);
            statement.setInt(2, equipeId);
            statement.executeUpdate();
        }
    }


    public void createDeveloppeur(int utilisateurId, String specialisation, String niveau, int anciennete, int equipeId) throws SQLException, IOException {
        String sql = "INSERT INTO developpeur (utilisateur_id, specialisation, niveau, anciennete, equipe_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement statement = connexion.prepareStatement(sql)) {
            statement.setInt(1, utilisateurId);
            statement.setString(2, specialisation);
            statement.setString(3, niveau);
            statement.setInt(4, anciennete);
            statement.setInt(5, equipeId);
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

    public void updateUtilisateur(Utilisateur utilisateur) throws SQLException, IOException {
        Connection connexion = null;
        try {
            connexion = ConnexionBD.getConnection();
            String sql = "UPDATE utilisateur SET nom = ?, email = ?, mot_de_passe = ?, role = ?, est_supprime = ? WHERE id = ?";
            PreparedStatement statement = connexion.prepareStatement(sql);
            statement.setString(1, utilisateur.getNom());
            statement.setString(2, utilisateur.getEmail());
            statement.setString(3, utilisateur.getMotDePasse());
            statement.setString(4, utilisateur.getRole());
            statement.setBoolean(5, utilisateur.isEstSupprime());
            statement.setInt(6, utilisateur.getId());
            statement.executeUpdate();
            logger.info("Utilisateur mis à jour : " + utilisateur.getNom());
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la mise à jour de l'utilisateur " + utilisateur.getId() + " : " + e.getMessage());
            throw e;
        }
    }

    public void supprimerUtilisateur(int id) throws SQLException, IOException {
        String sql = "UPDATE utilisateur SET est_supprime = TRUE WHERE id = ?";
        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement statement = connexion.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
    
    // Cette méthode récupère les IDs des développeurs qui sont assignés à un responsable
    public List<Integer> getDeveloppeurIdsByResponsableId(int responsableId) throws SQLException {
        List<Integer> developpeurIds = new ArrayList<>();
        String query = "SELECT d.utilisateur_id FROM developpeur d "
        		+ "WHERE d.equipe_id IN (SELECT e.id FROM equipe e WHERE e.responsable_id = ?)";

        try (PreparedStatement stmt = ConnexionBD.getConnection().prepareStatement(query)) {
            stmt.setInt(1, responsableId); // On lie l'ID du responsable à la requête

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                developpeurIds.add(rs.getInt("utilisateur_id"));
            }
        } catch (SQLException | IOException e) {
            throw new SQLException("Erreur lors de la récupération des développeurs : " + e.getMessage(), e);
        }

        return developpeurIds;
    }
    
    public int getResponsableIdByName(String nom) throws SQLException {
        String query = "SELECT r.id FROM responsable r " +
                       "JOIN utilisateur u ON r.utilisateur_id = u.id " +  // Utilisation de 'utilisateur_id' dans 'responsable'
                       "WHERE u.nom = ?";  // Recherche du responsable par le nom dans la table utilisateur
        try (PreparedStatement stmt = ConnexionBD.getConnection().prepareStatement(query)) {
            stmt.setString(1, nom);  // On lie le nom du responsable à la requête

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");  // Retourne l'ID du responsable
            } else {
                throw new SQLException("Aucun responsable trouvé avec ce nom.");
            }
        } catch (SQLException | IOException e) {
            throw new SQLException("Erreur lors de la récupération de l'ID du responsable : " + e.getMessage(), e);
        }
    }

    public Utilisateur rechercherUtilisateurParId(int idUtilisateur) throws SQLException, IOException {
        Utilisateur utilisateur = null;
        String sql = "SELECT * FROM utilisateur WHERE id = ? AND est_supprime = FALSE"; // Ne récupère que les utilisateurs non supprimés
        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement statement = connexion.prepareStatement(sql)) {
            statement.setInt(1, idUtilisateur);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    // Récupérer les données de l'utilisateur depuis le résultat de la requête
                    utilisateur = new Utilisateur();
                    utilisateur.setId(rs.getInt("id"));
                    utilisateur.setNom(rs.getString("nom"));
                    utilisateur.setEmail(rs.getString("email"));
                    utilisateur.setMotDePasse(rs.getString("mot_de_passe"));
                    utilisateur.setRole(rs.getString("role"));
                    // Assure-toi que tous les champs nécessaires de l'utilisateur sont récupérés
                }
            }
        }
        return utilisateur;
    }
    
    
    public List<Utilisateur> rechercherUtilisateursParNom(String nom) throws SQLException, IOException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        
        String query = "SELECT * FROM utilisateur WHERE nom LIKE ?";
        
        try (PreparedStatement stmt = ConnexionBD.getConnection().prepareStatement(query)) {
            stmt.setString(1, "%" + nom + "%");
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(rs.getInt("id"));
                utilisateur.setNom(rs.getString("nom"));
                utilisateur.setEmail(rs.getString("email"));
                utilisateur.setMotDePasse(rs.getString("mot_de_passe"));
                utilisateur.setEstSupprime(rs.getBoolean("est_supprime"));
                utilisateur.setRole(rs.getString("role"));
                utilisateurs.add(utilisateur);
            }
        }
        
        return utilisateurs;
    }
    
    
    public List<Utilisateur> getDeveloppeursParEquipe(int responsableId) throws SQLException, IOException {
        List<Utilisateur> developpeurs = new ArrayList<>();
        String query = "SELECT u.id, u.nom, u.email, u.role " +
                       "FROM utilisateur u " +
                       "JOIN developpeur d ON u.id = d.utilisateur_id " +
                       "JOIN equipe e ON d.equipe_id = e.id " +
                       "WHERE e.responsable_id = ?";
        
        try (PreparedStatement statement = ConnexionBD.getConnection().prepareStatement(query)) {
            
            statement.setInt(1, responsableId);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(resultSet.getInt("id"));
                utilisateur.setNom(resultSet.getString("nom"));
                utilisateur.setEmail(resultSet.getString("email"));
                utilisateur.setRole(resultSet.getString("role"));
                developpeurs.add(utilisateur);
            }
        }
        return developpeurs;
    }



}
