package com.gestionincidents.view;

import com.gestionincidents.controller.CommentaireController;
import com.gestionincidents.model.Commentaire;
import com.gestionincidents.model.Incident;
import com.gestionincidents.model.Utilisateur;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class FenetreCreationReponseCommentaire extends JFrame {

    private static final long serialVersionUID = 1L;
    private Color accentColor = new Color(70, 130, 180);
    private Color backgroundColor = new Color(245, 245, 245);
    private Color textColor = new Color(50, 50, 50);
    
    public FenetreCreationReponseCommentaire(int incidentId, int commentaireParentId, Utilisateur utilisateur, CommentaireController commentaireController) throws SQLException, IOException {
        setTitle("Répondre au commentaire " + commentaireParentId);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(backgroundColor);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(backgroundColor);
        add(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel labelCommentaire = new JLabel("Réponse :");
        labelCommentaire.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelCommentaire.setForeground(textColor);
        mainPanel.add(labelCommentaire, gbc);

        JTextArea champCommentaire = new JTextArea();
        champCommentaire.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        champCommentaire.setLineWrap(true);
        champCommentaire.setWrapStyleWord(true);
        JScrollPane scrollPaneCommentaire = new JScrollPane(champCommentaire);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(scrollPaneCommentaire, gbc);

        JButton boutonAjouter = createStyledButton("Ajouter");
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(boutonAjouter, gbc);

        boutonAjouter.addActionListener(e -> {
            String contenu = champCommentaire.getText();
            Commentaire commentaire = new Commentaire(contenu, LocalDateTime.now(), utilisateur, new Incident());
            try {
                commentaireController.createReponseCommentaire(commentaire, incidentId, commentaireParentId);
                JOptionPane.showMessageDialog(FenetreCreationReponseCommentaire.this, "Réponse ajoutée avec succès !");
                dispose();
            } catch (SQLException | IOException ex) {
                JOptionPane.showMessageDialog(FenetreCreationReponseCommentaire.this, "Erreur lors de l'ajout de la réponse : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBackground(Color.WHITE);
        button.setForeground(accentColor);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentColor, 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)));
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
}