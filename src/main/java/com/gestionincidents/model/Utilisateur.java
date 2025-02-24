package com.gestionincidents.model;

public class Utilisateur {

    private int id;
    private String nom;
    private String email;
    private String motDePasse;
    private String role; // Role est géré dans cette table
    private boolean estSupprime; // Pour la suppression logique

    public Utilisateur() {
    }

    public Utilisateur(int id, String nom, String email, String motDePasse, String role) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    // Getters et setters pour les attributs

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

	public boolean isEstSupprime() {
		return estSupprime;
	}

	public void setEstSupprime(boolean estSupprime) {
		this.estSupprime = estSupprime;
	}
}