package com.sav.interventions.controller;

import com.sav.interventions.DTO.InterventionWithReclamationDto;
import com.sav.interventions.DTO.PieceDeRechangeDto;
import com.sav.interventions.DTO.StatusDTO;
import com.sav.interventions.entity.Intervention;
import com.sav.interventions.feign.FactureFeignClient;
import com.sav.interventions.feign.MailFeignClient;
import com.sav.interventions.repository.InterventionRepository;
import com.sav.interventions.service.InterventionService;
import com.sav.interventions.utils.EmailRequestDto;
import com.sav.interventions.utils.PieceUtilisee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/interventions")
public class InterventionController {

    @Autowired
    private InterventionService interventionService;
    @Autowired
    private InterventionRepository interventionRepository;

    @Autowired
    private FactureFeignClient factureFeignClient;

    @Autowired
    private MailFeignClient mailFeignClient;

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


    @PostMapping("/{id}/ajouter-pieces")
    public ResponseEntity<Void> ajouterPieces(
            @PathVariable Long id,
            @RequestBody List<PieceUtilisee> pieces) {
        interventionService.ajouterPieces(id, pieces);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/pieces")
    public ResponseEntity<List<PieceUtilisee>> getPiecesUtilisees(@PathVariable Long id) {
        List<PieceUtilisee> pieces = interventionService.getPiecesUtiliseesParIntervention(id);
        return ResponseEntity.ok(pieces);
    }

    @DeleteMapping("/{id}/pieces/{pieceId}")
    public ResponseEntity<Intervention> removePieceFromIntervention(@PathVariable Long id, @PathVariable Long pieceId) {
        return ResponseEntity.ok(interventionService.removePieceFromIntervention(id, pieceId));
    }



/*
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

        InterventionWithReclamationDto updatedIntervention = interventionService.updateInterventionStatus(id, status);

        if ("TERMINEE".equalsIgnoreCase(status.getStatus())) {
            //String clientEmail = updatedIntervention.getReclamation().getClient().getEmail();
            String clientEmail = "jawadinizar2@gmail.com";
            Long reclamationId = updatedIntervention.getReclamation().getId();

            // Appel du microservice facture pour générer le PDF
            ResponseEntity<byte[]> pdfResponse = factureFeignClient.exportFacturePdf(reclamationId);

            if (pdfResponse.getStatusCode().is2xxSuccessful()) {
                byte[] pdfBytes = pdfResponse.getBody();

                EmailRequestDto emailRequest = new EmailRequestDto();
                emailRequest.setTo(clientEmail);
                emailRequest.setSubject("Facture Intervention #" + reclamationId);
                emailRequest.setText("Bonjour, veuillez trouver ci-joint votre facture.");
                emailRequest.setAttachment(Base64.getEncoder().encodeToString(pdfBytes));
                emailRequest.setFilename("facture_" + reclamationId + ".pdf");

                mailFeignClient.sendMailWithAttachment(emailRequest);
            } else {
                System.err.println("Erreur lors de la génération du PDF");
            }
        }

        return ResponseEntity.ok(updatedIntervention);
    }



    @GetMapping("/interventionParReclamation/{id}")
    public Intervention getInterventionByReclamation ( @PathVariable("id") Long idReclamation){
        return  interventionRepository.findByReclamationId(idReclamation);
    }


    @GetMapping("/stats/interventions")
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("nbTerminees", interventionService.getNombreInterventionsTerminees());
        stats.put("dureeMoyenne", interventionService.getDureeMoyenne());
        stats.put("piecesParIntervention", interventionService.getMoyennePiecesUtilisees());
        stats.put("reclamationsTraitees", interventionService.getReclamationsTraitees());
        stats.put("tempsAttenteMoyen", interventionService.getTempsMoyenAttente());
        stats.put("tauxRetard", interventionService.calculerTauxRetard());

        return stats;
    }


    @GetMapping("/by-technicien/matricule/{matricule}")
    public ResponseEntity<List<InterventionWithReclamationDto>> getInterventionsByMatricule(@PathVariable String matricule) {
        List<InterventionWithReclamationDto> interventions = interventionService.getInterventionsByTechnicienMatricule(matricule);
        return ResponseEntity.ok(interventions);
    }


    @GetMapping("/by-technicien/specialite")
    public ResponseEntity<List<InterventionWithReclamationDto>> getInterventionsBySpecialite(@RequestParam String specialite) {
        List<InterventionWithReclamationDto> interventions = interventionService.getInterventionsByTechnicienSpecialite(specialite);
        return ResponseEntity.ok(interventions);
    }



    @GetMapping("/by-technicien/id/{id}")
    public ResponseEntity<List<InterventionWithReclamationDto>> getInterventionsByTechnicienId(@PathVariable Long id) {
        List<InterventionWithReclamationDto> interventions = interventionService.getInterventionsByTechnicienId(id);
        return ResponseEntity.ok(interventions);
    }




}