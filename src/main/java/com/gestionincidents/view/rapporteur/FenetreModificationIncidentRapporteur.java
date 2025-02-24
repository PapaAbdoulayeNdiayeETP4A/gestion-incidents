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

public class FenetreModificationIncidentRapporteur extends JFrame {

    private static final long serialVersionUID = 1L;
    private Incident incident;
    private JComboBox<String> comboApplication;
    private JTextArea champDescription;
    private JComboBox<Priorite> comboPriorite;
    private JComboBox<Statut> comboStatut;
    private Map<String, Integer> applicationMap;

    public FenetreModificationIncidentRapporteur(int incidentId, IncidentController incidentController) throws SQLException {
        this.incident = incidentController.getIncident(incidentId);
        applicationMap = new HashMap<>();

        setTitle("Modification d'un Incident");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panneauPrincipal = new JPanel();
        panneauPrincipal.setLayout(new GridLayout(0, 2));
        add(panneauPrincipal);

        JLabel labelApplication = new JLabel("Application concernée :");
        comboApplication = new JComboBox<>();
        try {
            List<Application> applications = incidentController.getAllApplications();
            for (Application app : applications) {
                comboApplication.addItem(app.getNom());
                applicationMap.put(app.getNom(), app.getId());
            }
            comboApplication.setSelectedItem(incident.getApplicationConcernee().getNom());
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des applications : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        JLabel labelDescription = new JLabel("Description :");
        champDescription = new JTextArea(incident.getDescription());
        JLabel labelPriorite = new JLabel("Priorité :");
        comboPriorite = new JComboBox<>(Priorite.values());
        comboPriorite.setSelectedItem(incident.getPriorite());
        JLabel labelStatut = new JLabel("Statut :");
        comboStatut = new JComboBox<>(Statut.values());
        comboStatut.setSelectedItem(incident.getStatut());
        JButton boutonModifier = new JButton("Modifier");

        panneauPrincipal.add(labelApplication);
        panneauPrincipal.add(comboApplication);
        panneauPrincipal.add(labelDescription);
        panneauPrincipal.add(new JScrollPane(champDescription));
        panneauPrincipal.add(labelPriorite);
        panneauPrincipal.add(comboPriorite);
        panneauPrincipal.add(labelStatut);
        panneauPrincipal.add(comboStatut);
        panneauPrincipal.add(new JLabel());
        panneauPrincipal.add(boutonModifier);

        boutonModifier.addActionListener(e -> {
            String applicationNom = (String) comboApplication.getSelectedItem();
            int applicationId = applicationMap.get(applicationNom);
            String description = champDescription.getText();
            Priorite priorite = (Priorite) comboPriorite.getSelectedItem();
            Statut statut = (Statut) comboStatut.getSelectedItem();

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
                    JOptionPane.showMessageDialog(FenetreModificationIncidentRapporteur.this, "Application non trouvée.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (SQLException | IOException ex) {
                JOptionPane.showMessageDialog(FenetreModificationIncidentRapporteur.this, "Erreur lors de la récupération de l'application : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            incident.setDescription(description);
            incident.setDateModification(LocalDateTime.now());
            incident.setPriorite(priorite);
            incident.setStatut(statut);

            incidentController.updateIncident(incident);
            JOptionPane.showMessageDialog(FenetreModificationIncidentRapporteur.this, "Incident modifié avec succès !");
            dispose();
        });

        setVisible(true);
    }
}