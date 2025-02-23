package com.gestionincidents.view.responsable;

import javax.swing.*;
import java.awt.*;

public class FenetreAssignationIncidentResponsable extends JFrame {

    private JComboBox<String> comboIncident;
    private JComboBox<String> comboDeveloppeur;

    public FenetreAssignationIncidentResponsable() {
        setTitle("Assignation d'un Incident (Responsable)");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panneauPrincipal = new JPanel(new GridLayout(0, 2));
        add(panneauPrincipal);

        // Composants
        JLabel labelIncident = new JLabel("Incident :");
        comboIncident = new JComboBox<>(); // Remplir avec les incidents
        JLabel labelDeveloppeur = new JLabel("Développeur :");
        comboDeveloppeur = new JComboBox<>(); // Remplir avec les développeurs
        JButton boutonAssigner = new JButton("Assigner");

        // Ajout des composants au panneau
        panneauPrincipal.add(labelIncident);
        panneauPrincipal.add(comboIncident);
        panneauPrincipal.add(labelDeveloppeur);
        panneauPrincipal.add(comboDeveloppeur);
        panneauPrincipal.add(new JLabel()); // Espace vide
        panneauPrincipal.add(boutonAssigner);

        // Gestion des événements (exemple)
        boutonAssigner.addActionListener(e -> {
            // Logique pour assigner l'incident
            JOptionPane.showMessageDialog(FenetreAssignationIncidentResponsable.this, "Incident assigné !");
            dispose(); // Fermer la fenêtre
        });

        setVisible(true);
    }
}