package tn.enicarthage.gestionspectacle.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "spectacles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Spectacle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Public.class)
    private Long id;

    @JsonView(Views.Public.class)
    private String titre;

    @JsonView(Views.Public.class)
    private String description;

    @JsonView(Views.Public.class)
    private String imageUrl;

    @JsonView(Views.Public.class)
    private String categorie;

    @OneToMany(mappedBy = "spectacle", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonView(Views.Internal.class)
    private List<SpectacleDateLieu> datesLieux;

    @OneToMany(mappedBy = "spectacle", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonView(Views.Internal.class)
    private List<Rubrique> rubriques;
}