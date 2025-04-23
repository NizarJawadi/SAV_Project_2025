package com.sav.reclamtion.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProduitReclameDTO {
    private Produit produit;
    private Long nombreReclamations;
}