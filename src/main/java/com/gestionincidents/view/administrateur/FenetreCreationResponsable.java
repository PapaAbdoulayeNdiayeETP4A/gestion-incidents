package com.gestionincidents.view.administrateur;

import com.gestionincidents.controller.UtilisateurController;
import com.gestionincidents.model.dao.UtilisateurDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

public class FenetreCreationResponsable extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField departementField;
    private UtilisateurController utilisateurController;
    private int utilisateurId;
    public FenetreCreationResponsable(int utilisateurId, UtilisateurController utilisateurController) throws IOException {
        this.utilisateurId = utilisateurId;
        this.utilisateurController = utilisateurController;;
        new UtilisateurDAO();

        setTitle("Informations rapporteur");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panneauPrincipal = new JPanel(new GridLayout(6, 2));
        panneauPrincipal.add(new JLabel("Département :"));
        departementField = new JTextField();
        panneauPrincipal.add(departementField);

        JButton creerButton = new JButton("Créer");
        creerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                	createRespnsable();
                } catch (NumberFormatException | IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        panneauPrincipal.add(creerButton);

        add(panneauPrincipal);
        setVisible(true);
    }

    private void createRespnsable() throws NumberFormatException, IOException {
        try {
            utilisateurController.createResponsable(utilisateurId, departementField.getText());
            JOptionPane.showMessageDialog(this, "Informations responsable enregistrées.");
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement des informations développeur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}