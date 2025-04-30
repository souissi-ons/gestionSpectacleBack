package tn.enicarthage.gestionspectacle.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.gestionspectacle.dtos.ReservationDTO;
import tn.enicarthage.gestionspectacle.model.Lieu;
import tn.enicarthage.gestionspectacle.model.Reservation;
import tn.enicarthage.gestionspectacle.model.Spectacle;
import tn.enicarthage.gestionspectacle.model.SpectacleDateLieu;
import tn.enicarthage.gestionspectacle.security.SecurityService;
import tn.enicarthage.gestionspectacle.service.EmailService;
import tn.enicarthage.gestionspectacle.service.LieuService;
import tn.enicarthage.gestionspectacle.service.ReservationService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/email")
public class emailController {
    private final EmailService emailService;
    public emailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/test-email")
    public String testEmail() {
        Reservation reservation = new Reservation();
        reservation.setId(999L);
        reservation.setPrenom("Test");
        reservation.setEmail("souissi.ons.54@gmail.com"); // Remplacez par un vrai email
        reservation.setAvecCompte(false);

        SpectacleDateLieu seance = new SpectacleDateLieu();
        Spectacle spectacle = new Spectacle();
        spectacle.setTitre("Spectacle Test");
        seance.setSpectacle(spectacle);
        Lieu lieu = new Lieu();
        seance.setLieu(lieu);
        reservation.setSpectacleDateLieu(seance);
        reservation.setNbPlaces(2);

        emailService.envoyerEmailConfirmation(reservation);
        return "Email de test envoyé!";
    }
}
