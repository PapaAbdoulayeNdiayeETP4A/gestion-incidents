-- Utilisation de la base de données
USE gestion_incidents;

-- Insertions dans la table utilisateur
INSERT INTO utilisateur (nom, email, mot_de_passe, role) VALUES
('Jean Dupont', 'jean.dupont@example.com', 'motdepasse1', 'developpeur'),
('Marie Curie', 'marie.curie@example.com', 'motdepasse2', 'rapporteur'),
('Pierre Martin', 'pierre.martin@example.com', 'motdepasse3', 'responsable'),
('Sophie Germain', 'sophie.germain@example.com', 'motdepasse4', 'administrateur'),
('Louis Pasteur', 'louis.pasteur@example.com', 'motdepasse5', 'developpeur'),
('Ada Lovelace', 'ada.lovelace@example.com', 'motdepasse6', 'rapporteur');

-- Insertions dans la table administrateur
INSERT INTO administrateur (utilisateur_id) VALUES
(4);

-- Insertions dans la table equipe
INSERT INTO equipe (nom, description) VALUES
('Équipe Web', 'Développement d''applications web'),
('Équipe Mobile', 'Développement d''applications mobiles'),
('Équipe Support', 'Support technique et maintenance');

-- Insertions dans la table application
INSERT INTO application (nom, description, version, equipe_responsable_id) VALUES
('Gestion Projets', 'Application de gestion de projets', '1.0', 1),
('Suivi Incidents', 'Application de suivi des incidents', '2.0', 2),
('Base de Connaissance', 'Base de connaissance interne', '1.5', 3);

-- Insertions dans la table incident
INSERT INTO incident (application_concernee_id, description, date_signalement, priorite, statut, rapporteur_id, assigne_a_id, date_cloture, solution) VALUES
(1, 'Erreur 404 sur la page d''accueil', '2024-10-27', 'ELEVEE', 'EN_COURS', 2, 1, NULL, NULL),
(2, 'Problème de connexion sur l''application mobile', '2024-10-28', 'MOYENNE', 'OUVERT', 6, 5, NULL, NULL),
(3, 'Article manquant dans la base de connaissance', '2024-10-29', 'FAIBLE', 'RESOLU', 2, 1, '2024-10-30', 'Article ajouté');

-- Insertions dans la table commentaire
INSERT INTO commentaire (incident_id, contenu, date, auteur_id) VALUES
(1, 'J''ai reproduit l''erreur, je vais investiguer.', '2024-10-27 10:00:00', 1),
(1, 'Merci Jean, tiens-moi au courant.', '2024-10-27 11:00:00', 3),
(2, 'Je n''arrive pas à me connecter non plus.', '2024-10-28 12:00:00', 5);

-- Insertions dans la table fichier
INSERT INTO fichier (nom, date_upload, uploader_id, type, contenu, incident_id, commentaire_id) VALUES
('trace_erreur_404.txt', '2024-10-27 10:30:00', 1, 'trace', 'Contenu de la trace...', 1, NULL),
('capture_connexion.png', '2024-10-28 12:30:00', 5, 'image', NULL, 2, NULL),
('commentaire_image.png', '2024-10-28 13:30:00', 5, 'image', NULL, NULL, 3);

-- Insertions dans la table equipe_utilisateur
INSERT INTO equipe_utilisateur (equipe_id, utilisateur_id) VALUES
(1, 1),
(2, 5),
(1, 3);

-- Insertions dans la table developpeur
INSERT INTO developpeur (id, utilisateur_id, specialisation, niveau, anciennete, equipe_id) VALUES
(1, 1, 'Web', 'Expert', 5, 1),
(5, 5, 'Mobile', 'Intermédiaire', 2, 2);

-- Insertions dans la table rapporteur
INSERT INTO rapporteur (id, utilisateur_id, service, num_matricule) VALUES
(2, 2, 'Support Client', 'RC123'),
(6, 6, 'Marketing', 'RM456');

-- Insertions dans la table responsable
INSERT INTO responsable (id, utilisateur_id, departement) VALUES
(3, 3, 'Développement Web');

-- Insertions dans la table fichier_trace
INSERT INTO fichier_trace (id, contenu, logiciel_source) VALUES
(1, 'Contenu de la trace...', 'Apache Tomcat');

-- Insertions dans la table fichier_image
INSERT INTO fichier_image (id, contenu, largeur, hauteur, format) VALUES
(2, NULL, 800, 600, 'PNG'),
(3, NULL, 1024, 768, 'PNG');