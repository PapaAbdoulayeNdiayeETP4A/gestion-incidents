package com.gestionincidents.view.rapporteur;

import com.gestionincidents.controller.IncidentController;
import com.gestionincidents.model.*;

import javax.swing.*;
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
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panneauPrincipal = new JPanel();
        panneauPrincipal.setLayout(new GridLayout(0, 2));
        add(panneauPrincipal);

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
        JLabel labelPriorite = new JLabel("Priorité :");
        JComboBox<Priorite> comboPriorite = new JComboBox<>();
        comboPriorite.addItem(Priorite.FAIBLE);
        comboPriorite.addItem(Priorite.MOYENNE);
        comboPriorite.addItem(Priorite.ELEVEE);
        comboPriorite.addItem(Priorite.URGENTE);
        JButton boutonCreer = new JButton("Créer");

        // Ajout des composants au panneau
        panneauPrincipal.add(labelApplication);
        panneauPrincipal.add(comboApplication);
        panneauPrincipal.add(labelDescription);
        panneauPrincipal.add(new JScrollPane(champDescription));
        panneauPrincipal.add(labelPriorite);
        panneauPrincipal.add(comboPriorite);
        panneauPrincipal.add(new JLabel());
        panneauPrincipal.add(boutonCreer);

        // Gestion des événements
        boutonCreer.addActionListener(e -> {
        	String applicationNom = (String) comboApplication.getSelectedItem();
            int applicationId = applicationMap.get(applicationNom);
            String description = champDescription.getText();
            Priorite priorite = (Priorite) comboPriorite.getSelectedItem();
            Incident incident = new Incident();

            try {
                // Récupérer l'objet Application à partir de la liste des applications
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
            incident.setDateCreation(LocalDateTime.now()); // Utilisation de LocalDateTime
            incident.setPriorite(priorite);
            incident.setStatut(Statut.OUVERT);
            incident.setRapporteur(rapporteur);

            incidentController.createIncident(incident);
			JOptionPane.showMessageDialog(FenetreCreationIncidentRapporteur.this, "Incident créé avec succès !");
			dispose();
        });

        setVisible(true);
    }
}