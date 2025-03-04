-- Insertion des utilisateurs
INSERT INTO utilisateur (nom, email, mot_de_passe, est_supprime, role)
VALUES 
    ('Alice Dupont', 'alice@example.com', 'password1', FALSE, 'responsable'),
    ('Bob Martin', 'bob@example.com', 'password2', FALSE, 'developpeur'),
    ('Charlie Durand', 'charlie@example.com', 'password3', FALSE, 'rapporteur'),
    ('David Leroy', 'david@example.com', 'password4', FALSE, 'developpeur'),
    ('Emma Roussel', 'emma@example.com', 'password5', FALSE, 'rapporteur'),
    ('François Morel', 'francois@example.com', 'password6', FALSE, 'responsable'),
    ('Admin', 'admin@example.com', 'adminpass', FALSE, 'administrateur');

-- Insertion des responsables
INSERT INTO responsable (utilisateur_id, departement)
VALUES 
    (1, 'Informatique'),
    (6, 'Développement');

-- Insertion des équipes avec un responsable
INSERT INTO equipe (nom, description, responsable_id)
VALUES 
    ('Équipe Backend', 'Gestion des services API', 1),
    ('Équipe Frontend', 'Développement de l’interface utilisateur', 6);

-- Insertion des développeurs
INSERT INTO developpeur (utilisateur_id, specialisation, niveau, anciennete, equipe_id)
VALUES 
    (2, 'Backend Java', 'Senior', 5, 1),
    (4, 'Frontend Angular', 'Junior', 2, 2);

-- Insertion des rapporteurs
INSERT INTO rapporteur (utilisateur_id, service, num_matricule)
VALUES 
    (3, 'Support', 'RPT123'),
    (5, 'Service Qualité', 'RPT456');
   
-- Insertion de l'administrateur dans la table correspondante
INSERT INTO administrateur (utilisateur_id)
VALUES 
    (7);

-- Insertion d’applications
INSERT INTO application (nom, description, version, equipe_responsable_id)
VALUES 
    ('Application X', 'Gestion des incidents', '1.0.0', 1),
    ('Application Y', 'Suivi des performances', '2.3.1', 2);

-- Insertion d’incidents
INSERT INTO incident (application_concernee_id, description, date_signalement, priorite, statut, rapporteur_id, assigne_a_id)
VALUES 
    (1, 'Problème de connexion', '2024-02-20', 'HAUTE', 'OUVERT', 3, 2),
    (1, 'Erreur 500 sur la page de login', '2024-02-22', 'MOYENNE', 'OUVERT', 3, 2),
    (2, 'Affichage incorrect des graphiques', '2024-02-25', 'BASSE', 'OUVERT', 5, 4),
    (2, 'Lenteur sur le chargement des données', '2024-02-27', 'MOYENNE', 'OUVERT', 5, 4);

-- Insertion de commentaires
INSERT INTO commentaire (incident_id, contenu, date, auteur_id)
VALUES 
    (1, 'Nous avons identifié le problème, en cours de correction.', '2024-02-21 10:30:00', 2),
    (2, 'Un correctif est en cours de test.', '2024-02-23 14:45:00', 2),
    (3, 'Nous devons analyser les logs pour comprendre l’affichage défectueux.', '2024-02-26 09:20:00', 4),
    (4, 'Le problème semble être lié à une requête trop lourde.', '2024-02-28 15:00:00', 4);

-- Insertion de fichiers
INSERT INTO fichier (nom, date_upload, uploader_id, type, incident_id)
VALUES 
    ('log1.txt', '2024-02-21 11:00:00', 3, 'trace', 1),
    ('screenshot1.png', '2024-02-23 15:00:00', 3, 'image', 2),
    ('log2.txt', '2024-02-26 10:00:00', 5, 'trace', 3),
    ('screenshot2.jpg', '2024-02-28 16:00:00', 5, 'image', 4);

-- Insertion de fichiers traces
INSERT INTO fichier_trace (id, contenu, logiciel_source)
VALUES 
    (1, 'Erreur détectée dans la connexion DB.', 'Serveur API'),
    (3, 'Données mal formatées dans la requête SQL.', 'Serveur de reporting');

-- Insertion de fichiers images
INSERT INTO fichier_image (id, contenu, largeur, hauteur, format)
VALUES 
    (2, NULL, 1920, 1080, 'PNG'),
    (4, NULL, 1280, 720, 'JPG');

-- Association des utilisateurs aux équipes
INSERT INTO equipe_utilisateur (equipe_id, utilisateur_id)
VALUES 
    (1, 1), (1, 2), (2, 4), (2, 6);
