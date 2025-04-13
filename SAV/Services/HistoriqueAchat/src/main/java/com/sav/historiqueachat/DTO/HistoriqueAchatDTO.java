package com.sav.historiqueachat.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoriqueAchatDTO {

    private Long id;
    private Long clientId;
    private Long produitId;
    private String produitNom;
    private String produitDescription;
    private float prixUnitaire;
    private int quantite;
    private float prixTotal;
    private String dateAchat;
    private String garantieExpireLe;
    private String imageUrl;  // Champ ajouté pour l'URL de l'image

    // Constructeur pour faciliter la création de l'objet DTO
    public HistoriqueAchatDTO(Long id, Long clientId, Long produitId, String produitNom, String produitDescription,
                              float prixUnitaire, int quantite, float prixTotal, String dateAchat, String garantieExpireLe,
                              String imageUrl) {
        this.id = id;
        this.clientId = clientId;
        this.produitId = produitId;
        this.produitNom = produitNom;
        this.produitDescription = produitDescription;
        this.prixUnitaire = prixUnitaire;
        this.quantite = quantite;
        this.prixTotal = prixTotal;
        this.dateAchat = dateAchat;
        this.garantieExpireLe = garantieExpireLe;
        this.imageUrl = imageUrl;  // Ajout de l'URL de l'image
    }
}
