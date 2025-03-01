package com.gestionincidents.view;

import com.gestionincidents.controller.UtilisateurController;
import com.gestionincidents.model.Utilisateur;
import com.gestionincidents.model.dao.UtilisateurDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;

public class FenetreConnexion extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField champEmail;
    private JPasswordField champMotDePasse;
    private UtilisateurController utilisateurController;
    private JPanel panneauPrincipal;
    
    // Couleurs de thème
    private final Color COULEUR_PRIMAIRE = new Color(41, 128, 185); // Bleu
    private final Color COULEUR_SECONDAIRE = new Color(52, 152, 219); // Bleu clair
    private final Color COULEUR_TEXTE = new Color(52, 73, 94); // Gris foncé
    private final Color COULEUR_FOND = new Color(236, 240, 241); // Gris très clair
    private final Color COULEUR_ACCENT = new Color(231, 76, 60); // Rouge
    private final Font POLICE_TITRE = new Font("Segoe UI", Font.BOLD, 20);
    private final Font POLICE_LABEL = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font POLICE_BOUTON = new Font("Segoe UI", Font.BOLD, 14);

    public FenetreConnexion() {
        // Configuration de base de la fenêtre
        setTitle("Système de Gestion d'Incidents");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(COULEUR_FOND);
        setLayout(new BorderLayout());
        
        // Initialisation du contrôleur
        utilisateurController = new UtilisateurController();
        
        // Création du panneau principal avec effet de séparation
        panneauPrincipal = new JPanel(new GridLayout(1, 2));
        panneauPrincipal.setBackground(COULEUR_FOND);
        add(panneauPrincipal, BorderLayout.CENTER);
        
        // Panneau gauche (image/logo)
        JPanel panneauGauche = creerPanneauGauche();
        panneauPrincipal.add(panneauGauche);
        
        // Panneau droit (formulaire)
        JPanel panneauDroit = creerPanneauFormulaire();
        panneauPrincipal.add(panneauDroit);
        
        // Panneau du bas (footer)
        JPanel panneauBas = creerPanneauBas();
        add(panneauBas, BorderLayout.SOUTH);
        
        setVisible(true);
    }

    private JPanel creerPanneauGauche() {
        JPanel panneau = new JPanel(new BorderLayout());
        panneau.setBackground(COULEUR_PRIMAIRE);
        
        // Titre au centre
        JLabel labelTitre = new JLabel("Gestion d'Incidents", JLabel.CENTER);
        labelTitre.setFont(new Font("Segoe UI", Font.BOLD, 30));
        labelTitre.setForeground(Color.WHITE);
        
        // Sous-titre
        JLabel labelSousTitre = new JLabel("Suivi et résolution efficace", JLabel.CENTER);
        labelSousTitre.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        labelSousTitre.setForeground(new Color(236, 240, 241, 200));
        
        // Panneau pour centrer les deux labels
        JPanel panneauCentre = new JPanel(new GridLayout(2, 1, 0, 10));
        panneauCentre.setBackground(COULEUR_PRIMAIRE);
        panneauCentre.add(labelTitre);
        panneauCentre.add(labelSousTitre);
        
        // Ajouter un espace au-dessus et en dessous
        panneauCentre.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // Ajouter au panneau principal
        panneau.add(panneauCentre, BorderLayout.CENTER);
        
        // Ajouter une image ou un logo stylisé (simulée ici avec un JPanel personnalisé)
        JPanel panneauLogo = new JPanel() {

			private static final long serialVersionUID = 1L;

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
                g2d.setColor(COULEUR_PRIMAIRE);
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
        
        panneauLogo.setBackground(COULEUR_PRIMAIRE);
        panneau.add(panneauLogo, BorderLayout.NORTH);
        
        return panneau;
    }

    private JPanel creerPanneauFormulaire() {
        JPanel panneau = new JPanel();
        // Utiliser un panneau avec BoxLayout vertical pour mieux contrôler l'espacement
        panneau.setLayout(new BoxLayout(panneau, BoxLayout.Y_AXIS));
        panneau.setBackground(COULEUR_FOND);
        panneau.setBorder(new EmptyBorder(60, 50, 60, 50));

        // Titre du formulaire
        JLabel labelTitreConnexion = new JLabel("Connexion");
        labelTitreConnexion.setFont(POLICE_TITRE);
        labelTitreConnexion.setForeground(COULEUR_TEXTE);
        labelTitreConnexion.setAlignmentX(Component.CENTER_ALIGNMENT);
        panneau.add(labelTitreConnexion);
        
        // Espacement
        panneau.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // Panel pour email
        JPanel panelEmail = new JPanel(new BorderLayout(10, 5));
        panelEmail.setBackground(COULEUR_FOND);
        panelEmail.setMaximumSize(new Dimension(300, 70));
        
        JLabel labelEmail = new JLabel("Adresse Email");
        labelEmail.setFont(POLICE_LABEL);
        labelEmail.setForeground(COULEUR_TEXTE);
        
        champEmail = new JTextField();
        champEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        champEmail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COULEUR_SECONDAIRE, 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        
        panelEmail.add(labelEmail, BorderLayout.NORTH);
        panelEmail.add(champEmail, BorderLayout.CENTER);
        panneau.add(panelEmail);
        
        // Espacement
        panneau.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Panel pour mot de passe
        JPanel panelMotDePasse = new JPanel(new BorderLayout(10, 5));
        panelMotDePasse.setBackground(COULEUR_FOND);
        panelMotDePasse.setMaximumSize(new Dimension(300, 70));
        
        JLabel labelMotDePasse = new JLabel("Mot de passe");
        labelMotDePasse.setFont(POLICE_LABEL);
        labelMotDePasse.setForeground(COULEUR_TEXTE);
        
        champMotDePasse = new JPasswordField();
        champMotDePasse.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        champMotDePasse.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COULEUR_SECONDAIRE, 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        
        panelMotDePasse.add(labelMotDePasse, BorderLayout.NORTH);
        panelMotDePasse.add(champMotDePasse, BorderLayout.CENTER);
        panneau.add(panelMotDePasse);
        
        // Espacement
        panneau.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Bouton de connexion
        JButton boutonConnexion = creerBoutonStylise("Se Connecter", COULEUR_PRIMAIRE, Color.WHITE);
        boutonConnexion.setAlignmentX(Component.CENTER_ALIGNMENT);
        boutonConnexion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = champEmail.getText();
                String motDePasse = new String(champMotDePasse.getPassword());
                verifierConnexion(email, motDePasse);
            }
        });
        panneau.add(boutonConnexion);
        
        // Ajouter un espacement et message pour mot de passe oublié
        panneau.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JLabel labelOublie = new JLabel("Mot de passe oublié ?");
        labelOublie.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelOublie.setForeground(COULEUR_SECONDAIRE);
        labelOublie.setCursor(new Cursor(Cursor.HAND_CURSOR));
        labelOublie.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Ajout d'un effet de survol
        labelOublie.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                labelOublie.setForeground(COULEUR_ACCENT);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                labelOublie.setForeground(COULEUR_SECONDAIRE);
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(FenetreConnexion.this, 
                        "Veuillez contacter votre administrateur système pour réinitialiser votre mot de passe.", 
                        "Mot de passe oublié", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        panneau.add(labelOublie);
        
        return panneau;
    }
    
    private JPanel creerPanneauBas() {
        JPanel panneau = new JPanel(new BorderLayout());
        panneau.setBackground(COULEUR_FOND);
        panneau.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel labelVersion = new JLabel("Gestion d'Incidents v1.0");
        labelVersion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelVersion.setForeground(new Color(127, 140, 141));
        
        panneau.add(labelVersion, BorderLayout.WEST);
        
        JLabel labelCopyright = new JLabel("© 2025 Tous droits réservés");
        labelCopyright.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelCopyright.setForeground(new Color(127, 140, 141));
        
        panneau.add(labelCopyright, BorderLayout.EAST);
        
        return panneau;
    }
    
    private JButton creerBoutonStylise(String texte, Color couleurFond, Color couleurTexte) {
        JButton bouton = new JButton(texte);
        bouton.setFont(POLICE_BOUTON);
        bouton.setForeground(couleurTexte);
        bouton.setBackground(couleurFond);
        bouton.setFocusPainted(false);
        bouton.setBorderPainted(false);
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bouton.setMaximumSize(new Dimension(300, 40));
        bouton.setPreferredSize(new Dimension(200, 40));
        
        // Effet de survol
        bouton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                bouton.setBackground(COULEUR_SECONDAIRE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                bouton.setBackground(couleurFond);
            }
        });
        
        return bouton;
    }

    private void verifierConnexion(String email, String motDePasse) {
        if (email.isEmpty() || motDePasse.isEmpty()) {
            afficherMessage("Veuillez remplir tous les champs", "Erreur de saisie", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Afficher un indicateur de chargement
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        // Utiliser SwingWorker pour exécuter la vérification en arrière-plan
        SwingWorker<Integer, Void> worker = new SwingWorker<Integer, Void>() {
            @Override
            protected Integer doInBackground() throws Exception {
                return utilisateurController.verifierConnexion(email, motDePasse);
            }
            
            @Override
            protected void done() {
                try {
                    int resultat = get();
                    traiterResultatConnexion(resultat, email);
                } catch (Exception e) {
                    afficherMessage("Erreur lors de la connexion: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        };
        
        worker.execute();
    }
    
    private void traiterResultatConnexion(int resultat, String email) {
        switch (resultat) {
            case 1:
                // Connexion réussie
                try {
                    UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
                    Utilisateur utilisateur = utilisateurDAO.getUtilisateurParEmail(email);
                    afficherAnimation();
                    ouvrirFenetreRole(utilisateur);
                } catch (SQLException | IOException ex) {
                    afficherMessage("Erreur lors de la récupération de l'utilisateur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case -1:
                // Utilisateur supprimé
                afficherMessage("Ce compte n'existe pas ou a été supprimé.", "Erreur", JOptionPane.ERROR_MESSAGE);
                break;
            case 0:
                // Utilisateur non trouvé
                afficherMessage("Email ou mot de passe incorrect.", "Erreur", JOptionPane.ERROR_MESSAGE);
                // Effacer le champ mot de passe pour réessayer
                champMotDePasse.setText("");
                break;
            default:
                // Erreur
                afficherMessage("Erreur lors de la vérification de la connexion.", "Erreur", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }
    
    private void afficherAnimation() {
        // Animation simple de transition avant d'ouvrir la fenêtre principale
        Timer timer = new Timer(15, new ActionListener() {
            float alpha = 1.0f;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                alpha -= 0.05f;
                if (alpha <= 0) {
                    ((Timer)e.getSource()).stop();
                    setVisible(false);
                } else {
                    panneauPrincipal.setBackground(new Color(236, 240, 241, (int)(alpha * 255)));
                    panneauPrincipal.repaint();
                }
            }
        });
        timer.start();
    }
    
    private void afficherMessage(String message, String titre, int type) {
        // Personnaliser les messages d'alerte
        UIManager.put("OptionPane.background", COULEUR_FOND);
        UIManager.put("Panel.background", COULEUR_FOND);
        UIManager.put("OptionPane.messageForeground", COULEUR_TEXTE);
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.PLAIN, 14));
        
        JOptionPane.showMessageDialog(this, message, titre, type);
    }
    
    private void ouvrirFenetreRole(Utilisateur utilisateur) {
        if (utilisateur.getRole() != null) {
            switch (utilisateur.getRole()) {
                case "rapporteur":
                case "responsable":
                case "developpeur":
                case "administrateur":
                    new FenetrePrincipale(utilisateur).setVisible(true);
                    break;
                default:
                    afficherMessage("Rôle d'utilisateur inconnu.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            afficherMessage("Rôle d'utilisateur non défini.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        dispose();
    }
    
    public static void main(String[] args) {
        try {
            // Définir le look and feel de l'application
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Configurer les propriétés globales de l'UI
            UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("PasswordField.font", new Font("Segoe UI", Font.PLAIN, 14));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FenetreConnexion();
            }
        });
    }
}