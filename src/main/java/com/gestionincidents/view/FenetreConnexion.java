package com.gestionincidents.view;

import com.gestionincidents.controller.UtilisateurController;
import com.gestionincidents.model.Utilisateur;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FenetreConnexion extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField champEmail;
    private JPasswordField champMotDePasse;
    private UtilisateurController utilisateurController;

    public FenetreConnexion() {
        setTitle("Connexion");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panneauPrincipal = new JPanel(new GridLayout(0, 2));
        add(panneauPrincipal);

        // Composants
        JLabel labelEmail = new JLabel("Email :");
        champEmail = new JTextField();
        JLabel labelMotDePasse = new JLabel("Mot de passe :");
        champMotDePasse = new JPasswordField();
        JButton boutonConnexion = new JButton("Se connecter");

        // Ajout des composants au panneau
        panneauPrincipal.add(labelEmail);
        panneauPrincipal.add(champEmail);
        panneauPrincipal.add(labelMotDePasse);
        panneauPrincipal.add(champMotDePasse);
        panneauPrincipal.add(new JLabel()); // Espace vide
        panneauPrincipal.add(boutonConnexion);

        // Initialisation du contrôleur
        utilisateurController = new UtilisateurController();

        // Gestion des événements
        boutonConnexion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = champEmail.getText();
                String motDePasse = new String(champMotDePasse.getPassword());
                verifierConnexion(email, motDePasse);
            }
        });

        setVisible(true);
    }

    private void verifierConnexion(String email, String motDePasse) {
        Utilisateur utilisateur = utilisateurController.verifierConnexion(email, motDePasse);
        if (utilisateur != null) {
            ouvrirFenetreRole(utilisateur);
        } else {
            JOptionPane.showMessageDialog(this, "Email ou mot de passe incorrect.");
        }
    }
        
    private void ouvrirFenetreRole(Utilisateur utilisateur) {
        if (utilisateur.getRole() != null) {
            switch (utilisateur.getRole()) {
            case "rapporteur":
            	new FenetrePrincipale(utilisateur).setVisible(true);
                break;
            case "responsable":
            	new FenetrePrincipale(utilisateur).setVisible(true);
                break;
            case "developpeur":
            	new FenetrePrincipale(utilisateur).setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Rôle d'utilisateur inconnu.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Rôle d'utilisateur non défini.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        dispose();
    }
}