package com.sav.reclamtion.DTO;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Produit {

    private Long id;
    private String numeroSerie;
    private String nom;
    private String description;
    private String reference;
    private int dureeGarantie;
    private String statut;
    private float prix;
    private String imageUrl;
    private Long categorieId;
    private Long subCategoryId;

}
