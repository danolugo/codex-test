package com.matchme.controller;

import com.matchme.model.User;
import com.matchme.service.ChatService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatRestController {
    private final ChatService chatService;

    public ChatRestController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chats/{userId}")
    public List<?> getMessages(Authentication auth, @PathVariable Long userId) {
        User me = (User) auth.getPrincipal();
        return chatService.getRecentMessages(me, userId);
    }
}
