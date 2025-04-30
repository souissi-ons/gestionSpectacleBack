package tn.enicarthage.gestionspectacle.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Lieu")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idLieu")
    @JsonView(Views.Public.class)
    private Long id;

    @Column(name = "nomLieu")
    @JsonView(Views.Public.class)
    private String nomLieu;

    @JsonView(Views.Public.class)
    private String adresse;

    @JsonView(Views.Public.class)
    private int capacite;
}