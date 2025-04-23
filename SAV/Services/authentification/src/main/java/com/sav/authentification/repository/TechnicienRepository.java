package com.sav.authentification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sav.authentification.model.Technicien;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TechnicienRepository extends JpaRepository<Technicien, Long> {

    public List<Technicien> findBySpecialite (String specialite);

    @Query("SELECT DISTINCT t.specialite FROM Technicien t")
    List<String> findAllSpecialites();


    Optional<Technicien> findByMatricule(String matricule);

}
