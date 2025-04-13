package com.sav.piecederechange.service;

import com.sav.piecederechange.entity.PieceDeRechange;
import com.sav.piecederechange.repository.PieceDeRechangeRepository;
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
}