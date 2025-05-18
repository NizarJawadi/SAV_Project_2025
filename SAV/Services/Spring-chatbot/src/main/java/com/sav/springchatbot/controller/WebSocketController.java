package com.sav.springchatbot.controller;

import com.sav.springchatbot.services.ChatbotService;
import com.sav.springchatbot.utils.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatbotService chatbotService;

    public WebSocketController(SimpMessagingTemplate messagingTemplate, ChatbotService chatbotService) {
        this.messagingTemplate = messagingTemplate;
        this.chatbotService = chatbotService;
    }

    // Méthode pour recevoir le message du client via WebSocket et envoyer la réponse en temps réel
    @MessageMapping("/send")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        String response = chatbotService.getResponse(chatMessage.getUserId(), chatMessage.getMessage());
        messagingTemplate.convertAndSend("/topic/messages", response);
    }




}
