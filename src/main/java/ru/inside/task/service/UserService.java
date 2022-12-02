package ru.inside.task.service;

import ru.inside.task.dto.UserDtoRequest;
import ru.inside.task.dto.UserDtoResponse;
import ru.inside.task.model.User;

public interface UserService {

    /**
     * Create user
     *
     * @param userDtoRequest - object user
     * @return object UserDtoResponse
     */
    UserDtoResponse createUser(UserDtoRequest userDtoRequest);

    /**
     * Get user by id
     *
     * @param id user
     * @return object UserDtoResponse
     */
    UserDtoResponse getUserById(Long id);


    /**
     * Map from User to UserDtoResponse
     *
     * @param user оbject User
     * @return оbject DtoResponse
     */
    UserDtoResponse mapToUserDto(User user);

    /**
     * Map from UserDtoRequest to User
     *
     * @param userDtoRequest оbject userDtoRequest
     * @return object User
     */
    User mapToUser(UserDtoRequest userDtoRequest);
}
