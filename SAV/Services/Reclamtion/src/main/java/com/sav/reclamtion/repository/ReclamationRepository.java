package com.sav.reclamtion.repository;

import com.sav.reclamtion.entity.Reclamation;
import com.sav.reclamtion.entity.StatutReclamation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {
    List<Reclamation> findByClientId(Long clientId);



    // 🔹 Récupérer les réclamations par statut (EN_ATTENTE, EN_COURS, etc.)
    List<Reclamation> findByStatut(StatutReclamation statut);

    // 🔹 Optionnel : Rechercher les réclamations ouvertes (EN_ATTENTE ou EN_COURS)
    List<Reclamation> findByStatutIn(List<StatutReclamation> statuts);

}
