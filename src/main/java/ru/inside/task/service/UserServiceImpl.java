package ru.inside.task.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.inside.task.dto.UserDtoRequest;
import ru.inside.task.dto.UserDtoResponse;
import ru.inside.task.exception.BadRequestException;
import ru.inside.task.exception.NotFoundException;
import ru.inside.task.model.Role;
import ru.inside.task.model.User;
import ru.inside.task.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private static final int MAX_LENGTH_NAME = 150;
    private static final int MIN_LENGTH_PASSWORD = 5;
    private static final int MAX_LENGTH_PASSWORD = 150;

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleService roleService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDtoResponse createUser(UserDtoRequest userDtoRequest) {
        if (userDtoRequest == null) {
            throw new BadRequestException("Invalid user");
        }
        User userFromDB = userRepository.findByUsername(userDtoRequest.getName());
        if (userFromDB != null) {
            throw new BadRequestException("User's name exist");
        }
        if (!StringUtils.isNotBlank(userDtoRequest.getName())) {
            throw new BadRequestException("Invalid user's name");
        }
        if (userDtoRequest.getName().length() > MAX_LENGTH_NAME) {
            throw new BadRequestException("Too long user's name");
        }
        if (!StringUtils.isNotBlank(userDtoRequest.getPassword())) {
            throw new BadRequestException("Invalid user's password");
        }
        if (userDtoRequest.getPassword().length() > MAX_LENGTH_PASSWORD) {
            throw new BadRequestException("Too long user's password");
        }
        if (userDtoRequest.getPassword().length() < MIN_LENGTH_PASSWORD) {
            throw new BadRequestException("Too short user's password");
        }
        userDtoRequest.setPassword(bCryptPasswordEncoder.encode(userDtoRequest.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getRoleUser());
        userDtoRequest.setRoles(roles);
        return mapToUserDto(userRepository.save(mapToUser(userDtoRequest)));
    }

    @Override
    public UserDtoResponse getUserById(Long id) {
        if (id == null) {
            throw new BadRequestException("Invalid ID");
        }
        return mapToUserDto(userRepository.findById(id).orElseThrow(()
                -> new NotFoundException("User not found!")));
    }

    @Override
    public UserDtoResponse mapToUserDto(User user){
        UserDtoResponse userDtoResponse = new UserDtoResponse();
        //3 column
        userDtoResponse.setId(user.getId());
        userDtoResponse.setName(user.getUsername());
        userDtoResponse.setRoles(user.getRoles());
        return userDtoResponse;
    }
    
    @Override
    public User mapToUser(UserDtoRequest userDtoRequest){
        User user = new User();
        //3 column
        user.setUsername(userDtoRequest.getName());
        user.setPassword(userDtoRequest.getPassword());
        user.setRoles(userDtoRequest.getRoles());
        return user;
    }
}
