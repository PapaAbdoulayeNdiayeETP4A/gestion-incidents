package com.gestionincidents.view;

import com.gestionincidents.controller.CommentaireController;
import com.gestionincidents.model.Commentaire;
import com.gestionincidents.model.Incident;
import com.gestionincidents.model.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

public class FenetreCreationReponseCommentaire extends JFrame {

    private CommentaireController commentaireController;
    private int incidentId;
    private int commentaireParentId;
    private Utilisateur utilisateur;

    public FenetreCreationReponseCommentaire(int incidentId, int commentaireParentId, Utilisateur utilisateur, CommentaireController commentaireController) throws SQLException, IOException {
        this.incidentId = incidentId;
        this.commentaireParentId = commentaireParentId;
        this.utilisateur = utilisateur;
        this.commentaireController = commentaireController;

        setTitle("Répondre au commentaire " + commentaireParentId);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panneauPrincipal = new JPanel();
        panneauPrincipal.setLayout(new GridLayout(0, 2));
        add(panneauPrincipal);

        JLabel labelCommentaire = new JLabel("Réponse :");
        JTextArea champCommentaire = new JTextArea();
        JButton boutonAjouter = new JButton("Ajouter");

        panneauPrincipal.add(labelCommentaire);
        panneauPrincipal.add(new JScrollPane(champCommentaire));
        panneauPrincipal.add(new JLabel());
        panneauPrincipal.add(boutonAjouter);

        boutonAjouter.addActionListener(e -> {
            String contenu = champCommentaire.getText();
            Commentaire commentaire = new Commentaire(contenu, new Date(), utilisateur, new Incident());
            try {
                commentaireController.createReponseCommentaire(commentaire, incidentId, commentaireParentId);
                JOptionPane.showMessageDialog(FenetreCreationReponseCommentaire.this, "Réponse ajoutée avec succès !");
                dispose();
            } catch (SQLException | IOException ex) {
                JOptionPane.showMessageDialog(FenetreCreationReponseCommentaire.this, "Erreur lors de l'ajout de la réponse : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}