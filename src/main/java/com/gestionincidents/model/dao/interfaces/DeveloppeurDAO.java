package com.gestionincidents.model.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.gestionincidents.model.Developpeur;

public interface DeveloppeurDAO {
    List<Developpeur> getAllDeveloppeurs() throws SQLException;
    Developpeur getDeveloppeurById(int id) throws SQLException;
    void createDeveloppeur(Developpeur developpeur) throws SQLException;
    void updateDeveloppeur(Developpeur developpeur) throws SQLException;
    void deleteDeveloppeur(int id) throws SQLException;
    // Autres méthodes spécifiques à Developpeur
}