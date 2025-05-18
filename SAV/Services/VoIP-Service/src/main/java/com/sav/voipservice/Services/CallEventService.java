package com.sav.voipservice.Services;

import com.sav.voipservice.modelDTO.CallEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class CallEventService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendCallEvent(CallEvent callEvent) {
        messagingTemplate.convertAndSend("/topic/call-events", callEvent);  // Envoie l'événement à tous les abonnés
    }
}
