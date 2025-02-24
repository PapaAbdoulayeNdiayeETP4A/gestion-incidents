package com.gestionincidents.view;

import com.gestionincidents.controller.UtilisateurController;
import com.gestionincidents.model.Utilisateur;
import com.gestionincidents.view.administrateur.FenetreCreationUtilisateur;
import com.gestionincidents.view.rapporteur.FenetreCreationIncidentRapporteur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

public class FenetrePrincipale extends JFrame {

    private static final long serialVersionUID = 1L;
    private Utilisateur utilisateur;

    public FenetrePrincipale(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;

        setTitle("Gestion des Incidents - Fenêtre Principale");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panneauPrincipal = new JPanel(new GridLayout(0, 1));
        add(panneauPrincipal);

        JLabel labelBienvenue = new JLabel("Bienvenue dans l'application de gestion des incidents !");
        labelBienvenue.setHorizontalAlignment(SwingConstants.CENTER);
        panneauPrincipal.add(labelBienvenue);

        panneauPrincipal.add(Box.createVerticalStrut(20));

        // Boutons spécifiques au rôle
        if (utilisateur.getRole().equals("rapporteur")) {
            JButton boutonCreerIncident = new JButton("Créer un incident");
            JButton boutonConsulterIncidents = new JButton("Consulter les incidents");

            panneauPrincipal.add(boutonCreerIncident);
            panneauPrincipal.add(boutonConsulterIncidents);

            boutonCreerIncident.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        new FenetreCreationIncidentRapporteur(utilisateur).setVisible(true);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            boutonConsulterIncidents.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        new FenetreConsultationIncidents(utilisateur).setVisible(true);
                    } catch (SQLException | IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } else if (utilisateur.getRole().equals("developpeur")) {
            JButton boutonConsulterIncidents = new JButton("Consulter les incidents");
            panneauPrincipal.add(boutonConsulterIncidents);
            boutonConsulterIncidents.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        new FenetreConsultationIncidents(utilisateur).setVisible(true);
                    } catch (SQLException | IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

        } else if (utilisateur.getRole().equals("responsable")) {
            JButton boutonConsulterIncidents = new JButton("Consulter les incidents");
            panneauPrincipal.add(boutonConsulterIncidents);
            boutonConsulterIncidents.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        new FenetreConsultationIncidents(utilisateur).setVisible(true);
                    } catch (SQLException | IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } else if (utilisateur.getRole().equals("administrateur")) {
            JButton boutonCreerUtilisateur = new JButton("Créer un utilisateur");
            panneauPrincipal.add(boutonCreerUtilisateur);
            boutonCreerUtilisateur.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                    	UtilisateurController utilisateurController = new UtilisateurController();
                        new FenetreCreationUtilisateur(utilisateurController).setVisible(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(FenetrePrincipale.this, "Erreur lors de l'ouverture de la fenêtre de création d'utilisateur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }

        // Bouton de déconnexion
        JButton boutonDeconnexion = new JButton("Déconnexion");
        panneauPrincipal.add(boutonDeconnexion);

        boutonDeconnexion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Effacer les informations de l'utilisateur
            	FenetrePrincipale.this.utilisateur = null;

                // Fermer la fenêtre principale
                dispose();

                // Ouvrir la fenêtre de connexion
                new FenetreConnexion().setVisible(true);
            }
        });

        setVisible(true);
    }
}