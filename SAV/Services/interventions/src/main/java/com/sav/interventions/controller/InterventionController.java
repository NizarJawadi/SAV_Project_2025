package com.sav.interventions.controller;

import com.sav.interventions.DTO.InterventionWithReclamationDto;
import com.sav.interventions.DTO.PieceDeRechangeDto;
import com.sav.interventions.DTO.StatusDTO;
import com.sav.interventions.entity.Intervention;
import com.sav.interventions.repository.InterventionRepository;
import com.sav.interventions.service.InterventionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/interventions")
public class InterventionController {

    @Autowired
    private InterventionService interventionService;
    @Autowired
    private InterventionRepository interventionRepository;

    @GetMapping
    public ResponseEntity<List<InterventionWithReclamationDto>> getAllInterventions() {
        return ResponseEntity.ok(interventionService.getAllInterventions());
    }
    @PostMapping
    public ResponseEntity<Intervention> createIntervention(@RequestBody Intervention intervention) {
        return ResponseEntity.ok(interventionService.createIntervention(intervention));
    }


   /* @GetMapping("/{id}/pieces")
    public List<PieceDeRechangeDto> getInterventionPieces(@PathVariable Long id) {
        return interventionService.getInterventionPieces(id);
    }*/

    @GetMapping("/{id}")
    public ResponseEntity<InterventionWithReclamationDto> getInterventionById(@PathVariable Long id) {
        return ResponseEntity.ok(interventionService.getInterventionById(id));
    }

/*
    @PostMapping("/{id}/pieces/{pieceId}")
    public ResponseEntity<Intervention> addPieceToIntervention(@PathVariable Long id, @PathVariable Long pieceId) {
        return ResponseEntity.ok(interventionService.addPieceToIntervention(id, pieceId));
    }

    @DeleteMapping("/{id}/pieces/{pieceId}")
    public ResponseEntity<Intervention> removePieceFromIntervention(@PathVariable Long id, @PathVariable Long pieceId) {
        return ResponseEntity.ok(interventionService.removePieceFromIntervention(id, pieceId));
    }

    @PutMapping("/{id}/cloturer")
    public ResponseEntity<Intervention> cloturerIntervention(@PathVariable Long id) {
        return ResponseEntity.ok(interventionService.cloturerIntervention(id));
    }
*/
   /* @GetMapping("/technicien/{technicienId}")
    public ResponseEntity<List<InterventionWithReclamationDto>> getInterventionsByTechnicien(@PathVariable Long technicienId) {
        return ResponseEntity.ok(interventionService.getInterventionsByTechnicien(technicienId));
    }*/


    @PutMapping("/{id}/status")
    public ResponseEntity<InterventionWithReclamationDto> updateInterventionStatus(
            @PathVariable("id") Long id,
            @RequestBody StatusDTO status) throws ChangeSetPersister.NotFoundException {

        // Vérifie si l'ID existe dans la base de données
            InterventionWithReclamationDto updatedIntervention = interventionService.updateInterventionStatus(id, status);
            return ResponseEntity.ok(updatedIntervention);


    }


    @GetMapping("/interventionParReclamation/{id}")
    public Intervention getInterventionByReclamation ( @PathVariable("id") Long idReclamation){
        return  interventionRepository.findByReclamationId(idReclamation);
    }



}