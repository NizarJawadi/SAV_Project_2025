package com.sav.interventions.repository;

import com.sav.interventions.entity.Intervention;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterventionRepository extends JpaRepository<Intervention, Long> {

    List<Intervention> findByTechnicienId(Long technicienId);

    Intervention findByReclamationId(Long reclamationId);

}
