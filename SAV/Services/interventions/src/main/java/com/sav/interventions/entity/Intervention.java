package com.sav.interventions.entity;

import com.sav.interventions.utils.PieceUtilisee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "interventions")
public class Intervention {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateIntervention;

    @Column(nullable = false)
    private String statut; // "EN_ATTENTE", "EN_COURS", "TERMINEE"

    @Column(nullable = false)
    private Long technicienId;

    @Column(nullable = false)
    private Long reclamationId; // Lien avec la réclamation

    @Column
    private LocalDateTime dateDebut ;

    @Column
    private LocalDateTime dateFin ;

    @Column
    private LocalDateTime DateDeadLine ;

    @ElementCollection
    @CollectionTable(name = "intervention_pieces", joinColumns = @JoinColumn(name = "intervention_id"))
    private List<PieceUtilisee> piecesUtilisees = new ArrayList<>();



}