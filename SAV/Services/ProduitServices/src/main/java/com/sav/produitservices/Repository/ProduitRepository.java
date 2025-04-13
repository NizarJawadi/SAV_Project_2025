package com.sav.produitservices.Repository;

import com.sav.produitservices.Entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProduitRepository  extends JpaRepository<Produit,Long> {

    public List<Produit> findBySubCategoryId(Long subCategoryId);
    public Produit findByNom(String name);
}
