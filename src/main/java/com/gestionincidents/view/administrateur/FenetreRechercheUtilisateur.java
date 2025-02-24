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

public class FenetreRechercheUtilisateur extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField rechercheField;
    private JRadioButton nomRadioButton;
    private JRadioButton idRadioButton;
    private JTable resultatTable;
    private DefaultTableModel tableModel;
    private UtilisateurController utilisateurController;
    private JButton modifierButton;
    private JButton supprimerButton;

    public FenetreRechercheUtilisateur(UtilisateurController utilisateurController) {
        this.utilisateurController = utilisateurController;

        setTitle("Rechercher un utilisateur");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panneauPrincipal = new JPanel(new BorderLayout());

        // Panneau de recherche
        JPanel panneauRecherche = new JPanel(new FlowLayout());
        rechercheField = new JTextField(20);
        nomRadioButton = new JRadioButton("Nom");
        idRadioButton = new JRadioButton("ID");
        ButtonGroup groupeRadio = new ButtonGroup();
        groupeRadio.add(nomRadioButton);
        groupeRadio.add(idRadioButton);
        nomRadioButton.setSelected(true); // Par défaut, recherche par nom
        JButton rechercherButton = new JButton("Rechercher");
        panneauRecherche.add(rechercheField);
        panneauRecherche.add(nomRadioButton);
        panneauRecherche.add(idRadioButton);
        panneauRecherche.add(rechercherButton);
        panneauPrincipal.add(panneauRecherche, BorderLayout.NORTH);

        modifierButton = new JButton("Modifier");
        supprimerButton = new JButton("Supprimer");
        JPanel panneauBoutons = new JPanel(new FlowLayout());
        panneauBoutons.add(modifierButton);
        panneauBoutons.add(supprimerButton);
        panneauPrincipal.add(panneauBoutons, BorderLayout.SOUTH);

        // Action du bouton Modifier
        modifierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    modifierUtilisateur();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        // Action du bouton Supprimer
        supprimerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    supprimerUtilisateur();
                } catch (SQLException | IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        // Tableau des résultats
        String[] colonnes = {"ID", "Nom", "Email", "Rôle"};
        tableModel = new DefaultTableModel(colonnes, 0);
        resultatTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultatTable);
        panneauPrincipal.add(scrollPane, BorderLayout.CENTER);

        // Action du bouton Rechercher
        rechercherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    rechercherUtilisateur();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        add(panneauPrincipal);
        setVisible(true);
    }

    private void rechercherUtilisateur() throws IOException {
        String recherche = rechercheField.getText();
        tableModel.setRowCount(0); // Effacer les résultats précédents

        try {
            if (nomRadioButton.isSelected()) {
                List<Utilisateur> utilisateurs = utilisateurController.rechercherUtilisateursParNom(recherche);
                for (Utilisateur utilisateur : utilisateurs) {
                    if (!utilisateur.isEstSupprime()) { // Exclure les utilisateurs supprimés
                        tableModel.addRow(new Object[]{utilisateur.getId(), utilisateur.getNom(), utilisateur.getEmail(), utilisateur.getRole()});
                    }
                }
            } else if (idRadioButton.isSelected()) {
                try {
                    int id = Integer.parseInt(recherche);
                    Utilisateur utilisateur = utilisateurController.rechercherUtilisateurParId(id);
                    if (utilisateur != null && !utilisateur.isEstSupprime()) { // Exclure les utilisateurs supprimés
                        tableModel.addRow(new Object[]{utilisateur.getId(), utilisateur.getNom(), utilisateur.getEmail(), utilisateur.getRole()});
                    } else {
                        JOptionPane.showMessageDialog(this, "Utilisateur non trouvé ou supprimé.", "Information", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Veuillez saisir un ID valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void modifierUtilisateur() throws IOException {
        int ligneSelectionnee = resultatTable.getSelectedRow();
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
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération de l'utilisateur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerUtilisateur() throws SQLException, IOException {
        int ligneSelectionnee = resultatTable.getSelectedRow();
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
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de l'utilisateur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}