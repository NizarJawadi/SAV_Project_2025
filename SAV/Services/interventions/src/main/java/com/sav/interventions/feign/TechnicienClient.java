package com.sav.interventions.feign;

import com.sav.interventions.DTO.TechnicienDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "technicien-service", url = "localhost:9090")
public interface TechnicienClient {

    @GetMapping("/techniciens/matricule/{matricule}")
    TechnicienDTO getTechnicienByMatricule(@PathVariable String matricule);

    @GetMapping("/techniciens/specialite/{specialite}")
    List<TechnicienDTO> getTechniciensBySpecialite(@PathVariable String specialite);

    @GetMapping("/technicien/{id}")
    TechnicienDTO getTechnicienById(@PathVariable Long id);
}