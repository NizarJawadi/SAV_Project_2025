package com.sav.categoriesService.repository;

import com.sav.categoriesService.entity.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorieRepository extends JpaRepository<Categorie, Long> {
}
