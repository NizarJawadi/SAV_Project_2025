package com.sav.interventions.utils;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PieceUtilisee {
    private Long pieceId;
    private String pieceName;
    private Integer quantite;
    private Double prixUnitaire;
}

