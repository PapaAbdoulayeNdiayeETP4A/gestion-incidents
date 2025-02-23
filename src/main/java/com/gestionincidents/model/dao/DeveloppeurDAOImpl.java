package com.gestionincidents.model.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gestionincidents.model.Developpeur;
import com.gestionincidents.model.dao.interfaces.DeveloppeurDAO;
import com.gestionincidents.utils.ConnexionBD;

public class DeveloppeurDAOImpl implements DeveloppeurDAO {

    private Connection connection;

    public DeveloppeurDAOImpl() throws SQLException, IOException {
        try {
            connection = ConnexionBD.getConnection();
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public List<Developpeur> getAllDeveloppeurs() throws SQLException {
        List<Developpeur> developpeurs = new ArrayList<>();
        String sql = "SELECT u.id, u.nom, u.email, u.role, d.specialisation, d.niveau, d.anciennete, d.equipe_id " +
                "FROM utilisateur u " +
                "INNER JOIN developpeur d ON u.id = d.id";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Developpeur developpeur = new Developpeur();
                developpeur.setId(resultSet.getInt("id"));
                developpeur.setNom(resultSet.getString("nom"));
                developpeur.setEmail(resultSet.getString("email"));
                developpeur.setRole(resultSet.getString("role"));
                developpeur.setSpecialisation(resultSet.getString("specialisation"));
                developpeur.setNiveau(resultSet.getString("niveau"));
                developpeur.setAnciennete(resultSet.getInt("anciennete"));
                // Récupérer l'équipe (si nécessaire)
                developpeurs.add(developpeur);
            }
        }
        return developpeurs;
    }

    @Override
    public Developpeur getDeveloppeurById(int id) throws SQLException {
        String sql = "SELECT u.id, u.nom, u.email, u.role, d.specialisation, d.niveau, d.anciennete, d.equipe_id " +
                "FROM utilisateur u " +
                "INNER JOIN developpeur d ON u.id = d.id " +
                "WHERE u.id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Developpeur developpeur = new Developpeur();
                    developpeur.setId(resultSet.getInt("id"));
                    developpeur.setNom(resultSet.getString("nom"));
                    developpeur.setEmail(resultSet.getString("email"));
                    developpeur.setRole(resultSet.getString("role"));
                    developpeur.setSpecialisation(resultSet.getString("specialisation"));
                    developpeur.setNiveau(resultSet.getString("niveau"));
                    developpeur.setAnciennete(resultSet.getInt("anciennete"));
                    return developpeur;
                }
            }
        }
        return null;
    }

    @Override
    public void createDeveloppeur(Developpeur developpeur) throws SQLException {
        String sqlUtilisateur = "INSERT INTO utilisateur (id, nom, email, role) VALUES (?, ?, ?, ?)";
        String sqlDeveloppeur = "INSERT INTO developpeur (id, specialisation, niveau, anciennete, equipe_id) VALUES (?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false); // Début de la transaction

            // Insertion dans la table utilisateur
            try (PreparedStatement statementUtilisateur = connection.prepareStatement(sqlUtilisateur)) {
                statementUtilisateur.setInt(1, developpeur.getId());
                statementUtilisateur.setString(2, developpeur.getNom());
                statementUtilisateur.setString(3, developpeur.getEmail());
                statementUtilisateur.setString(4, developpeur.getRole());
                statementUtilisateur.executeUpdate();
            }

            // Insertion dans la table developpeur
            try (PreparedStatement statementDeveloppeur = connection.prepareStatement(sqlDeveloppeur)) {
                statementDeveloppeur.setInt(1, developpeur.getId());
                statementDeveloppeur.setString(2, developpeur.getSpecialisation());
                statementDeveloppeur.setString(3, developpeur.getNiveau());
                statementDeveloppeur.setInt(4, developpeur.getAnciennete());
                // statementDeveloppeur.setInt(5, developpeur.getEquipeId()); // Si vous avez un champ equipeId
                statementDeveloppeur.setNull(5, java.sql.Types.INTEGER); // Si equipeId peut être null
                statementDeveloppeur.executeUpdate();
            }

            connection.commit(); // Validation de la transaction
        } catch (SQLException e) {
            connection.rollback(); // Annulation de la transaction en cas d'erreur
            throw e;
        } finally {
            connection.setAutoCommit(true); // Réinitialisation de l'auto-commit
        }
    }

    @Override
    public void updateDeveloppeur(Developpeur developpeur) throws SQLException {
        String sqlUtilisateur = "UPDATE utilisateur SET nom = ?, email = ?, role = ? WHERE id = ?";
        String sqlDeveloppeur = "UPDATE developpeur SET specialisation = ?, niveau = ?, anciennete = ?, equipe_id = ? WHERE id = ?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement statementUtilisateur = connection.prepareStatement(sqlUtilisateur)) {
                statementUtilisateur.setString(1, developpeur.getNom());
                statementUtilisateur.setString(2, developpeur.getEmail());
                statementUtilisateur.setString(3, developpeur.getRole());
                statementUtilisateur.setInt(4, developpeur.getId());
                statementUtilisateur.executeUpdate();
            }

            try (PreparedStatement statementDeveloppeur = connection.prepareStatement(sqlDeveloppeur)) {
                statementDeveloppeur.setString(1, developpeur.getSpecialisation());
                statementDeveloppeur.setString(2, developpeur.getNiveau());
                statementDeveloppeur.setInt(3, developpeur.getAnciennete());
                // statementDeveloppeur.setInt(4, developpeur.getEquipeId()); // Si vous avez un champ equipeId
                statementDeveloppeur.setNull(4, java.sql.Types.INTEGER); // Si equipeId peut être null
                statementDeveloppeur.setInt(5, developpeur.getId());
                statementDeveloppeur.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void deleteDeveloppeur(int id) throws SQLException {
        String sqlDeveloppeur = "DELETE FROM developpeur WHERE id = ?";
        String sqlUtilisateur = "DELETE FROM utilisateur WHERE id = ?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement statementDeveloppeur = connection.prepareStatement(sqlDeveloppeur)) {
                statementDeveloppeur.setInt(1, id);
                statementDeveloppeur.executeUpdate();
            }

            try (PreparedStatement statementUtilisateur = connection.prepareStatement(sqlUtilisateur)) {
                statementUtilisateur.setInt(1, id);
                statementUtilisateur.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}