package com.sav.historiqueachat.Entity;


import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Produit {

    private Long id;
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
