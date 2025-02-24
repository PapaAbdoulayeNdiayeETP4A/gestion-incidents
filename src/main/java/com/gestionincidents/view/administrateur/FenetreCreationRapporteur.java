package com.gestionincidents.view.administrateur;

import com.gestionincidents.controller.UtilisateurController;
import com.gestionincidents.model.dao.UtilisateurDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

public class FenetreCreationRapporteur extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField serviceField;
    private JTextField matriculeField;
    private UtilisateurController utilisateurController;
    private int utilisateurId;
    public FenetreCreationRapporteur(int utilisateurId, UtilisateurController utilisateurController) throws IOException {
        this.utilisateurId = utilisateurId;
        this.utilisateurController = utilisateurController;;
        new UtilisateurDAO();

        setTitle("Informations rapporteur");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panneauPrincipal = new JPanel(new GridLayout(6, 2));
        panneauPrincipal.add(new JLabel("Service :"));
        serviceField = new JTextField();
        panneauPrincipal.add(serviceField);
        panneauPrincipal.add(new JLabel("Matricule :"));
        matriculeField = new JTextField();
        panneauPrincipal.add(matriculeField);

        JButton creerButton = new JButton("Créer");
        creerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                	createRapporteur();
                } catch (NumberFormatException | IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        panneauPrincipal.add(creerButton);

        add(panneauPrincipal);
        setVisible(true);
    }

    private void createRapporteur() throws NumberFormatException, IOException {
        try {
            utilisateurController.createRapporteur(utilisateurId, serviceField.getText(), matriculeField.getText());
            JOptionPane.showMessageDialog(this, "Informations développeur enregistrées.");
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement des informations développeur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}