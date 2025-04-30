package tn.enicarthage.gestionspectacle.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rubrique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private double hDebutR;
    private double dureeRub;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "artiste_id")
    @JsonView(Views.Public.class)
    private Artiste artiste;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "spectacle_id")
    private Spectacle spectacle;
}