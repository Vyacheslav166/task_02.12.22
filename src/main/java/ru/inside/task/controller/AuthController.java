package ru.inside.task.controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.inside.task.dto.AuthDtoRequest;
import ru.inside.task.dto.AuthDtoResponse;
import ru.inside.task.exception.BadRequestException;
import ru.inside.task.model.User;
import ru.inside.task.repository.UserRepository;
import ru.inside.task.security.JwtTokenProvider;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController{
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private static final Logger logger = LogManager.getLogger(BadRequestException.class);

    public AuthController(AuthenticationManager authenticationManager
            , UserRepository userRepository
            , JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthDtoResponse> authenticate(@RequestBody AuthDtoRequest request) {
        try {
            UsernamePasswordAuthenticationToken up = new UsernamePasswordAuthenticationToken(request.getName()
                    , request.getPassword());
            authenticationManager.authenticate(up);
            User user = userRepository.findByUsername(request.getName());
            String token = jwtTokenProvider.createToken(request.getName());
            AuthDtoResponse response = AuthDtoResponse.builder().token(token).build();
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            logger.error("Invalid name/password combination" + e.getMessage());
            return new ResponseEntity<>(AuthDtoResponse.builder().error("Invalid name/password combination").build()
                    , HttpStatus.FORBIDDEN);
        }
    }
}
