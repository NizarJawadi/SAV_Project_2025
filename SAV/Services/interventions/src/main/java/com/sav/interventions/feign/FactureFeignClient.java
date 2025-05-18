package com.sav.interventions.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "facture-service", url = "http://localhost:6666/factures") // adapte au nom réel
public interface FactureFeignClient {

    @GetMapping(value = "/export/{reclamationId}", produces = MediaType.APPLICATION_PDF_VALUE)
    ResponseEntity<byte[]> exportFacturePdf(@PathVariable("reclamationId") Long reclamationId);
}
