package com.sav.interventions.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReclamationDTO{

private Long id;
private Long clientId;
private Long produitId;
private String description;
private StatutReclamation  statut;
@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
private LocalDateTime dateReclamation;
private Long responsableSAVId;
}

