package com.sav.factureservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PieceUtilisee {
    private Long pieceId;
    private String pieceName;
    private Integer quantite;
    private Double prixUnitaire;


    // Méthode pour calculer le prix total HT pour cette pièce
    public double calculerTotalHT() {
        return prixUnitaire * quantite;
    }

    // Méthode pour calculer la TVA pour cette pièce
    public double calculerTVA(double tauxTVA) {
        return calculerTotalHT() * tauxTVA;
    }

    // Méthode pour calculer le prix TTC pour cette pièce
    public double calculerTotalTTC(double tauxTVA) {
        return calculerTotalHT() + calculerTVA(tauxTVA);
    }
}

