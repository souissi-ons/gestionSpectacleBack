package tn.enicarthage.gestionspectacle.dtos;

import tn.enicarthage.gestionspectacle.model.Artiste;

public class RubriqueDTO {
    private Long id;
    private String type;
    private double hDebutR;
    private double dureeRub;
    private Artiste artiste;

    // Getters
    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public double getHDebutR() {
        return hDebutR;
    }

    public double getDureeRub() {
        return dureeRub;
    }

    public Artiste getArtiste() {
        return artiste;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setHDebutR(double hDebutR) {
        this.hDebutR = hDebutR;
    }

    public void setDureeRub(double dureeRub) {
        this.dureeRub = dureeRub;
    }

    public void setArtiste(Artiste artiste) {
        this.artiste = artiste;
    }

    @Override
    public String toString() {
        return "RubriqueDTO{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", hDebutR=" + hDebutR +
                ", dureeRub=" + dureeRub +
                ", artiste=" + artiste +
                '}';
    }

    public RubriqueDTO(Long idRub, String type, double hDebutR, double dureeRub, Artiste artiste) {
        this.id = idRub != null ? idRub : 0L;
        this.type = type != null ? type : "";
        this.hDebutR = hDebutR;
        this.dureeRub = dureeRub;
        this.artiste = artiste;
    }
}