package com.sav.produitservices.Controller;

import com.sav.produitservices.Entity.Produit;
import com.sav.produitservices.Repository.ProduitRepository;
import com.sav.produitservices.Services.ProduitService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/produits")
class ProduitController {

    @Autowired
    ProduitService produitService;

    @Autowired
    ProduitRepository produitRepository;

    @Value("${upload.dir}")
    private String uploadDir;

    @PostMapping("/{id}/uploadImage")
    public ResponseEntity<String> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        Optional<Produit> produitOpt = produitRepository.findById(id);
        if (produitOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            // Générer un nom de fichier unique
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File destFile = new File(uploadDir + fileName);
            file.transferTo(destFile);

            // Mettre à jour l'URL de l'image du produit
            Produit produit = produitOpt.get();
            produit.setImageUrl("/uploads/" + fileName);
            produitRepository.save(produit);

            return ResponseEntity.ok("Image uploadée avec succès : " + produit.getImageUrl());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erreur lors de l'upload de l'image");
        }
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<String> getImageUrl(@PathVariable Long id, HttpServletRequest request) {
        Optional<Produit> produitOpt = produitRepository.findById(id);
        return produitOpt.map(produit -> {
            String imageUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + produit.getImageUrl();
            return ResponseEntity.ok(imageUrl);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/all")
    public List<Produit> getAllProduits() {
        List<Produit> produits = produitService.getAllProduits();

        for (Produit produit : produits) {
            // Construire l'URL de l'image
            if (produit.getImageUrl() != null) {
                produit.setImageUrl("http://localhost:9999/images/" + produit.getImageUrl());
            }
        }
        return produits;
    }


    @GetMapping("/{id}")
    public Produit getProduitById(@PathVariable("id") Long id) {
        return produitService.getProduitById(id);
    }

    @PostMapping
    public ResponseEntity<Produit> createProduit(
            @RequestParam("nom") String nom,
            @RequestParam("description") String description,
            @RequestParam("prix") float prix,
            @RequestParam("categorieId") Long categorieId,
            @RequestParam("subCategoryId") Long subCategoryId,
            @RequestParam int dureeGarantie,
            @RequestParam(value = "reference", required = false) String reference,
            @RequestParam(value = "statut", required = false) String statut,
            @RequestParam(value = "imageUrl", required = false) MultipartFile imageUrl,
            @RequestParam(value = "guidePdf", required = false) MultipartFile guidePdf) {

        try {
            String imageFileName = null;
            String pdfFileName = null;

            // ==== Sauvegarde de l'image ====
            if (imageUrl != null && !imageUrl.isEmpty()) {
                imageFileName = UUID.randomUUID() + "_" + imageUrl.getOriginalFilename();
                String imageUploadDir = "D:/images/";

                File imageDir = new File(imageUploadDir);
                if (!imageDir.exists()) {
                    imageDir.mkdirs();
                }

                File destImage = new File(imageUploadDir + imageFileName);
                imageUrl.transferTo(destImage);
            }

            // ==== Sauvegarde du PDF ====
            if (guidePdf != null && !guidePdf.isEmpty()) {
                pdfFileName = UUID.randomUUID() + "_" + guidePdf.getOriginalFilename();
                String pdfUploadDir = "D:/pdf/";

                File pdfDir = new File(pdfUploadDir);
                if (!pdfDir.exists()) {
                    pdfDir.mkdirs();
                }

                File destPdf = new File(pdfUploadDir + pdfFileName);
                guidePdf.transferTo(destPdf);
            }

            // ==== Création de l'objet Produit ====
            Produit produit = new Produit();
            produit.setNom(nom);
            produit.setDescription(description);
            produit.setPrix(prix);
            produit.setStatut(statut);
            produit.setReference(reference);
            produit.setDureeGarantie(dureeGarantie);
            produit.setCategorieId(categorieId);
            produit.setSubCategoryId(subCategoryId);
            produit.setImageUrl(imageFileName);
            produit.setGuideTechniqueUrl(pdfFileName);

            if (produit.getNumeroSerie() == null || produit.getNumeroSerie().isEmpty()) {
                produit.setNumeroSerie("SN-" + UUID.randomUUID().toString());
            }

            Produit savedProduit = produitService.addProduct(produit);
            return ResponseEntity.ok(savedProduit);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    @GetMapping("/subcategorie/{subCategorieId}")
    public ResponseEntity<List<Produit>> getProduitsBySubCategorie(@PathVariable Long subCategorieId) {
        List<Produit> produits = produitService.getProduitsBySubCategorie(subCategorieId);

        for (Produit produit : produits) {
            // Construire l'URL de l'image
            if (produit.getImageUrl() != null) {
                produit.setImageUrl("http://localhost:9999/images/" + produit.getImageUrl());
            }
        }
        return ResponseEntity.ok(produits);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduit(@PathVariable Long id) {
        Optional<Produit> produitOpt = produitRepository.findById(id);
        if (produitOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produit non trouvé.");
        }
        // Récupérer le produit et supprimer l'image associée (si nécessaire)
        Produit produit = produitOpt.get();
        if (produit.getImageUrl() != null) {
            // Supprimer l'image du système de fichiers si elle existe
            File imageFile = new File(uploadDir + produit.getImageUrl());
            if (imageFile.exists()) {
                imageFile.delete(); // Supprimer le fichier de l'image
            }
        }

        // Supprimer le produit de la base de données
        produitRepository.delete(produit);

        return ResponseEntity.ok("Produit supprimé avec succès.");
    }

    @GetMapping("/by-nom")
    public Produit getProduitByNom(@RequestParam("nom") String nom){
        return  produitService.getProduitParNom(nom);
    };



    @PutMapping("/upload-guide/{id}")
    public ResponseEntity<?> uploadGuidePdf(
            @PathVariable Long id,
            @RequestParam("guidePdf") MultipartFile guidePdf) {

        try {
            // Vérifie si le produit existe
            Produit produit = produitService.getProduitById(id);
            if (produit == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produit non trouvé");
            }

            // Sauvegarde du PDF
            String pdfFileName = UUID.randomUUID() + "_" + guidePdf.getOriginalFilename();
            String pdfUploadDir = "D:/pdf/";

            File pdfDir = new File(pdfUploadDir);
            if (!pdfDir.exists()) {
                pdfDir.mkdirs();
            }

            File destPdf = new File(pdfUploadDir + pdfFileName);
            guidePdf.transferTo(destPdf);

            // Mise à jour du produit
            produit.setGuideTechniqueUrl(pdfFileName);
            Produit updatedProduit = produitService.addProduct(produit); // ou updateProduit

            return ResponseEntity.ok(updatedProduit);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors du téléchargement du fichier : " + e.getMessage());
        }
    }

    @GetMapping("/{id}/guide")
    public ResponseEntity<String> getGuidePdfUrl(@PathVariable Long id, HttpServletRequest request) {
        Optional<Produit> produitOpt = produitRepository.findById(id);
        if (produitOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produit non trouvé");
        }

        Produit produit = produitOpt.get();
        String guideFilename = produit.getGuideTechniqueUrl();

        if (guideFilename == null || guideFilename.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Guide technique non disponible pour ce produit");
        }

        // On suppose que le PDF est accessible via la route /pdf/
        String guideUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/pdf/" + guideFilename;
        return ResponseEntity.ok(guideUrl);
    }

    @GetMapping("getPDF/{filename:.+}")
    public ResponseEntity<Resource> getPdf(@PathVariable String filename) throws IOException {
        Path path = Paths.get("D:/pdf/" + filename);
        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }
        Resource resource = new UrlResource(path.toUri());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }


}
