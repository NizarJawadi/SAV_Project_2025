package com.sav.reclamtion.DTO;

import com.sav.reclamtion.entity.StatutReclamation;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReclamationDTO {
    private Long id;
    private ClientDTO clientDTO; // L'ajout du clientDTO
    private Produit produit; // Détails du produit
    private String description;
    private StatutReclamation statut;
    private LocalDateTime dateReclamation;
    private Long responsableSAVId;
    private String responsableSipNumber;
    private Long interventionId;
    private String guideTechniqueUrl;

}