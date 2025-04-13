package com.sav.voipservice.controller;

import com.sav.voipservice.Services.CallService;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/call")
public class CallController {

    @Autowired
    private CallService callService;

    /**
     * Endpoint pour initier un appel.
     * Exemple d’URL : POST http://localhost:8080/api/call/make?from=1000&to=1001
     *
     * @param from Identifiant SIP de l’émetteur.
     * @param to   Extension ou numéro à joindre.
     * @return Message de succès ou d'erreur.
     */
    @PostMapping("/make")
    public ResponseEntity<String> makeCall(@RequestParam String from, @RequestParam String to) {
        try {
            callService.makeCall(from, to);
            return ResponseEntity.ok("Appel initié avec succès");
        } catch (IOException | AuthenticationFailedException | TimeoutException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'initiation de l'appel : " + e.getMessage());
        }
    }
}