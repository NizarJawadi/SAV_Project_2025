package com.sav.reclamtion.feign;

import com.sav.reclamtion.DTO.InterventionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "intervention-service", url = "http://localhost:8050")
public interface InterventionFeignClient {

    @PostMapping("/interventions")
    InterventionDTO createIntervention(@RequestBody InterventionDTO intervention);
}