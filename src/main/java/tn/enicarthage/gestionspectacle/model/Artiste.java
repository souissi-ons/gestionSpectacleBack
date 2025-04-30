package tn.enicarthage.gestionspectacle.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Artiste {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idArt;

    private String nomArt;
    private String prenomArt;
    private String specialite;

    @OneToMany(mappedBy = "artiste", fetch = FetchType.EAGER)
    @JsonView(Views.Internal.class) // Ne sera pas sérialisé  dans la vue Public
    @JsonIgnore
    private List<Rubrique> rubriques;


}
