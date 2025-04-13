
package com.sav.historiqueachat.controller;

import com.sav.historiqueachat.DTO.HistoriqueAchatDTO;
import com.sav.historiqueachat.Entity.AchatRequest;
import com.sav.historiqueachat.Entity.HistoriqueAchat;
import com.sav.historiqueachat.service.HistoriqueAchatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/historique")
public class HistoriqueAchatController {

    @Autowired
    private HistoriqueAchatService service;


    @PostMapping("/ajouter")
    public HistoriqueAchat ajouterAchat(@RequestBody AchatRequest achatRequest) {

        return service.ajouterAchat(
                achatRequest.getClientId(),
                achatRequest.getProduitId(),
                achatRequest.getQuantite(),
                //achatRequest.getGarantieExpireLe(),
                achatRequest.getPrixUnitaire()
        );
    }

    @GetMapping("/all")
    public List<HistoriqueAchat> getAllAchats() {
        return service.getAllAchats();
    }

    @GetMapping("/client/{clientId}")
    public List<HistoriqueAchatDTO> getAchatsByClient(@PathVariable Long clientId) {
        return service.getAchatsByClient(clientId);
    }

    @GetMapping("/produit/{produitId}")
    public List<HistoriqueAchat> getAchatsByProduit(@PathVariable Long produitId) {
        return service.getAchatsByProduit(produitId);
    }
}
