package tn.enicarthage.gestionspectacle.service;

import tn.enicarthage.gestionspectacle.model.Lieu;
import tn.enicarthage.gestionspectacle.repository.LieuRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LieuService {
    private final LieuRepository lieuRepository;

    public LieuService(LieuRepository lieuRepository) {
        this.lieuRepository = lieuRepository;
    }

    public List<Lieu> getAllLieux() {
        return lieuRepository.findAll();
    }

    public Lieu getLieuById(Long id) {
        return lieuRepository.findById(id).orElse(null);
    }

    public Lieu createLieu(Lieu lieu) {
        return lieuRepository.save(lieu);
    }

    public Lieu updateLieu(Long id, Lieu lieuDetails) {
        Lieu lieu = lieuRepository.findById(id).orElse(null);
        if (lieu != null) {
            lieu.setNomLieu(lieuDetails.getNomLieu());
            lieu.setAdresse(lieuDetails.getAdresse());
            lieu.setCapacite(lieuDetails.getCapacite());
            return lieuRepository.save(lieu);
        }
        return null;
    }

    public void deleteLieu(Long id) {
        lieuRepository.deleteById(id);
    }

    // Ajoutez cette méthode à votre LieuService
    public List<Lieu> searchByNom(String nom) {
        return lieuRepository.findByNomLieuContainingIgnoreCase(nom);
    }
}