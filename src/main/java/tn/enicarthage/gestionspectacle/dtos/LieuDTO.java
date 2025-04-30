package tn.enicarthage.gestionspectacle.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import tn.enicarthage.gestionspectacle.model.Views;

public class LieuDTO {
    @JsonView(Views.Public.class)
    private Long id;

    @JsonView(Views.Public.class)
    private String nom;

    @JsonView(Views.Public.class)
    private String adresse;

    @JsonView(Views.Public.class)
    private String ville;

    @JsonView(Views.Public.class)
    private int capacite;

    @JsonView(Views.Public.class)
    private String coordonneesGps;

    // Constructeurs
    public LieuDTO() {
    }

    public LieuDTO(Long id, String nom, String adresse, String ville,
                   int capacite, String coordonneesGps) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.ville = ville;
        this.capacite = capacite;
        this.coordonneesGps = coordonneesGps;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public String getCoordonneesGps() {
        return coordonneesGps;
    }

    public void setCoordonneesGps(String coordonneesGps) {
        this.coordonneesGps = coordonneesGps;
    }

    // Méthode toString() pour le débogage
    @Override
    public String toString() {
        return "LieuDTO{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", ville='" + ville + '\'' +
                ", capacite=" + capacite +
                ", coordonneesGps='" + coordonneesGps + '\'' +
                '}';
    }
}