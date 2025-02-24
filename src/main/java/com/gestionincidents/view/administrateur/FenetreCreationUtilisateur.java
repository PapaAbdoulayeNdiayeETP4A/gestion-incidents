package com.gestionincidents.view.administrateur;

import com.gestionincidents.controller.UtilisateurController;
import com.gestionincidents.model.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

public class FenetreCreationUtilisateur extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField nomField;
    private JTextField emailField;
    private JPasswordField motDePasseField;
    private JComboBox<String> roleComboBox;
    private UtilisateurController utilisateurController;

    public FenetreCreationUtilisateur(UtilisateurController utilisateurController) {
        this.utilisateurController = utilisateurController;
        setTitle("Créer un utilisateur");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panneauPrincipal = new JPanel(new GridLayout(5, 2));
        panneauPrincipal.add(new JLabel("Nom :"));
        nomField = new JTextField();
        panneauPrincipal.add(nomField);
        panneauPrincipal.add(new JLabel("Email :"));
        emailField = new JTextField();
        panneauPrincipal.add(emailField);
        panneauPrincipal.add(new JLabel("Mot de passe :"));
        motDePasseField = new JPasswordField();
        panneauPrincipal.add(motDePasseField);
        panneauPrincipal.add(new JLabel("Rôle :"));
        String[] roles = {"developpeur", "rapporteur", "responsable", "administrateur"};
        roleComboBox = new JComboBox<>(roles);
        panneauPrincipal.add(roleComboBox);

        JButton creerButton = new JButton("Créer");
        creerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
					createUtilisateur();
				} catch (SQLException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        panneauPrincipal.add(creerButton);

        add(panneauPrincipal);
        setVisible(true);
    }

    private void createUtilisateur() throws SQLException, IOException {
        String nom = nomField.getText();
        String email = emailField.getText();
        String motDePasse = new String(motDePasseField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(nom);
        utilisateur.setEmail(email);
        utilisateur.setMotDePasse(motDePasse);
        utilisateur.setRole(role);

        utilisateurController.createUtilisateur(utilisateur);
		JOptionPane.showMessageDialog(this, "Utilisateur créé avec succès.");

		// Open role-specific window
		switch (role) {
		    case "developpeur":
		        new FenetreCreationDeveloppeur(utilisateur.getId(), utilisateurController).setVisible(true);
		        break;
		    case "rapporteur":
		        new FenetreCreationRapporteur(utilisateur.getId(), utilisateurController).setVisible(true);
		        break;
		    case "responsable":
		        new FenetreCreationResponsable(utilisateur.getId(), utilisateurController).setVisible(true);
		        break;
		    default:
		        break;
		}

		dispose();
    }
}