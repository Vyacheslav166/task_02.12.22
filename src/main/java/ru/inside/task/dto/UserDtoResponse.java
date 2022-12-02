package ru.inside.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.inside.task.model.Role;

import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoResponse {
    private Long id;
    private String name;
    private Set<Role> roles;
}
