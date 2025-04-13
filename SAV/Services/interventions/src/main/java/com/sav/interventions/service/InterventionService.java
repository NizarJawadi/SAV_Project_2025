package com.sav.interventions.service;

import com.sav.interventions.DTO.InterventionWithReclamationDto;
import com.sav.interventions.DTO.ReclamationDTO;
import com.sav.interventions.DTO.StatusDTO;
import com.sav.interventions.DTO.StatutReclamation;
import com.sav.interventions.entity.Intervention;
import com.sav.interventions.feign.ClientClient;
import com.sav.interventions.feign.ReclamationClient;
import com.sav.interventions.repository.InterventionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

        // Récupération des informations du client
        Object client = reclamation != null ? clientClient.getClientById(reclamation.getClientId()).getBody() : null;

        return new InterventionWithReclamationDto(intervention, reclamation, client);
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
            return new InterventionWithReclamationDto(intervention , interventionOpt.getReclamation() , interventionOpt.getClient());
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

}
