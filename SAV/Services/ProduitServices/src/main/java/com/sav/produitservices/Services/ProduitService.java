package com.sav.produitservices.Services;

import com.sav.produitservices.DTO.CategorieDTO;
import com.sav.produitservices.DTO.SousCategorieDTO;
import com.sav.produitservices.Entity.Produit;
import com.sav.produitservices.FeignClient.CategoryClient;
import com.sav.produitservices.Repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduitService {

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private ProduitRepository produitRepository;

    public List<Produit> getAllProduits(){
        return produitRepository.findAll();
    }

    public Produit getProduitById(Long id){
        return produitRepository.findById(id).orElse(null);
    }

    public Produit addProduct(Produit produit) {
        // Vérifier si la catégorie et la sous-catégorie existent
        CategorieDTO category = categoryClient.getCategory(produit.getCategorieId());
        List<SousCategorieDTO> subCategory = categoryClient.getSubCategory(produit.getSubCategoryId());

        if (category == null || subCategory == null) {
            throw new RuntimeException("Catégorie ou sous-catégorie invalide !");
        }

        return produitRepository.save(produit);
    }
    public Produit getProduitParNom( String nom ) {
        return produitRepository.findByNom(nom);
    }


    public List<Produit> getProduitsBySubCategorie(Long subCategorieId) {
        return produitRepository.findBySubCategoryId(subCategorieId);
    }
}
