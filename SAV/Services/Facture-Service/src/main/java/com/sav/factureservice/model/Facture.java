package com.sav.factureservice.model;

import com.sav.factureservice.feign.ReclamationFeignClient;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Entity

@ToString
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Stocker uniquement l'ID de l'intervention
    @Column(name = "intervention_id", nullable = false)
    private Long interventionId;

    // Stocker l'ID du client à partir de l'intervention
    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "technicien_id", nullable = false)
    private Long technicienId;

    @Column(name = "produit_id", nullable = false)
    private Long produitId;

    @Column(name = "reclamation_id", nullable = false)
    private Long reclamationId;

    // Totaux calculés dynamiquement
    @Transient
    private Double totalHT;

    @Transient
    private Double totalTVA;

    @Transient
    private Double totalTTC;

    private LocalDate dateFacture;


    // Méthodes de calcul des totaux
    public Double getTotalHT(List<Produit> produits, Intervention intervention, ReclamationFeignClient reclamationFeignClient) {
        // Récupérer la réclamation via l'ID de l'intervention
        Reclamation reclamation = reclamationFeignClient.getReclamationByInterventionId(intervention.getId());
        Long produitId = reclamation.getProduitId();

        // Calcul du montant HT pour le produit associé à l'intervention (via la réclamation)
        double totalProduit = produits.stream()
                .filter(produit -> produit.getId().equals(produitId))
                .mapToDouble(Produit::getPrix)
                .sum();

        // Calcul du montant HT pour l'intervention
        double totalIntervention = calculateHTForIntervention(intervention);

        return totalProduit + totalIntervention;
    }

    public Double getTotalTVA(List<Produit> produits, Intervention intervention, ReclamationFeignClient reclamationFeignClient) {
        // Récupérer la réclamation via l'ID de l'intervention
        Reclamation reclamation = reclamationFeignClient.getReclamationByInterventionId(intervention.getId());
        Long produitId = reclamation.getProduitId();

        // Calcul de la TVA pour le produit associé à l'intervention (via la réclamation)
        double totalProduitTVA = produits.stream()
                .filter(produit -> produit.getId().equals(produitId))
                .mapToDouble(produit -> produit.getPrix() * 0.20)  // Exemple : TVA à 20%
                .sum();

        // Calcul de la TVA pour l'intervention
        double totalInterventionTVA = calculateTVAForIntervention(intervention);

        return totalProduitTVA + totalInterventionTVA;
    }

    public Double getTotalTTC(List<Produit> produits, Intervention intervention, ReclamationFeignClient reclamationFeignClient) {
        return getTotalHT(produits, intervention, reclamationFeignClient) + getTotalTVA(produits, intervention, reclamationFeignClient);
    }

    // Méthodes pour calculer HT et TVA pour l'intervention (logique à ajuster selon le contexte)
    private double calculateHTForIntervention(Intervention intervention) {
        // Exemple de calcul d'HT pour l'intervention
        return 50.0; // A remplacer par la logique de calcul spécifique
    }

    private double calculateTVAForIntervention(Intervention intervention) {
        // Exemple de calcul de la TVA pour l'intervention
        return 10.0; // A remplacer par la logique de calcul spécifique
    }
}
