package com.gestionincidents.view.responsable;

import com.gestionincidents.controller.IncidentController;
import com.gestionincidents.controller.UtilisateurController;
import com.gestionincidents.model.Incident;
import com.gestionincidents.model.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class FenetreAssignationIncidentResponsable extends JFrame {

	private static final long serialVersionUID = 1L;
	private JComboBox<Incident> comboIncident;
    private JComboBox<Utilisateur> comboDeveloppeur;
    private IncidentController incidentController;
    private UtilisateurController utilisateurController;

    public FenetreAssignationIncidentResponsable() throws SQLException, IOException {
        this.incidentController = new IncidentController();
        this.utilisateurController = new UtilisateurController();

        setTitle("Assignation d'un Incident (Responsable)");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panneauPrincipal = new JPanel(new GridLayout(0, 2));
        add(panneauPrincipal);

        // Composants
        JLabel labelIncident = new JLabel("Incident :");
        comboIncident = new JComboBox<>();
        List<Incident> incidents = incidentController.getIncidents();
		for (Incident incident : incidents) {
		    comboIncident.addItem(incident);
		}

        JLabel labelDeveloppeur = new JLabel("Développeur :");
        comboDeveloppeur = new JComboBox<>();
        try {
            List<Utilisateur> developpeurs = utilisateurController.getUtilisateurs().stream()
                    .filter(utilisateur -> "Développeur".equals(utilisateur.getRole()))
                    .collect(Collectors.toList());
            for (Utilisateur developpeur : developpeurs) {
                comboDeveloppeur.addItem(developpeur);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des développeurs : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        JButton boutonAssigner = new JButton("Assigner");

        // Ajout des composants au panneau
        panneauPrincipal.add(labelIncident);
        panneauPrincipal.add(comboIncident);
        panneauPrincipal.add(labelDeveloppeur);
        panneauPrincipal.add(comboDeveloppeur);
        panneauPrincipal.add(new JLabel()); // Espace vide
        panneauPrincipal.add(boutonAssigner);

        // Gestion des événements
        boutonAssigner.addActionListener(e -> {
            Incident incidentSelectionne = (Incident) comboIncident.getSelectedItem();
            Utilisateur developpeurSelectionne = (Utilisateur) comboDeveloppeur.getSelectedItem();

            if (incidentSelectionne != null && developpeurSelectionne != null) {
                incidentSelectionne.setAssigneA(developpeurSelectionne);
                incidentController.updateIncident(incidentSelectionne);
				JOptionPane.showMessageDialog(FenetreAssignationIncidentResponsable.this, "Incident assigné avec succès !");
				dispose();
            } else {
                JOptionPane.showMessageDialog(FenetreAssignationIncidentResponsable.this, "Veuillez sélectionner un incident et un développeur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}