package com.sav.voipservice.controller;

import com.sav.voipservice.Services.AmiService;
import com.sav.voipservice.Services.CallEventService;
import com.sav.voipservice.modelDTO.CallEvent;
import org.asteriskjava.AsteriskVersion;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ami")
public class AmiController {

    private final AmiService amiService;
    private final ManagerConnection managerConnection;

    public AmiController(AmiService amiService,
                         ManagerConnection managerConnection) {
        this.amiService = amiService;
        this.managerConnection = managerConnection;
    }

    @GetMapping("/status")
    public ResponseEntity<AmiStatusResponse> getStatus() {
        return ResponseEntity.ok(new AmiStatusResponse(
                managerConnection.getState().name(),
                managerConnection.getVersion().toString(),
                amiService.isConnected()
        ));
    }

    @PostMapping("/reconnect")
    public ResponseEntity<String> reconnect() {
        try {
            if (managerConnection.getState() == ManagerConnectionState.CONNECTED) {
                return ResponseEntity.ok("Already connected");
            }

            amiService.connectWithRetry();
            return ResponseEntity.ok("Reconnection initiated");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Reconnection failed: " + e.getMessage());
        }
    }

    @PostMapping("/disconnect")
    public ResponseEntity<String> disconnect() {
        try {
            if (managerConnection.getState() == ManagerConnectionState.CONNECTED) {
                managerConnection.logoff();
                return ResponseEntity.ok("Successfully disconnected");
            }
            return ResponseEntity.ok("Already disconnected");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Disconnection failed: " + e.getMessage());
        }
    }

    // Classe interne pour la réponse structurée
    private static class AmiStatusResponse {
        private final String state;
        private final String version;
        private final boolean connected;

        public AmiStatusResponse(String state, String version, boolean connected) {
            this.state = state;
            this.version = version;
            this.connected = connected;
        }

        // Getters
        public String getState() { return state; }
        public String getVersion() { return version; }
        public boolean isConnected() { return connected; }
    }


    @Autowired
    private CallEventService callEventService;

    // Endpoint pour envoyer un événement d'appel
    @PostMapping("/send-call-event")
    public String sendCallEvent() {
        // Crée un événement d'appel
        CallEvent callEvent = new CallEvent();
        callEvent.setEventType("NewChannel");
        callEvent.setTimestamp("2025-05-13T03:36:03");

        // Appelle la méthode sendCallEvent() pour envoyer l'événement
        callEventService.sendCallEvent(callEvent);
        System.out.println(callEvent);
        return "Call Event Sent!";
    }
}