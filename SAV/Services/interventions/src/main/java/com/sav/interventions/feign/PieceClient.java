package com.sav.interventions.feign;

import com.sav.interventions.DTO.PieceDeRechangeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "PIECE-SERVICE", url = "http://localhost:8110") // Modifier selon ton port
public interface PieceClient {
    @GetMapping("/pieces")
    List<PieceDeRechangeDto> getAllPieces();

    @GetMapping("/pieces/{id}")
    PieceDeRechangeDto getPieceById(@PathVariable Long id);
}