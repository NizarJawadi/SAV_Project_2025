package com.sav.historiqueachat.feign;

import com.sav.historiqueachat.Entity.Produit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "produit-service", url = "http://localhost:9999/produits")
public interface ProduitServiceClient {
    @GetMapping("/{id}")
    Produit getProduitById(@PathVariable("id") Long id);
}