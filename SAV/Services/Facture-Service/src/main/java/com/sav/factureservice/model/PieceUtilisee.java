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
}

