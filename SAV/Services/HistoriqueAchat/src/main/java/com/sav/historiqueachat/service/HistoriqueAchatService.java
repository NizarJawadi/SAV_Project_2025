package com.sav.historiqueachat.service;

import com.sav.historiqueachat.DTO.HistoriqueAchatDTO;
import com.sav.historiqueachat.Entity.HistoriqueAchat;
import com.sav.historiqueachat.Entity.Produit;
import com.sav.historiqueachat.feign.ProduitServiceClient;
import com.sav.historiqueachat.feign.UserServiceClient;
import com.sav.historiqueachat.repository.HistoriqueAchatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HistoriqueAchatService {

    @Autowired
    private HistoriqueAchatRepository repository;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private ProduitServiceClient produitServiceClient;
    // Ajouter un achat
    public HistoriqueAchat ajouterAchat(Long clientId, Long produitId, int quantite, float prixUnitaire) {
        // Vérifier si le client existe
        Object client = userServiceClient.getClientById(clientId);
        if (client == null) {
            throw new RuntimeException("Client non trouvé");
        }

        // Vérifier si le produit existe
        Object produitObj = produitServiceClient.getProduitById(produitId);
        if (produitObj == null) {
            throw new RuntimeException("Produit non trouvé");
        }

        // Récupérer la durée de garantie en mois du produit
        // Supposons que le produit est un objet de type Produit et qu'il a un champ dureeGarantieEnMois
        Produit produit = (Produit) produitObj;
        int dureeGarantieEnMois = produit.getDureeGarantie();

        // Créer l'objet HistoriqueAchat
        HistoriqueAchat achat = HistoriqueAchat.builder()
                .clientId(clientId)
                .produitId(produitId)
                .quantite(quantite)
                .prixTotal(quantite * prixUnitaire)
                .dateAchat(LocalDateTime.now())
                .build();

        // Calculer la date d'expiration de la garantie
        if (achat.getDateAchat() != null && dureeGarantieEnMois > 0) {
            // Date d'expiration = date d'achat + durée de garantie en mois
            achat.setGarantieExpireLe(java.sql.Date.valueOf(achat.getDateAchat()
                    .plusMonths(dureeGarantieEnMois)
                    .toLocalDate()));
        }

        // Sauvegarder l'achat dans la base de données
        return repository.save(achat);
    }

    // Récupérer tous les achats
    public List<HistoriqueAchat> getAllAchats() {
        return repository.findAll();
    }

    // Récupérer les achats par client
   // public List<HistoriqueAchat> getAchatsByClient(Long clientId) {
    //    return repository.findByClientId(clientId);
   // }


    public List<HistoriqueAchatDTO> getAchatsByClient(Long clientId) {
        List<HistoriqueAchat> achats = repository.findByClientId(clientId);
        List<HistoriqueAchatDTO> achatDTOs = new ArrayList<>();

        for (HistoriqueAchat achat : achats) {
            // Récupérer les informations du produit
            Produit produit = produitServiceClient.getProduitById(achat.getProduitId());


            // Créer un DTO avec les informations de l'achat et du produit, y compris l'image
            HistoriqueAchatDTO dto = new HistoriqueAchatDTO(
                    achat.getId(),
                    achat.getClientId(),
                    achat.getProduitId(),
                    produit.getNom(),
                    produit.getDescription(),
                    produit.getPrix(),
                    achat.getQuantite(),
                    achat.getPrixTotal(),
                    achat.getDateAchat().toString(),
                    achat.getGarantieExpireLe() != null ? achat.getGarantieExpireLe().toString() : null,
                    produit.getImageUrl()  // Inclure l'URL de l'image
            );
                achatDTOs.add(dto);
            }


        return achatDTOs;
    }

    // Récupérer les achats par produit
    public List<HistoriqueAchat> getAchatsByProduit(Long produitId) {
        return repository.findByProduitId(produitId);
    }
}