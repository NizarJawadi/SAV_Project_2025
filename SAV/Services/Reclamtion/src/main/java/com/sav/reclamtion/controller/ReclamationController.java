package com.sav.reclamtion.controller;

import com.sav.reclamtion.DTO.ClientDTO;
import com.sav.reclamtion.DTO.InterventionDTO;
import com.sav.reclamtion.DTO.ProduitReclameDTO;
import com.sav.reclamtion.DTO.ReclamationDTO;
import com.sav.reclamtion.entity.Reclamation;
import com.sav.reclamtion.entity.StatutReclamation;
import com.sav.reclamtion.feign.ClientFeignClient;
import com.sav.reclamtion.feign.UserFeignClient;
import com.sav.reclamtion.repository.ReclamationRepository;
import com.sav.reclamtion.service.ReclamationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reclamations")
public class ReclamationController {

    @Autowired
    private ReclamationService reclamationService;

    @Autowired
    private ClientFeignClient clientFeignClient;

    @Autowired
    private ReclamationRepository reclamationRepository;
    @Autowired
    private UserFeignClient userFeignClient;

    @PostMapping("/add")
    public ResponseEntity<Reclamation> creerReclamation(@RequestBody Reclamation reclamation) {
        Reclamation nouvelleReclamation = reclamationService.createReclamation(reclamation);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouvelleReclamation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reclamation> getReclamation(@PathVariable Long id) {
        return reclamationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/annuler")
    public ResponseEntity<Reclamation> annulerReclamation(@PathVariable Long id) {
        Reclamation reclamation = reclamationService.annulerReclamation(id);
        return ResponseEntity.ok(reclamation);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ReclamationDTO>> getReclamationsByClient(@PathVariable Long clientId) {
        List<ReclamationDTO> reclamations = reclamationService.getReclamationsByClient(clientId);

        String sipNumber;
        for (ReclamationDTO reclamationDTO : reclamations) {
            if (reclamationDTO.getResponsableSAVId() != null) {
                sipNumber = userFeignClient.getUserSipNumber(reclamationDTO.getResponsableSAVId());
                reclamationDTO.setResponsableSipNumber(sipNumber);
            }
            else{
                reclamationDTO.setResponsableSipNumber(null);
            }

        }



        return ResponseEntity.ok(reclamations);
    }

    @GetMapping("/client1/{clientId}")
    public List<Reclamation> getReclamationsByClient1(@PathVariable Long clientId) {
       return  reclamationRepository.findByClientId(clientId);

    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<ReclamationDTO>> getReclamationsByStatut(@PathVariable String statut) {
        try {
            StatutReclamation statutEnum = StatutReclamation.valueOf(statut.toUpperCase());
            return ResponseEntity.ok(reclamationService.getReclamationsByEtat(statutEnum));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReclamationDTO>> getAllReclamations() {
        List<ReclamationDTO> reclamations = reclamationService.getAllReclamations();

        return ResponseEntity.ok(reclamations);
    }

    @PutMapping("/{reclamationId}/assign")
    public ResponseEntity<String> assignReclamation(
            @PathVariable Long reclamationId,
            @RequestParam Long technicienId,
            @RequestParam Long responsableSAVId,
            @RequestParam String deadline) {

        LocalDateTime dateDeadline = LocalDateTime.parse(deadline); // ISO format

        InterventionDTO intervention = reclamationService.createInterventionForReclamation(reclamationId, technicienId, dateDeadline);
        reclamationService.assignReclamation(reclamationId, responsableSAVId);

        return ResponseEntity.ok("Réclamation assignée avec succès !");
    }



    @PutMapping("/{id}/update-intervention")
    public ResponseEntity<Void> updateReclamationWithIntervention(@PathVariable Long id, @RequestParam Long interventionId) {
        Reclamation reclamation = reclamationRepository.findById(id).orElseThrow();
        //reclamation.setInterventionId(interventionId);
        reclamationRepository.save(reclamation);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<Reclamation> updateReclamationStatus(@PathVariable("id") Long id, @RequestBody StatutReclamation statusDto) {
        Reclamation reclamation = reclamationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réclamation non trouvée"));

        // Mise à jour du statut de la réclamation
        reclamation.setStatut(statusDto);
        reclamationRepository.save(reclamation);

        // Retourner la réclamation mise à jour
        return ResponseEntity.ok(reclamation);
    }


    @GetMapping("/par-jour")
    public Map<String, Long> getReclamationsParJour() {
        return reclamationService.getReclamationsGroupBy("DAY");
    }

    @GetMapping("/par-semaine")
    public Map<String, Long> getReclamationsParSemaine() {
        return reclamationService.getReclamationsGroupBy("WEEK");
    }

    @GetMapping("/par-mois")
    public Map<String, Long> getReclamationsParMois() {
        return reclamationService.getReclamationsGroupBy("MONTH");
    }

    @GetMapping("/par-annee")
    public Map<String, Long> getReclamationsParAnnee() {
        return reclamationService.getReclamationsGroupBy("YEAR");
    }

    @GetMapping("/stats-par-jour")
    public Map<String, Map<String, Long>> getStatsParJour() {
       return reclamationService.getStatsParJour();
    }


    @GetMapping("/produits-plus-reclames")
    public List<ProduitReclameDTO> getProduitsLesPlusReclames() {
        return reclamationService.getProduitsLesPlusReclames();
    }


}
