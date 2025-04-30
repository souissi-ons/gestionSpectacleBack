package tn.enicarthage.gestionspectacle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.enicarthage.gestionspectacle.model.Spectacle;

import java.util.Date;
import java.util.List;

@Repository
public interface SpectacleRepository extends JpaRepository<Spectacle, Long> {
    @Query("SELECT DISTINCT s FROM Spectacle s LEFT JOIN FETCH s.datesLieux WHERE s.id = :id")
    Spectacle findByIdWithDatesLieux(@Param("id") Long id);

    @Query("SELECT s FROM Spectacle s LEFT JOIN FETCH s.rubriques")
    List<Spectacle> findAllWithRubriques();

    List<Spectacle> findByDatesLieuxLieuId(Long lieuId);

    List<Spectacle> findByCategorieAndDatesLieuxDateAndDatesLieuxHeureDebutAndDatesLieuxLieuId(
            String categorie, Date date, Double heureDebut, Long lieuId);

    List<Spectacle> findAllByOrderByDatesLieuxDateAsc();

    List<Spectacle> findByCategorie(String categorie);

    List<Spectacle> findByDatesLieuxDate(Date date);
}