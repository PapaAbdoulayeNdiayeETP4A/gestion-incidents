package com.gestionincidents.controller;

import com.gestionincidents.model.dao.CommentaireDAO;
import com.gestionincidents.model.Commentaire;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CommentaireController {

    private CommentaireDAO commentaireDAO;

    public CommentaireController() throws SQLException, IOException {
        this.commentaireDAO = new CommentaireDAO();
    }

    public List<Commentaire> getCommentaires(int incidentId) throws SQLException, IOException {
        return commentaireDAO.getCommentaires(incidentId);
    }

    public Commentaire getCommentaire(int id) throws SQLException, IOException {
        return commentaireDAO.getCommentaireById(id);
    }

    public void createCommentaire(Commentaire commentaire, int incidentId) throws SQLException, IOException {
        commentaireDAO.createCommentaire(commentaire, incidentId);
    }

    public void updateCommentaire(Commentaire commentaire) throws SQLException, IOException {
        commentaireDAO.updateCommentaire(commentaire);
    }

    public void deleteCommentaire(int id) throws SQLException, IOException {
        commentaireDAO.deleteCommentaire(id);
    }
    
    public void createReponseCommentaire(Commentaire commentaire, int incidentId, int commentaireParentId) throws SQLException, IOException {
        commentaireDAO.createReponseCommentaire(commentaire, incidentId, commentaireParentId);
    }
}