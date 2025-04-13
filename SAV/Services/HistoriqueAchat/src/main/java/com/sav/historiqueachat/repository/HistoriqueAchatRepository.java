package com.sav.historiqueachat.repository;

import com.sav.historiqueachat.Entity.HistoriqueAchat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoriqueAchatRepository extends JpaRepository<HistoriqueAchat, Long> {
    List<HistoriqueAchat> findByClientId(Long clientId);
    List<HistoriqueAchat> findByProduitId(Long produitId);
}