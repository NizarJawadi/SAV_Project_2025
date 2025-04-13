package com.sav.categoriesService.repository;

import com.sav.categoriesService.entity.SousCategorie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SousCategorieRepository extends JpaRepository<SousCategorie, Long> {

   public List<SousCategorie> findByCategorieId(Long categorieId);
}
