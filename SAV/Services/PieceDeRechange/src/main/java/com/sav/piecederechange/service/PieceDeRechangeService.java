package com.sav.piecederechange.service;

import com.sav.piecederechange.entity.PieceDeRechange;
import com.sav.piecederechange.exceptions.ResourceNotFoundException;
import com.sav.piecederechange.repository.PieceDeRechangeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PieceDeRechangeService {
    @Autowired
    private PieceDeRechangeRepository pieceDeRechangeRepository;

    public List<PieceDeRechange> getAllPieces() {
        return pieceDeRechangeRepository.findAll();
    }

    public Optional<PieceDeRechange> getPieceById(Long id) {
        return pieceDeRechangeRepository.findById(id);
    }

    public PieceDeRechange addPiece(PieceDeRechange piece) {
        return pieceDeRechangeRepository.save(piece);
    }

    public void deletePiece(Long id) {
        pieceDeRechangeRepository.deleteById(id);
    }

    @Transactional
    public PieceDeRechange updatePiece(Long id, PieceDeRechange updatedPiece) {
        // Vérifier si la pièce existe
        PieceDeRechange existingPiece = pieceDeRechangeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pièce non trouvée avec id : " + id));

        // Mettre à jour les champs
        existingPiece.setNom(updatedPiece.getNom());
        existingPiece.setReference(updatedPiece.getReference());
        existingPiece.setPrix(updatedPiece.getPrix());
        existingPiece.setQuantiteStock(updatedPiece.getQuantiteStock());

        // Enregistrer la pièce mise à jour dans la base de données
        return pieceDeRechangeRepository.save(existingPiece);
    }
}