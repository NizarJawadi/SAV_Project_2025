package com.sav.factureservice.controller;


import com.sav.factureservice.model.*;
import com.sav.factureservice.service.FactureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/factures")
public class FactureController {

    @Autowired
    private FactureService factureService;

    // Endpoint pour générer une facture à partir de l'ID de la réclamation
    @PostMapping("/generate/{reclamationId}")
    public Facture generateFacture(@PathVariable Long reclamationId) {
        return factureService.generateFacture(reclamationId);
    }

    @GetMapping("/export/{reclamationId}")
    public ResponseEntity<byte[]> exportPdf(@PathVariable Long reclamationId) {
        Facture facture = factureService.generateFacture(reclamationId);

        // Récupération des données nécessaires
        Reclamation reclamation = factureService.getReclamation(reclamationId);
        Client client = factureService.getClient(facture.getClientId());
        Technicien technicien = factureService.getTechnicien(facture.getTechnicienId());
        Produit produit = factureService.getProduit(facture.getProduitId());
        Intervention intervention = factureService.getInterventionParReclamation(reclamationId);
        //System.out.println("dans facture je recupére : " +intervention);
        try {
            byte[] pdfBytes = factureService.exportFacture(facture, client, technicien, produit, intervention, reclamation);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "inline; filename=facture_" + facture.getId() + ".pdf")
                    .header("Content-Type", "application/pdf")
                    .body(pdfBytes);
        } catch (Exception e) {
            // Log l'erreur pour avoir plus de détails
            e.printStackTrace();  // Affiche l'exception dans la console
            return ResponseEntity.internalServerError().body("Error generating PDF".getBytes());
        }
    }



}