package com.sav.reclamtion.service;

import com.sav.reclamtion.DTO.ClientDTO;
import com.sav.reclamtion.DTO.InterventionDTO;
import com.sav.reclamtion.DTO.Produit;
import com.sav.reclamtion.DTO.ReclamationDTO;
import com.sav.reclamtion.entity.Reclamation;
import com.sav.reclamtion.entity.StatutReclamation;
import com.sav.reclamtion.feign.ClientFeignClient;
import com.sav.reclamtion.feign.InterventionFeignClient;
import com.sav.reclamtion.feign.ProduitServiceClient;
import com.sav.reclamtion.repository.ReclamationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReclamationService {

    @Autowired
    private ReclamationRepository reclamationRepository;

    @Autowired
    private ProduitServiceClient produitServiceClient;

    @Autowired
    private InterventionFeignClient interventionClient;

    @Autowired
    ClientFeignClient clientFeignClient;

    public Reclamation createReclamation(Reclamation reclamation) {
        ZonedDateTime dateReclamation = ZonedDateTime.now(ZoneId.of("Africa/Blantyre"));
        reclamation.setDateReclamation(dateReclamation.toLocalDateTime());
        reclamation.setStatut(StatutReclamation.EN_ATTENTE);
        return reclamationRepository.save(reclamation);
    }

    public Reclamation annulerReclamation(Long reclamationId) {
        Reclamation reclamation = reclamationRepository.findById(reclamationId)
                .orElseThrow(() -> new RuntimeException("Réclamation introuvable avec ID: " + reclamationId));

        if (!reclamation.getStatut().equals(StatutReclamation.EN_ATTENTE)) {
            throw new IllegalStateException("Seules les réclamations en attente peuvent être annulées.");
        }

        reclamation.setStatut(StatutReclamation.ANNULEE);
        return reclamationRepository.save(reclamation);
    }

    public List<ReclamationDTO> getAllReclamations() {
        return reclamationRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReclamationDTO> getReclamationsByEtat(StatutReclamation etat) {
        return reclamationRepository.findByStatut(etat)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ReclamationDTO convertToDTO(Reclamation reclamation) {
        ReclamationDTO dto = new ReclamationDTO();
        dto.setId(reclamation.getId());

        // Récupérer l'objet ClientDTO complet à partir du service ClientFeignClient
        System.out.println("Récupération du client pour l'ID: " + reclamation.getClientId());
        ClientDTO clientDTO = clientFeignClient.getClientById(reclamation.getClientId());

        // Affichage des informations reçues
        System.out.println("ClientDTO renvoyé par Feign : " + clientDTO);

        // Assurez-vous que le client n'est pas nul
        if (clientDTO != null) {
            System.out.println("Client trouvé: " + clientDTO);
            dto.setClientDTO(clientDTO);
        } else {
            System.out.println("Client introuvable pour ID: " + reclamation.getClientId());
            dto.setClientDTO(new ClientDTO()); // Assurer qu'on ne laisse pas une valeur nulle
        }

        // Reste de la conversion
        dto.setDescription(reclamation.getDescription());
        dto.setStatut(reclamation.getStatut());
        dto.setDateReclamation(reclamation.getDateReclamation());
        dto.setResponsableSAVId(reclamation.getResponsableSAVId());
        dto.setProduit(produitServiceClient.getProduitById(reclamation.getProduitId()));

        System.out.println("DTO final: " + dto);

        return dto;
    }


    public List<ReclamationDTO> getReclamationsByClient(Long clientId) {
        List<Reclamation> reclamations = reclamationRepository.findByClientId(clientId);

        return reclamations.stream().map(reclamation -> {
            Produit produit = produitServiceClient.getProduitById(reclamation.getProduitId());

            // Construire l'objet DTO pour inclure les détails du produit
            ReclamationDTO reclamationDTO = new ReclamationDTO();
            reclamationDTO.setId(reclamation.getId());
            reclamationDTO.setClientDTO(clientFeignClient.getClientById(reclamation.getClientId()));
            reclamationDTO.setProduit(produit); // Ajouter les détails du produit
            reclamationDTO.setDescription(reclamation.getDescription());
            reclamationDTO.setStatut(reclamation.getStatut());
            reclamationDTO.setDateReclamation(reclamation.getDateReclamation());
            reclamationDTO.setResponsableSAVId(reclamation.getResponsableSAVId());
            //reclamationDTO.setInterventionId(reclamation.getInterventionId());

            return reclamationDTO;
        }).collect(Collectors.toList());
    }


    public void assignReclamation(Long reclamationId, Long responsableSAVId) {
        Reclamation reclamation = reclamationRepository.findById(reclamationId)
                .orElseThrow(() -> new RuntimeException("Réclamation non trouvée"));
        reclamation.setStatut(StatutReclamation.EN_COURS);
        reclamation.setResponsableSAVId(responsableSAVId);
        //reclamation.setInterventionId(technicienId); // On affecte le technicien

        reclamationRepository.save(reclamation);
    }


    // Méthode pour créer une intervention à partir d'une réclamation
    public InterventionDTO createInterventionForReclamation(Long reclamationId, Long technicienId) {
        // Créer une intervention à partir des informations nécessaires
        InterventionDTO intervention = new InterventionDTO();
        intervention.setReclamationId(reclamationId);
        intervention.setTechnicienId(technicienId);

        intervention.setStatut("EN_ATTENTE");
        intervention.setDateIntervention(LocalDateTime.now());

        // Appel au Feign Client pour créer l'intervention dans le service
        return interventionClient.createIntervention(intervention);
    }
}
