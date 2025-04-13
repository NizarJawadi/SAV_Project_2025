package com.sav.piecederechange.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pieces_rechange")
public class PieceDeRechange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, unique = true)
    private String reference;

    @Column(nullable = false)
    private Double prix;

    @Column(nullable = false)
    private Integer quantiteStock;
}