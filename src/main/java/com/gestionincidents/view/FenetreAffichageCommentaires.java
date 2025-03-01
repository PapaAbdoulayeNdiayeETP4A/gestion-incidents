package com.gestionincidents.view;

import com.gestionincidents.controller.CommentaireController;
import com.gestionincidents.model.Commentaire;
import com.gestionincidents.model.Utilisateur;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Comparator;
import java.util.ArrayList;

public class FenetreAffichageCommentaires extends JFrame {

    private static final long serialVersionUID = 1L;
    private int incidentId;
    private CommentaireController commentaireController;
    private Utilisateur utilisateur;
    private JPanel panneauCommentaires;
    private JScrollPane scrollPane;
    
    private Color accentColor = new Color(70, 130, 180); // Bleu acier
    private Color backgroundColor = new Color(245, 245, 245); // Gris très clair
    private Color textColor = new Color(50, 50, 50); // Gris foncé
    private Color cardColor = Color.WHITE;
    private Color highlightColor = new Color(240, 248, 255); // Bleu très clair

    public FenetreAffichageCommentaires(int incidentId, CommentaireController commentaireController, Utilisateur utilisateur) throws SQLException, IOException {
        this.incidentId = incidentId;
        this.commentaireController = commentaireController;
        this.utilisateur = utilisateur;
        
        configureWindow();
        createUI();
        loadComments();
    }
    
    private void configureWindow() {
        setTitle("Commentaires de l'incident #" + incidentId);
        setSize(700, 500);
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
        
        // En-tête avec titre
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Panel central pour les commentaires avec défilement
        panneauCommentaires = new JPanel();
        panneauCommentaires.setLayout(new BoxLayout(panneauCommentaires, BoxLayout.Y_AXIS));
        panneauCommentaires.setBackground(backgroundColor);
        
        scrollPane = new JScrollPane(panneauCommentaires);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel pour l'ajout de commentaire
        JPanel actionPanel = createActionPanel();
        mainPanel.add(actionPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(backgroundColor);
        headerPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JLabel titleLabel = new JLabel("Commentaires de l'incident #" + incidentId);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(accentColor);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JLabel infoLabel = new JLabel("Cliquez sur 'Répondre' pour ajouter une réponse à un commentaire");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        infoLabel.setForeground(textColor);
        headerPanel.add(infoLabel, BorderLayout.SOUTH);
        
        return headerPanel;
    }
    
    private JPanel createActionPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(backgroundColor);
        actionPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JButton newCommentButton = new JButton("Ajouter un commentaire");
        newCommentButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        newCommentButton.setBackground(accentColor);
        newCommentButton.setForeground(Color.WHITE);
        newCommentButton.setFocusPainted(false);
        newCommentButton.setBorder(new EmptyBorder(8, 15, 8, 15));
        newCommentButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        newCommentButton.addActionListener(e -> {
            try {
                new FenetreCreationCommentaire(incidentId, utilisateur, commentaireController).setVisible(true);
                // Recharger les commentaires après ajout
                loadComments();
            } catch (SQLException | IOException ex) {
                showErrorDialog("Erreur lors de la création du commentaire", ex);
            }
        });
        
        actionPanel.add(newCommentButton);
        return actionPanel;
    }
    
    private void loadComments() throws SQLException, IOException {
        List<Commentaire> commentaires = commentaireController.getCommentaires(incidentId);
        
        // Organiser les commentaires avec leurs réponses
        Map<Integer, List<Commentaire>> commentsTree = organizeComments(commentaires);
        
        displayComments(commentsTree);
    }
    
    /**
     * Organise les commentaires en arborescence (commentaires parents et leurs réponses)
     */
    private Map<Integer, List<Commentaire>> organizeComments(List<Commentaire> commentaires) {
        // Map pour stocker les commentaires parents (ID null) et leurs réponses
        Map<Integer, List<Commentaire>> commentsTree = new HashMap<>();
        
        // Liste pour les commentaires parents (sans parent)
        List<Commentaire> parentComments = new ArrayList<>();
        
        // Séparer les commentaires parents et les réponses
        for (Commentaire commentaire : commentaires) {
            if (commentaire.getCommentaireParentId() == null) {
                parentComments.add(commentaire);
            } else {
                // Ajouter la réponse à la liste des réponses du parent
                commentsTree.computeIfAbsent(commentaire.getCommentaireParentId(), k -> new ArrayList<>())
                           .add(commentaire);
            }
        }
        
        // Trier les commentaires parents par date (plus récent en premier)
        Collections.sort(parentComments, Comparator.comparing(Commentaire::getDate).reversed());
        
        // Ajouter les commentaires parents à l'arbre
        for (Commentaire parent : parentComments) {
            commentsTree.put(parent.getId(), commentsTree.getOrDefault(parent.getId(), new ArrayList<>()));
        }
        
        // Trier les réponses par date
        for (List<Commentaire> responses : commentsTree.values()) {
            Collections.sort(responses, Comparator.comparing(Commentaire::getDate));
        }
        
        return commentsTree;
    }
    
