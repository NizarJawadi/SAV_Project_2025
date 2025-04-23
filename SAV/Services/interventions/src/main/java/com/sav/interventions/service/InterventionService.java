package com.sav.interventions.service;

import com.sav.interventions.DTO.*;
import com.sav.interventions.Exception.ResourceNotFoundException;
import com.sav.interventions.entity.Intervention;
import com.sav.interventions.feign.ClientClient;
import com.sav.interventions.feign.PieceClient;
import com.sav.interventions.feign.ReclamationClient;
import com.sav.interventions.feign.TechnicienClient;
import com.sav.interventions.repository.InterventionRepository;
import com.sav.interventions.utils.PieceUtilisee;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InterventionService {

    @Autowired
    private InterventionRepository interventionRepository;

    @Autowired
    private ReclamationClient reclamationClient;

    @Autowired
    private ClientClient clientClient;

    @Autowired
    private PieceClient pieceClient;

    @Autowired
    private TechnicienClient technicienClient;

    public List<InterventionWithReclamationDto> getAllInterventions() {
        List<Intervention> interventions = interventionRepository.findAll();
        return interventions.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public Intervention createIntervention(Intervention intervention) {
        // Enregistrement de l'intervention dans la base de données
        return interventionRepository.save(intervention);
    }

    public InterventionWithReclamationDto getInterventionById(Long id) {
        Intervention intervention = interventionRepository.findById(id).orElseThrow(() -> new RuntimeException("Intervention introuvable"));
        return mapToDto(intervention);
    }

    private InterventionWithReclamationDto mapToDto(Intervention intervention) {
        // Récupération des informations de la réclamation
        ReclamationDTO reclamation = reclamationClient.getReclamationById(intervention.getReclamationId()).getBody();
        TechnicienDTO technicien = technicienClient.getTechnicienById(intervention.getTechnicienId());

        System.out.println(intervention);
        // Récupération des informations du client
        Object client = reclamation != null ? clientClient.getClientById(reclamation.getClientId()).getBody() : null;

        return new InterventionWithReclamationDto(intervention, reclamation, client , technicien);
    }


    public InterventionWithReclamationDto updateInterventionStatus(Long id, StatusDTO status) throws ChangeSetPersister.NotFoundException {
        InterventionWithReclamationDto interventionOpt = getInterventionById(id);
        if (interventionOpt != null) {
            Intervention intervention = interventionOpt.getIntervention();
            // Mise à jour de l'état de l'intervention
            intervention.setStatut(status.getStatus());
            interventionRepository.save(intervention);

            // Interaction avec la réclamation associée via Feign
            if (intervention.getStatut().equals("EN_COURS")) {
                // Si l'intervention est en cours, on met la réclamation en "en cours"
                intervention.setDateDebut(LocalDateTime.now());
                updateReclamationStatus(intervention.getReclamationId(), StatutReclamation.EN_COURS);
                interventionRepository.save(intervention);
            } else if (intervention.getStatut().equals("TERMINEE")) {
                // Si l'intervention est terminée, on met la réclamation en "résolue"
                intervention.setDateFin(LocalDateTime.now());
                updateReclamationStatus(intervention.getReclamationId(), StatutReclamation.RESOLUE);
                interventionRepository.save(intervention);
            }

            // Retourner les données mises à jour de l'intervention et de la réclamation
            return new InterventionWithReclamationDto(intervention , interventionOpt.getReclamation() , interventionOpt.getClient() , interventionOpt.getTechnicien());
        } else {
            throw new ChangeSetPersister.NotFoundException();
        }
    }

    private void updateReclamationStatus(Long reclamationId, StatutReclamation status) {
        // Appel Feign pour mettre à jour le statut de la réclamation
        try {
                reclamationClient.updateReclamationStatus(reclamationId, status);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la réclamation avec l'ID " + reclamationId, e);
        }
    }


    Intervention getInterventionParReclamation ( Long idReclamation) {
       return  interventionRepository.findByReclamationId(idReclamation) ;
    }



    public List<PieceUtilisee> getPiecesUtiliseesParIntervention(Long interventionId) {
        // Récupérer l'intervention depuis la base de données
        Intervention intervention = interventionRepository.findById(interventionId)
                .orElseThrow(() -> new ResourceNotFoundException("Intervention non trouvée"));

        // Liste pour stocker les pièces avec leur quantité utilisée
        List<PieceUtilisee> piecesAvecQuantite = new ArrayList<>();

        // Parcourir les pièces utilisées et récupérer les détails de chaque pièce via le client Feign
        for (PieceUtilisee pieceUtilisee : intervention.getPiecesUtilisees()) {
            // Récupérer la pièce depuis le microservice des pièces
            PieceDeRechangeDto piece = pieceClient.getPieceById(pieceUtilisee.getPieceId());

            // Créer un DTO avec la quantité utilisée
            PieceUtilisee pieceUtilisee1 = new PieceUtilisee();
            pieceUtilisee1.setPieceId(piece.getId());
            pieceUtilisee1.setPieceName(piece.getNom()); // Nom de la pièce
            pieceUtilisee1.setQuantite(pieceUtilisee.getQuantite()); // Quantité utilisée

            // Ajouter la pièce avec la quantité à la liste
            piecesAvecQuantite.add(pieceUtilisee1);
        }

        return piecesAvecQuantite;
    }



    public void ajouterPieces(Long interventionId, List<PieceUtilisee> pieces) {
        Intervention intervention = interventionRepository.findById(interventionId)
                .orElseThrow(() -> new RuntimeException("Intervention non trouvée"));
        for (PieceUtilisee dto : pieces) {
            PieceUtilisee pieceUtilisee = new PieceUtilisee();
            pieceUtilisee.setPieceId(dto.getPieceId());
            pieceUtilisee.setPieceName(dto.getPieceName());
            pieceUtilisee.setQuantite(dto.getQuantite());
            pieceUtilisee.setPrixUnitaire(dto.getPrixUnitaire());

            intervention.getPiecesUtilisees().add(pieceUtilisee);
        }

        interventionRepository.save(intervention);
    }


    @Transactional
    public Intervention removePieceFromIntervention(Long interventionId, Long pieceId) {
        Intervention intervention = interventionRepository.findById(interventionId)
                .orElseThrow(() -> new ResourceNotFoundException("Intervention not found"));

        boolean removed = false;

        for (Iterator<PieceUtilisee> iterator = intervention.getPiecesUtilisees().iterator(); iterator.hasNext(); ) {
            PieceUtilisee piece = iterator.next();
            if (piece.getPieceId().equals(pieceId)) {
                iterator.remove(); // Supprime uniquement la première occurrence
                removed = true;
                break;
            }
        }

        if (!removed) {
            throw new IllegalArgumentException("Piece with ID " + pieceId + " not found in this intervention.");
        }

        // Persister la mise à jour
        return interventionRepository.save(intervention);
    }



    public double calculerTauxRetard() {
        List<Intervention> interventions = interventionRepository.findAll();

        long totalTerminees = interventions.stream()
                .filter(i -> i.getStatut().equalsIgnoreCase("TERMINEE"))
                .count();

        long enRetard = interventions.stream()
                .filter(i -> i.getStatut().equalsIgnoreCase("TERMINEE"))
                .filter(i -> i.getDateFin() != null && i.getDateDeadLine() != null)
                .filter(i -> i.getDateFin().isAfter(i.getDateDeadLine()))
                .count();

        if (totalTerminees == 0) return 0.0;

        return (double) enRetard / totalTerminees * 100;
    }


    public long getNombreInterventionsTerminees() {
        return interventionRepository.findAll()
                .stream()
                .filter(i -> "TERMINEE".equalsIgnoreCase(i.getStatut()))
                .count();
    }

    public double getDureeMoyenne() {
        List<Intervention> terminees = interventionRepository.findAll()
                .stream()
                .filter(i -> "TERMINEE".equalsIgnoreCase(i.getStatut()))
                .filter(i -> i.getDateDebut() != null && i.getDateFin() != null)
                .toList();

        if (terminees.isEmpty()) return 0;

        double totalHeures = terminees.stream()
                .mapToDouble(i -> java.time.Duration.between(i.getDateDebut(), i.getDateFin()).toMinutes() / 60.0)
                .sum();

        return totalHeures / terminees.size();
    }


    public double getMoyennePiecesUtilisees() {
        List<Intervention> interventions = interventionRepository.findAll();

        if (interventions.isEmpty()) return 0;

        double totalPieces = interventions.stream()
                .mapToDouble(i -> i.getPiecesUtilisees()
                        .stream()
                        .mapToInt(p -> p.getQuantite() != null ? p.getQuantite() : 0)
                        .sum())
                .sum();

        return totalPieces / interventions.size();
    }


    public long getReclamationsTraitees() {
        return interventionRepository.findAll()
                .stream()
                .filter(i -> "TERMINEE".equalsIgnoreCase(i.getStatut()))
                .map(Intervention::getReclamationId)
                .distinct()
                .count();
    }

    public double getTempsMoyenAttente() {
        List<Intervention> interventions = interventionRepository.findAll()
                .stream()
                .filter(i -> i.getDateDebut() != null)
                .filter(i -> i.getDateIntervention() != null)
                .toList();

        if (interventions.isEmpty()) return 0;

        double totalJours = interventions.stream()
                .mapToDouble(i -> java.time.Duration.between(i.getDateIntervention(), i.getDateDebut()).toHours() / 24.0)
                .sum();

        return totalJours / interventions.size();
    }


    public List<InterventionWithReclamationDto> getInterventionsByTechnicienMatricule(String matricule) {
        TechnicienDTO technicien = technicienClient.getTechnicienByMatricule(matricule);
        List<Intervention> interventions = interventionRepository.findByTechnicienId(technicien.getIdUser());
        return interventions.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<InterventionWithReclamationDto> getInterventionsByTechnicienSpecialite(String specialite) {
        List<TechnicienDTO> techniciens = technicienClient.getTechniciensBySpecialite(specialite);
        List<Intervention> interventions = new ArrayList<>();
        for (TechnicienDTO t : techniciens) {
            interventions.addAll(interventionRepository.findByTechnicienId(t.getIdUser()));
        }
        return interventions.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<InterventionWithReclamationDto> getInterventionsByTechnicienId(Long id) {
        List<Intervention> interventions = interventionRepository.findByTechnicienId(id);
        return interventions.stream().map(this::mapToDto).collect(Collectors.toList());
    }



}
