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
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class FenetrePrincipale extends JFrame {

    private static final long serialVersionUID = 1L;
    private Utilisateur utilisateur;
    private Color couleurFond = new Color(30, 114, 168);
    private Color couleurTexteBlanche = Color.WHITE;
    private Color couleurBouton = new Color(41, 128, 185);
    private Font fontTitre = new Font("Arial", Font.BOLD, 24);
    private Font fontSousTitre = new Font("Arial", Font.PLAIN, 14);
    private Font fontBouton = new Font("Arial", Font.BOLD, 14);

    public FenetrePrincipale(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;

        setTitle("Système de Gestion d'Incidents");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Utilisation d'un BorderLayout pour la fenêtre principale
        setLayout(new BorderLayout());
        
        // Panneau gauche (bleu avec logo et titre)
        JPanel panneauGauche = creerPanneauGauche();
        add(panneauGauche, BorderLayout.WEST);
        
        // Panneau droit (gris clair avec les boutons)
        JPanel panneauDroit = creerPanneauDroit();
        add(panneauDroit, BorderLayout.CENTER);

        setVisible(true);
    }
    
    private JPanel creerPanneauGauche() {
        JPanel panneauGauche = new JPanel();
        panneauGauche.setLayout(new BorderLayout());
        panneauGauche.setBackground(couleurFond);
        panneauGauche.setPreferredSize(new Dimension(400, getHeight()));
        
        // Contenu central (logo et titre)
        JPanel panneauCentral = new JPanel();
        panneauCentral.setLayout(new BoxLayout(panneauCentral, BoxLayout.Y_AXIS));
        panneauCentral.setBackground(couleurFond);
        panneauCentral.setBorder(new EmptyBorder(80, 0, 0, 0));
        
        // Logo (bug dans cercle blanc)
        JPanel panneauLogo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                
                // Activer l'antialiasing pour un rendu plus lisse
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Dessiner un cercle avec le symbole d'un bug
                g2d.setColor(Color.WHITE);
                int diameter = Math.min(getWidth(), getHeight()) / 2;
                int x = (getWidth() - diameter) / 2;
                int y = (getHeight() - diameter) / 2;
                
                // Dessiner le cercle extérieur
                g2d.fillOval(x, y, diameter, diameter);
                
                // Dessiner l'icône du bug
                g2d.setColor(couleurFond); // Utiliser couleurFond comme couleur primaire
                g2d.setStroke(new BasicStroke(3));
                
                // Corps du bug
                int bugWidth = diameter * 2/3;
                int bugHeight = diameter * 2/3;
                int bugX = x + (diameter - bugWidth) / 2;
                int bugY = y + (diameter - bugHeight) / 2;
                
                g2d.fillOval(bugX, bugY, bugWidth, bugHeight);
                
                // Pattes du bug
                g2d.setColor(Color.WHITE);
                
                // Pattes gauches
                g2d.drawLine(bugX, bugY + bugHeight/3, bugX - bugWidth/4, bugY + bugHeight/3 - bugHeight/5);
                g2d.drawLine(bugX, bugY + bugHeight/2, bugX - bugWidth/4, bugY + bugHeight/2);
                g2d.drawLine(bugX, bugY + bugHeight*2/3, bugX - bugWidth/4, bugY + bugHeight*2/3 + bugHeight/5);
                
                // Pattes droites
                g2d.drawLine(bugX + bugWidth, bugY + bugHeight/3, bugX + bugWidth + bugWidth/4, bugY + bugHeight/3 - bugHeight/5);
                g2d.drawLine(bugX + bugWidth, bugY + bugHeight/2, bugX + bugWidth + bugWidth/4, bugY + bugHeight/2);
                g2d.drawLine(bugX + bugWidth, bugY + bugHeight*2/3, bugX + bugWidth + bugWidth/4, bugY + bugHeight*2/3 + bugHeight/5);
                
                g2d.dispose();
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(200, 200);
            }
        };
        
        panneauCentral.add(panneauLogo);
        
        // Espace après le logo
        panneauCentral.add(Box.createVerticalStrut(50));
        
        // Reste du code inchangé...
        // Titre "Gestion d'Incidents"
        JLabel labelTitre = new JLabel("Gestion d'Incidents");
        labelTitre.setFont(fontTitre);
        labelTitre.setForeground(couleurTexteBlanche);
        labelTitre.setAlignmentX(Component.CENTER_ALIGNMENT);
        panneauCentral.add(labelTitre);
        
        // Panneau pour le slogan (en bas)
        JPanel panneauSlogan = new JPanel();
        panneauSlogan.setLayout(new BoxLayout(panneauSlogan, BoxLayout.Y_AXIS));
        panneauSlogan.setBackground(couleurFond);
        
        // Sous-titre "Suivi et résolution efficace"
        JLabel labelSousTitre = new JLabel("Suivi et résolution efficace");
        labelSousTitre.setFont(fontSousTitre);
        labelSousTitre.setForeground(couleurTexteBlanche);
        labelSousTitre.setAlignmentX(Component.CENTER_ALIGNMENT);
        panneauSlogan.add(labelSousTitre);
        
        // Espace avant le bas de page
        panneauSlogan.add(Box.createVerticalStrut(40));
        
        // Version en bas
        JLabel labelVersion = new JLabel("Gestion d'Incidents v1.0");
        labelVersion.setFont(new Font("Arial", Font.PLAIN, 10));
        labelVersion.setForeground(couleurTexteBlanche);
        labelVersion.setAlignmentX(Component.CENTER_ALIGNMENT);
        panneauSlogan.add(labelVersion);
        panneauSlogan.add(Box.createVerticalStrut(10));
        
        // Ajouter les panneaux au panneau principal
        panneauGauche.add(panneauCentral, BorderLayout.CENTER);
        panneauGauche.add(panneauSlogan, BorderLayout.SOUTH);
        
        return panneauGauche;
    }
    
    private JPanel creerPanneauDroit() {
        JPanel panneauDroit = new JPanel();
        panneauDroit.setLayout(new BoxLayout(panneauDroit, BoxLayout.Y_AXIS));
        panneauDroit.setBackground(new Color(230, 235, 238)); // Couleur gris clair
        panneauDroit.setBorder(new EmptyBorder(60, 40, 40, 40));
        
        // Titre du panneau
        JLabel labelTitre = new JLabel("Tableau de bord");
        labelTitre.setFont(new Font("Arial", Font.BOLD, 24));
        labelTitre.setAlignmentX(Component.CENTER_ALIGNMENT);
        panneauDroit.add(labelTitre);
        
        // Espace après le titre
        panneauDroit.add(Box.createVerticalStrut(20));
        
        // Label de bienvenue
        JLabel labelBienvenue = new JLabel("Bienvenue " + utilisateur.getNom() + " (" + utilisateur.getRole() + ")");
        labelBienvenue.setFont(new Font("Arial", Font.PLAIN, 16));
        labelBienvenue.setAlignmentX(Component.CENTER_ALIGNMENT);
        panneauDroit.add(labelBienvenue);
        
        // Espace avant les boutons
        panneauDroit.add(Box.createVerticalStrut(40));
        
        // Panel pour les boutons fonctionnels
        JPanel panneauBoutons = new JPanel();
        panneauBoutons.setLayout(new GridLayout(0, 1, 0, 15));
        panneauBoutons.setBackground(new Color(230, 235, 238));
        panneauBoutons.setAlignmentX(Component.CENTER_ALIGNMENT);
        panneauBoutons.setMaximumSize(new Dimension(300, 300));
        
        // Ajout des boutons spécifiques au rôle
        ajouterBoutonsSelonRole(panneauBoutons);
        
        panneauDroit.add(panneauBoutons);
        
        // Espace avant déconnexion
        panneauDroit.add(Box.createVerticalStrut(20));
        
        // Bouton déconnexion
        JButton boutonDeconnexion = creerBouton("Déconnexion");
        boutonDeconnexion.setMaximumSize(new Dimension(300, 40));
        boutonDeconnexion.setAlignmentX(Component.CENTER_ALIGNMENT);
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
        panneauDroit.add(boutonDeconnexion);
        
        // Espace flexible
        panneauDroit.add(Box.createVerticalGlue());
        
        // Copyright
        JLabel labelCopyright = new JLabel("© 2025 Tous droits réservés");
        labelCopyright.setFont(new Font("Arial", Font.PLAIN, 10));
        labelCopyright.setAlignmentX(Component.CENTER_ALIGNMENT);
        panneauDroit.add(labelCopyright);
        
        return panneauDroit;
    }
    
    private void ajouterBoutonsSelonRole(JPanel panneauBoutons) {
        if (utilisateur.getRole().equals("rapporteur")) {
            JButton boutonCreerIncident = creerBouton("Créer un incident");
            JButton boutonConsulterIncidents = creerBouton("Consulter les incidents");
            
            panneauBoutons.add(boutonCreerIncident);
            panneauBoutons.add(boutonConsulterIncidents);
            
            boutonCreerIncident.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        new FenetreCreationIncidentRapporteur(utilisateur).setVisible(true);
                    } catch (SQLException ex) {
                        afficherErreur("Erreur lors de la création d'incident", ex);
                    }
                }
            });
            
            boutonConsulterIncidents.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        new FenetreConsultationIncidents(utilisateur).setVisible(true);
                    } catch (SQLException | IOException ex) {
                        afficherErreur("Erreur lors de la consultation des incidents", ex);
                    }
                }
            });
        } else if (utilisateur.getRole().equals("developpeur")) {
            JButton boutonConsulterIncidents = creerBouton("Consulter les incidents");
            panneauBoutons.add(boutonConsulterIncidents);
            boutonConsulterIncidents.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        new FenetreConsultationIncidents(utilisateur).setVisible(true);
                    } catch (SQLException | IOException ex) {
                        afficherErreur("Erreur lors de la consultation des incidents", ex);
                    }
                }
            });
        } else if (utilisateur.getRole().equals("responsable")) {
            JButton boutonConsulterIncidents = creerBouton("Consulter les incidents");
            panneauBoutons.add(boutonConsulterIncidents);
            boutonConsulterIncidents.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        new FenetreConsultationIncidents(utilisateur).setVisible(true);
                    } catch (SQLException | IOException ex) {
                        afficherErreur("Erreur lors de la consultation des incidents", ex);
                    }
                }
            });

        } else if (utilisateur.getRole().equals("administrateur")) {
            JButton boutonCreerUtilisateur = creerBouton("Créer un utilisateur");
            JButton rechercherUtilisateur = creerBouton("Rechercher utilisateur");
            JButton listeUtilisateursButton = creerBouton("Liste des utilisateurs");
            
            panneauBoutons.add(boutonCreerUtilisateur);
            panneauBoutons.add(rechercherUtilisateur);
            panneauBoutons.add(listeUtilisateursButton);
            
            boutonCreerUtilisateur.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        UtilisateurController utilisateurController = new UtilisateurController();
                        new FenetreCreationUtilisateur(utilisateurController).setVisible(true);
                    } catch (Exception ex) {
                        afficherErreur("Erreur lors de l'ouverture de la fenêtre de création d'utilisateur", ex);
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
                        afficherErreur("Erreur lors de l'ouverture de la fenêtre de recherche", ex);
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
                        afficherErreur("Erreur lors de l'affichage de la liste des utilisateurs", e1);
                    }
                }
            });
        }
    }
    
    // Méthode utilitaire pour la création de boutons standardisés
    private JButton creerBouton(String texte) {
        JButton bouton = new JButton(texte);
        bouton.setFont(fontBouton);
        bouton.setBackground(couleurBouton);
        bouton.setForeground(Color.WHITE);
        bouton.setFocusPainted(false);
        bouton.setBorderPainted(false);
        bouton.setPreferredSize(new Dimension(300, 40));
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return bouton;
    }
    
    // Méthode utilitaire pour afficher les erreurs
    private void afficherErreur(String message, Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, message + " : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}