    private void displayComments(Map<Integer, List<Commentaire>> commentsTree) {
        panneauCommentaires.removeAll();
        
        if (commentsTree.isEmpty()) {
            JLabel emptyLabel = new JLabel("Aucun commentaire pour cet incident");
            emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            emptyLabel.setForeground(textColor);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panneauCommentaires.add(Box.createVerticalGlue());
            panneauCommentaires.add(emptyLabel);
            panneauCommentaires.add(Box.createVerticalGlue());
        } else {
            // Parcourir les commentaires parents
            for (Map.Entry<Integer, List<Commentaire>> entry : commentsTree.entrySet()) {
                int parentId = entry.getKey();
                List<Commentaire> responses = entry.getValue();
                
                // Trouver le commentaire parent
                Commentaire parentComment = null;
                try {
                    parentComment = commentaireController.getCommentaire(parentId);
                } catch (SQLException | IOException e) {
                    showErrorDialog("Erreur lors de la récupération du commentaire", e);
                    continue;
                }
                
                if (parentComment != null && parentComment.getCommentaireParentId() == null) {
                    // Ajouter le commentaire parent
                    JPanel commentCard = createCommentCard(parentComment, false);
                    panneauCommentaires.add(commentCard);
                    
                    // Ajouter ses réponses
                    for (Commentaire response : responses) {
                        JPanel responseCard = createCommentCard(response, true);
                        panneauCommentaires.add(responseCard);
                    }
                    
                    // Ajouter un espace entre les groupes de commentaires
                    panneauCommentaires.add(Box.createVerticalStrut(15));
                }
            }
        }
        
        panneauCommentaires.revalidate();
        panneauCommentaires.repaint();
    }
    
    private JPanel createCommentCard(Commentaire commentaire, boolean isResponse) {
        JPanel card = new JPanel(new BorderLayout(10, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        card.setBackground(cardColor);
        
        // Ajouter marge à gauche pour les réponses
        if (isResponse) {
            card.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(0, 40, 0, 0),
                card.getBorder()));
        }
        
        // En-tête du commentaire
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(cardColor);
        
        // Auteur et date
        JPanel authorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        authorPanel.setBackground(cardColor);
        
        JLabel authorLabel = new JLabel(commentaire.getAuteur().getNom());
        authorLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        authorLabel.setForeground(accentColor);
        
        JLabel dateLabel = new JLabel(" • " + formatDate(commentaire.getDate()));
        dateLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        dateLabel.setForeground(new Color(120, 120, 120));
        
        authorPanel.add(authorLabel);
        authorPanel.add(dateLabel);
        headerPanel.add(authorPanel, BorderLayout.WEST);
        
        if (isResponse) {
            JLabel replyLabel = new JLabel("Réponse");
            replyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            replyLabel.setForeground(new Color(120, 120, 120));
            headerPanel.add(replyLabel, BorderLayout.EAST);
        }
        
        card.add(headerPanel, BorderLayout.NORTH);
        
        // Contenu du commentaire
        JTextArea contentArea = new JTextArea(commentaire.getContenu());
        contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setBackground(cardColor);
        contentArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Ajuster la hauteur en fonction du contenu
        int lineCount = contentArea.getText().split("\n").length;
        int rowHeight = Math.max(2, Math.min(8, lineCount)); // Entre 2 et 8 lignes
        contentArea.setRows(rowHeight);
        
        card.add(contentArea, BorderLayout.CENTER);
        
        // Pied du commentaire (bouton de réponse)
        if (!isResponse) {
            JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            footerPanel.setBackground(cardColor);
            
            JButton replyButton = createStyledButton("Répondre");
            replyButton.addActionListener(e -> {
                try {
                    new FenetreCreationReponseCommentaire(incidentId, commentaire.getId(), utilisateur, commentaireController).setVisible(true);
                    // Recharger les commentaires après ajout d'une réponse
                    loadComments();
                } catch (SQLException | IOException ex) {
                    showErrorDialog("Erreur lors de la création de la réponse", ex);
                }
            });
            
            footerPanel.add(replyButton);
            card.add(footerPanel, BorderLayout.SOUTH);
        }
        
        // Effet de survol
        MouseAdapter hoverEffect = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(highlightColor);
                headerPanel.setBackground(highlightColor);
                authorPanel.setBackground(highlightColor);
                contentArea.setBackground(highlightColor);
                if (!isResponse) {
                    JPanel footerPanel = (JPanel) card.getComponent(2);
                    footerPanel.setBackground(highlightColor);
                }
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(cardColor);
                headerPanel.setBackground(cardColor);
                authorPanel.setBackground(cardColor);
                contentArea.setBackground(cardColor);
                if (!isResponse) {
                    JPanel footerPanel = (JPanel) card.getComponent(2);
                    footerPanel.setBackground(cardColor);
                }
            }
        };
        
        card.addMouseListener(hoverEffect);
        
        return card;
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setBackground(Color.WHITE);
        button.setForeground(accentColor);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentColor, 1),
                BorderFactory.createEmptyBorder(3, 8, 3, 8)));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(accentColor);
                button.setForeground(Color.WHITE);
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
                button.setForeground(accentColor);
            }
        });
        
        return button;
    }
    
    private String formatDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return date.format(formatter);
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