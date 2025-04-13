package com.sav.categoriesService.services;

import com.sav.categoriesService.DTO.CategorieDTO;
import com.sav.categoriesService.DTO.SousCategorieDTO;
import com.sav.categoriesService.Utils.CategorieMapper;
import com.sav.categoriesService.entity.Categorie;
import com.sav.categoriesService.entity.SousCategorie;
import com.sav.categoriesService.exceptions.ResourceNotFoundException;
import com.sav.categoriesService.repository.CategorieRepository;
import com.sav.categoriesService.repository.SousCategorieRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategorieService {
    private final CategorieRepository categorieRepository;
    private final SousCategorieRepository sousCategorieRepository;

    public CategorieService(CategorieRepository categorieRepository, SousCategorieRepository sousCategorieRepository) {
        this.categorieRepository = categorieRepository;
        this.sousCategorieRepository = sousCategorieRepository;
    }

    // Ajouter une catégorie
    public CategorieDTO addCategorie(CategorieDTO categoryDTO) {
        Categorie categorie = new Categorie();
        categorie.setNom(categoryDTO.getNom());
        categorie = categorieRepository.save(categorie);
        return CategorieMapper.toCategorieDTO(categorie);
    }

    // Ajouter une sous-catégorie
    public SousCategorieDTO addSousCategorie(Long categorieId, SousCategorieDTO subCategoryDTO) {
        Categorie categorie = categorieRepository.findById(categorieId)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));

        SousCategorie sousCategorie = new SousCategorie();
        sousCategorie.setNom(subCategoryDTO.getNom());
        sousCategorie.setCategorie(categorie);
        sousCategorie = sousCategorieRepository.save(sousCategorie);

        return CategorieMapper.toSousCategorieDTO(sousCategorie);
    }

    public CategorieDTO getCategory(Long id) {
        // Logique pour récupérer la catégorie par ID
        Categorie categorie = categorieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return new CategorieDTO(categorie);
    }

    // Retourne toutes les catégories sous forme de DTOs
    public List<CategorieDTO> getAllCategories() {
        return categorieRepository.findAll().stream()
                .map(CategorieMapper::toCategorieDTO)
                .collect(Collectors.toList());
    }

    // Retourne les sous-catégories d'une catégorie sous forme de DTOs
    public List<SousCategorieDTO> getSousCategoriesByCategorie(Long categorieId) {
        return sousCategorieRepository.findByCategorieId(categorieId).stream()
                .map(CategorieMapper::toSousCategorieDTO)
                .collect(Collectors.toList());
    }

}