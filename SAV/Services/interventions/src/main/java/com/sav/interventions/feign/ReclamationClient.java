package com.sav.interventions.feign;

import com.sav.interventions.DTO.ReclamationDTO;
import com.sav.interventions.DTO.StatutReclamation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "Reclamtion-service" , url = "http://localhost:8060")
public interface ReclamationClient {

    @PutMapping("/reclamations/{id}/update-intervention")
    void updateReclamationWithIntervention(@PathVariable Long id, @RequestParam Long interventionId);


    @GetMapping("/reclamations/{id}")
    ResponseEntity<ReclamationDTO> getReclamationById(@PathVariable Long id);

    @PutMapping("/reclamations/{id}")
    void updateReclamationStatus(@PathVariable("id") Long id, @RequestBody StatutReclamation statusDto);


}