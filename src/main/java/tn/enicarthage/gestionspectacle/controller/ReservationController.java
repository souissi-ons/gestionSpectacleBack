package tn.enicarthage.gestionspectacle.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.gestionspectacle.dtos.ReservationDTO;
import tn.enicarthage.gestionspectacle.model.Reservation;
import tn.enicarthage.gestionspectacle.security.SecurityService;
import tn.enicarthage.gestionspectacle.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final SecurityService securityService;

    public ReservationController(ReservationService reservationService, SecurityService securityService) {
        this.reservationService = reservationService;
        this.securityService =securityService;
    }

    @PostMapping
    public ResponseEntity<?> createReservation(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody ReservationDTO reservationDTO) {

        try {
            Reservation reservation;
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                // Réservation sans compte
                reservation = reservationService.createAnonymousReservation(reservationDTO);
            } else {
                // Vérifier le token seulement si présent
                String token = authHeader.substring(7);
                String username = securityService.extractUsername(token);

                if (username == null) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }

                reservation = reservationService.createReservation(reservationDTO);
            }
            return ResponseEntity.ok(reservation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reservation>> getReservationsByUser(@PathVariable Long userId) {
        List<Reservation> reservations = reservationService.getReservationsByUser(userId);
        return ResponseEntity.ok(reservations);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }


}