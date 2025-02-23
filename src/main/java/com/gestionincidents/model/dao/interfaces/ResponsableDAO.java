package com.gestionincidents.model.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.gestionincidents.model.Responsable;

public interface ResponsableDAO {
    List<Responsable> getAllResponsables() throws SQLException;
    Responsable getResponsableById(int id) throws SQLException;
    void createResponsable(Responsable responsable) throws SQLException;
    void updateResponsable(Responsable responsable) throws SQLException;
    void deleteResponsable(int id) throws SQLException;
    // Autres méthodes spécifiques à Responsable
}