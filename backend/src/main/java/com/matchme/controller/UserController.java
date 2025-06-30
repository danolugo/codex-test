package com.matchme.controller;

import com.matchme.model.Profile;
import com.matchme.model.User;
import com.matchme.repository.ProfileRepository;
import com.matchme.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public UserController(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @GetMapping("/me")
    public Map<String, Object> me(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "displayName", user.getDisplayName()
        );
    }

    @GetMapping("/me/profile")
    public Profile getMyProfile(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return profileRepository.findByUserId(user.getId()).orElse(new Profile());
    }

    @PutMapping("/me/profile")
    public Profile updateMyProfile(Authentication auth, @RequestBody Profile incoming) {
        User user = (User) auth.getPrincipal();
        Profile profile = profileRepository.findByUserId(user.getId()).orElseGet(() -> {
            Profile p = new Profile();
            p.setUser(user);
            return p;
        });
        profile.setLocation(incoming.getLocation());
        profile.setIndustry(incoming.getIndustry());
        profile.setSkills(incoming.getSkills());
        profile.setStartupStage(incoming.getStartupStage());
        profile.setLookingFor(incoming.getLookingFor());
        profile.setInterests(incoming.getInterests());
        profile.setAboutMe(incoming.getAboutMe());
        return profileRepository.save(profile);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(u -> Map.of("id", u.getId(), "displayName", u.getDisplayName()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{id}/profile")
    public ResponseEntity<?> getProfile(@PathVariable Long id) {
        return profileRepository.findByUserId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
