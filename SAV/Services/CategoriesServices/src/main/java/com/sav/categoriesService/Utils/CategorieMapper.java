package com.sav.categoriesService.Utils;

import com.sav.categoriesService.DTO.CategorieDTO;
import com.sav.categoriesService.DTO.SousCategorieDTO;
import com.sav.categoriesService.entity.Categorie;
import com.sav.categoriesService.entity.SousCategorie;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CategorieMapper {
    public static CategorieDTO toCategorieDTO(Categorie categorie) {
        return new CategorieDTO(
                categorie.getId(),
                categorie.getNom(),
                categorie.getSousCategories().stream()
                        .map(CategorieMapper::toSousCategorieDTO)
                        .collect(Collectors.toList())
        );
    }

    public static SousCategorieDTO toSousCategorieDTO(SousCategorie sousCategorie) {
        return new SousCategorieDTO(
                sousCategorie.getId(),
                sousCategorie.getNom(),
                sousCategorie.getCategorie().getId()
        );
    }
}
