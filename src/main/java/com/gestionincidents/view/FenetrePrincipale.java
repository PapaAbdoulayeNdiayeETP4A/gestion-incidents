package com.gestionincidents.view;

import com.gestionincidents.controller.IncidentController;
import com.gestionincidents.controller.UtilisateurController;
import com.gestionincidents.model.Incident;
import com.gestionincidents.model.Utilisateur;
import com.gestionincidents.view.administrateur.FenetreCreationUtilisateur;
import com.gestionincidents.view.administrateur.FenetreListeUtilisateurs;
import com.gestionincidents.view.administrateur.FenetreRechercheUtilisateur;
import com.gestionincidents.view.rapporteur.FenetreCreationIncidentRapporteur;
import com.gestionincidents.view.responsable.FenetreAssignationIncidentResponsable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


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
            JButton buttonAssignerIncident = new JButton("Assigner un incident");
            panneauPrincipal.add(boutonConsulterIncidents);
            panneauPrincipal.add(buttonAssignerIncident);
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
            
            buttonAssignerIncident.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        IncidentController incidentController = new IncidentController(); // Instance locale
                        UtilisateurController utilisateurController = new UtilisateurController(); // Instance locale manquante

                        List<Utilisateur> developpeurs = utilisateurController.getUtilisateurs();
                        List<Incident> incidents = incidentController.getIncidentsOuverts();

                        boolean developpeurDisponible = developpeurs.stream()
                            .filter(dev -> "developpeur".equals(dev.getRole()) && !dev.isEstSupprime())
                            .anyMatch(dev -> {
                                try {
                                    return incidentController.getIncidentsAssignesADeveloppeur(dev.getId()).isEmpty();
                                } catch (SQLException | IOException ex) {
                                    ex.printStackTrace();
                                    return false;
                                }
                            });

                        boolean incidentDisponible =  incidents != null && !incidents.isEmpty();

                        if (!developpeurDisponible || !incidentDisponible) {
                            String message = "Aucun incident disponible ou tous les développeurs ont été assignés.";
                            JOptionPane.showMessageDialog(null, message, "Erreur", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        new FenetreAssignationIncidentResponsable(incidentController, utilisateurController, utilisateur).setVisible(true);

                    } catch (SQLException | IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Erreur d'ouverture : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

        } else if (utilisateur.getRole().equals("administrateur")) {
            JButton boutonCreerUtilisateur = new JButton("Créer un utilisateur");
            JButton rechercherUtilisateur = new JButton("Rechercher utilisateur");
            JButton listeUtilisateursButton = new JButton("Liste des utilisateurs");
            panneauPrincipal.add(boutonCreerUtilisateur);
            panneauPrincipal.add(rechercherUtilisateur);
            panneauPrincipal.add(listeUtilisateursButton);
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
            
            rechercherUtilisateur.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                    	UtilisateurController utilisateurController = new UtilisateurController();
                        new FenetreRechercheUtilisateur(utilisateurController).setVisible(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(FenetrePrincipale.this, "Erreur lors de l'ouverture de la fenêtre de recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            listeUtilisateursButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	UtilisateurController utilisateurController = new UtilisateurController();
                    try {
						new FenetreListeUtilisateurs(utilisateurController).setVisible(true);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
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