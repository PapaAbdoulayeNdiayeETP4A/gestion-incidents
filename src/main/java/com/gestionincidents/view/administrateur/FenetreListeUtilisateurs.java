package com.gestionincidents.view.administrateur;

import com.gestionincidents.controller.UtilisateurController;
import com.gestionincidents.model.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FenetreListeUtilisateurs extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTable utilisateurTable;
    private DefaultTableModel tableModel;
    private UtilisateurController utilisateurController;

    public FenetreListeUtilisateurs(UtilisateurController utilisateurController) throws SQLException {
        this.utilisateurController = utilisateurController;

        setTitle("Liste des utilisateurs");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panneauPrincipal = new JPanel(new BorderLayout());

        // Tableau des utilisateurs
        String[] colonnes = {"ID", "Nom", "Email", "Rôle"};
        tableModel = new DefaultTableModel(colonnes, 0);
        utilisateurTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(utilisateurTable);
        panneauPrincipal.add(scrollPane, BorderLayout.CENTER);

        // Boutons Modifier et Supprimer
        JPanel panneauBoutons = new JPanel(new FlowLayout());
        JButton modifierButton = new JButton("Modifier");
        JButton supprimerButton = new JButton("Supprimer");
        panneauBoutons.add(modifierButton);
        panneauBoutons.add(supprimerButton);
        panneauPrincipal.add(panneauBoutons, BorderLayout.SOUTH);

        // Action du bouton Modifier
        modifierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifierUtilisateur();
            }
        });

        // Action du bouton Supprimer
        supprimerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                supprimerUtilisateur();
            }
        });

     // Dans FenetreListeUtilisateurs.java

     // ... (autres initialisations)

     List<Utilisateur> utilisateurs = utilisateurController.getUtilisateurs();
	 for (Utilisateur utilisateur : utilisateurs) {
	     if (!utilisateur.isEstSupprime()) { // Exclure les utilisateurs supprimés
	         tableModel.addRow(new Object[]{utilisateur.getId(), utilisateur.getNom(), utilisateur.getEmail(), utilisateur.getRole()});
	     }
	 }

        add(panneauPrincipal);
        setVisible(true);
    }

    private void modifierUtilisateur() {
        int ligneSelectionnee = utilisateurTable.getSelectedRow();
        if (ligneSelectionnee == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un utilisateur à modifier.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int idUtilisateur = (int) tableModel.getValueAt(ligneSelectionnee, 0);
        try {
            Utilisateur utilisateur = utilisateurController.rechercherUtilisateurParId(idUtilisateur);
            if (utilisateur != null) {
                new FenetreModificationUtilisateur(utilisateur, utilisateurController).setVisible(true);
            }
        } catch (SQLException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération de l'utilisateur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerUtilisateur() {
        int ligneSelectionnee = utilisateurTable.getSelectedRow();
        if (ligneSelectionnee == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un utilisateur à supprimer.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int idUtilisateur = (int) tableModel.getValueAt(ligneSelectionnee, 0);
        int reponse = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cet utilisateur ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (reponse == JOptionPane.YES_OPTION) {
            try {
                utilisateurController.supprimerUtilisateur(idUtilisateur);
                tableModel.removeRow(ligneSelectionnee);
                JOptionPane.showMessageDialog(this, "Utilisateur supprimé avec succès.", "Information", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException | IOException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de l'utilisateur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
