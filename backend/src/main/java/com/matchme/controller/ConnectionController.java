package com.matchme.controller;

import com.matchme.model.User;
import com.matchme.service.ConnectionService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/connections")
public class ConnectionController {
    private final ConnectionService connectionService;

    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @PostMapping
    public void sendRequest(Authentication auth, @RequestBody Map<String, Long> body) {
        User user = (User) auth.getPrincipal();
        connectionService.requestConnection(user, body.get("targetId"));
    }

    @GetMapping
    public Map<String, Object> listConnections(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return Map.of("connections", connectionService.getConnections(user));
    }
}
