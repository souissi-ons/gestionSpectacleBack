package tn.enicarthage.gestionspectacle.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.gestionspectacle.model.Lieu;
import tn.enicarthage.gestionspectacle.service.LieuService;

import java.util.List;

@RestController
@RequestMapping("/api/lieux")
public class LieuController {
    private final LieuService lieuService;

    public LieuController(LieuService lieuService) {
        this.lieuService = lieuService;
    }

    @GetMapping
    public ResponseEntity<List<Lieu>> getAllLieux() {
        return ResponseEntity.ok(lieuService.getAllLieux());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lieu> getLieuById(@PathVariable Long id) {
        Lieu lieu = lieuService.getLieuById(id);
        return lieu != null ? ResponseEntity.ok(lieu) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Lieu> createLieu(@RequestBody Lieu lieu) {
        return ResponseEntity.ok(lieuService.createLieu(lieu));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Lieu> updateLieu(@PathVariable Long id, @RequestBody Lieu lieuDetails) {
        Lieu updatedLieu = lieuService.updateLieu(id, lieuDetails);
        return updatedLieu != null ? ResponseEntity.ok(updatedLieu) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLieu(@PathVariable Long id) {
        lieuService.deleteLieu(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Lieu>> searchLieux(@RequestParam String nom) {
        return ResponseEntity.ok(lieuService.searchByNom(nom));
    }
}