package com.sav.categoriesService.controller;


import com.sav.categoriesService.DTO.CategorieDTO;
import com.sav.categoriesService.DTO.SousCategorieDTO;
import com.sav.categoriesService.entity.Categorie;
import com.sav.categoriesService.entity.SousCategorie;

import com.sav.categoriesService.services.CategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategorieController {

    @Autowired
    private CategorieService categorieService;

    // Ajouter une catégorie
    @PostMapping
    public CategorieDTO addCategorie(@RequestBody CategorieDTO categoryDTO) {
        return categorieService.addCategorie(categoryDTO);
    }

    // Ajouter une sous-catégorie
    @PostMapping("/{id}/subcategories")
    public SousCategorieDTO addSousCategorie(@PathVariable Long id, @RequestBody SousCategorieDTO subCategoryDTO) {
        return categorieService.addSousCategorie(id, subCategoryDTO);
    }


    @GetMapping
    public List<CategorieDTO> getAllCategories() {
        return categorieService.getAllCategories();
    }


    @GetMapping("/{id}")
    public CategorieDTO getCategory(@PathVariable Long id) {
        return categorieService.getCategory(id);
    }


    @GetMapping("/{id}/subcategories")
    public List<SousCategorieDTO> getSousCategories(@PathVariable Long id) {
        return categorieService.getSousCategoriesByCategorie(id);
    }
}