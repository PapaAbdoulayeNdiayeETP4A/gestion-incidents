package com.gestionincidents.view.administrateur;

import com.gestionincidents.controller.UtilisateurController;
import com.gestionincidents.model.dao.EquipeDAO;
import com.gestionincidents.model.Equipe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FenetreCreationResponsable extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField departementField;
    private JComboBox<String> equipeComboBox;  // Nouveau JComboBox pour les équipes
    private UtilisateurController utilisateurController;
    private int utilisateurId;

    public FenetreCreationResponsable(int utilisateurId, UtilisateurController utilisateurController) throws IOException, SQLException {
        this.utilisateurId = utilisateurId;
        this.utilisateurController = utilisateurController;

        setTitle("Informations responsable");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Charger les équipes disponibles
        List<Equipe> equipes = new EquipeDAO().getEquipes();  // Méthode pour récupérer toutes les équipes
        String[] equipeNames = new String[equipes.size()];
        for (int i = 0; i < equipes.size(); i++) {
            equipeNames[i] = equipes.get(i).getNom();
        }

        JPanel panneauPrincipal = new JPanel(new GridLayout(6, 2));

        panneauPrincipal.add(new JLabel("Département :"));
        departementField = new JTextField();
        panneauPrincipal.add(departementField);

        panneauPrincipal.add(new JLabel("Sélectionner une équipe :"));
        equipeComboBox = new JComboBox<>(equipeNames);  // Affichage des noms des équipes
        panneauPrincipal.add(equipeComboBox);

        JButton creerButton = new JButton("Créer");
        creerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    createResponsable();
                } catch (NumberFormatException | IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        panneauPrincipal.add(creerButton);

        add(panneauPrincipal);
        setVisible(true);
    }

    private void createResponsable() throws NumberFormatException, IOException {
        try {
            // Récupérer l'équipe sélectionnée
            String equipeName = (String) equipeComboBox.getSelectedItem();
            int equipeId = new EquipeDAO().getEquipeIdByName(equipeName);  // Méthode pour récupérer l'ID de l'équipe par son nom

            utilisateurController.createResponsable(utilisateurId, departementField.getText(), equipeId);
            JOptionPane.showMessageDialog(this, "Informations responsable enregistrées.");
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement des informations responsable : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la conversion des données.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
