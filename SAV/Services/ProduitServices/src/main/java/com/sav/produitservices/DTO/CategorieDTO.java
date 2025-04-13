package com.sav.produitservices.DTO;


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
    private List<SousCategorieDTO> SousCategories; // Liste des sous-catégories



}
