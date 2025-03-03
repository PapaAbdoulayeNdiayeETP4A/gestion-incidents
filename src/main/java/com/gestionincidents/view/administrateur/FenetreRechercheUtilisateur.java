package com.gestionincidents.view.administrateur;

import com.gestionincidents.controller.UtilisateurController;
import com.gestionincidents.model.Utilisateur;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FenetreRechercheUtilisateur extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField rechercheField;
    private JRadioButton nomRadioButton;
    private JRadioButton idRadioButton;
    private JTable resultatTable;
    private DefaultTableModel tableModel;
    private UtilisateurController utilisateurController;
    private JButton modifierButton;
    private JButton supprimerButton;
    private JButton ajouterButton;
    private JButton rafraichirButton;
    private Color couleurPrincipale = new Color(66, 133, 244);
    private Color couleurSecondaire = new Color(232, 240, 254);
    private Color couleurAccent = new Color(234, 67, 53);

    public FenetreRechercheUtilisateur(UtilisateurController utilisateurController) {
        this.utilisateurController = utilisateurController;
        
        setTitle("Gestion des Utilisateurs");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Créer un panneau principal avec marge
        JPanel panneauPrincipal = new JPanel(new BorderLayout(15, 15));
        panneauPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        panneauPrincipal.setBackground(Color.WHITE);

        // Titre élégant
        JLabel titreLabel = new JLabel("Recherche et Gestion des Utilisateurs");
        titreLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titreLabel.setForeground(couleurPrincipale);
        titreLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panneauPrincipal.add(titreLabel, BorderLayout.NORTH);

        // Panneau central qui contient recherche et résultats
        JPanel panneauCentral = new JPanel(new BorderLayout(0, 15));
        panneauCentral.setBackground(Color.WHITE);
        
        // Panneau de recherche stylisé
        JPanel panneauRecherche = new JPanel();
        panneauRecherche.setLayout(new BoxLayout(panneauRecherche, BoxLayout.Y_AXIS));
        panneauRecherche.setBackground(couleurSecondaire);
        panneauRecherche.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(couleurPrincipale, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        // Titre de la section recherche
        JLabel rechercheLabel = new JLabel("Critères de recherche");
        rechercheLabel.setFont(new Font("Arial", Font.BOLD, 16));
        rechercheLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panneauRecherche.add(rechercheLabel);
        panneauRecherche.add(Box.createVerticalStrut(10));
        
        // Zone de texte stylisée
        JPanel champPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        champPanel.setBackground(couleurSecondaire);
        champPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        rechercheField = new JTextField(25);
        rechercheField.setFont(new Font("Arial", Font.PLAIN, 14));
        rechercheField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        champPanel.add(rechercheField);
        
        panneauRecherche.add(champPanel);
        panneauRecherche.add(Box.createVerticalStrut(10));
        
        // Options de recherche
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        optionsPanel.setBackground(couleurSecondaire);
        optionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        nomRadioButton = new JRadioButton("Par Nom");
        idRadioButton = new JRadioButton("Par ID");
        nomRadioButton.setFont(new Font("Arial", Font.PLAIN, 14));
        idRadioButton.setFont(new Font("Arial", Font.PLAIN, 14));
        nomRadioButton.setBackground(couleurSecondaire);
        idRadioButton.setBackground(couleurSecondaire);
        
        ButtonGroup groupeRadio = new ButtonGroup();
        groupeRadio.add(nomRadioButton);
        groupeRadio.add(idRadioButton);
        nomRadioButton.setSelected(true);
        
        optionsPanel.add(nomRadioButton);
        optionsPanel.add(idRadioButton);
        panneauRecherche.add(optionsPanel);
        panneauRecherche.add(Box.createVerticalStrut(15));
        
        // Boutons de recherche
        JPanel boutonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        boutonsPanel.setBackground(couleurSecondaire);
        boutonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton rechercherButton = createStyledButton("Rechercher", couleurPrincipale, Color.WHITE);
        boutonsPanel.add(rechercherButton);
        
        rafraichirButton = createStyledButton("Actualiser", new Color(76, 175, 80), Color.WHITE);
        boutonsPanel.add(rafraichirButton);
        
        panneauRecherche.add(boutonsPanel);
        panneauCentral.add(panneauRecherche, BorderLayout.NORTH);
        
        // Tableau des résultats stylisé
        String[] colonnes = {"ID", "Nom", "Email", "Rôle", "Statut"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendre le tableau non éditable
            }
        };
        
        resultatTable = new JTable(tableModel);
        resultatTable.setFont(new Font("Arial", Font.PLAIN, 14));
        resultatTable.setRowHeight(35);
        resultatTable.setShowGrid(false);
        resultatTable.setIntercellSpacing(new Dimension(0, 0));
        resultatTable.setSelectionBackground(couleurSecondaire);
        resultatTable.setSelectionForeground(Color.BLACK);
        
        // Style d'en-tête de tableau
        JTableHeader header = resultatTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(couleurPrincipale);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        // Centrer le contenu des cellules
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < resultatTable.getColumnCount(); i++) {
            resultatTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Double-clic pour modifier
        resultatTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    try {
                        modifierUtilisateur();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(resultatTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panneauCentral.add(scrollPane, BorderLayout.CENTER);
        panneauPrincipal.add(panneauCentral, BorderLayout.CENTER);
        
        // Panneau d'actions (boutons en bas)
        JPanel panneauActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panneauActions.setBackground(Color.WHITE);
        
        ajouterButton = createStyledButton("Ajouter", new Color(76, 175, 80), Color.WHITE);
        modifierButton = createStyledButton("Modifier", couleurPrincipale, Color.WHITE);
        supprimerButton = createStyledButton("Supprimer", couleurAccent, Color.WHITE);
        
        panneauActions.add(ajouterButton);
        panneauActions.add(modifierButton);
        panneauActions.add(supprimerButton);
        panneauPrincipal.add(panneauActions, BorderLayout.SOUTH);

        // Action du bouton Rechercher
        rechercherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    rechercherUtilisateur();
                } catch (IOException e1) {
                    afficherErreur("Erreur lors de la recherche", e1.getMessage());
                }
            }
        });
        
        // Action du bouton Actualiser
        rafraichirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    chargerTousLesUtilisateurs();
                } catch (IOException | SQLException e1) {
                    afficherErreur("Erreur de chargement", e1.getMessage());
                }
            }
        });

        // Action du bouton Modifier
        modifierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    modifierUtilisateur();
                } catch (IOException e1) {
                    afficherErreur("Erreur de modification", e1.getMessage());
                }
            }
        });

        // Action du bouton Ajouter
        ajouterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ouvrirFenetreAjout();
            }
        });

        // Action du bouton Supprimer
        supprimerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    supprimerUtilisateur();
                } catch (SQLException | IOException e1) {
                    afficherErreur("Erreur de suppression", e1.getMessage());
                }
            }
        });

        add(panneauPrincipal);
        
        // Charger tous les utilisateurs au démarrage
        try {
            chargerTousLesUtilisateurs();
        } catch (IOException | SQLException e) {
            afficherErreur("Erreur de chargement initial", e.getMessage());
        }
        
        setVisible(true);
    }
    
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 40));
        
        // Effet hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void chargerTousLesUtilisateurs() throws IOException, SQLException {
        tableModel.setRowCount(0);
        List<Utilisateur> utilisateurs = utilisateurController.getUtilisateurs();
        for (Utilisateur utilisateur : utilisateurs) {
            if (!utilisateur.isEstSupprime()) {
                tableModel.addRow(new Object[]{
                    utilisateur.getId(), 
                    utilisateur.getNom(), 
                    utilisateur.getEmail(), 
                    utilisateur.getRole(),
                    "Actif"
                });
            }
        }
    }

    private void rechercherUtilisateur() throws IOException {
        String recherche = rechercheField.getText().trim();
        
        if (recherche.isEmpty()) {
            afficherInfo("Champ vide", "Veuillez saisir un critère de recherche.");
            return;
        }
        
        tableModel.setRowCount(0); // Effacer les résultats précédents

        try {
            if (nomRadioButton.isSelected()) {
                List<Utilisateur> utilisateurs = utilisateurController.rechercherUtilisateursParNom(recherche);
                if (utilisateurs.isEmpty()) {
                    afficherInfo("Aucun résultat", "Aucun utilisateur trouvé avec ce nom.");
                    return;
                }
                
                for (Utilisateur utilisateur : utilisateurs) {
                    if (!utilisateur.isEstSupprime()) {
                        tableModel.addRow(new Object[]{
                            utilisateur.getId(), 
                            utilisateur.getNom(), 
                            utilisateur.getEmail(), 
                            utilisateur.getRole(),
                            "Actif"
                        });
                    }
                }
            } else if (idRadioButton.isSelected()) {
                try {
                    int id = Integer.parseInt(recherche);
                    Utilisateur utilisateur = utilisateurController.rechercherUtilisateurParId(id);
                    if (utilisateur != null && !utilisateur.isEstSupprime()) {
                        tableModel.addRow(new Object[]{
                            utilisateur.getId(), 
                            utilisateur.getNom(), 
                            utilisateur.getEmail(), 
                            utilisateur.getRole(),
                            "Actif"
                        });
                    } else {
                        afficherInfo("Aucun résultat", "Utilisateur non trouvé ou supprimé.");
                    }
                } catch (NumberFormatException ex) {
                    afficherErreur("Format invalide", "Veuillez saisir un ID numérique valide.");
                }
            }
        } catch (SQLException ex) {
            afficherErreur("Erreur de recherche", ex.getMessage());
        }
    }
    
    private void ouvrirFenetreAjout() {
        // À implémenter selon la structure de votre application
        // Exemple:
        // new FenetreAjoutUtilisateur(utilisateurController, this).setVisible(true);
        afficherInfo("Fonctionnalité", "L'ajout d'utilisateur sera disponible dans la prochaine version.");
    }
    
    private void modifierUtilisateur() throws IOException {
        int ligneSelectionnee = resultatTable.getSelectedRow();
        if (ligneSelectionnee == -1) {
            afficherInfo("Sélection requise", "Veuillez sélectionner un utilisateur à modifier.");
            return;
        }

        int idUtilisateur = (int) tableModel.getValueAt(ligneSelectionnee, 0);
        try {
            Utilisateur utilisateur = utilisateurController.rechercherUtilisateurParId(idUtilisateur);
            if (utilisateur != null) {
                new FenetreModificationUtilisateur(utilisateur, utilisateurController, this).setVisible(true);
            }
        } catch (SQLException ex) {
            afficherErreur("Erreur de récupération", ex.getMessage());
        }
    }

    private void supprimerUtilisateur() throws SQLException, IOException {
        int ligneSelectionnee = resultatTable.getSelectedRow();
        if (ligneSelectionnee == -1) {
            afficherInfo("Sélection requise", "Veuillez sélectionner un utilisateur à supprimer.");
            return;
        }

        int idUtilisateur = (int) tableModel.getValueAt(ligneSelectionnee, 0);
        String nomUtilisateur = (String) tableModel.getValueAt(ligneSelectionnee, 1);
        
        // Boîte de dialogue de confirmation personnalisée
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Êtes-vous sûr de vouloir supprimer l'utilisateur " + nomUtilisateur + " ?"), BorderLayout.CENTER);
        
        int reponse = JOptionPane.showConfirmDialog(
            this, 
            panel, 
            "Confirmation de suppression", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (reponse == JOptionPane.YES_OPTION) {
            try {
                utilisateurController.supprimerUtilisateur(idUtilisateur);
                tableModel.removeRow(ligneSelectionnee);
                afficherSucces("Suppression réussie", "L'utilisateur " + nomUtilisateur + " a été supprimé avec succès.");
            } catch (SQLException ex) {
                afficherErreur("Erreur de suppression", ex.getMessage());
            }
        }
    }
    
    // Méthodes pour les boîtes de dialogue stylisées
    private void afficherInfo(String titre, String message) {
        JOptionPane.showMessageDialog(this, message, titre, JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void afficherErreur(String titre, String message) {
        JOptionPane.showMessageDialog(this, message, titre, JOptionPane.ERROR_MESSAGE);
    }
    
    private void afficherSucces(String titre, String message) {
        JOptionPane.showMessageDialog(this, message, titre, JOptionPane.PLAIN_MESSAGE);
    }
    
    // Méthode pour rafraîchir le tableau après une modification
    public void rafraichirTableau() {
        try {
            chargerTousLesUtilisateurs();
        } catch (IOException | SQLException e) {
            afficherErreur("Erreur de rafraîchissement", e.getMessage());
        }
    }
    
    // Classe interne pour la fenêtre de modification
    public class FenetreModificationUtilisateur extends JFrame {
        private Utilisateur utilisateur;
        private UtilisateurController controller;
        private FenetreRechercheUtilisateur parent;
        private JTextField nomField;
        private JTextField emailField;
        private JComboBox<String> roleComboBox;
        
        public FenetreModificationUtilisateur(Utilisateur utilisateur, UtilisateurController controller, FenetreRechercheUtilisateur parent) {
            this.utilisateur = utilisateur;
            this.controller = controller;
            this.parent = parent;
            
            setTitle("Modifier l'utilisateur");
            setSize(400, 350);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
            JPanel panel = new JPanel(new BorderLayout(15, 15));
            panel.setBorder(new EmptyBorder(20, 20, 20, 20));
            panel.setBackground(Color.WHITE);
            
            // Titre
            JLabel titreLabel = new JLabel("Modification de l'utilisateur");
            titreLabel.setFont(new Font("Arial", Font.BOLD, 18));
            titreLabel.setForeground(couleurPrincipale);
            panel.add(titreLabel, BorderLayout.NORTH);
            
            // Formulaire
            JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 15));
            formPanel.setBackground(Color.WHITE);
            
            formPanel.add(new JLabel("Nom :"));
            nomField = new JTextField(utilisateur.getNom());
            formPanel.add(nomField);
            
            formPanel.add(new JLabel("Email :"));
            emailField = new JTextField(utilisateur.getEmail());
            formPanel.add(emailField);
            
            formPanel.add(new JLabel("Rôle :"));
            String[] roles = {"Administrateur", "Technicien", "Utilisateur"};
            roleComboBox = new JComboBox<>(roles);
            roleComboBox.setSelectedItem(utilisateur.getRole());
            formPanel.add(roleComboBox);
            
            panel.add(formPanel, BorderLayout.CENTER);
            
            // Boutons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(Color.WHITE);
            
            JButton annulerButton = createStyledButton("Annuler", Color.GRAY, Color.WHITE);
            JButton enregistrerButton = createStyledButton("Enregistrer", couleurPrincipale, Color.WHITE);
            
            annulerButton.addActionListener(e -> dispose());
            enregistrerButton.addActionListener(e -> {
                try {
                    sauvegarderModifications();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            });
            
            buttonPanel.add(annulerButton);
            buttonPanel.add(enregistrerButton);
            panel.add(buttonPanel, BorderLayout.SOUTH);
            
            add(panel);
        }
        
        private void sauvegarderModifications() throws SQLException, IOException {
            utilisateur.setNom(nomField.getText());
            utilisateur.setEmail(emailField.getText());
            utilisateur.setRole((String) roleComboBox.getSelectedItem());
            
            controller.updateUtilisateur(utilisateur);
            parent.rafraichirTableau();
            JOptionPane.showMessageDialog(this, "Utilisateur modifié avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }
}