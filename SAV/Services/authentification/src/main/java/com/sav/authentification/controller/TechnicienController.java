package com.sav.authentification.controller;

import com.sav.authentification.model.Roles;
import com.sav.authentification.model.Technicien;
import com.sav.authentification.model.User;
import com.sav.authentification.services.TechnicienServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("technicien")
public class TechnicienController {

    @Autowired
    private TechnicienServiceImpl technicienService ;


    // Ajouter un technicien
    @PostMapping("/add")
    void addTechnician(@RequestBody Technicien t) {
        t.setRole(Roles.TECHNICIEN);  // Associer le rôle "TECHNICIAN" au nouvel utilisateur
        technicienService.addTechnicien(t);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Technicien> updateTechnicien(@PathVariable Long id, @RequestBody Technicien technicien) {
        Technicien updatedTechnicien = technicienService.updateTechnicien(id, technicien);
        if (updatedTechnicien == null) {
            return ResponseEntity.notFound().build(); // Si le technicien n'est pas trouvé
        }
        return ResponseEntity.ok(updatedTechnicien); // Retourner le technicien mis à jour
    }


    //@Secured("ADMIN") // Autoriser uniquement les utilisateurs avec le rôle "ROLE_USER"
    @GetMapping(path = "techniciens")
    List<Technicien> getAllTechnicien() {
        return technicienService.getAllTechniciens();
    }

    @GetMapping("/{id}")
    User getUserById(@PathVariable Long id ) {
        return technicienService.getTechnicienById(id);
    }

    @DeleteMapping("/remove/{id}")
    void deleteUser (@PathVariable Long id) {
        technicienService.deleteTechnicien(id);
    }

    @GetMapping("/specialite")
    public ResponseEntity<List<Technicien>> getTechniciensBySpecialite(@RequestParam String specialite) {
        List<Technicien> techniciens = technicienService.getTechnicienBySpecialite(specialite);
        return ResponseEntity.ok(techniciens);
    }


    @GetMapping("/specialites")
    public ResponseEntity<List<String>> getAllSpecialites() {
        List<String> specialites = technicienService.getAllSpecialites();
        return ResponseEntity.ok(specialites);
    }

    @GetMapping("/count-by-specialite")
    public ResponseEntity<Map<String, Long>> countTechniciensBySpecialite() {
        Map<String, Long> data = technicienService.countTechniciensBySpecialite();
        return ResponseEntity.ok(data);
    }


    @GetMapping("/matricule/{matricule}")
    public ResponseEntity<Technicien> getTechnicienByMatricule(@PathVariable String matricule) {
        Technicien technicien = technicienService.getTechnicienByMatricule(matricule);
        if (technicien == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(technicien);
    }


}
