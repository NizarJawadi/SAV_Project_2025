package com.sav.historiqueachat.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoriqueAchat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clientId;  // ID du client
    private Long produitId; // ID du produit
    private Date garantieExpireLe ;
    private int quantite;
    private float prixTotal;
    private LocalDateTime dateAchat;




}
