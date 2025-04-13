package com.sav.authentification.controller;

import com.sav.authentification.model.Roles;
import com.sav.authentification.model.Technicien;
import com.sav.authentification.model.User;
import com.sav.authentification.services.TechnicienServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("technicien")
public class TechnicienController {

    @Autowired
    private TechnicienServiceImpl technicienService ;


    // Ajouter un technicien
    void addTechnician(@RequestBody Technicien t) {
        t.setRole(Roles.TECHNICIEN);  // Associer le rôle "TECHNICIAN" au nouvel utilisateur
        technicienService.addTechnicien(t);
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


}
