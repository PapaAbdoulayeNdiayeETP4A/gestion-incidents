package com.gestionincidents.view.rapporteur;

import com.gestionincidents.controller.IncidentController;
import com.gestionincidents.model.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class FenetreModificationIncidentRapporteur extends JFrame {

    private Incident incident;
    private IncidentController incidentController;

    private JComboBox<String> comboApplication;
    private JTextArea champDescription;
    private JComboBox<Priorite> comboPriorite;
    private JComboBox<Statut> comboStatut;

    public FenetreModificationIncidentRapporteur(int incidentId, IncidentController incidentController) throws SQLException {
        this.incidentController = incidentController;
        this.incident = incidentController.getIncident(incidentId);

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
            String description = champDescription.getText();
            Priorite priorite = (Priorite) comboPriorite.getSelectedItem();
            Statut statut = (Statut) comboStatut.getSelectedItem();

            incident.getApplicationConcernee().setNom(applicationNom);
            incident.setDescription(description);
            incident.setDateModification(new Date());
            incident.setPriorite(priorite);
            incident.setStatut(statut);

            incidentController.updateIncident(incident);
			JOptionPane.showMessageDialog(FenetreModificationIncidentRapporteur.this, "Incident modifié avec succès !");
			dispose();
        });

        setVisible(true);
    }
}