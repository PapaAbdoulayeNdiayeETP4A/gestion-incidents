package com.gestionincidents.view;

import com.gestionincidents.controller.IncidentController;
import com.gestionincidents.model.Incident;
import com.gestionincidents.model.Statut;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class FenetreChangementStatutIncident extends JFrame {

    private static final long serialVersionUID = 1L;
    private Incident incident;
    private JComboBox<Statut> comboStatut;
    private Color accentColor = new Color(70, 130, 180);
    private Color backgroundColor = new Color(245, 245, 245);
    private Color textColor = new Color(50, 50, 50);
    
    public FenetreChangementStatutIncident(int incidentId, IncidentController incidentController) throws SQLException {
        this.incident = incidentController.getIncident(incidentId);

        setTitle("Changement de Statut d'un Incident");
        setSize(450, 200);
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

        JLabel labelStatut = new JLabel("Nouveau Statut :");
        labelStatut.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelStatut.setForeground(textColor);
        mainPanel.add(labelStatut, gbc);

        comboStatut = new JComboBox<>(Statut.values());
        comboStatut.setSelectedItem(incident.getStatut());
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainPanel.add(comboStatut, gbc);

        JButton boutonChanger = createStyledButton("Changer");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(boutonChanger, gbc);

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