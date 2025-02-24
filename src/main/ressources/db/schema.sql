-- Suppression de la base de données (si elle existe)
DROP DATABASE IF EXISTS gestion_incidents;

-- Création de la base de données (si elle n'existe pas déjà)
CREATE DATABASE IF NOT EXISTS gestion_incidents;

-- Utilisation de la base de données
USE gestion_incidents;

-- Table utilisateur
CREATE TABLE utilisateur (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255), -- Mot de passe haché
    role VARCHAR(50) -- 'developpeur', 'rapporteur', 'responsable'
);

-- Table administrateur
CREATE TABLE administrateur (
    id INT PRIMARY KEY AUTO_INCREMENT,
    utilisateur_id INT UNIQUE NOT NULL,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id)
);

-- Table equipe
CREATE TABLE equipe (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255) NOT NULL,
    description TEXT
);

-- Table application
CREATE TABLE application (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255),
    description TEXT,
    version VARCHAR(50),
    equipe_responsable_id INT,
    FOREIGN KEY (equipe_responsable_id) REFERENCES equipe(id)
);

-- Table incident
CREATE TABLE incident (
    id INT PRIMARY KEY AUTO_INCREMENT,
    application_concernee_id INT NOT NULL,
    description TEXT NOT NULL,
    date_signalement DATE NOT NULL,
    priorite VARCHAR(50) NOT NULL DEFAULT 'MOYENNE', -- 'FAIBLE', 'MOYENNE', 'ELEVEE', 'URGENTE'
    statut VARCHAR(50) NOT NULL DEFAULT 'OUVERT', -- 'OUVERT', 'EN_COURS', 'RESOLU', 'CLOS'
    rapporteur_id INT,
    assigne_a_id INT,
    date_cloture DATE,
    solution TEXT,
    FOREIGN KEY (application_concernee_id) REFERENCES application(id),
    FOREIGN KEY (rapporteur_id) REFERENCES utilisateur(id),
    FOREIGN KEY (assigne_a_id) REFERENCES utilisateur(id)
);

-- Table commentaire
CREATE TABLE commentaire (
    id INT PRIMARY KEY AUTO_INCREMENT,
    incident_id INT,
    contenu TEXT,
    date DATETIME,
    auteur_id INT,
    FOREIGN KEY (incident_id) REFERENCES incident(id),
    FOREIGN KEY (auteur_id) REFERENCES utilisateur(id)
);

-- Table fichier
CREATE TABLE fichier (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255),
    date_upload DATETIME,
    uploader_id INT,
    type VARCHAR(50), -- 'trace', 'image'
    contenu MEDIUMBLOB, -- Pour les images, ou TEXT pour les traces
    incident_id INT,
    commentaire_id INT, -- Clé étrangère vers la table commentaire
    FOREIGN KEY (uploader_id) REFERENCES utilisateur(id),
    FOREIGN KEY (incident_id) REFERENCES incident(id),
    FOREIGN KEY (commentaire_id) REFERENCES commentaire(id),
    CONSTRAINT check_incident_commentaire CHECK ((incident_id IS NOT NULL AND commentaire_id IS NULL) OR (incident_id IS NULL AND commentaire_id IS NOT NULL))
);

-- Table de jointure entre equipe et utilisateur (pour la relation Many-to-Many)
CREATE TABLE equipe_utilisateur (
    equipe_id INT,
    utilisateur_id INT,
    FOREIGN KEY (equipe_id) REFERENCES equipe(id),
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id),
    PRIMARY KEY (equipe_id, utilisateur_id) -- Clé primaire composée
);

-- Tables pour les classes filles de Utilisateur
CREATE TABLE developpeur (
    id INT PRIMARY KEY,
    utilisateur_id INT UNIQUE NOT NULL,
    specialisation VARCHAR(255),
    niveau VARCHAR(50),
    anciennete INT,
    equipe_id INT,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id),
    FOREIGN KEY (equipe_id) REFERENCES equipe(id)
);

CREATE TABLE rapporteur (
    id INT PRIMARY KEY,
    utilisateur_id INT UNIQUE NOT NULL,
    service VARCHAR(255),
    num_matricule VARCHAR(255),
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id)
);

CREATE TABLE responsable (
    id INT PRIMARY KEY,
    utilisateur_id INT UNIQUE NOT NULL,
    departement VARCHAR(255),
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id)
);

-- Tables pour les classes filles de Fichier
CREATE TABLE fichier_trace (
    id INT PRIMARY KEY,
    contenu TEXT,
    logiciel_source VARCHAR(255),
    FOREIGN KEY (id) REFERENCES fichier(id)
);

CREATE TABLE fichier_image (
    id INT PRIMARY KEY,
    contenu MEDIUMBLOB,
    largeur INT,
    hauteur INT,
    format VARCHAR(50),
    FOREIGN KEY (id) REFERENCES fichier(id)
);

-- Index pour améliorer les performances des requêtes
CREATE INDEX idx_incident_rapporteur_id ON incident (rapporteur_id);
CREATE INDEX idx_incident_assigne_a_id ON incident (assigne_a_id);
CREATE INDEX idx_commentaire_auteur_id ON commentaire (auteur_id);