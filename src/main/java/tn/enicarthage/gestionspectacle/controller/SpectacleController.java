package tn.enicarthage.gestionspectacle.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.gestionspectacle.dtos.SpectacleDateLieuDTO;
import tn.enicarthage.gestionspectacle.model.Spectacle;
import tn.enicarthage.gestionspectacle.model.SpectacleDateLieu;
import tn.enicarthage.gestionspectacle.service.SpectacleService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/spectacles")
public class SpectacleController {
    private final SpectacleService spectacleService;

    public SpectacleController(SpectacleService spectacleService) {
        this.spectacleService = spectacleService;
    }

    @GetMapping
    public List<Spectacle> getAllSpectacles() {
        return spectacleService.getAllSpectacles();
    }

    @GetMapping("/{id}")
    public Spectacle getSpectacleById(@PathVariable Long id) {
        return spectacleService.getSpectacleById(id);
    }

    @GetMapping("/filtre")
    public List<Spectacle> filtrerSpectacles(
            @RequestParam(required = false) String categorie,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam(required = false) Double heure,
            @RequestParam(required = false) Long lieuId) {
        return spectacleService.filtrerSpectacles(categorie, date, heure, lieuId);
    }

    @GetMapping("/{id}/dates-lieux")
    public List<SpectacleDateLieuDTO> getDatesLieuxForSpectacle(@PathVariable Long id) {
        return spectacleService.getDatesLieuxWithAvailableSeats(id);
    }

    @GetMapping("/date-lieu/{id}/places")
    public int getPlacesRestantes(@PathVariable Long id) {
        return spectacleService.getPlacesRestantes(id);
    }
}