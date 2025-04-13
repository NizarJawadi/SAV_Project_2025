package com.sav.factureservice.service;

import com.sav.factureservice.feign.*;
import com.sav.factureservice.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FactureService {

    private final ReclamationFeignClient reclamationFeign;
    private final ProduitFeignClient produitFeign;
    private final ClientFeignClient clientFeign;
    private final InterventionFeignClient interventionFeign;
    private final TechnicienFeignClient technicienFeignClient;

    private static final double TVA_RATE = 0.19;

    public Facture generateFacture(Long reclamationId) {
        // Récupération des données
        Reclamation reclamation = reclamationFeign.getReclamationById(reclamationId);
        Produit produit = produitFeign.getProduitById(reclamation.getProduitId());
        Client client = clientFeign.getClientById(reclamation.getClientId());
        Intervention intervention = interventionFeign.getInterventionsForReclamation(reclamationId);
        Technicien technicien = technicienFeignClient.getTechnicienById(intervention.getTechnicienId());

        double totalHT = calculateTotalHT(produit, intervention, technicien);
        double totalTVA = calculateTotalTVA(produit);
        double totalTTC = totalHT + totalTVA;

        Facture facture = new Facture();
        facture.setId((long) (Math.random() * Long.MAX_VALUE));
        facture.setClientId(client.getIdUser());
        facture.setTechnicienId(technicien.getIdUser());
        facture.setInterventionId(intervention.getId());
        facture.setReclamationId(reclamationId);
        facture.setProduitId(produit.getId());
        facture.setTotalHT(totalHT);
        facture.setTotalTVA(totalTVA);
        facture.setTotalTTC(totalTTC);
        facture.setDateFacture(LocalDate.now());

        log.info("Facture générée avec total HT: {}, TVA: {}, TTC: {}", totalHT, totalTVA, totalTTC);

        return facture;
    }

    private double calculateTotalHT(Produit produit, Intervention intervention, Technicien technicien) {
        double totalProduit = produit.getPrix();

        Duration duration = Duration.between(intervention.getDateDebut(), intervention.getDateFin());
        double durationInHours = duration.toMinutes() / 60.0;

        double tarifHoraire = technicien.getTarifParHeure();
        double totalIntervention = durationInHours * tarifHoraire;

        return totalProduit + totalIntervention;
    }

    private double calculateTotalTVA(Produit produit) {
        double totalProduitTVA = produit.getPrix() * TVA_RATE;
        double totalInterventionTVA = 10.0; // Exemple fixe

        return totalProduitTVA + totalInterventionTVA;
    }

    public byte[] exportFacture(Facture facture,
                                Client client,
                                Technicien technicien,
                                Produit produit,
                                Intervention intervention,
                                Reclamation reclamation) throws JRException {
        System.out.println(facture.toString());
        Map<String, Object> params = new HashMap<>();

        // Infos générales
        params.put("vendeurNom", "SAV Company");
        params.put("vendeurAdresse", technicien.getAdresse());
        params.put("clientNom", client.getUsername());
        params.put("clientAdresse", client.getVille() + " " + client.getCodePostal());
        params.put("dateFacturation", facture.getDateFacture().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        if (facture.getId() != null) {
            params.put("numeroFacture", "FAC-"+ String.valueOf(facture.getId()));
            System.out.println(facture.getId());
        } else {
            System.out.println(facture.getId());
            params.put("numeroFacture", "FAC-Non spécifié");
        }
        params.put("echeance", "30 jours");
        params.put("paiement", "Carte / Virement");
        params.put("informationsAdditionnelles", "Merci de votre confiance !");

        // Infos produit
        params.put("referenceProduit", produit.getReference());
        params.put("nomProduit", produit.getNom());
        params.put("descriptionProduit", produit.getDescription());

        // Infos technicien
        params.put("nomTechnicien", technicien.getUsername());
        params.put("matriculeTechnicien", technicien.getMatricule());

        // Infos réclamation
        params.put("descriptionReclamation", reclamation.getDescription());
        params.put("dateReclamation", reclamation.getDateReclamation().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        //Infos Intervention
        params.put("PieceDeRechange ", intervention.getPiecesIds());

        System.out.println(technicien.getTarifParHeure());

        if (intervention.getDateDebut() != null && intervention.getDateFin() != null) {
            Duration duration = Duration.between(intervention.getDateDebut(), intervention.getDateFin());
            long dureeMinutes = duration.toMinutes();

            long heures = dureeMinutes / 60;
            long minutes = dureeMinutes % 60;

            String dureeFormatted = heures + "h " + minutes + "min";
            params.put("dureeIntervention", dureeFormatted);

            if (technicien.getTarifParHeure() != 0.0) {
                double tarifHoraire = technicien.getTarifParHeure();
                params.put("tarifHoraire", tarifHoraire);

                double dureeHeures = dureeMinutes / 60.0;
                double coutMainOeuvre = dureeHeures * tarifHoraire;

                double TotalTVAMainOuvre =  coutMainOeuvre * TVA_RATE;
                params.put("tarifMainOeuvre", String.format("%.2f DNT", coutMainOeuvre));
                params.put("totalTVAMainOeuvre", String.format("%.2f DNT", TotalTVAMainOuvre));
                params.put("totalTTCMainOeuvre", String.format("%.2f DNT", TotalTVAMainOuvre+ coutMainOeuvre));

            } else {
                params.put("tarifMainOeuvre", "Tarif non défini");
            }

        } else {
            params.put("dureeIntervention", "Date début ou fin manquante");
            params.put("tarifMainOeuvre", "Impossible de calculer (dates null)");
        }
        params.put("tarifHoraire", String.format("%.2f DNT", technicien.getTarifParHeure()));
        params.put("Main d'oeuvre", "Main d'oeuvre");


        // Totaux
        params.put("totalHT", String.format("%.2f DNT", facture.getTotalHT()));
        params.put("totalTVA", String.format("%.2f DNT", facture.getTotalTVA()));
        params.put("totalTTC", String.format("%.2f DNT", facture.getTotalTTC()));
        params.put("TVA_percent", (int) (TVA_RATE*100) +" %");
        // Lignes de la facture




        // Logo
        InputStream logoStream = getClass().getResourceAsStream("/static/logo.png");
        if (logoStream == null) {
            log.warn("Logo file not found!");
        }
        params.put("logo", logoStream);

        // Template
        InputStream templateStream = getClass().getResourceAsStream("/reports/FactureTemplate.jrxml");
        if (templateStream == null) {
            throw new JRException("Template file not found: /reports/FactureTemplate.jrxml");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(templateStream);
        params.forEach((key, value) -> System.out.println(key + ": " + value));

        JasperPrint print = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());

        return JasperExportManager.exportReportToPdf(print);
    }


    public double calculeTotalTVAMainOuvre(double dureeHeures ,double tarifHoraire){

        double totalMainOeuvre = dureeHeures * tarifHoraire;
        return totalMainOeuvre * TVA_RATE;
    }




    public Reclamation getReclamation(Long id) {
        return reclamationFeign.getReclamationById(id);
    }

    public Client getClient(Long id) {
        return clientFeign.getClientById(id);
    }

    public Technicien getTechnicien(Long id) {
        return technicienFeignClient.getTechnicienById(id);
    }

    public Produit getProduit(Long id) {
        return produitFeign.getProduitById(id);
    }

    public Intervention getInterventionParReclamation(Long id) {
        return interventionFeign.getInterventionsForReclamation(id);
    }

}
