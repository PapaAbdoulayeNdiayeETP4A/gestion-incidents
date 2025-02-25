package com.gestionincidents.model.dao;

import com.gestionincidents.model.Equipe;
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

public class EquipeDAO {
    
    private static final Logger logger = LogManager.getLogger(EquipeDAO.class);

    public List<Equipe> getEquipes() throws SQLException, IOException {
        List<Equipe> equipes = new ArrayList<>();
        try (Connection connexion = ConnexionBD.getConnection()) {
            String sql = "SELECT e.id, e.nom, e.description, e.responsable_id FROM equipe e";
            try (PreparedStatement statement = connexion.prepareStatement(sql);
                 ResultSet resultat = statement.executeQuery()) {
                while (resultat.next()) {
                    Equipe equipe = new Equipe();
                    equipe.setId(resultat.getInt("id"));
                    equipe.setNom(resultat.getString("nom"));
                    equipe.setDescription(resultat.getString("description"));
                    equipe.setResponsableId(resultat.getInt("responsable_id"));
                    equipes.add(equipe);
                }
            }
            logger.info("Récupération des équipes depuis la base de données.");
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la récupération des équipes : " + e.getMessage(), e);
            throw e;
        }
        return equipes;
    }

    public Equipe getEquipeById(int equipeId) throws SQLException, IOException {
        try (Connection connexion = ConnexionBD.getConnection()) {
            String sql = "SELECT e.id, e.nom, e.description, e.responsable_id FROM equipe e WHERE e.id = ?";
            try (PreparedStatement statement = connexion.prepareStatement(sql)) {
                statement.setInt(1, equipeId);
                try (ResultSet resultat = statement.executeQuery()) {
                    if (resultat.next()) {
                        Equipe equipe = new Equipe();
                        equipe.setId(resultat.getInt("id"));
                        equipe.setNom(resultat.getString("nom"));
                        equipe.setDescription(resultat.getString("description"));
                        equipe.setResponsableId(resultat.getInt("responsable_id"));
                        return equipe;
                    }
                }
            }
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la récupération de l'équipe " + equipeId + " : " + e.getMessage(), e);
            throw e;
        }
        return null;
    }

    public void createEquipe(Equipe equipe) throws SQLException, IOException {
        try (Connection connexion = ConnexionBD.getConnection()) {
            String sql = "INSERT INTO equipe (nom, description, responsable_id) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connexion.prepareStatement(sql)) {
                statement.setString(1, equipe.getNom());
                statement.setString(2, equipe.getDescription());
                statement.setInt(3, equipe.getResponsableId());
                statement.executeUpdate();
            }
            logger.info("Équipe créée : " + equipe.getNom());
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la création de l'équipe : " + e.getMessage(), e);
            throw e;
        }
    }

    public void updateEquipe(Equipe equipe) throws SQLException, IOException {
        try (Connection connexion = ConnexionBD.getConnection()) {
            String sql = "UPDATE equipe SET nom = ?, description = ?, responsable_id = ? WHERE id = ?";
            try (PreparedStatement statement = connexion.prepareStatement(sql)) {
                statement.setString(1, equipe.getNom());
                statement.setString(2, equipe.getDescription());
                statement.setInt(3, equipe.getResponsableId());
                statement.setInt(4, equipe.getId());
                statement.executeUpdate();
            }
            logger.info("Équipe mise à jour : " + equipe.getNom());
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la mise à jour de l'équipe " + equipe.getNom() + " : " + e.getMessage(), e);
            throw e;
        }
    }

    public void deleteEquipe(int equipeId) throws SQLException, IOException {
        try (Connection connexion = ConnexionBD.getConnection()) {
            String sql = "DELETE FROM equipe WHERE id = ?";
            try (PreparedStatement statement = connexion.prepareStatement(sql)) {
                statement.setInt(1, equipeId);
                statement.executeUpdate();
            }
            logger.info("Équipe supprimée : " + equipeId);
        } catch (SQLException | IOException e) {
            logger.error("Erreur lors de la suppression de l'équipe " + equipeId + " : " + e.getMessage(), e);
            throw e;
        }
    }

    public int getEquipeIdByName(String nomEquipe) throws SQLException, IOException {
        String sql = "SELECT id FROM equipe WHERE nom = ?";
        try (Connection connexion = ConnexionBD.getConnection();
             PreparedStatement statement = connexion.prepareStatement(sql)) {
            statement.setString(1, nomEquipe);
            try (ResultSet resultat = statement.executeQuery()) {
                if (resultat.next()) {
                    return resultat.getInt("id");
                }
            }
        }
        return -1; // Retourne -1 si l'équipe n'est pas trouvée
    }

    public Utilisateur getResponsableByEquipeId(int equipeId) throws SQLException, IOException {
        String query = "SELECT u.* FROM utilisateur u " +
                       "JOIN responsable r ON u.id = r.utilisateur_id " +
                       "JOIN equipe e ON r.utilisateur_id = e.responsable_id " +
                       "WHERE e.id = ?";
        try (PreparedStatement stmt = ConnexionBD.getConnection().prepareStatement(query)) {
            stmt.setInt(1, equipeId); // On lie l'ID de l'équipe

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Utilisateur responsable = new Utilisateur();
                responsable.setId(rs.getInt("id"));
                responsable.setNom(rs.getString("nom"));
                responsable.setEmail(rs.getString("email"));
                // Ajouter d'autres informations de l'utilisateur si nécessaire
                return responsable;
            } else {
                throw new SQLException("Aucun responsable trouvé pour l'équipe.");
            }
        }
    }

}
