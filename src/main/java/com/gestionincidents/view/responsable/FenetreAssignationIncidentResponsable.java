package com.gestionincidents.view.responsable;

import com.gestionincidents.controller.ApplicationController;
import com.gestionincidents.controller.EquipeController;
import com.gestionincidents.controller.IncidentController;
import com.gestionincidents.controller.UtilisateurController;
import com.gestionincidents.model.Application;
import com.gestionincidents.model.Equipe;
import com.gestionincidents.model.Incident;
import com.gestionincidents.model.Utilisateur;
import com.gestionincidents.model.Statut;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.io.IOException;
import java.util.List;

public class FenetreAssignationIncidentResponsable extends JFrame {

    private JComboBox<Utilisateur> developpeurComboBox;
    private JComboBox<Incident> incidentComboBox;
    private IncidentController incidentController;
    private UtilisateurController utilisateurController;

    public FenetreAssignationIncidentResponsable(IncidentController incidentController, UtilisateurController utilisateurController, Utilisateur responsable) {
        this.incidentController = incidentController;
        this.utilisateurController = utilisateurController;

        setTitle("Assigner un incident");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panneauPrincipal = new JPanel(new GridLayout(3, 2));

        // Développeurs
        panneauPrincipal.add(new JLabel("Développeur :"));
        developpeurComboBox = new JComboBox<>();
        try {
            List<Utilisateur> developpeurs = utilisateurController.getDeveloppeursParEquipe(responsable.getId());
            for (Utilisateur developpeur : developpeurs) {
                if (!developpeur.isEstSupprime()) {
                    // Vérifier si le développeur a des incidents assignés
                    List<Incident> incidentsAssignes = incidentController.getIncidentsAssignesADeveloppeur(developpeur.getId());
                        developpeurComboBox.addItem(developpeur);
                }
            }
        } catch (SQLException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des développeurs : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        // Personnalisation de l'affichage des développeurs
        developpeurComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Utilisateur) {
                    Utilisateur developpeur = (Utilisateur) value;
                    setText(developpeur.getNom()); // Afficher le nom du développeur
                }
                return this;
            }
        });
        panneauPrincipal.add(developpeurComboBox);

     // Incidents ouverts
        panneauPrincipal.add(new JLabel("Incident :"));
        incidentComboBox = new JComboBox<>();

        try {
            // Récupérer l'équipe du responsable
            EquipeController equipeController = new EquipeController();
            ApplicationController applicationController = new ApplicationController();
            
            Equipe equipe = equipeController.getEquipeByResponsableId(responsable.getId());
            if (equipe == null) {
                JOptionPane.showMessageDialog(this, "Aucune équipe trouvée pour ce responsable.", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Récupérer l'application gérée par cette équipe
            Application application = applicationController.getApplicationsByEquipeId(equipe.getId());
            if (application == null) {
                JOptionPane.showMessageDialog(this, "Aucune application trouvée pour cette équipe.", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Récupérer les incidents ouverts et filtrer uniquement ceux de l'application concernée
            List<Incident> incidents = incidentController.getIncidents();
            for (Incident incident : incidents) {
                if (incident.getApplicationConcernee() != null && incident.getApplicationConcernee().getId() == application.getId()) {
                    incidentComboBox.addItem(incident);
                }
            }

        } catch (SQLException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des incidents : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        // Personnalisation de l'affichage des incidents
        incidentComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Incident) {
                    Incident incident = (Incident) value;
                    setText(incident.getDescription()); // Remplacer getDescription() par l'attribut correct
                }
                return this;
            }
        });

        panneauPrincipal.add(incidentComboBox);


        // Bouton Assigner
        JButton assignerButton = new JButton("Assigner");
        assignerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                assignerIncident();
            }
        });
        panneauPrincipal.add(assignerButton);

        add(panneauPrincipal);
        setVisible(true);
    }

    private void assignerIncident() {
        Utilisateur developpeur = (Utilisateur) developpeurComboBox.getSelectedItem();
        Incident incident = (Incident) incidentComboBox.getSelectedItem();

        try {
            // Vérifier si l'incident est déjà assigné
            List<Incident> incidentsAssignes = incidentController.getIncidentsAssignesADeveloppeur(developpeur.getId());
           
            // Assigner l'incident
            incidentController.assignerIncidentADeveloppeur(incident.getId(), developpeur.getId());
            // Récupérer l'utilisateur assigné depuis la base de données
            Utilisateur utilisateurAssigne = utilisateurController.getUtilisateur(developpeur.getId());
            incident.setAssigneA(utilisateurAssigne);
            incident.setStatut(Statut.ASSIGNE);

            JOptionPane.showMessageDialog(this, "Incident assigné avec succès.", "Information", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            
        } catch (SQLException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'assignation de l'incident : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
