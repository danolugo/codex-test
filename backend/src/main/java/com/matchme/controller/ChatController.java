package com.matchme.controller;

import com.matchme.model.Message;
import com.matchme.model.User;
import com.matchme.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.DestinationVariable;

@Controller
public class ChatController {
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(ChatService chatService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat/{recipientId}")
    public void sendMessage(Authentication auth, @DestinationVariable Long recipientId, @Payload String content) {
        User sender = (User) auth.getPrincipal();
        Message saved = chatService.saveMessage(sender, recipientId, content);
        messagingTemplate.convertAndSendToUser(recipientId.toString(), "/queue/messages", saved);
        messagingTemplate.convertAndSendToUser(sender.getId().toString(), "/queue/messages", saved);
    }
}
