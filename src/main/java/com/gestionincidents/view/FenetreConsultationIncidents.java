package com.gestionincidents.view;

import com.gestionincidents.controller.ApplicationController;
import com.gestionincidents.controller.CommentaireController;
import com.gestionincidents.controller.EquipeController;
import com.gestionincidents.controller.IncidentController;
import com.gestionincidents.controller.UtilisateurController;
import com.gestionincidents.model.Application;
import com.gestionincidents.model.Equipe;
import com.gestionincidents.model.Incident;
import com.gestionincidents.model.Statut;
import com.gestionincidents.model.Utilisateur;
import com.gestionincidents.view.rapporteur.FenetreModificationIncidentRapporteur;
import com.gestionincidents.view.responsable.FenetreAssignationIncidentResponsable;

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
                try {
                    // Utiliser les controllers au lieu des DAO directs
                    EquipeController equipeController = new EquipeController();
                    ApplicationController applicationController = new ApplicationController();
                    
                    // Récupérer l'équipe dirigée par le responsable
                    Equipe equipe = equipeController.getEquipeByResponsableId(utilisateur.getId());
                    
                    if (equipe == null) {
                        JOptionPane.showMessageDialog(this, "Aucune équipe trouvée pour ce responsable.", "Information", JOptionPane.INFORMATION_MESSAGE);
                        return new ArrayList<>();
                    }
                    
                    // Récupérer l'application gérée par cette équipe
                    Application application = applicationController.getApplicationsByEquipeId(equipe.getId());

                    if (application == null) {
                        JOptionPane.showMessageDialog(this, "Aucune application trouvée pour cette équipe.", "Information", JOptionPane.INFORMATION_MESSAGE);
                        return new ArrayList<>();
                    }
                    
                    // Filtrer les incidents qui concernent cette application
                    return incidents.stream()
                        .filter(incident -> 
                            incident.getApplicationConcernee() != null && incident.getApplicationConcernee().getId() == application.getId()
                        )
                        .collect(Collectors.toList());
                } catch (SQLException | IOException e) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des incidents de l'application : " 
                        + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    return new ArrayList<>();
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
            boolean assignerAutorise = utilisateur.getRole().equals("responsable");

            // Définir les noms de colonnes
            String[] colonnes;
            if (changerStatutAutorise && assignerAutorise) {
                colonnes = new String[]{"ID", "Application", "Description", "Priorité", "Statut", "Assigné à", "Commentaires", "Changer Statut", "Assigner"};
            } else if (modifierAutorise && changerStatutAutorise) {
                colonnes = new String[]{"ID", "Application", "Description", "Priorité", "Statut", "Assigné à", "Commentaires", "Modifier", "Changer Statut"};
            } else if (changerStatutAutorise) {
                colonnes = new String[]{"ID", "Application", "Description", "Priorité", "Statut", "Assigné à", "Commentaires", "Changer Statut"};
            } else {
                colonnes = new String[]{"ID", "Application", "Description", "Priorité", "Statut", "Assigné à", "Commentaires"};
            }

            modeleTableau.setColumnIdentifiers(colonnes);

            for (Incident incident : incidents) {
                Object[] donnee;
                if (changerStatutAutorise && assignerAutorise) {
                    donnee = new Object[]{
                            incident.getId(),
                            incident.getApplicationConcernee().getNom(),
                            incident.getDescription(),
                            incident.getPriorite(),
                            incident.getStatut(),
                            incident.getAssigneA() != null ? incident.getAssigneA().getNom() : "Non assigné",
                            "Afficher/Ajouter",
                            "Changer Statut",
                            "Assigner"
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
                                "Modifier",
                                "Changer Statut"
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

                // Ajout du gestionnaire d'événements pour le bouton "Assigner"
                tableauIncidents.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        int colonne = tableauIncidents.getSelectedColumn();
                        int ligne = tableauIncidents.rowAtPoint(e.getPoint());
                        if (ligne >= 0 && tableauIncidents.getColumnName(colonne).equals("Assigner") && utilisateur.getRole().equals("responsable")) {
                            int incidentId = (int) modeleTableau.getValueAt(ligne, 0); // Récupérer l'ID de l'incident

                            try {
                                IncidentController incidentController = new IncidentController();
                                UtilisateurController utilisateurController = new UtilisateurController();

                                // Récupérer uniquement l'incident sélectionné
                                Incident incident = incidentController.getIncident(incidentId);

                                if (incident == null) {
                                    JOptionPane.showMessageDialog(null, "Incident introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
                                } else if (incident.getStatut() != Statut.NOUVEAU && incident.getStatut() != Statut.RE_OUVERT) {
                                    JOptionPane.showMessageDialog(null, "Cet incident ne peut-être assigné ou ré-assigné.", "Erreur", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    new FenetreAssignationIncidentResponsable(incidentController, utilisateurController, utilisateur).setVisible(true);
                                }
                            } catch (SQLException | IOException ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(null, "Erreur d'ouverture : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                });

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour du tableau : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
}
