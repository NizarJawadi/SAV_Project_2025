package com.sav.factureservice.feign;
import com.sav.factureservice.model.Intervention;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "intervention-service" , url = "http://localhost:8050")
public interface InterventionFeignClient {

    @GetMapping("/interventions/{id}")
    Intervention getInterventionById(@PathVariable("id") Long id);
    @GetMapping("/interventions/interventionParReclamation/{id}")
    Intervention getInterventionsForReclamation (@PathVariable("id") Long id);
}
