package ru.inside.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.inside.task.model.Role;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoRequest {
    private String name;
    private String password;
    private Set<Role> roles;
}
