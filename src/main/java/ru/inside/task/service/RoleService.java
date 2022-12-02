package ru.inside.task.service;

import ru.inside.task.dto.RoleDtoRequest;
import ru.inside.task.dto.RoleDtoResponse;
import ru.inside.task.model.Role;

public interface RoleService {

    /**
     * Create role
     *
     * @param roleDtoRequest - object Role
     * @return object UserRoleDtoResponse
     */
    RoleDtoResponse createRole(RoleDtoRequest roleDtoRequest);

    /**
     * Get role by id
     *
     * @param id role
     * @return object RoleDtoResponse
     */
    RoleDtoResponse getRoleById(Long id);

    /**
     * Get ROLE_USER
     *
     * @return object Role
     */
    Role getRoleUser();

    /**
     * Map from Role to RoleDtoResponse
     *
     * @param role оbject Role
     * @return оbject RoleDtoResponse
     */
    RoleDtoResponse mapToRoleDto(Role role);

    /**
     * Map from RoletoRequest to Role
     *
     * @param roleDtoRequest оbject RoleDtoRequest
     * @return object Role
     */
    Role mapToRole(RoleDtoRequest roleDtoRequest);
}
