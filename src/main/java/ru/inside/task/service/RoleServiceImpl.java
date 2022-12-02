package ru.inside.task.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.inside.task.dto.RoleDtoRequest;
import ru.inside.task.dto.RoleDtoResponse;
import ru.inside.task.exception.BadRequestException;
import ru.inside.task.exception.NotFoundException;
import ru.inside.task.model.Role;
import ru.inside.task.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {

    private static final int MAX_LENGTH_NAME = 150;
    private static final int MAX_LENGTH_DESCRIPTION = 255;

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleDtoResponse createRole(RoleDtoRequest roleDtoRequest) {
        if (roleDtoRequest == null) {
            throw new BadRequestException("Invalid role");
        }
        if (!StringUtils.isNotBlank(roleDtoRequest.getName())) {
            throw new BadRequestException("Invalid role's name");
        }
        if (roleDtoRequest.getName().length() > MAX_LENGTH_NAME) {
            throw new BadRequestException("Too long role's name");
        }
        if (!StringUtils.isNotBlank(roleDtoRequest.getSlug())) {
            throw new BadRequestException("Invalid role's slug");
        }
        if (roleDtoRequest.getSlug().length() > MAX_LENGTH_NAME) {
            throw new BadRequestException("Too long role's slug");
        }
        if (!StringUtils.isNotBlank(roleDtoRequest.getDescription())) {
            throw new BadRequestException("Invalid role's description");
        }
        if (roleDtoRequest.getSlug().length() > MAX_LENGTH_DESCRIPTION) {
            throw new BadRequestException("Too long role's description");
        }
        return mapToRoleDto(roleRepository.save(mapToRole(roleDtoRequest)));
    }

    @Override
    public RoleDtoResponse getRoleById(Long id) {
        if (id == null) {
            throw new BadRequestException("Invalid ID");
        }
        return mapToRoleDto(roleRepository.findById(id).orElseThrow(()
                -> new NotFoundException("Role not found!")));
    }

    @Override
    public Role getRoleUser() {
        Role roleUserFromDB = roleRepository.findByName("ROLE_USER");
        if (roleUserFromDB == null) {
            return roleRepository.save(new Role(1L, "ROLE_USER", "user", "Role for simple user"));
        }
        return roleUserFromDB;
    }

    //из entity в dto
    @Override
    public RoleDtoResponse mapToRoleDto(Role role) {
        RoleDtoResponse roleDtoResponse = new RoleDtoResponse();
        //4 column
        roleDtoResponse.setId(role.getId());
        roleDtoResponse.setName(role.getName());
        roleDtoResponse.setSlug(role.getSlug());
        roleDtoResponse.setDescription(role.getDescription());
        return roleDtoResponse;
    }

    //из dto в entity
    @Override
    public Role mapToRole(RoleDtoRequest roleDtoRequest) {
        Role role = new Role();
        //3 column
        role.setName(roleDtoRequest.getName());
        role.setSlug(roleDtoRequest.getSlug());
        role.setDescription(roleDtoRequest.getDescription());
        return role;
    }
}