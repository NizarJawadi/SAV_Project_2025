package com.sav.interventions.DTO;

import lombok.Data;



@Data
public class PieceDeRechangeDto {
    private Long id;
    private String nom;
    private String reference;
    private Double prix;
    private Integer quantiteStock;
}