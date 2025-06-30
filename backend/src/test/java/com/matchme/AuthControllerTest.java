package com.matchme;

import com.matchme.model.User;
import com.matchme.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void registerAndLogin() throws Exception {
        String body = "{\"email\":\"user@example.com\",\"password\":\"secret\"}";
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());

        User user = userRepository.findByEmail("user@example.com").orElseThrow();
        assertThat(passwordEncoder.matches("secret", user.getPassword())).isTrue();

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());
    }


    @Test
    void loginFailsWithWrongPassword() throws Exception {
        // register first
        String body = "{\"email\":\"wrong@example.com\",\"password\":\"pass\"}";
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());

        String bad = "{\"email\":\"wrong@example.com\",\"password\":\"bad\"}";
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bad))
                .andExpect(status().isUnauthorized());
    }
}

