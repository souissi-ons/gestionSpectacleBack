package tn.enicarthage.gestionspectacle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.enicarthage.gestionspectacle.model.Rubrique;

import java.util.List;

@Repository
public interface RubriqueRepository extends JpaRepository<Rubrique, Long> {
    @Query("SELECT r FROM Rubrique r WHERE r.spectacle.id = :spectacleId")
    List<Rubrique> findRubriquesBySpectacleId(@Param("spectacleId") Long spectacleId);

    List<Rubrique> findBySpectacleId(Long spectacleId);
}