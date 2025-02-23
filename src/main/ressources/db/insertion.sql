-- Utilisation de la base de données
USE gestion_incidents;

-- Insertion dans la table utilisateur
INSERT INTO utilisateur (nom, email, role, mot_de_passe) VALUES
('Jean Dupont', 'jean.dupont@example.com', 'rapporteur', '$2a$10$abcdefghijklmnopqrstuvwx'), -- Mot de passe haché (bcrypt)
('Marie Curie', 'marie.curie@example.com', 'developpeur', '$2a$10$1234567890abcdefghijklm'),
('Pierre Martin', 'pierre.martin@example.com', 'responsable', '$2a$10$zyxwvutsrqponmlkjihgfed');

-- Insertion dans la table equipe
INSERT INTO equipe (nom) VALUES
('Equipe Alpha'),
('Equipe Beta');

-- Insertion dans la table application
INSERT INTO application (nom, description, version, equipe_responsable_id) VALUES
('Appli RH', 'Application de gestion des ressources humaines', '1.0', 1),
('CRM Client', 'Application de gestion de la relation client', '2.0', 2);

-- Insertion dans la table incident
INSERT INTO incident (application_concernee_nom, description, date_signalement, priorite, statut, rapporteur_id, assigne_a_id) VALUES
('Appli RH', 'Problème de connexion', '2023-10-26', 'ELEVEE', 'OUVERT', 1, 2),
('CRM Client', 'Erreur d''affichage des données client', '2023-10-25', 'MOYENNE', 'EN_COURS', 1, 2);

-- Insertion dans la table commentaire
INSERT INTO commentaire (incident_id, contenu, date, auteur_id) VALUES
(1, 'J''ai essayé de me connecter plusieurs fois sans succès.', '2023-10-26 10:00:00', 1),
(2, 'Les données client ne s''affichent pas correctement dans le tableau.', '2023-10-25 14:30:00', 2);

-- Insertion dans la table fichier (exemple avec un fichier trace)
INSERT INTO fichier (nom, date_upload, uploader_id, type, contenu, incident_id) VALUES
('trace_connexion.txt', '2023-10-26 10:15:00', 1, 'trace', 'Erreur de connexion : ...', 1);

-- Insertion dans la table equipe_utilisateur
INSERT INTO equipe_utilisateur (equipe_id, utilisateur_id) VALUES
(1, 2),
(2, 3);

-- Insertion dans la table developpeur
INSERT INTO developpeur (id, specialisation, niveau, anciennete, equipe_id) VALUES
(2, 'Développement backend', 'Senior', 5, 1);

-- Insertion dans la table rapporteur
INSERT INTO rapporteur (id, service, num_matricule) VALUES
(1, 'Service RH', 'RH123');

-- Insertion dans la table responsable
INSERT INTO responsable (id, departement) VALUES
(3, 'Département CRM');

-- Insertion dans la table fichier_trace
INSERT INTO fichier_trace (id, contenu, logiciel_source) VALUES
(1, 'Erreur de connexion : ...', 'Appli RH');