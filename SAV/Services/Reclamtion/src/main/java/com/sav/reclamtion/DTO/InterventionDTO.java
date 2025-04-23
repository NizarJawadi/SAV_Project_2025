package com.sav.reclamtion.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InterventionDTO {

        private Long id;
       // private String description;
         private Long  reclamationId ;
         private LocalDateTime dateIntervention;
        private String statut; // "EN_ATTENTE", "EN_COURS", "TERMINEE"
        private Long technicienId;

        private LocalDateTime DateDeadLine ;
}
