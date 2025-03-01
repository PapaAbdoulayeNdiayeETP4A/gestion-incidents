package com.gestionincidents.view.responsable;

import com.gestionincidents.controller.ApplicationController;
import com.gestionincidents.controller.EquipeController;
import com.gestionincidents.controller.IncidentController;
import com.gestionincidents.controller.UtilisateurController;
import com.gestionincidents.model.Application;
import com.gestionincidents.model.Equipe;
import com.gestionincidents.model.Incident;
import com.gestionincidents.model.Utilisateur;
import com.gestionincidents.model.Statut;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.io.IOException;
import java.util.List;

public class FenetreAssignationIncidentResponsable extends JFrame {

	private static final long serialVersionUID = 1L;
	private JComboBox<Utilisateur> developpeurComboBox;
    private JComboBox<Incident> incidentComboBox;
    private IncidentController incidentController;
    private UtilisateurController utilisateurController;
    private Color accentColor = new Color(70, 130, 180);
    private Color backgroundColor = new Color(245, 245, 245);
    private Color textColor = new Color(50, 50, 50);

    public FenetreAssignationIncidentResponsable(IncidentController incidentController, UtilisateurController utilisateurController, Utilisateur responsable) {
        this.incidentController = incidentController;
        this.utilisateurController = utilisateurController;

        setTitle("Assigner un incident");
        setSize(500, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(backgroundColor);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(backgroundColor);
        add(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Développeurs
        JLabel developpeurLabel = new JLabel("Développeur :");
        developpeurLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        developpeurLabel.setForeground(textColor);
        mainPanel.add(developpeurLabel, gbc);

        developpeurComboBox = new JComboBox<>();
        try {
            List<Utilisateur> developpeurs = utilisateurController.getDeveloppeursParEquipe(responsable.getId());
            for (Utilisateur developpeur : developpeurs) {
                if (!developpeur.isEstSupprime()) {
                    developpeurComboBox.addItem(developpeur);
                }
            }
        } catch (SQLException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des développeurs : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        developpeurComboBox.setRenderer(new DefaultListCellRenderer() {

			private static final long serialVersionUID = 1L;

			@Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Utilisateur) {
                    Utilisateur developpeur = (Utilisateur) value;
                    setText(developpeur.getNom());
                }
                return this;
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainPanel.add(developpeurComboBox, gbc);

        // Incidents ouverts
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel incidentLabel = new JLabel("Incident :");
        incidentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        incidentLabel.setForeground(textColor);
        mainPanel.add(incidentLabel, gbc);

        incidentComboBox = new JComboBox<>();
        try {
            EquipeController equipeController = new EquipeController();
            ApplicationController applicationController = new ApplicationController();
            Equipe equipe = equipeController.getEquipeByResponsableId(responsable.getId());
            if (equipe == null) {
                JOptionPane.showMessageDialog(this, "Aucune équipe trouvée pour ce responsable.", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Application application = applicationController.getApplicationsByEquipeId(equipe.getId());
            if (application == null) {
                JOptionPane.showMessageDialog(this, "Aucune application trouvée pour cette équipe.", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            List<Incident> incidents = incidentController.getIncidents();
            for (Incident incident : incidents) {
                if (incident.getApplicationConcernee() != null && incident.getApplicationConcernee().getId() == application.getId()) {
                    incidentComboBox.addItem(incident);
                }
            }
        } catch (SQLException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des incidents : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        incidentComboBox.setRenderer(new DefaultListCellRenderer() {

			private static final long serialVersionUID = 1L;

			@Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Incident) {
                    Incident incident = (Incident) value;
                    setText(incident.getDescription());
                }
                return this;
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainPanel.add(incidentComboBox, gbc);

        // Bouton Assigner
        JButton assignerButton = createStyledButton("Assigner");
        assignerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                assignerIncident();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(assignerButton, gbc);

        setVisible(true);
    }

    private void assignerIncident() {
        Utilisateur developpeur = (Utilisateur) developpeurComboBox.getSelectedItem();
        Incident incident = (Incident) incidentComboBox.getSelectedItem();

        try {
            incidentController.assignerIncidentADeveloppeur(incident.getId(), developpeur.getId());
            Utilisateur utilisateurAssigne = utilisateurController.getUtilisateur(developpeur.getId());
            incident.setAssigneA(utilisateurAssigne);
            incident.setStatut(Statut.ASSIGNE);

            JOptionPane.showMessageDialog(this, "Incident assigné avec succès.", "Information", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'assignation de l'incident : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
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