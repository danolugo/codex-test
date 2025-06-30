package com.matchme.service;

import com.matchme.model.User;
import com.matchme.repository.ConnectionRepository;
import com.matchme.repository.ProfileRepository;
import com.matchme.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ConnectionRepository connectionRepository;

    public RecommendationService(UserRepository userRepository,
                                 ProfileRepository profileRepository,
                                 ConnectionRepository connectionRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.connectionRepository = connectionRepository;
    }

    /**
     * Very basic recommendation logic: return up to 10 user IDs of users who have
     * a profile and are not the current user.
     */
    public List<Long> getRecommendations(User currentUser) {
        return userRepository.findAll().stream()
                .filter(u -> !u.getId().equals(currentUser.getId()))
                .filter(u -> profileRepository.findByUserId(u.getId()).isPresent())
                .filter(u -> connectionRepository.findByRequesterAndRecipient(currentUser, u).isEmpty()
                        && connectionRepository.findByRequesterAndRecipient(u, currentUser).isEmpty())
                .limit(10)
                .map(User::getId)
                .collect(Collectors.toList());
    }
}
