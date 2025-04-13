package com.sav.produitservices.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SousCategorieDTO {
    private Long id;
    private String nom;
    private Long categorieId; // ID de la catégorie parent
}
