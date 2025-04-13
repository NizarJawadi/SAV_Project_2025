package com.sav.factureservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Produit {

    private Long id;
    private String nom;
    private String description;
    private String reference;
    private int dureeGarantie;
    private String statut; // Par exemple "NEUF", "EN_REPARATION", "HORS_SERVICE"
    private float prix;
    private String imageUrl; // URL de l'image du produit
    private String numeroSerie;
    private Long categorieId;  // ID de la catégorie (référence à une catégorie)
    private Long subCategoryId;

}
