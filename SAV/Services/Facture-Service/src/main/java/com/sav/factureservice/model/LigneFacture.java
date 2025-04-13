package com.sav.factureservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LigneFacture {
    private String description;
    private Integer quantite;
    private String unite;
    private double prixUnitaireHT;
    private Integer tva;
    private Double totalTVA;
    private Double totalTTC;
}