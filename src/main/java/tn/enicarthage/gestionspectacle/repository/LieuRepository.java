package tn.enicarthage.gestionspectacle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.enicarthage.gestionspectacle.model.Lieu;

import java.util.List;

@Repository
public interface LieuRepository extends JpaRepository<Lieu, Long> {
    // Méthodes personnalisées si nécessaire
    List    <Lieu> findByNomLieuContainingIgnoreCase(String nom);
}