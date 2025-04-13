package com.sav.factureservice.feign;
import com.sav.factureservice.model.Reclamation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "reclamation-service" , url = "http://localhost:8060")
public interface ReclamationFeignClient {

    @GetMapping("/reclamations/{id}")
    Reclamation getReclamationById(@PathVariable("id") Long id);

    @GetMapping("/reclamationParIntervention/{id}")
    Reclamation getReclamationByInterventionId(@PathVariable("id") Long interventionId) ;
}
