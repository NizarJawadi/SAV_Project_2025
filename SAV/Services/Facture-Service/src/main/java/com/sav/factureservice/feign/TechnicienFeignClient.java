package com.sav.factureservice.feign;


import com.sav.factureservice.model.Produit;
import com.sav.factureservice.model.Technicien;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "Technicien-service" , url = "http://localhost:9090")
public interface TechnicienFeignClient {

    @GetMapping("/technicien/{id}")
    Technicien getTechnicienById(@PathVariable("id") Long id);
}
