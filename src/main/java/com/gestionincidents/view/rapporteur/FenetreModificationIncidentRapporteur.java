package com.gestionincidents.view.rapporteur;

import com.gestionincidents.controller.IncidentController;
import com.gestionincidents.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private Color accentColor = new Color(70, 130, 180);
    private Color backgroundColor = new Color(245, 245, 245);
    private Color textColor = new Color(50, 50, 50);

    public FenetreModificationIncidentRapporteur(int incidentId, IncidentController incidentController) throws SQLException {
        this.incident = incidentController.getIncident(incidentId);
        applicationMap = new HashMap<>();

        setTitle("Modification d'un Incident");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(backgroundColor);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(backgroundColor);
        add(mainPanel);
     // ... (Partie précédente du code)

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel labelApplication = new JLabel("Application concernée :");
        labelApplication.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelApplication.setForeground(textColor);
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
        labelDescription.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelDescription.setForeground(textColor);
        champDescription = new JTextArea(incident.getDescription());
        champDescription.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        champDescription.setLineWrap(true);
        champDescription.setWrapStyleWord(true);
        JScrollPane scrollPaneDescription = new JScrollPane(champDescription);

        JLabel labelPriorite = new JLabel("Priorité :");
        labelPriorite.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelPriorite.setForeground(textColor);
        comboPriorite = new JComboBox<>(Priorite.values());
        comboPriorite.setSelectedItem(incident.getPriorite());

        JLabel labelStatut = new JLabel("Statut :");
        labelStatut.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelStatut.setForeground(textColor);
        comboStatut = new JComboBox<>(Statut.values());
        comboStatut.setSelectedItem(incident.getStatut());

        JButton boutonModifier = createStyledButton("Modifier");

        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(labelApplication, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainPanel.add(comboApplication, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        mainPanel.add(labelDescription, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        mainPanel.add(scrollPaneDescription, gbc);

        gbc.gridx = 0;
        gbc.gridy += 3;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weighty = 0;
        mainPanel.add(labelPriorite, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(comboPriorite, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(labelStatut, gbc);

        gbc.gridx = 1;
        mainPanel.add(comboStatut, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(boutonModifier, gbc);

        boutonModifier.addActionListener(e -> {
            String applicationNom = (String) comboApplication.getSelectedItem();
            int applicationId = applicationMap.get(applicationNom);
            String description = champDescription.getText();
            Priorite priorite = (Priorite) comboPriorite.getSelectedItem();
            Statut statut = (Statut) comboStatut.getSelectedItem();

            try {
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

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBackground(Color.WHITE);
        button.setForeground(accentColor);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentColor, 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(accentColor);
                button.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
                button.setForeground(accentColor);
            }
        });

        return button;
    }
}