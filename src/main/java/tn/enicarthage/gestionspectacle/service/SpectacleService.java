package tn.enicarthage.gestionspectacle.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import tn.enicarthage.gestionspectacle.dtos.SpectacleDateLieuDTO;
import tn.enicarthage.gestionspectacle.model.*;
import tn.enicarthage.gestionspectacle.repository.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpectacleService {
    private final SpectacleRepository spectacleRepository;
    private final SpectacleDateLieuRepository spectacleDateLieuRepository;
    private final RubriqueRepository rubriqueRepository;
    private final LieuRepository lieuRepository;
    private final ReservationRepository reservationRepository;


    public SpectacleService(SpectacleRepository spectacleRepository,
                            SpectacleDateLieuRepository spectacleDateLieuRepository,
                            RubriqueRepository rubriqueRepository,
                            LieuRepository lieuRepository,
                            ReservationRepository reservationRepository
) {
        this.spectacleRepository = spectacleRepository;
        this.spectacleDateLieuRepository = spectacleDateLieuRepository;
        this.rubriqueRepository = rubriqueRepository;
        this.lieuRepository = lieuRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Spectacle> getAllSpectacles() {
        return spectacleRepository.findAllByOrderByDatesLieuxDateAsc();
    }

    public Spectacle getSpectacleById(Long id) {
        return spectacleRepository.findById(id).orElse(null);
    }

    public List<Spectacle> filtrerSpectacles(String categorie, Date date, Double heure, Long lieuId) {
        if (categorie != null && date != null && heure != null && lieuId != null) {
            return spectacleRepository.findByCategorieAndDatesLieuxDateAndDatesLieuxHeureDebutAndDatesLieuxLieuId(
                    categorie, date, heure, lieuId);
        } else if (categorie != null) {
            return spectacleRepository.findByCategorie(categorie);
        } else if (date != null) {
            return spectacleRepository.findByDatesLieuxDate(date);
        } else if (lieuId != null) {
            return spectacleRepository.findByDatesLieuxLieuId(lieuId);
        } else {
            return getAllSpectacles();
        }
    }

    public List<SpectacleDateLieu> getDatesLieuxForSpectacle(Long spectacleId) {
        return spectacleDateLieuRepository.findBySpectacleId(spectacleId);
    }

    public int getPlacesRestantes(Long spectacleDateLieuId) {
        SpectacleDateLieu dateLieu = spectacleDateLieuRepository.findById(spectacleDateLieuId)
                .orElseThrow(() -> new RuntimeException("Date/Lieu non trouvé"));

        // Calcul des places réservées PAYÉES seulement
        int placesReservees = dateLieu.getReservations().stream()
                .filter(res -> "PAID".equals(res.getPaymentStatus()))
                .mapToInt(Reservation::getNbPlaces)
                .sum();

        return dateLieu.getCapacite() - placesReservees;
    }

    public List<SpectacleDateLieuDTO> getDatesLieuxWithAvailableSeats(Long spectacleId) {
        List<SpectacleDateLieu> datesLieux = spectacleDateLieuRepository.findBySpectacleId(spectacleId);

        return datesLieux.stream().map(dateLieu -> {
            SpectacleDateLieuDTO dto = new SpectacleDateLieuDTO();
            // Mapper les champs de base
            dto.setId(dateLieu.getId());
            dto.setDate(dateLieu.getDate());
            dto.setHeureDebut(dateLieu.getHeureDebut());
            dto.setPrix((int) dateLieu.getPrix());
            dto.setCapacite(dateLieu.getCapacite());

            // Calculer les places disponibles
            int placesReservees = reservationRepository.countPaidReservationsForDateLieu(dateLieu.getId());
            dto.setPlacesDisponibles(dateLieu.getCapacite() - placesReservees);

            return dto;
        }).collect(Collectors.toList());
    }
}