package com.sav.piecederechange.controller;

import com.sav.piecederechange.entity.PieceDeRechange;
import com.sav.piecederechange.service.PieceDeRechangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pieces")
public class PieceDeRechangeController {
    @Autowired
    private PieceDeRechangeService pieceDeRechangeService;

    @GetMapping
    public List<PieceDeRechange> getAllPieces() {
        return pieceDeRechangeService.getAllPieces();
    }

    @GetMapping("/{id}")
    public Optional<PieceDeRechange> getPieceById(@PathVariable Long id) {
        return pieceDeRechangeService.getPieceById(id);
    }

    @PostMapping
    public PieceDeRechange addPiece(@RequestBody PieceDeRechange piece) {
        return pieceDeRechangeService.addPiece(piece);
    }

    @DeleteMapping("/{id}")
    public void deletePiece(@PathVariable Long id) {
        pieceDeRechangeService.deletePiece(id);
    }
}