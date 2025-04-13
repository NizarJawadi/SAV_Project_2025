package com.sav.springchatbot.feign;

import com.sav.springchatbot.DTO.ProduitDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "produit-service", url = "http://localhost:9999")
public interface ProduitFeignClient {

    @GetMapping("/produits/{id}")
    ProduitDTO getProduitById(@PathVariable("id") Long produitId);


    @GetMapping("/produits/all")
    List<ProduitDTO> getAllProduits();

    @GetMapping("/produits/by-nom")
    ProduitDTO getProduitByNom(@RequestParam("nom") String nom);
}