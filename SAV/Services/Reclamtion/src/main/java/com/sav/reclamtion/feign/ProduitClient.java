package com.sav.reclamtion.feign;

import com.sav.reclamtion.DTO.Produit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "produits-service" ,  url = "http://localhost:9999")
public interface ProduitClient {
    @GetMapping("/produits/{id}/garantie")
    boolean verifierGarantie(@PathVariable("id") Long produitId);

    @GetMapping("/produits/{id}")
    Produit getProduitById(@PathVariable("id") Long id);
}
