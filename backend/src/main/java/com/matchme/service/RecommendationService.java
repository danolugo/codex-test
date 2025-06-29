package com.matchme.service;

import com.matchme.model.Profile;
import com.matchme.model.User;
import com.matchme.repository.ProfileRepository;
import com.matchme.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public RecommendationService(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    /**
     * Very basic recommendation logic: return up to 10 user IDs of users who have
     * a profile and are not the current user.
     */
    public List<Long> getRecommendations(User currentUser) {
        return userRepository.findAll().stream()
                .filter(u -> !u.getId().equals(currentUser.getId()))
                .filter(u -> profileRepository.findByUserId(u.getId()).isPresent())
                .limit(10)
                .map(User::getId)
                .collect(Collectors.toList());
    }
}
