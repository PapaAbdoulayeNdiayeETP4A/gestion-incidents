package com.gestionincidents.view;

import com.gestionincidents.controller.CommentaireController;
import com.gestionincidents.model.Commentaire;
import com.gestionincidents.model.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FenetreAffichageCommentaires extends JFrame {

    private int incidentId;
    private CommentaireController commentaireController;
    private DefaultTableModel modeleTableau;

    public FenetreAffichageCommentaires(int incidentId, CommentaireController commentaireController, Utilisateur rapporteur) throws SQLException, IOException {
        this.incidentId = incidentId;
        this.commentaireController = commentaireController;

        setTitle("Commentaires de l'incident " + incidentId);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panneauPrincipal = new JPanel(new BorderLayout());
        add(panneauPrincipal);

        String[] colonnes = {"Auteur", "Date", "Contenu", "Répondre à"};
        modeleTableau = new DefaultTableModel(colonnes, 0);
        JTable tableauCommentaires = new JTable(modeleTableau);
        JScrollPane scrollPane = new JScrollPane(tableauCommentaires);
        panneauPrincipal.add(scrollPane, BorderLayout.CENTER);

        mettreAJourTableau();

        JButton ajouterCommentaire = new JButton("Ajouter un commentaire");
        ajouterCommentaire.addActionListener(e -> {
            try {
                new FenetreCreationCommentaire(incidentId, rapporteur, commentaireController).setVisible(true);
            } catch (SQLException | IOException ex) {
                JOptionPane.showMessageDialog(FenetreAffichageCommentaires.this, "Erreur lors de l'ouverture de la fenêtre de commentaire : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        panneauPrincipal.add(ajouterCommentaire, BorderLayout.SOUTH);

        tableauCommentaires.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tableauCommentaires.rowAtPoint(evt.getPoint());
                int col = tableauCommentaires.columnAtPoint(evt.getPoint());
                if (col == 3 && row >= 0) { // Colonne "Répondre à"
                    int commentaireParentId = (int) modeleTableau.getValueAt(row, 0); // ID du commentaire parent
                    try {
                        new FenetreCreationReponseCommentaire(incidentId, commentaireParentId, rapporteur, commentaireController).setVisible(true);
                    } catch (SQLException | IOException ex) {
                        JOptionPane.showMessageDialog(FenetreAffichageCommentaires.this, "Erreur lors de l'ouverture de la fenêtre de réponse : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        setVisible(true);
    }

    public void mettreAJourTableau() throws SQLException, IOException {
        modeleTableau.setRowCount(0);
        List<Commentaire> commentaires = commentaireController.getCommentaires(incidentId);
        if (commentaires != null) {
            for (Commentaire commentaire : commentaires) {
                Object[] donnee = {
                        commentaire.getId(),
                        commentaire.getAuteur().getNom(),
                        commentaire.getDate(),
                        commentaire.getContenu(),
                        commentaire.getCommentaireParentId() != null ? "Répondre" : ""
                };
                modeleTableau.addRow(donnee);
            }
        }
    }
}