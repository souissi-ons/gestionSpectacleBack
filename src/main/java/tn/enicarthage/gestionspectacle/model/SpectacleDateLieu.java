package tn.enicarthage.gestionspectacle.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpectacleDateLieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Public.class)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "spectacle_id")
    @JsonView(Views.Public.class)
    private Spectacle spectacle;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lieu_id")
    @JsonView(Views.Public.class)
    private Lieu lieu;

    @JsonView(Views.Public.class)
    private Date date;

    @JsonView(Views.Public.class)
    private double heureDebut;

    @JsonView(Views.Public.class)
    private double prix;

    @JsonView(Views.Public.class)
    private int capacite;

    @OneToMany(mappedBy = "spectacleDateLieu", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonView(Views.Internal.class)
    private List<Reservation> reservations;

}