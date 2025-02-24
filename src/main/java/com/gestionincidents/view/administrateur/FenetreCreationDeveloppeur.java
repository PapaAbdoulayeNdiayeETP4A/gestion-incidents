package com.gestionincidents.view.administrateur;

import com.gestionincidents.controller.UtilisateurController;
import com.gestionincidents.model.Equipe;
import com.gestionincidents.model.Utilisateur;
import com.gestionincidents.model.dao.EquipeDAO;
import com.gestionincidents.model.dao.UtilisateurDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FenetreCreationDeveloppeur extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField specialisationField;
    private JTextField niveauField;
    private JTextField ancienneteField;
    private JComboBox<String> equipeComboBox;
    private JComboBox<String> responsableComboBox;
    private UtilisateurController utilisateurController;
    private int utilisateurId;
    private EquipeDAO equipeDAO;
    private UtilisateurDAO utilisateurDAO;

    public FenetreCreationDeveloppeur(int utilisateurId, UtilisateurController utilisateurController) throws IOException {
        this.utilisateurId = utilisateurId;
        this.utilisateurController = utilisateurController;
        this.equipeDAO = new EquipeDAO();
        this.utilisateurDAO = new UtilisateurDAO();

        setTitle("Informations développeur");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panneauPrincipal = new JPanel(new GridLayout(6, 2));
        panneauPrincipal.add(new JLabel("Spécialisation :"));
        specialisationField = new JTextField();
        panneauPrincipal.add(specialisationField);
        panneauPrincipal.add(new JLabel("Niveau :"));
        niveauField = new JTextField();
        panneauPrincipal.add(niveauField);
        panneauPrincipal.add(new JLabel("Ancienneté :"));
        ancienneteField = new JTextField();
        panneauPrincipal.add(ancienneteField);

        // Équipe ComboBox
        panneauPrincipal.add(new JLabel("Équipe :"));
        equipeComboBox = new JComboBox<>();
        try {
            List<Equipe> equipes = equipeDAO.getEquipes();
            for (Equipe equipe : equipes) {
                equipeComboBox.addItem(equipe.getNom());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des équipes : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        panneauPrincipal.add(equipeComboBox);

        // Responsable ComboBox
        panneauPrincipal.add(new JLabel("Responsable :"));
        responsableComboBox = new JComboBox<>();
        try {
            List<Utilisateur> responsables = utilisateurDAO.getAllResponsables();
            for (Utilisateur responsable : responsables) {
                responsableComboBox.addItem(responsable.getNom());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des responsables : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        panneauPrincipal.add(responsableComboBox);

        JButton creerButton = new JButton("Créer");
        creerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    createDeveloppeur();
                } catch (NumberFormatException | IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        panneauPrincipal.add(creerButton);

        add(panneauPrincipal);
        setVisible(true);
    }

    private void createDeveloppeur() throws NumberFormatException, IOException {
        try {
            // Récupérer les IDs à partir des noms sélectionnés
            int equipeId = equipeDAO.getEquipeIdByName((String) equipeComboBox.getSelectedItem());
            int responsableId = utilisateurDAO.getResponsableIdByName((String) responsableComboBox.getSelectedItem());

            utilisateurController.createDeveloppeur(utilisateurId, specialisationField.getText(), niveauField.getText(), Integer.parseInt(ancienneteField.getText()), equipeId, responsableId);
            JOptionPane.showMessageDialog(this, "Informations développeur enregistrées.");
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement des informations développeur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}