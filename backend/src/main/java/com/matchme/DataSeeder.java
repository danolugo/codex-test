package com.matchme;

import com.matchme.model.Profile;
import com.matchme.model.User;
import com.matchme.repository.ProfileRepository;
import com.matchme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class DataSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed:false}")
    private boolean seed;

    public DataSeeder(UserRepository userRepository,
                      ProfileRepository profileRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!seed || userRepository.count() > 0) {
            return;
        }
        for (int i = 1; i <= 100; i++) {
            User u = new User();
            u.setEmail("user" + i + "@example.com");
            u.setPassword(passwordEncoder.encode("password"));
            u.setDisplayName("User " + i);
            userRepository.save(u);

            Profile p = new Profile();
            p.setUser(u);
            p.setLocation("City " + (i % 10));
            p.setIndustry(i % 2 == 0 ? "Tech" : "Health");
            p.setSkills(i % 3 == 0 ? "Designer" : "Engineer");
            p.setStartupStage("idea");
            p.setLookingFor("Partner");
            profileRepository.save(p);
        }
    }
}
