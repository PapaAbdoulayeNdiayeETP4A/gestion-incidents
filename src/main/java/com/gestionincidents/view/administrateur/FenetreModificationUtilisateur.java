package com.gestionincidents.view.administrateur;

import com.gestionincidents.controller.UtilisateurController;
import com.gestionincidents.model.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class FenetreModificationUtilisateur extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField nomField;
    private JTextField emailField;
    private JPasswordField motDePasseField;
    private JComboBox<String> roleComboBox;
    private UtilisateurController utilisateurController;
    private Utilisateur utilisateur;

    public FenetreModificationUtilisateur(Utilisateur utilisateur, UtilisateurController utilisateurController) {
        this.utilisateur = utilisateur;
        this.utilisateurController = utilisateurController;

        setTitle("Modifier un utilisateur");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panneauPrincipal = new JPanel(new GridLayout(5, 2));
        panneauPrincipal.add(new JLabel("Nom :"));
        nomField = new JTextField(utilisateur.getNom());
        panneauPrincipal.add(nomField);
        panneauPrincipal.add(new JLabel("Email :"));
        emailField = new JTextField(utilisateur.getEmail());
        panneauPrincipal.add(emailField);
        panneauPrincipal.add(new JLabel("Mot de passe :"));
        motDePasseField = new JPasswordField(utilisateur.getMotDePasse());
        panneauPrincipal.add(motDePasseField);
        panneauPrincipal.add(new JLabel("Rôle :"));
        String[] roles = {"developpeur", "rapporteur", "responsable", "administrateur"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setSelectedItem(utilisateur.getRole());
        panneauPrincipal.add(roleComboBox);

        JButton modifierButton = new JButton("Modifier");
        modifierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
					modifierUtilisateur();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        panneauPrincipal.add(modifierButton);

        add(panneauPrincipal);
        setVisible(true);
    }

    private void modifierUtilisateur() throws SQLException {
        String nom = nomField.getText();
        String email = emailField.getText();
        String motDePasse = new String(motDePasseField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        utilisateur.setNom(nom);
        utilisateur.setEmail(email);
        utilisateur.setMotDePasse(motDePasse);
        utilisateur.setRole(role);

        utilisateurController.updateUtilisateur(utilisateur);
		JOptionPane.showMessageDialog(this, "Utilisateur modifié avec succès.");
		dispose();
    }
}