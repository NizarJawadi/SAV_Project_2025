package com.sav.springchatbot.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProduitDTO {


        private Long id;
        private String nom;
        private String description;

        private String reference;
        //private LocalDate dateAchat;
        private int dureeGarantie;
        private String statut;
        private float prix;
        private String imageUrl; // Stocke l'URL de l'image
        private String numeroSerie;
        private String guideTechniqueUrl;


        private Long categorieId;  // Stocke seulement l'ID de la catégorie
        private Long subCategoryId; // Stocke seulement l'ID de la sous-catégorie



}
