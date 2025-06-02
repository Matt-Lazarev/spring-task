package com.selivanov.service;

import com.selivanov.dto.UserDto;
import com.selivanov.entity.Role;
import com.selivanov.exception.NoSuchEntityException;
import com.selivanov.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    @Transactional
    public void saveRole(String name) {
        Role role = new Role();
        role.setName(name);

        roleRepository.save(role);
    }

    @Transactional
    public Role findRoleByName(String role) {
        return roleRepository.findRoleByName(role)
                .orElseThrow(() -> new NoSuchEntityException("Role with name: %s not found".formatted(role)));
    }
}
