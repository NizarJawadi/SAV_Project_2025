package com.sav.factureservice.model;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Reclamation {

    private Long id;
    private Long clientId;
    private Long produitId;
    private String description;
    private LocalDateTime dateReclamation;
    private Long responsableSAVId;
}
