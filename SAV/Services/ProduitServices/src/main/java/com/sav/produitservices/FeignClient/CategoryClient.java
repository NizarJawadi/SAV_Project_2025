package com.sav.produitservices.FeignClient;



import com.sav.produitservices.DTO.CategorieDTO;
import com.sav.produitservices.DTO.SousCategorieDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "categories-Service", url = "http://localhost:9988")
public interface CategoryClient {


    @GetMapping("/categories/{id}")
    CategorieDTO getCategory(@PathVariable("id") Long id);

    @GetMapping("/categories/{id}/subcategories")
    List<SousCategorieDTO> getSubCategory(@PathVariable("id") Long id);

}
