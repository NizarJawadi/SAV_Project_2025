package com.sav.produitservices.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String description;

    @Column(unique = true)
    private String reference;
    //private LocalDate dateAchat;
    private int dureeGarantie;
    private String statut;
    private float prix;
    private String imageUrl; // Stocke l'URL de l'image
    @Column(unique = true, nullable = false)
    private String numeroSerie;
    @Column(name = "guide_technique_url")
    private String guideTechniqueUrl;


    private Long categorieId;  // Stocke seulement l'ID de la catégorie
    private Long subCategoryId; // Stocke seulement l'ID de la sous-catégorie
}
