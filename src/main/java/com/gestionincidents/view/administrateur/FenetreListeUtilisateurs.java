package com.gestionincidents.view.administrateur;

import com.gestionincidents.controller.UtilisateurController;
import com.gestionincidents.model.Utilisateur;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FenetreListeUtilisateurs extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable utilisateurTable;
    private DefaultTableModel tableModel;
    private UtilisateurController utilisateurController;
    private JTextField rechercheTF;
    private JButton rafraichirBtn;
    private JLabel statusLabel;
    
    // Définition des couleurs pour une interface moderne
    private static final Color COULEUR_FOND = new Color(240, 240, 245);
    private static final Color COULEUR_ACCENT = new Color(70, 130, 180);
    private static final Color COULEUR_TEXTE = new Color(50, 50, 50);
    private static final Color COULEUR_BOUTON = new Color(70, 130, 180);
    private static final Color COULEUR_BOUTON_TEXTE = Color.WHITE;
    private static final Color COULEUR_TABLEAU_ENTETE = new Color(70, 130, 180);
    private static final Color COULEUR_TABLEAU_ALTERNE = new Color(245, 245, 250);
    private static final Font POLICE_TITRE = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font POLICE_NORMALE = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font POLICE_BOUTON = new Font("Segoe UI", Font.BOLD, 12);

    public FenetreListeUtilisateurs(UtilisateurController utilisateurController) throws SQLException {
        this.utilisateurController = utilisateurController;

        setTitle("Gestion des Utilisateurs");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBackground(COULEUR_FOND);
        
        // Définir l'icône de l'application (si disponible)
        // setIconImage(new ImageIcon("path/to/icon.png").getImage());

        // Création du panneau principal avec un padding
        JPanel panneauPrincipal = new JPanel(new BorderLayout(10, 10));
        panneauPrincipal.setBackground(COULEUR_FOND);
        panneauPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Panneau de titre
        JPanel panneauTitre = new JPanel(new BorderLayout());
        panneauTitre.setBackground(COULEUR_FOND);
        JLabel lblTitre = new JLabel("Liste des Utilisateurs");
        lblTitre.setFont(POLICE_TITRE);
        lblTitre.setForeground(COULEUR_TEXTE);
        panneauTitre.add(lblTitre, BorderLayout.WEST);
        
        // Panneau de recherche
        JPanel panneauRecherche = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panneauRecherche.setBackground(COULEUR_FOND);
        rechercheTF = new JTextField(15);
        rechercheTF.setFont(POLICE_NORMALE);
        JButton rechercherBtn = new JButton("Rechercher");
        personaliserBouton(rechercherBtn);
        panneauRecherche.add(new JLabel("Rechercher: "));
        panneauRecherche.add(rechercheTF);
        panneauRecherche.add(rechercherBtn);
        panneauTitre.add(panneauRecherche, BorderLayout.EAST);
        
        panneauPrincipal.add(panneauTitre, BorderLayout.NORTH);

        // Tableau des utilisateurs avec un modèle personnalisé qui empêche l'édition
        String[] colonnes = {"ID", "Nom", "Email", "Rôle", "Statut"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendre le tableau non éditable
            }
        };
        
        utilisateurTable = new JTable(tableModel);
        utilisateurTable.setFont(POLICE_NORMALE);
        utilisateurTable.setRowHeight(30);
        utilisateurTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        utilisateurTable.setGridColor(new Color(230, 230, 230));
        utilisateurTable.setShowVerticalLines(true);
        utilisateurTable.setBackground(Color.WHITE);
        
        // Personnalisation de l'en-tête du tableau
        JTableHeader header = utilisateurTable.getTableHeader();
        header.setFont(POLICE_BOUTON);
        header.setBackground(COULEUR_TABLEAU_ENTETE);
        header.setForeground(Color.WHITE);
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        
        // Centrer le contenu des cellules
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < utilisateurTable.getColumnCount(); i++) {
            utilisateurTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Permettre le tri des colonnes
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        utilisateurTable.setRowSorter(sorter);
        
        // Double-clic pour modifier
        utilisateurTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    modifierUtilisateur();
                }
            }
        });
        
        // ScrollPane pour le tableau avec bordure
        JScrollPane scrollPane = new JScrollPane(utilisateurTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panneauPrincipal.add(scrollPane, BorderLayout.CENTER);

        // Boutons d'action
        JPanel panneauBoutons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panneauBoutons.setBackground(COULEUR_FOND);
        JButton modifierBtn = new JButton("Modifier");
        personaliserBouton(modifierBtn);
        JButton supprimerBtn = new JButton("Supprimer");
        supprimerBtn.setBackground(new Color(220, 53, 69));
        supprimerBtn.setForeground(Color.WHITE);
        supprimerBtn.setFont(POLICE_BOUTON);
        supprimerBtn.setFocusPainted(false);
        supprimerBtn.setBorderPainted(false);
        rafraichirBtn = new JButton("Rafraîchir");
        personaliserBouton(rafraichirBtn);
        
        panneauBoutons.add(modifierBtn);
        panneauBoutons.add(supprimerBtn);
        panneauBoutons.add(rafraichirBtn);
        
        // Panneau de status avec BorderLayout.SOUTH
        JPanel panneauStatus = new JPanel(new BorderLayout());
        panneauStatus.setBackground(COULEUR_FOND);
        statusLabel = new JLabel("Prêt");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        panneauStatus.add(statusLabel, BorderLayout.WEST);
        panneauStatus.add(panneauBoutons, BorderLayout.EAST);
        
        panneauPrincipal.add(panneauStatus, BorderLayout.SOUTH);

        // Actions des boutons
        modifierBtn.addActionListener(e -> modifierUtilisateur());

        supprimerBtn.addActionListener(e -> supprimerUtilisateur());
        
        rafraichirBtn.addActionListener(e -> {
            try {
                rafraichirTableau();
                statusLabel.setText("Données actualisées");
            } catch (SQLException ex) {
                afficherErreur("Erreur lors de l'actualisation des données: " + ex.getMessage());
            }
        });
        
        rechercherBtn.addActionListener(e -> rechercherUtilisateurs());
        rechercheTF.addActionListener(e -> rechercherUtilisateurs()); // Recherche en appuyant sur Entrée
        
        // Chargement initial des données
        try {
            rafraichirTableau();
        } catch (SQLException ex) {
            afficherErreur("Erreur lors du chargement des utilisateurs: " + ex.getMessage());
        }

        add(panneauPrincipal);
        setVisible(true);
    }

    private void personaliserBouton(JButton bouton) {
        bouton.setBackground(COULEUR_BOUTON);
        bouton.setForeground(COULEUR_BOUTON_TEXTE);
        bouton.setFont(POLICE_BOUTON);
        bouton.setFocusPainted(false);
        bouton.setBorderPainted(false);
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    public void rafraichirTableau() throws SQLException {
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
        statusLabel.setText("Total: " + tableModel.getRowCount() + " utilisateur(s)");
    }
    
    private void rechercherUtilisateurs() {
        String terme = rechercheTF.getText().trim();
        try {
            if (terme.isEmpty()) {
                rafraichirTableau();
                return;
            }
            
            tableModel.setRowCount(0);
            List<Utilisateur> utilisateurs;
            
            // Essayer de parser comme un ID si c'est numérique
            if (terme.matches("\\d+")) {
                Utilisateur utilisateur = utilisateurController.rechercherUtilisateurParId(Integer.parseInt(terme));
                if (utilisateur != null && !utilisateur.isEstSupprime()) {
                    tableModel.addRow(new Object[]{
                        utilisateur.getId(), 
                        utilisateur.getNom(), 
                        utilisateur.getEmail(), 
                        utilisateur.getRole(),
                        "Actif"
                    });
                }
            } else {
                // Sinon rechercher par nom
                utilisateurs = utilisateurController.rechercherUtilisateursParNom(terme);
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
            
            statusLabel.setText("Résultats: " + tableModel.getRowCount() + " utilisateur(s) trouvé(s)");
        } catch (Exception ex) {
            afficherErreur("Erreur lors de la recherche: " + ex.getMessage());
        }
    }

    private void modifierUtilisateur() {
        int ligneSelectionnee = utilisateurTable.getSelectedRow();
        if (ligneSelectionnee == -1) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner un utilisateur à modifier.", 
                "Sélection requise", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Convertir l'index de la ligne sélectionnée en index du modèle en cas de tri
        int modelRow = utilisateurTable.convertRowIndexToModel(ligneSelectionnee);
        int idUtilisateur = (int) tableModel.getValueAt(modelRow, 0);
        
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            statusLabel.setText("Chargement de l'utilisateur...");
            
            Utilisateur utilisateur = utilisateurController.rechercherUtilisateurParId(idUtilisateur);
            if (utilisateur != null) {
                FenetreModificationUtilisateur fenetreModif = new FenetreModificationUtilisateur(utilisateur, utilisateurController);
                fenetreModif.setLocationRelativeTo(this);
                //fenetreModif.setModal(true); // Si c'est un JDialog
                fenetreModif.setVisible(true);
                
                // Rafraîchir après fermeture
                rafraichirTableau();
            }
        } catch (SQLException | IOException ex) {
            afficherErreur("Erreur lors de la récupération de l'utilisateur: " + ex.getMessage());
        } finally {
            setCursor(Cursor.getDefaultCursor());
            statusLabel.setText("Prêt");
        }
    }

    private void supprimerUtilisateur() {
        int ligneSelectionnee = utilisateurTable.getSelectedRow();
        if (ligneSelectionnee == -1) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner un utilisateur à supprimer.", 
                "Sélection requise", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Convertir l'index de la ligne sélectionnée en index du modèle en cas de tri
        int modelRow = utilisateurTable.convertRowIndexToModel(ligneSelectionnee);
        int idUtilisateur = (int) tableModel.getValueAt(modelRow, 0);
        String nomUtilisateur = (String) tableModel.getValueAt(modelRow, 1);
        
        int reponse = JOptionPane.showConfirmDialog(this, 
            "Êtes-vous sûr de vouloir supprimer l'utilisateur \"" + nomUtilisateur + "\" ?", 
            "Confirmation de suppression", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (reponse == JOptionPane.YES_OPTION) {
            try {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                statusLabel.setText("Suppression en cours...");
                
                utilisateurController.supprimerUtilisateur(idUtilisateur);
                rafraichirTableau();
                
                statusLabel.setText("Utilisateur supprimé avec succès");
            } catch (SQLException | IOException ex) {
                afficherErreur("Erreur lors de la suppression: " + ex.getMessage());
            } finally {
                setCursor(Cursor.getDefaultCursor());
            }
        }
    }
    
    private void afficherErreur(String message) {
        JOptionPane.showMessageDialog(this, 
            message, 
            "Erreur", 
            JOptionPane.ERROR_MESSAGE);
        statusLabel.setText("Erreur: " + message);
    }
}