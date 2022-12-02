package ru.inside.task.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.inside.task.dto.AuthDtoRequest;
import ru.inside.task.dto.NoteDtoRequest;
import ru.inside.task.dto.UserDtoRequest;
import ru.inside.task.model.Note;
import ru.inside.task.model.Role;
import ru.inside.task.model.User;
import ru.inside.task.repository.NoteRepository;
import ru.inside.task.repository.UserRepository;
import ru.inside.task.service.NoteService;
import ru.inside.task.service.UserService;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class NoteControllerTest {

    private String token;
    private User user;
    final Set<Role> ROLES_1 = new HashSet<>(List.of(
            new Role(1L, "ROLE_USER", "user", "Role for simple user")
    ));
    final User USER_1 = new User(1L, "Piter", "Password", ROLES_1);
    final Note NOTE_1 = new Note(1L, "Note one", USER_1);
    final Note NOTE_2 = new Note(2L, "Note two", USER_1);
    final Note NOTE_3 = new Note(3L, "Note tree", USER_1);
    final Note NOTE_4 = new Note(4L, "Note four", USER_1);


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    UserService userService;
    @Autowired
    NoteService noteService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    NoteRepository noteRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void dataUp() throws Exception {
        userService.createUser(new UserDtoRequest(USER_1.getUsername(), USER_1.getPassword(), null));
        AuthDtoRequest authDto = new AuthDtoRequest(USER_1.getUsername(), USER_1.getPassword());
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectToJson(authDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        token = "Bearer_" + giveTokenFromString(body);
    }

    @AfterEach
    void dataDown() {
        noteRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void correctSaveMessage() throws Exception {
        NoteDtoRequest noteDto = new NoteDtoRequest(NOTE_1.getUser().getUsername(), NOTE_1.getMessage());
        mockMvc.perform(post("/api/v1/note").header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectToJson(noteDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(NOTE_1.getUser().getUsername()))
                .andExpect(jsonPath("$.message").value(NOTE_1.getMessage()));
    }

    @Test
    public void saveMessageWithoutToken() throws Exception {
        NoteDtoRequest noteDto = new NoteDtoRequest(NOTE_1.getMessage(), NOTE_1.getUser().getUsername());
        mockMvc.perform(post("/api/v1/note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectToJson(noteDto)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }


    @Test
    public void saveMessageWithBadToken() throws Exception {
        String fakeToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJQaXRlciIsImlhdCI6MTY2OTg1Nzc5MSwiZXhwIjoxNjY5ODc5MzkxfQ." +
                "poiYi56-bvZtwPFyUh0M8t6DRZu2pKYU80h2ud8rqbt";
        NoteDtoRequest noteDto = new NoteDtoRequest(NOTE_1.getMessage(), NOTE_1.getUser().getUsername());
        mockMvc.perform(post("/api/v1/note").header("Authorization", fakeToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectToJson(noteDto)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void giveHistory_3_Message() throws Exception {
        noteService.createNote(new NoteDtoRequest(NOTE_1.getUser().getUsername(), NOTE_1.getMessage()));
        noteService.createNote(new NoteDtoRequest(NOTE_2.getUser().getUsername(), NOTE_2.getMessage()));
        noteService.createNote(new NoteDtoRequest(NOTE_3.getUser().getUsername(), NOTE_3.getMessage()));
        noteService.createNote(new NoteDtoRequest(NOTE_4.getUser().getUsername(), NOTE_4.getMessage()));

        NoteDtoRequest noteDto = new NoteDtoRequest(NOTE_1.getUser().getUsername(), "history 3");
        mockMvc.perform(post("/api/v1/note").header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectToJson(noteDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].name").value(NOTE_1.getUser().getUsername()))
                .andExpect(jsonPath("$[0].message").value(NOTE_4.getMessage()))
                .andExpect(jsonPath("$[1].message").value(NOTE_3.getMessage()))
                .andExpect(jsonPath("$[2].message").value(NOTE_2.getMessage()))
                .andDo(print());
    }

    public static String objectToJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    public static String giveTokenFromString(String str) {
        return str.substring(str.indexOf("\"token\":\"") + 9, str.lastIndexOf("\""));
    }
}
