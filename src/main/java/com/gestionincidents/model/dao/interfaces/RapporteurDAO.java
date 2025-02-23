package com.gestionincidents.model.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.gestionincidents.model.Rapporteur;

public interface RapporteurDAO {
    List<Rapporteur> getAllRapporteurs() throws SQLException;
    Rapporteur getRapporteurById(int id) throws SQLException;
    void createRapporteur(Rapporteur rapporteur) throws SQLException;
    void updateRapporteur(Rapporteur rapporteur) throws SQLException;
    void deleteRapporteur(int id) throws SQLException;
    // Autres méthodes spécifiques à Rapporteur
}