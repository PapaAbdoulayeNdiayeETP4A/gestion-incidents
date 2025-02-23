package com.gestionincidents.model.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gestionincidents.model.Rapporteur;
import com.gestionincidents.model.dao.interfaces.RapporteurDAO;
import com.gestionincidents.utils.ConnexionBD;

public class RapporteurDAOImpl implements RapporteurDAO {

    private Connection connection;

    public RapporteurDAOImpl() throws SQLException, IOException {
        try {
            connection = ConnexionBD.getConnection();
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public List<Rapporteur> getAllRapporteurs() throws SQLException {
        List<Rapporteur> rapporteurs = new ArrayList<>();
        String sql = "SELECT u.id, u.nom, u.email, u.role, r.service, r.num_matricule " +
                "FROM utilisateur u " +
                "INNER JOIN rapporteur r ON u.id = r.id";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Rapporteur rapporteur = new Rapporteur();
                rapporteur.setId(resultSet.getInt("id"));
                rapporteur.setNom(resultSet.getString("nom"));
                rapporteur.setEmail(resultSet.getString("email"));
                rapporteur.setRole(resultSet.getString("role"));
                rapporteur.setService(resultSet.getString("service"));
                rapporteur.setNumMatricule(resultSet.getString("num_matricule"));
                rapporteurs.add(rapporteur);
            }
        }
        return rapporteurs;
    }

    @Override
    public Rapporteur getRapporteurById(int id) throws SQLException {
        Rapporteur rapporteur = null;
        String sql = "SELECT u.id, u.nom, u.email, u.role, r.service, r.num_matricule " +
                "FROM utilisateur u " +
                "INNER JOIN rapporteur r ON u.id = r.id WHERE u.id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    rapporteur = new Rapporteur();
                    rapporteur.setId(resultSet.getInt("id"));
                    rapporteur.setNom(resultSet.getString("nom"));
                    rapporteur.setEmail(resultSet.getString("email"));
                    rapporteur.setRole(resultSet.getString("role"));
                    rapporteur.setService(resultSet.getString("service"));
                    rapporteur.setNumMatricule(resultSet.getString("num_matricule"));
                }
            }
        }
        return rapporteur;
    }

    @Override
    public void createRapporteur(Rapporteur rapporteur) throws SQLException {
        String sqlUtilisateur = "INSERT INTO utilisateur (id, nom, email, role) VALUES (?, ?, ?, ?)";
        String sqlRapporteur = "INSERT INTO rapporteur (id, service, num_matricule) VALUES (?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement statementUtilisateur = connection.prepareStatement(sqlUtilisateur)) {
                statementUtilisateur.setInt(1, rapporteur.getId());
                statementUtilisateur.setString(2, rapporteur.getNom());
                statementUtilisateur.setString(3, rapporteur.getEmail());
                statementUtilisateur.setString(4, rapporteur.getRole());
                statementUtilisateur.executeUpdate();
            }

            try (PreparedStatement statementRapporteur = connection.prepareStatement(sqlRapporteur)) {
                statementRapporteur.setInt(1, rapporteur.getId());
                statementRapporteur.setString(2, rapporteur.getService());
                statementRapporteur.setString(3, rapporteur.getNumMatricule());
                statementRapporteur.executeUpdate();
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
    public void updateRapporteur(Rapporteur rapporteur) throws SQLException {
        String sqlUtilisateur = "UPDATE utilisateur SET nom = ?, email = ?, role = ? WHERE id = ?";
        String sqlRapporteur = "UPDATE rapporteur SET service = ?, num_matricule = ? WHERE id = ?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement statementUtilisateur = connection.prepareStatement(sqlUtilisateur)) {
                statementUtilisateur.setString(1, rapporteur.getNom());
                statementUtilisateur.setString(2, rapporteur.getEmail());
                statementUtilisateur.setString(3, rapporteur.getRole());
                statementUtilisateur.setInt(4, rapporteur.getId());
                statementUtilisateur.executeUpdate();
            }

            try (PreparedStatement statementRapporteur = connection.prepareStatement(sqlRapporteur)) {
                statementRapporteur.setString(1, rapporteur.getService());
                statementRapporteur.setString(2, rapporteur.getNumMatricule());
                statementRapporteur.setInt(3, rapporteur.getId());
                statementRapporteur.executeUpdate();
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
    public void deleteRapporteur(int id) throws SQLException {
        String sqlUtilisateur = "DELETE FROM utilisateur WHERE id = ?";
        String sqlRapporteur = "DELETE FROM rapporteur WHERE id = ?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement statementRapporteur = connection.prepareStatement(sqlRapporteur)) {
                statementRapporteur.setInt(1, id);
                statementRapporteur.executeUpdate();
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