package tn.enicarthage.gestionspectacle.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "spectacle_date_lieu_id")
    private SpectacleDateLieu spectacleDateLieu;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Utilisateur utilisateur;

    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private int nbPlaces;
    private LocalDateTime dateReservation;
    private String paymentMethod;
    private String paymentStatus;
    private boolean avecCompte;
}