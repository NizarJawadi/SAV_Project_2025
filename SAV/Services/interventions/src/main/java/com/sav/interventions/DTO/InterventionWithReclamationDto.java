package com.sav.interventions.DTO;

import com.sav.interventions.entity.Intervention;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InterventionWithReclamationDto {
    private Intervention intervention;
    private ReclamationDTO reclamation;
    private Object client; // Ajouter les informations du client
}

