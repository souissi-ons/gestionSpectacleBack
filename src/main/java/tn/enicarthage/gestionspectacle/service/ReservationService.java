package tn.enicarthage.gestionspectacle.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.hibernate.Hibernate;
import tn.enicarthage.gestionspectacle.dtos.ReservationDTO;
import tn.enicarthage.gestionspectacle.model.*;
import tn.enicarthage.gestionspectacle.repository.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final SpectacleDateLieuRepository spectacleDateLieuRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final EmailService emailService;
    private final JavaMailSender mailSender;

    public ReservationService(ReservationRepository reservationRepository,
                              SpectacleDateLieuRepository spectacleDateLieuRepository,
                              EmailService emailService,
                              UtilisateurRepository utilisateurRepository,
                              JavaMailSender mailSender) {
        this.reservationRepository = reservationRepository;
        this.spectacleDateLieuRepository = spectacleDateLieuRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.emailService = emailService;
        this.mailSender = mailSender;
    }

    @Transactional
    public Reservation createReservation(ReservationDTO reservationDTO) {
        SpectacleDateLieu seance = spectacleDateLieuRepository.findById(reservationDTO.getSpectacleDateLieuId())
                .orElseThrow(() -> new RuntimeException("Séance non trouvée"));

        // Vérifier les places disponibles
        int placesDisponibles = seance.getCapacite() -
                seance.getReservations().stream()
                        .mapToInt(Reservation::getNbPlaces)
                        .sum();

        if (reservationDTO.getNbPlaces() > placesDisponibles) {
            throw new RuntimeException("Nombre de places demandé non disponible");
        }

        Reservation reservation = new Reservation();
        reservation.setSpectacleDateLieu(seance);
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
        sendConfirmationEmail(savedReservation);

        return savedReservation;
    }

    private void sendConfirmationEmail(Reservation reservation) {
        try {
            System.out.println("=== TENTATIVE ENVOI EMAIL ===");
            System.out.println("Destinataire: " + reservation.getEmail());

            // Recharger l'entité avec toutes les relations
            Reservation freshReservation = reservationRepository.findById(reservation.getId())
                    .orElseThrow(() -> new RuntimeException("Reservation non trouvée"));

            // Forcer le chargement des relations
            Hibernate.initialize(freshReservation.getSpectacleDateLieu());
            if (freshReservation.getSpectacleDateLieu() != null) {
                Hibernate.initialize(freshReservation.getSpectacleDateLieu().getSpectacle());
            }

            System.out.println("Données complètes chargées, envoi email...");
            emailService.envoyerEmailConfirmation(freshReservation);
            System.out.println("+++ EMAIL ENVOYE +++");
        } catch (Exception e) {
            System.err.println("!!! ERREUR ENVOI EMAIL !!!");
            e.printStackTrace();
            throw new RuntimeException("Échec d'envoi de l'email de confirmation", e);
        }
    }

    public List<Reservation> getReservationsByUser(Long userId) {
        return reservationRepository.findByUtilisateurId(userId);
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        reservation.setPaymentStatus("CANCELLED");
        reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation createAnonymousReservation(ReservationDTO reservationDTO) {
        validateAnonymousReservationData(reservationDTO);
        Reservation reservation = buildAnonymousReservation(reservationDTO);
        Reservation savedReservation = reservationRepository.save(reservation);
        sendConfirmationEmail(savedReservation);
        return savedReservation;
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
        SpectacleDateLieu seance = spectacleDateLieuRepository.findById(dto.getSpectacleDateLieuId())
                .orElseThrow(() -> new EntityNotFoundException("Séance non trouvée"));

        return Reservation.builder()
                .spectacleDateLieu(seance)
                .utilisateur(null)
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .email(dto.getEmail())
                .telephone(dto.getTelephone())
                .nbPlaces(dto.getNbPlaces())
                .dateReservation(LocalDateTime.now(ZoneId.of("Europe/Paris")))
                .paymentMethod(dto.getPaymentMethod())
                .paymentStatus("PAID".equals(dto.getPaymentStatus()) ? "PAID" : "PENDING")
                .avecCompte(false)
                .build();
    }

    @Transactional
    public void confirmPayment(Long reservationId) {
        reservationRepository.updatePaymentStatus(reservationId, "PAID");
    }
}