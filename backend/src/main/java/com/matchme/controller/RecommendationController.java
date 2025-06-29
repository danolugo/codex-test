package com.matchme.controller;

import com.matchme.model.User;
import com.matchme.service.RecommendationService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RecommendationController {
    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/recommendations")
    public Map<String, Object> getRecommendations(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return Map.of("recommendations", recommendationService.getRecommendations(user));
    }
}
