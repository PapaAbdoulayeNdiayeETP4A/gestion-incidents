package com.gestionincidents.view.rapporteur;

import com.gestionincidents.controller.IncidentController;
import com.gestionincidents.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FenetreCreationIncidentRapporteur extends JFrame {

    private static final long serialVersionUID = 1L;
    private IncidentController incidentController;
    private Map<String, Integer> applicationMap;

    public FenetreCreationIncidentRapporteur(Utilisateur rapporteur) throws SQLException {
        applicationMap = new HashMap<>();
        try {
            this.incidentController = new IncidentController();
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'initialisation du contrôleur d'incidents : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setTitle("Création d'un Incident (Rapporteur)");
        setSize(600, 450); // Augmentation de la hauteur pour plus d'espace
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panneauPrincipal = new JPanel();
        panneauPrincipal.setLayout(new GridBagLayout()); // Utilisation de GridBagLayout pour plus de flexibilité
        panneauPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20)); // Ajout de marges
        add(panneauPrincipal);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Champs du formulaire
        JLabel labelApplication = new JLabel("Application concernée :");
        JComboBox<String> comboApplication = new JComboBox<>();
        try {
            List<Application> applications = incidentController.getAllApplications();
            for (Application app : applications) {
                comboApplication.addItem(app.getNom());
                applicationMap.put(app.getNom(), app.getId());
            }
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des applications : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        JLabel labelDescription = new JLabel("Description :");
        JTextArea champDescription = new JTextArea();
        champDescription.setLineWrap(true);
        champDescription.setWrapStyleWord(true);
        JScrollPane scrollPaneDescription = new JScrollPane(champDescription);
        JLabel labelPriorite = new JLabel("Priorité :");
        JComboBox<Priorite> comboPriorite = new JComboBox<>();
        comboPriorite.addItem(Priorite.FAIBLE);
        comboPriorite.addItem(Priorite.MOYENNE);
        comboPriorite.addItem(Priorite.ELEVEE);
        comboPriorite.addItem(Priorite.URGENTE);
        JButton boutonCreer = new JButton("Créer");
        boutonCreer.setBackground(new Color(50, 150, 250)); // Couleur bleue pour le bouton
        boutonCreer.setForeground(Color.WHITE);
        boutonCreer.setFocusPainted(false);

        // Ajout des composants au panneau
        gbc.gridx = 0;
        gbc.gridy++;
        panneauPrincipal.add(labelApplication, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panneauPrincipal.add(comboApplication, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panneauPrincipal.add(labelDescription, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        panneauPrincipal.add(scrollPaneDescription, gbc);

        gbc.gridx = 0;
        gbc.gridy += 3;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weighty = 0;
        panneauPrincipal.add(labelPriorite, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panneauPrincipal.add(comboPriorite, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        panneauPrincipal.add(boutonCreer, gbc);

        // Gestion des événements
        boutonCreer.addActionListener(e -> {
            String applicationNom = (String) comboApplication.getSelectedItem();
            int applicationId = applicationMap.get(applicationNom);
            String description = champDescription.getText();
            Priorite priorite = (Priorite) comboPriorite.getSelectedItem();
            Incident incident = new Incident();

            try {
                List<Application> applications = incidentController.getAllApplications();
                Application applicationSelectionnee = applications.stream()
                        .filter(app -> app.getId() == applicationId)
                        .findFirst()
                        .orElse(null);

                if (applicationSelectionnee != null) {
                    incident.setApplicationConcernee(applicationSelectionnee);
                } else {
                    JOptionPane.showMessageDialog(FenetreCreationIncidentRapporteur.this, "Application non trouvée.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (SQLException | IOException ex) {
                JOptionPane.showMessageDialog(FenetreCreationIncidentRapporteur.this, "Erreur lors de la récupération de l'application : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            incident.setDescription(description);
            incident.setDateCreation(LocalDateTime.now());
            incident.setPriorite(priorite);
            incident.setStatut(Statut.NOUVEAU);
            incident.setRapporteur(rapporteur);

            incidentController.createIncident(incident);
            JOptionPane.showMessageDialog(FenetreCreationIncidentRapporteur.this, "Incident créé avec succès !");
            dispose();
        });

        setVisible(true);
    }
}