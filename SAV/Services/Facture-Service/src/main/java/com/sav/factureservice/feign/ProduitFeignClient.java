package com.sav.factureservice.feign;
import com.sav.factureservice.model.Produit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "produit-service" , url = "http://localhost:9999")
public interface ProduitFeignClient {

    @GetMapping("/produits/{id}")
    Produit getProduitById(@PathVariable("id") Long id);
}
