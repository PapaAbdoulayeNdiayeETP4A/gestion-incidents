package com.gestionincidents.view;

import com.gestionincidents.controller.IncidentController;
import com.gestionincidents.model.Incident;
import com.gestionincidents.model.Statut;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class FenetreChangementStatutIncident extends JFrame {

    private Incident incident;
    private IncidentController incidentController;
    private JComboBox<Statut> comboStatut;

    public FenetreChangementStatutIncident(int incidentId, IncidentController incidentController) throws SQLException {
        this.incidentController = incidentController;
        this.incident = incidentController.getIncident(incidentId);

        setTitle("Changement de Statut d'un Incident");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panneauPrincipal = new JPanel();
        panneauPrincipal.setLayout(new GridLayout(0, 2));
        add(panneauPrincipal);

        JLabel labelStatut = new JLabel("Nouveau Statut :");
        comboStatut = new JComboBox<>(Statut.values());
        comboStatut.setSelectedItem(incident.getStatut());
        JButton boutonChanger = new JButton("Changer");

        panneauPrincipal.add(labelStatut);
        panneauPrincipal.add(comboStatut);
        panneauPrincipal.add(new JLabel());
        panneauPrincipal.add(boutonChanger);

        boutonChanger.addActionListener(e -> {
            Statut nouveauStatut = (Statut) comboStatut.getSelectedItem();
            try {
                incidentController.changerStatutIncident(incident.getId(), nouveauStatut);
                JOptionPane.showMessageDialog(FenetreChangementStatutIncident.this, "Statut modifié avec succès !");
                dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(FenetreChangementStatutIncident.this, "Erreur lors du changement de statut : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}