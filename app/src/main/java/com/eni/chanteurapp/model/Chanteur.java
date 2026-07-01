package com.eni.chanteurapp.model;

public class Chanteur {

    // Les attributs correspondent exactement aux colonnes de la table SQLite
    private int idchant;
    private String nom;
    private String datenais;
    private String photo;

    // Constructeur SANS id (utilisé pour créer un nouveau chanteur avant insertion en BD)
    public Chanteur(String nom, String datenais, String photo) {
        this.nom = nom;
        this.datenais = datenais;
        this.photo = photo;
    }

    // Constructeur AVEC id (utilisé quand on récupère un chanteur depuis la BD)
    public Chanteur(int idchant, String nom, String datenais, String photo) {
        this.idchant = idchant;
        this.nom = nom;
        this.datenais = datenais;
        this.photo = photo;
    }

    // Getters — permettent de lire les valeurs depuis l'extérieur de la classe
    public int getIdchant()   { return idchant; }
    public String getNom()    { return nom; }
    public String getDatenais() { return datenais; }
    public String getPhoto()  { return photo; }

    // Setters — permettent de modifier les valeurs depuis l'extérieur
    public void setIdchant(int idchant)     { this.idchant = idchant; }
    public void setNom(String nom)          { this.nom = nom; }
    public void setDatenais(String datenais) { this.datenais = datenais; }
    public void setPhoto(String photo)      { this.photo = photo; }
}