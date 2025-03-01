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
import com.gestionincidents.view.rapporteur.FenetreCreationIncidentRapporteur;
import com.gestionincidents.view.rapporteur.FenetreModificationIncidentRapporteur;
import com.gestionincidents.view.responsable.FenetreAssignationIncidentResponsable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.border.*;
import java.awt.event.*;

public class FenetreConsultationIncidents extends JFrame {

    private static final long serialVersionUID = 1L;
    private IncidentController incidentController;
    private CommentaireController commentaireController;
    private Utilisateur utilisateur;
    private JPanel panneauIncidents;
    private JScrollPane scrollPane;
    private JTextField searchField;
    private JComboBox<String> filterComboBox;
    private Color accentColor = new Color(70, 130, 180); // Bleu acier
    private Color backgroundColor = new Color(245, 245, 245); // Gris très clair
    private Color textColor = new Color(50, 50, 50); // Gris foncé
    private Color highlightColor = new Color(240, 248, 255); // Bleu très clair

    public FenetreConsultationIncidents(Utilisateur utilisateur) throws SQLException, IOException {
        this.utilisateur = utilisateur;
        try {
            this.incidentController = new IncidentController();
            this.commentaireController = new CommentaireController();
        } catch (SQLException | IOException e) {
            showErrorDialog("Erreur lors de l'initialisation des contrôleurs", e);
            dispose();
            return;
        }

        configureWindow();
        createUI();
        refreshIncidents();
    }

    private void configureWindow() {
        setTitle("Gestion des Incidents");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(backgroundColor);
    }

