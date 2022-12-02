package ru.inside.task.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.inside.task.dto.AuthDtoRequest;
import ru.inside.task.dto.UserDtoRequest;
import ru.inside.task.model.Role;
import ru.inside.task.model.User;
import ru.inside.task.repository.UserRepository;
import ru.inside.task.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    final Set<Role> ROLES_1 = new HashSet<>(List.of(
            new Role(1L, "ROLE_USER", "user", "Role for simple user")
    ));
    final User USER_1 = new User(1L, "Piter", "Password", ROLES_1);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void dataUp() {
        userService.createUser(new UserDtoRequest(USER_1.getUsername(), USER_1.getPassword(), null));
    }

    @AfterEach
    void dataDown() {
        userRepository.deleteAll();
    }

    @Test
    public void correctLoginTest() throws Exception {
        AuthDtoRequest authDto = new AuthDtoRequest(USER_1.getUsername(), USER_1.getPassword());
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectToJson(authDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    public void badCredentials() throws Exception {
        AuthDtoRequest authDto = new AuthDtoRequest("None", USER_1.getPassword());
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectToJson(authDto)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    public static String objectToJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
}
