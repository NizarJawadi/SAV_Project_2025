package com.sav.factureservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Intervention {


    private Long id;
    private LocalDateTime dateIntervention;
    private String statut; // "EN_ATTENTE", "EN_COURS", "TERMINEE"
    private Long technicienId;
    private Long reclamationId; // Lien avec la réclamation

    private LocalDateTime dateDebut ;
    private LocalDateTime dateFin ;
    private List<Long> piecesIds;

}
