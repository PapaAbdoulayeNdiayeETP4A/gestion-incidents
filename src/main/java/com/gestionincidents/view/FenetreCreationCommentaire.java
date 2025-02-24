package com.gestionincidents.view;

import com.gestionincidents.controller.CommentaireController;
import com.gestionincidents.model.Commentaire;
import com.gestionincidents.model.Incident;
import com.gestionincidents.model.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class FenetreCreationCommentaire extends JFrame {

	private static final long serialVersionUID = 1L;
	public FenetreCreationCommentaire(int incidentId, Utilisateur utilisateur, CommentaireController commentaireController) throws SQLException, IOException {
        setTitle("Ajouter un Commentaire");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panneauPrincipal = new JPanel();
        panneauPrincipal.setLayout(new GridLayout(0, 2));
        add(panneauPrincipal);

        JLabel labelCommentaire = new JLabel("Commentaire :");
        JTextArea champCommentaire = new JTextArea();
        JButton boutonAjouter = new JButton("Ajouter");

        panneauPrincipal.add(labelCommentaire);
        panneauPrincipal.add(new JScrollPane(champCommentaire));
        panneauPrincipal.add(new JLabel());
        panneauPrincipal.add(boutonAjouter);

        boutonAjouter.addActionListener(e -> {
            String contenu = champCommentaire.getText();
            Commentaire commentaire = new Commentaire(contenu, LocalDateTime.now(), utilisateur, new Incident());
            try {
                commentaireController.createCommentaire(commentaire, incidentId);
                JOptionPane.showMessageDialog(FenetreCreationCommentaire.this, "Commentaire ajouté avec succès !");
                dispose();
            } catch (SQLException | IOException ex) {
                JOptionPane.showMessageDialog(FenetreCreationCommentaire.this, "Erreur lors de l'ajout du commentaire : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}