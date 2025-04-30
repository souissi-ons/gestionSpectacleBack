package tn.enicarthage.gestionspectacle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.enicarthage.gestionspectacle.model.SpectacleDateLieu;

import java.util.List;

@Repository
public interface SpectacleDateLieuRepository extends JpaRepository<SpectacleDateLieu, Long> {
    List<SpectacleDateLieu> findBySpectacleId(Long spectacleId);

}