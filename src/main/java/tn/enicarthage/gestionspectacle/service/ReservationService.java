package tn.enicarthage.gestionspectacle.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import tn.enicarthage.gestionspectacle.dtos.ReservationDTO;
import tn.enicarthage.gestionspectacle.model.*;
import tn.enicarthage.gestionspectacle.repository.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final SpectacleDateLieuRepository spectacleDateLieuRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final EmailService emailService;

    public ReservationService(ReservationRepository reservationRepository,
                              SpectacleDateLieuRepository spectacleDateLieuRepository,
                              EmailService emailService,
                              UtilisateurRepository utilisateurRepository
                              ) {
        this.reservationRepository = reservationRepository;
        this.spectacleDateLieuRepository = spectacleDateLieuRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.emailService = emailService;
    }

    public Reservation createReservation(ReservationDTO reservationDTO) {
        SpectacleDateLieu seance = spectacleDateLieuRepository.findById(reservationDTO.getSpectacleDateLieuId())
                .orElseThrow(() -> new RuntimeException("Séance non trouvée"));

        Reservation reservation = new Reservation();
        reservation.setSpectacleDateLieu(seance);

        // Vérifier les places disponibles
        int placesDisponibles = seance.getCapacite() -
                seance.getReservations().stream()
                        .mapToInt(Reservation::getNbPlaces)
                        .sum();

        if (reservationDTO.getNbPlaces() > placesDisponibles) {
            throw new RuntimeException("Nombre de places demandé non disponible");
        }

        reservation.setNbPlaces(reservationDTO.getNbPlaces());
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        reservation.setDateReservation(LocalDateTime.parse(reservationDTO.getDateReservation(), formatter));
        reservation.setPaymentMethod(reservationDTO.getPaymentMethod());
        reservation.setPaymentStatus(reservationDTO.getPaymentStatus() != null ?
                reservationDTO.getPaymentStatus() : "PENDING");

        if (reservationDTO.getUserId() != null) {
            Utilisateur utilisateur = utilisateurRepository.findById(reservationDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            reservation.setUtilisateur(utilisateur);
            reservation.setAvecCompte(true);
            reservation.setNom(utilisateur.getNom());
            reservation.setPrenom(utilisateur.getPrenom());
            reservation.setEmail(utilisateur.getEmail());

        } else {
            reservation.setNom(reservationDTO.getNom());
            reservation.setPrenom(reservationDTO.getPrenom());
            reservation.setEmail(reservationDTO.getEmail());
            reservation.setTelephone(reservationDTO.getTelephone());
            reservation.setAvecCompte(false);
        }

        Reservation savedReservation = reservationRepository.save(reservation);

        // Envoyer email de confirmation
        try {
            emailService.envoyerEmailConfirmation(savedReservation);
        } catch (Exception e) {
            // Log l'erreur mais ne pas faire échouer la réservation
            System.out.println("Échec d'envoi d'email" + e);
        }


        return savedReservation;
    }

    public List<Reservation> getReservationsByUser(Long userId) {
        return reservationRepository.findByUtilisateurId(userId);
    }

    public void cancelReservation(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));


        reservation.setPaymentStatus("CANCELLED");
        reservationRepository.save(reservation);

        // Envoyer email d'annulation
//        emailService.envoyerEmailAnnulation(reservation);
    }

    public Reservation createAnonymousReservation(ReservationDTO reservationDTO) {
        // 1. Validation des données obligatoires pour les anonymes
        validateAnonymousReservationData(reservationDTO);

        // 2. Construction de l'entité Reservation
        Reservation reservation = buildAnonymousReservation(reservationDTO);

        // 4. Sauvegarde
        return reservationRepository.save(reservation);
    }

    private void validateAnonymousReservationData(ReservationDTO dto) {
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new IllegalArgumentException("L'email est obligatoire pour les réservations sans compte");
        }
        if (dto.getTelephone() == null || dto.getTelephone().isBlank()) {
            throw new IllegalArgumentException("Le téléphone est obligatoire");
        }
        if (dto.getNbPlaces() <= 0) {
            throw new IllegalArgumentException("Le nombre de places doit être positif");
        }
        if (dto.getSpectacleDateLieuId() == null) {
            throw new IllegalArgumentException("La séance doit être spécifiée");
        }
    }

    private Reservation buildAnonymousReservation(ReservationDTO dto) {
        // Récupération de la séance (à adapter selon votre repository)
        SpectacleDateLieu seance = spectacleDateLieuRepository.findById(dto.getSpectacleDateLieuId())
                .orElseThrow(() -> new EntityNotFoundException("Séance non trouvée"));

        return Reservation.builder()
                .spectacleDateLieu(seance)
                .utilisateur(null) // Pas d'utilisateur pour les anonymes
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .email(dto.getEmail())
                .dateReservation(LocalDateTime.now(ZoneId.of("Europe/Paris")))
                .telephone(dto.getTelephone())
                .nbPlaces(dto.getNbPlaces())
                .dateReservation(LocalDateTime.now()) // Date actuelle
                .paymentMethod(dto.getPaymentMethod())
                .paymentStatus("PAID".equals(dto.getPaymentStatus()) ? "PAID" : "PENDING")
                .avecCompte(false) // Marqué comme réservation sans compte
                .build();
    }
    @Transactional
    public void confirmPayment(Long reservationId) {
        reservationRepository.updatePaymentStatus(reservationId, "PAID");
    }
}