package ru.inside.task.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.inside.task.dto.UserDtoRequest;
import ru.inside.task.dto.UserDtoResponse;
import ru.inside.task.service.UserService;

/**
 * Registration controller
 */
@RestController
@RequestMapping("/api/v1/registration")
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDtoResponse> createUser (@RequestBody UserDtoRequest userDtoRequest) {
        return ResponseEntity.ok(userService.createUser(userDtoRequest));
    }
}
