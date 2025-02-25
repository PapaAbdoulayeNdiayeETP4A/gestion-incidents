package com.gestionincidents.view;

import com.gestionincidents.controller.CommentaireController;
import com.gestionincidents.controller.IncidentController;
import com.gestionincidents.model.Incident;
import com.gestionincidents.model.Utilisateur;
import com.gestionincidents.model.dao.UtilisateurDAO;
import com.gestionincidents.view.rapporteur.FenetreModificationIncidentRapporteur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FenetreConsultationIncidents extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable tableauIncidents;
    private DefaultTableModel modeleTableau;
    private IncidentController incidentController;
    private CommentaireController commentaireController;
    private Utilisateur utilisateur;

    public FenetreConsultationIncidents(Utilisateur utilisateur) throws SQLException, IOException {
        this.utilisateur = utilisateur;
        try {
            this.incidentController = new IncidentController();
            this.commentaireController = new CommentaireController();
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'initialisation des contrôleurs : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setTitle("Consultation des Incidents");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panneauPrincipal = new JPanel(new BorderLayout());
        add(panneauPrincipal);

        String[] colonnes = {"ID", "Application", "Description", "Priorité", "Statut", "Assigné à", "Commentaires", "Actions"};
        modeleTableau = new DefaultTableModel(colonnes, 0);
        tableauIncidents = new JTable(modeleTableau);
        JScrollPane scrollPane = new JScrollPane(tableauIncidents);
        panneauPrincipal.add(scrollPane, BorderLayout.CENTER);

        List<Incident> incidents = getIncidentsByUserRole(); // Récupérer les incidents en fonction du rôle
        mettreAJourTableau(incidents);

        tableauIncidents.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int colonne = tableauIncidents.getSelectedColumn();
                int ligne = tableauIncidents.rowAtPoint(e.getPoint());
                if (ligne >= 0) {
                    int incidentId = (int) modeleTableau.getValueAt(ligne, 0);

                    try {
                        if (tableauIncidents.getColumnName(colonne).equals("Modifier")) {
                            if (utilisateur.getRole().equals("rapporteur")) {
                                new FenetreModificationIncidentRapporteur(incidentId, incidentController).setVisible(true);
                            }
                            mettreAJourTableau(getIncidentsByUserRole());

                        } else if (tableauIncidents.getColumnName(colonne).equals("Changer Statut")) {
                            if (utilisateur.getRole().equals("rapporteur") || utilisateur.getRole().equals("developpeur") || utilisateur.getRole().equals("responsable")) {
                                new FenetreChangementStatutIncident(incidentId, incidentController).setVisible(true);
                            }
                            mettreAJourTableau(getIncidentsByUserRole());
                        }
                    } catch (SQLException | IOException ex) {
                        JOptionPane.showMessageDialog(FenetreConsultationIncidents.this, "Erreur lors de la gestion des incidents : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        tableauIncidents.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int colonne = tableauIncidents.getColumnModel().getColumnIndex("Commentaires");
                int ligne = tableauIncidents.rowAtPoint(e.getPoint());
                if (colonne == tableauIncidents.getSelectedColumn() && ligne >= 0) {
                    int incidentId = (int) modeleTableau.getValueAt(ligne, 0);
                    try {
                        if (commentaireController.getCommentaires(incidentId).isEmpty()) {
                            int reponse = JOptionPane.showConfirmDialog(FenetreConsultationIncidents.this, "Cet incident n'a aucun commentaire. Voulez-vous en ajouter un ?", "Aucun commentaire", JOptionPane.YES_NO_OPTION);
                            if (reponse == JOptionPane.YES_OPTION) {
                                new FenetreCreationCommentaire(incidentId, utilisateur, commentaireController).setVisible(true);
                                mettreAJourTableau(getIncidentsByUserRole());
                            }
                        } else {
                            new FenetreAffichageCommentaires(incidentId, commentaireController, utilisateur).setVisible(true);
                        }
                    } catch (SQLException | IOException ex) {
                        JOptionPane.showMessageDialog(FenetreConsultationIncidents.this, "Erreur lors de la gestion des commentaires : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        setVisible(true);
    }

    private List<Incident> getIncidentsByUserRole() throws SQLException, IOException {
        List<Incident> incidents = incidentController.getIncidents();
        switch (utilisateur.getRole()) {
            case "rapporteur":
                return incidents.stream()
                        .filter(incident -> incident.getRapporteur() != null && incident.getRapporteur().getId() == utilisateur.getId())
                        .collect(Collectors.toList());
            case "developpeur":
                return incidents.stream()
                        .filter(incident -> incident.getAssigneA() != null && incident.getAssigneA().getId() == utilisateur.getId())
                        .collect(Collectors.toList());
            case "responsable":
                // Récupérer les ID des développeurs de l'équipe du responsable
                UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
                try {
                    List<Integer> developpeurIds = utilisateurDAO.getDeveloppeurIdsByResponsableId(utilisateur.getId());
                    return incidents.stream()
                            .filter(incident -> incident.getAssigneA() != null && developpeurIds.contains(incident.getAssigneA().getId()))
                            .collect(Collectors.toList());
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des développeurs de l'équipe : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    return new ArrayList<>(); // Retourner une liste vide en cas d'erreur
                }
            default:
                return incidents; // Afficher tous les incidents pour les autres rôles
        }
    }

    public void mettreAJourTableau(List<Incident> incidents) {
        modeleTableau.setRowCount(0);
        try {
            // Déterminer les colonnes à ajouter
            boolean modifierAutorise = utilisateur.getRole().equals("rapporteur");
            boolean changerStatutAutorise = utilisateur.getRole().equals("rapporteur") || utilisateur.getRole().equals("responsable") || utilisateur.getRole().equals("developpeur");

            // Définir les noms de colonnes
            String[] colonnes;
            if (modifierAutorise && changerStatutAutorise) {
                colonnes = new String[]{"ID", "Application", "Description", "Priorité", "Statut", "Assigné à", "Commentaires", "Modifier", "Changer Statut"};
            } else if (modifierAutorise) {
                colonnes = new String[]{"ID", "Application", "Description", "Priorité", "Statut", "Assigné à", "Commentaires", "Modifier"};
            } else if (changerStatutAutorise) {
                colonnes = new String[]{"ID", "Application", "Description", "Priorité", "Statut", "Assigné à", "Commentaires", "Changer Statut"};
            } else {
                colonnes = new String[]{"ID", "Application", "Description", "Priorité", "Statut", "Assigné à", "Commentaires"};
            }

            modeleTableau.setColumnIdentifiers(colonnes);

            for (Incident incident : incidents) {
                Object[] donnee;
                if (modifierAutorise && changerStatutAutorise) {
                    donnee = new Object[]{
                            incident.getId(),
                            incident.getApplicationConcernee().getNom(),
                            incident.getDescription(),
                            incident.getPriorite(),
                            incident.getStatut(),
                            incident.getAssigneA() != null ? incident.getAssigneA().getNom() : "Non assigné",
                            "Afficher/Ajouter",
                            "Modifier",
                            "Changer Statut"
                    };
                } else if (modifierAutorise) {
                    donnee = new Object[]{
                            incident.getId(),
                            incident.getApplicationConcernee().getNom(),
                            incident.getDescription(),
                            incident.getPriorite(),
                            incident.getStatut(),
                            incident.getAssigneA() != null ? incident.getAssigneA().getNom() : "Non assigné",
                            "Afficher/Ajouter",
                            "Modifier"
                    };
                } else if (changerStatutAutorise) {
                    donnee = new Object[]{
                            incident.getId(),
                            incident.getApplicationConcernee().getNom(),
                            incident.getDescription(),
                            incident.getPriorite(),
                            incident.getStatut(),
                            incident.getAssigneA() != null ? incident.getAssigneA().getNom() : "Non assigné",
                            "Afficher/Ajouter",
                            "Changer Statut"
                    };
                } else {
                    donnee = new Object[]{
                            incident.getId(),
                            incident.getApplicationConcernee().getNom(),
                            incident.getDescription(),
                            incident.getPriorite(),
                            incident.getStatut(),
                            incident.getAssigneA() != null ? incident.getAssigneA().getNom() : "Non assigné",
                            "Afficher/Ajouter"
                    };
                }
                modeleTableau.addRow(donnee);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour du tableau : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