    private void createUI() {
        // Panel principal avec BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(backgroundColor);
        setContentPane(mainPanel);
        
        // En-tête avec titre et recherche
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Panel central pour les cartes d'incidents avec défilement
        panneauIncidents = new JPanel();
        panneauIncidents.setLayout(new BoxLayout(panneauIncidents, BoxLayout.Y_AXIS));
        panneauIncidents.setBackground(backgroundColor);
        
        scrollPane = new JScrollPane(panneauIncidents);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel pour action rapide (création d'incident)
        if (utilisateur.getRole().equals("rapporteur")) {
            JPanel actionPanel = createActionPanel();
            mainPanel.add(actionPanel, BorderLayout.SOUTH);
        }
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBackground(backgroundColor);
        headerPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        // Titre avec icône
        JLabel titleLabel = new JLabel("Gestion des Incidents");
        titleLabel.setFont(new Font("Segoe UI Symbol", Font.BOLD, 24));
        titleLabel.setForeground(accentColor);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Panel de recherche et filtres
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(backgroundColor);
        
        // Filtre par statut
        JLabel filterLabel = new JLabel("Filtrer par: ");
        filterLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
        searchPanel.add(filterLabel);
        
        String[] filterOptions = {"Tous", "Nouveau", "En cours", "Résolu", "Fermé", "Re-ouvert"};
        filterComboBox = new JComboBox<>(filterOptions);
        filterComboBox.setPreferredSize(new Dimension(120, 30));
        filterComboBox.addActionListener(e -> {
            try {
                refreshIncidents();
            } catch (SQLException | IOException ex) {
                showErrorDialog("Erreur lors du filtrage", ex);
            }
        });
        searchPanel.add(filterComboBox);
        
        // Champ de recherche
        searchField = new JTextField(15);
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentColor),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    refreshIncidents();
                } catch (SQLException | IOException ex) {
                    showErrorDialog("Erreur lors de la recherche", ex);
                }
            }
        });
        
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Segoe UI Symbol Symbol", Font.PLAIN, 14));
        searchPanel.add(searchIcon);
        searchPanel.add(searchField);
        
        headerPanel.add(searchPanel, BorderLayout.EAST);
        
        return headerPanel;
    }

    private JPanel createActionPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(backgroundColor);
        
        JButton createButton = new JButton("Nouveau Incident");
        createButton.setFont(new Font("Segoe UI Symbol", Font.BOLD, 14));
        createButton.setBackground(accentColor);
        createButton.setForeground(Color.WHITE);
        createButton.setFocusPainted(false);
        createButton.setBorder(new EmptyBorder(8, 15, 8, 15));
        createButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        createButton.addActionListener(e -> {
            try {
                new FenetreCreationIncidentRapporteur(utilisateur).setVisible(true);
                refreshIncidents();
            } catch (Exception ex) {
                showErrorDialog("Erreur lors de la création d'incident", ex);
            }
        });
        
        actionPanel.add(createButton);
        return actionPanel;
    }
    
    private void refreshIncidents() throws SQLException, IOException {
        List<Incident> incidents = getIncidentsByUserRole();
        
        // Appliquer le filtre si nécessaire
        String filterValue = (String) filterComboBox.getSelectedItem();
        if (filterValue != null && !filterValue.equals("Tous")) {
            incidents = incidents.stream()
                    .filter(i -> i.getStatut().toString().equalsIgnoreCase(filterValue))
                    .collect(Collectors.toList());
        }
        
        // Appliquer la recherche si nécessaire
        String searchText = searchField.getText().toLowerCase().trim();
        if (!searchText.isEmpty()) {
            incidents = incidents.stream()
                    .filter(i -> 
                        (i.getDescription() != null && i.getDescription().toLowerCase().contains(searchText)) ||
                        (i.getApplicationConcernee() != null && i.getApplicationConcernee().getNom().toLowerCase().contains(searchText)) ||
                        (i.getPriorite() != null && i.getPriorite().toString().toLowerCase().contains(searchText)) ||
                        (i.getStatut() != null && i.getStatut().toString().toLowerCase().contains(searchText))
                    )
                    .collect(Collectors.toList());
        }
        
        displayIncidents(incidents);
    }
    
    private void displayIncidents(List<Incident> incidents) {
        panneauIncidents.removeAll();
        
        if (incidents.isEmpty()) {
            JLabel emptyLabel = new JLabel("Aucun incident correspondant aux critères");
            emptyLabel.setFont(new Font("Segoe UI Symbol", Font.ITALIC, 16));
            emptyLabel.setForeground(textColor);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panneauIncidents.add(Box.createVerticalGlue());
            panneauIncidents.add(emptyLabel);
            panneauIncidents.add(Box.createVerticalGlue());
        } else {
            for (Incident incident : incidents) {
                JPanel incidentCard = createIncidentCard(incident);
                panneauIncidents.add(incidentCard);
                panneauIncidents.add(Box.createVerticalStrut(10));
            }
        }
        
        panneauIncidents.revalidate();
        panneauIncidents.repaint();
    }
    
    private JPanel createIncidentCard(Incident incident) {
        JPanel card = new JPanel(new BorderLayout(10, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        card.setBackground(Color.WHITE);
        
        // En-tête de la carte
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        // ID et application
        JPanel idAppPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        idAppPanel.setBackground(Color.WHITE);
        
        JLabel idLabel = new JLabel("#" + incident.getId());
        idLabel.setFont(new Font("Segoe UI Symbol", Font.BOLD, 14));
        idLabel.setForeground(accentColor);
        
        JLabel appLabel = new JLabel(" | " + incident.getApplicationConcernee().getNom());
        appLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
        
        idAppPanel.add(idLabel);
        idAppPanel.add(appLabel);
        headerPanel.add(idAppPanel, BorderLayout.WEST);
        
        // Statut et priorité
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        statusPanel.setBackground(Color.WHITE);
        
        JLabel statusLabel = new JLabel(incident.getStatut().toString());
        statusLabel.setFont(new Font("Segoe UI Symbol", Font.BOLD, 12));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setOpaque(true);
        statusLabel.setBorder(new EmptyBorder(3, 8, 3, 8));
        
        // Couleur selon le statut
        switch (incident.getStatut()) {
            case NOUVEAU:
                statusLabel.setBackground(new Color(65, 105, 225)); // Bleu royal
                break;
            case ASSIGNE:
                statusLabel.setBackground(new Color(255, 165, 0)); // Orange
                break;
            case RESOLU:
                statusLabel.setBackground(new Color(60, 179, 113)); // Vert moyen
                break;
            case CLOTURE:
                statusLabel.setBackground(new Color(105, 105, 105)); // Gris
                break;
            case RE_OUVERT:
                statusLabel.setBackground(new Color(220, 20, 60)); // Cramoisi
                break;
            case EN_ATTENTE:
            	statusLabel.setBackground(new Color(255, 215, 0)); // Or
        }
        
        JLabel priorityLabel = new JLabel(incident.getPriorite().toString());
        priorityLabel.setFont(new Font("Segoe UI Symbol", Font.BOLD, 12));
        priorityLabel.setForeground(Color.WHITE);
        priorityLabel.setOpaque(true);
        priorityLabel.setBorder(new EmptyBorder(3, 8, 3, 8));
        
        // Couleur selon la priorité
        switch (incident.getPriorite()) {
            case FAIBLE:
                priorityLabel.setBackground(new Color(70, 130, 180)); // Bleu acier
                break;
            case MOYENNE:
                priorityLabel.setBackground(new Color(255, 165, 0)); // Orange
                break;
            case ELEVEE:
                priorityLabel.setBackground(new Color(178, 34, 34)); // Rouge brique
                break;
            case URGENTE:
                priorityLabel.setBackground(new Color(139, 0, 0)); // Rouge foncé
                break;
        }
        
        statusPanel.add(priorityLabel);
        statusPanel.add(statusLabel);
        headerPanel.add(statusPanel, BorderLayout.EAST);
        
        card.add(headerPanel, BorderLayout.NORTH);
        
        // Description
        JTextArea descriptionArea = new JTextArea(incident.getDescription());
        descriptionArea.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(Color.WHITE);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        descriptionArea.setRows(2);
        
        card.add(descriptionArea, BorderLayout.CENTER);
        
        // Pied de carte
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.WHITE);
        
        // Information sur l'assignation
        JPanel assigneePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        assigneePanel.setBackground(Color.WHITE);
        
        JLabel assigneeLabel = new JLabel("👤 " + (incident.getAssigneA() != null ? incident.getAssigneA().getNom() : "Non assigné"));
        assigneeLabel.setFont(new Font("Segoe UI Symbol", Font.ITALIC, 12));
        assigneeLabel.setForeground(textColor);
        
        assigneePanel.add(assigneeLabel);
        footerPanel.add(assigneePanel, BorderLayout.WEST);
        
        // Boutons d'action
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        // Commentaires
        JButton commentButton = createStyledButton("💬 Commentaires");
        commentButton.addActionListener(e -> handleCommentaires(incident.getId()));
        
        buttonPanel.add(commentButton);
        
        // Actions spécifiques au rôle
        if (utilisateur.getRole().equals("rapporteur")) {
            JButton editButton = createStyledButton("✏️ Modifier");
            editButton.addActionListener(e -> {
                try {
                    new FenetreModificationIncidentRapporteur(incident.getId(), incidentController).setVisible(true);
                    refreshIncidents();
                } catch (SQLException | IOException ex) {
                    showErrorDialog("Erreur lors de la modification", ex);
                }
            });
            buttonPanel.add(editButton);
        }
        
        if (utilisateur.getRole().equals("rapporteur") || 
            utilisateur.getRole().equals("developpeur") || 
            utilisateur.getRole().equals("responsable")) {
            
            JButton statusButton = createStyledButton("🔄 Statut");
            statusButton.addActionListener(e -> {
                try {
                    new FenetreChangementStatutIncident(incident.getId(), incidentController).setVisible(true);
                    refreshIncidents();
                } catch (SQLException | IOException ex) {
                    showErrorDialog("Erreur lors du changement de statut", ex);
                }
            });
            buttonPanel.add(statusButton);
        }
        
        if (utilisateur.getRole().equals("responsable")) {
            JButton assignButton = createStyledButton("👤 Assigner");
            assignButton.addActionListener(e -> {
                try {
                    if (incident.getStatut() != Statut.NOUVEAU && incident.getStatut() != Statut.RE_OUVERT) {
                        JOptionPane.showMessageDialog(this, 
                            "Cet incident ne peut être assigné ou ré-assigné.",
                            "Information", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    
                    UtilisateurController utilisateurController = new UtilisateurController();
                    new FenetreAssignationIncidentResponsable(incidentController, utilisateurController, utilisateur).setVisible(true);
                    refreshIncidents();
                } catch (SQLException | IOException ex) {
                    showErrorDialog("Erreur lors de l'assignation", ex);
                }
            });
            buttonPanel.add(assignButton);
        }
        
        footerPanel.add(buttonPanel, BorderLayout.EAST);
        card.add(footerPanel, BorderLayout.SOUTH);
        
        // Effet de survol
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(highlightColor);
                headerPanel.setBackground(highlightColor);
                idAppPanel.setBackground(highlightColor);
                statusPanel.setBackground(highlightColor);
                descriptionArea.setBackground(highlightColor);
                footerPanel.setBackground(highlightColor);
                assigneePanel.setBackground(highlightColor);
                buttonPanel.setBackground(highlightColor);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
                headerPanel.setBackground(Color.WHITE);
                idAppPanel.setBackground(Color.WHITE);
                statusPanel.setBackground(Color.WHITE);
                descriptionArea.setBackground(Color.WHITE);
                footerPanel.setBackground(Color.WHITE);
                assigneePanel.setBackground(Color.WHITE);
                buttonPanel.setBackground(Color.WHITE);
            }
        });
        
        return card;
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 12));
        button.setBackground(Color.WHITE);
        button.setForeground(accentColor);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentColor, 1),
                BorderFactory.createEmptyBorder(3, 8, 3, 8)));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(accentColor);
                button.setForeground(Color.WHITE);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
                button.setForeground(accentColor);
            }
        });
        
        return button;
    }
    
    private void handleCommentaires(int incidentId) {
        try {
            if (commentaireController.getCommentaires(incidentId).isEmpty()) {
                int reponse = JOptionPane.showConfirmDialog(
                    this,
                    "Cet incident n'a aucun commentaire. Voulez-vous en ajouter un ?",
                    "Aucun commentaire",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (reponse == JOptionPane.YES_OPTION) {
                    new FenetreCreationCommentaire(incidentId, utilisateur, commentaireController).setVisible(true);
                    refreshIncidents();
                }
            } else {
                new FenetreAffichageCommentaires(incidentId, commentaireController, utilisateur).setVisible(true);
            }
        } catch (SQLException | IOException ex) {
            showErrorDialog("Erreur lors de la gestion des commentaires", ex);
        }
    }
    
    private List<Incident> getIncidentsByUserRole() throws SQLException, IOException {
        List<Incident> incidents = incidentController.getIncidents();
        
        switch (utilisateur.getRole()) {
            case "rapporteur":
                return incidents.stream()
                        .filter(incident -> incident.getRapporteur() != null && 
                                           incident.getRapporteur().getId() == utilisateur.getId())
                        .collect(Collectors.toList());
                
            case "developpeur":
                return incidents.stream()
                        .filter(incident -> incident.getAssigneA() != null && 
                                           incident.getAssigneA().getId() == utilisateur.getId())
                        .collect(Collectors.toList());
                
            case "responsable":
                try {
                    EquipeController equipeController = new EquipeController();
                    ApplicationController applicationController = new ApplicationController();
                    
                    Equipe equipe = equipeController.getEquipeByResponsableId(utilisateur.getId());
                    
                    if (equipe == null) {
                        JOptionPane.showMessageDialog(this, 
                            "Aucune équipe trouvée pour ce responsable.", 
                            "Information", JOptionPane.INFORMATION_MESSAGE);
                        return new ArrayList<>();
                    }
                    
                    Application application = applicationController.getApplicationsByEquipeId(equipe.getId());
                    
                    if (application == null) {
                        JOptionPane.showMessageDialog(this, 
                            "Aucune application trouvée pour cette équipe.", 
                            "Information", JOptionPane.INFORMATION_MESSAGE);
                        return new ArrayList<>();
                    }
                    
                    return incidents.stream()
                            .filter(incident -> incident.getApplicationConcernee() != null && 
                                               incident.getApplicationConcernee().getId() == application.getId())
                            .collect(Collectors.toList());
                } catch (SQLException | IOException e) {
                    showErrorDialog("Erreur lors de la récupération des incidents", e);
                    return new ArrayList<>();
                }
                
            default:
                return incidents; // Afficher tous les incidents pour les autres rôles
        }
    }
    
    private void showErrorDialog(String message, Exception e) {
        JOptionPane.showMessageDialog(
            this,
            message + " : " + e.getMessage(),
            "Erreur",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
