package com.sav.springchatbot.feign;

import com.sav.springchatbot.DTO.ReclamationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "reclamation-service", url = "http://localhost:8060")
public interface ReclamationFeignClient {

    @GetMapping("/reclamations/statut")
    String getStatutReclamation(@RequestParam("userId") String userId,
                                @RequestParam("produit") String produit);

    @GetMapping("/reclamations/client1/{clientId}")
    List<ReclamationDTO> getReclamationsByUser(@PathVariable("clientId") Long userId);

}