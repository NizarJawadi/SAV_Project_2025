package com.sav.reclamtion.feign;

import com.sav.reclamtion.DTO.ClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "authentification", url = "http://localhost:9090")  // Remplacez l'URL par celle de votre service
public interface ClientFeignClient {

    @GetMapping("/client/{id}")
    ClientDTO getClientById(@PathVariable("id") Long id);
}
