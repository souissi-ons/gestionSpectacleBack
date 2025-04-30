package tn.enicarthage.gestionspectacle.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import tn.enicarthage.gestionspectacle.model.Views;

import java.util.Date;

public class SpectacleDateLieuDTO {
    @JsonView(Views.Public.class)
    private Long id;

    @JsonView(Views.Public.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @JsonView(Views.Public.class)
    private double heureDebut;

    @JsonView(Views.Public.class)
    private double prix;

    @JsonView(Views.Public.class)
    private int capacite;

    @JsonView(Views.Public.class)
    private int placesDisponibles = -1;

    @JsonView(Views.Public.class)
    private LieuDTO lieu;

    // Constructeurs
    public SpectacleDateLieuDTO() {
    }

    public SpectacleDateLieuDTO(Long id, Date date, double heureDebut, double prix,
                                int capacite, int placesDisponibles, LieuDTO lieu) {
        this.id = id;
        this.date = date;
        this.heureDebut = heureDebut;
        this.prix = prix;
        this.capacite = capacite;
        this.placesDisponibles = placesDisponibles;
        this.lieu = lieu;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(double heureDebut) {
        this.heureDebut = heureDebut;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public int getPlacesDisponibles() {
        return placesDisponibles;
    }

    public void setPlacesDisponibles(int placesDisponibles) {
        this.placesDisponibles = placesDisponibles;
    }

    public LieuDTO getLieu() {
        return lieu;
    }

    public void setLieu(LieuDTO lieu) {
        this.lieu = lieu;
    }

    // Méthode toString pour le débogage
    @Override
    public String toString() {
        return "SpectacleDateLieuDTO{" +
                "id=" + id +
                ", date=" + date +
                ", heureDebut=" + heureDebut +
                ", prix=" + prix +
                ", capacite=" + capacite +
                ", placesDisponibles=" + placesDisponibles +
                ", lieu=" + lieu +
                '}';
    }
}