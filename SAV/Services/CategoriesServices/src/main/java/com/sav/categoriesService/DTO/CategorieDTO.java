package com.sav.categoriesService.DTO;


import com.sav.categoriesService.entity.Categorie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CategorieDTO {
    private Long id;
    private String nom;
    private List<SousCategorieDTO> subCategories; // Liste des sous-catégories

    public CategorieDTO(Categorie categorie) {
        this.id = categorie.getId();
        this.nom = categorie.getNom();
    }

}
