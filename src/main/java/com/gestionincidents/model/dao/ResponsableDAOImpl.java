package com.gestionincidents.model.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gestionincidents.model.Responsable;
import com.gestionincidents.model.dao.interfaces.ResponsableDAO;
import com.gestionincidents.utils.ConnexionBD;

public class ResponsableDAOImpl implements ResponsableDAO {

    private Connection connection;

    public ResponsableDAOImpl() throws SQLException, IOException {
        try {
            connection = ConnexionBD.getConnection();
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public List<Responsable> getAllResponsables() throws SQLException {
        List<Responsable> responsables = new ArrayList<>();
        String sql = "SELECT u.id, u.nom, u.email, u.role, r.departement " +
                "FROM utilisateur u " +
                "INNER JOIN responsable r ON u.id = r.id";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Responsable responsable = new Responsable();
                responsable.setId(resultSet.getInt("id"));
                responsable.setNom(resultSet.getString("nom"));
                responsable.setEmail(resultSet.getString("email"));
                responsable.setRole(resultSet.getString("role"));
                responsable.setDepartement(resultSet.getString("departement"));
                responsables.add(responsable);
            }
        }
        return responsables;
    }

    @Override
    public Responsable getResponsableById(int id) throws SQLException {
        Responsable responsable = null;
        String sql = "SELECT u.id, u.nom, u.email, u.role, r.departement " +
                "FROM utilisateur u " +
                "INNER JOIN responsable r ON u.id = r.id WHERE u.id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    responsable = new Responsable();
                    responsable.setId(resultSet.getInt("id"));
                    responsable.setNom(resultSet.getString("nom"));
                    responsable.setEmail(resultSet.getString("email"));
                    responsable.setRole(resultSet.getString("role"));
                    responsable.setDepartement(resultSet.getString("departement"));
                }
            }
        }
        return responsable;
    }

    @Override
    public void createResponsable(Responsable responsable) throws SQLException {
        String sqlUtilisateur = "INSERT INTO utilisateur (id, nom, email, role) VALUES (?, ?, ?, ?)";
        String sqlResponsable = "INSERT INTO responsable (id, departement) VALUES (?, ?)";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement statementUtilisateur = connection.prepareStatement(sqlUtilisateur)) {
                statementUtilisateur.setInt(1, responsable.getId());
                statementUtilisateur.setString(2, responsable.getNom());
                statementUtilisateur.setString(3, responsable.getEmail());
                statementUtilisateur.setString(4, responsable.getRole());
                statementUtilisateur.executeUpdate();
            }

            try (PreparedStatement statementResponsable = connection.prepareStatement(sqlResponsable)) {
                statementResponsable.setInt(1, responsable.getId());
                statementResponsable.setString(2, responsable.getDepartement());
                statementResponsable.executeUpdate();
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
    public void updateResponsable(Responsable responsable) throws SQLException {
        String sqlUtilisateur = "UPDATE utilisateur SET nom = ?, email = ?, role = ? WHERE id = ?";
        String sqlResponsable = "UPDATE responsable SET departement = ? WHERE id = ?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement statementUtilisateur = connection.prepareStatement(sqlUtilisateur)) {
                statementUtilisateur.setString(1, responsable.getNom());
                statementUtilisateur.setString(2, responsable.getEmail());
                statementUtilisateur.setString(3, responsable.getRole());
                statementUtilisateur.setInt(4, responsable.getId());
                statementUtilisateur.executeUpdate();
            }

            try (PreparedStatement statementResponsable = connection.prepareStatement(sqlResponsable)) {
                statementResponsable.setString(1, responsable.getDepartement());
                statementResponsable.setInt(2, responsable.getId());
                statementResponsable.executeUpdate();
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
    public void deleteResponsable(int id) throws SQLException {
        String sqlUtilisateur = "DELETE FROM utilisateur WHERE id = ?";
        String sqlResponsable = "DELETE FROM responsable WHERE id = ?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement statementResponsable = connection.prepareStatement(sqlResponsable)) {
                statementResponsable.setInt(1, id);
                statementResponsable.executeUpdate();
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