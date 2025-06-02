package com.selivanov.service;

import com.selivanov.dto.UserDto;
import com.selivanov.dto.security.PasswordChangeRequest;
import com.selivanov.dto.security.PasswordChangeResponse;
import com.selivanov.entity.ApplicationUser;
import com.selivanov.entity.Role;
import com.selivanov.exception.NoSuchEntityException;
import com.selivanov.repository.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ApplicationUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserDto findAuthenticatedUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findUserByUsername(username)
                .map(user -> new UserDto(
                        username,
                        null,
                        user.getEmail(),
                        getUserRole(authentication.getAuthorities())
                ))
                .orElseThrow(() -> new NoSuchEntityException("User with username = %s not found".formatted(username)));
    }

    @Transactional
    public void createUser(UserDto userDto) {
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setUsername(userDto.username());
        applicationUser.setRoles(Set.of(roleService.findRoleByName(userDto.role())));
        applicationUser.setPassword(passwordEncoder.encode(userDto.password()));
        applicationUser.setEmail(userDto.email());
        userRepository.save(applicationUser);
    }

    @Transactional
    public PasswordChangeResponse updatePassword(Integer id, PasswordChangeRequest request) { //PasswordChangeRequest (String oldPassword, String newPassword)
        ApplicationUser applicationUser = userRepository.findById(id).orElseThrow(() ->
                new NoSuchEntityException("User with id = %d not found".formatted(id)));

        boolean matches = passwordEncoder.matches(request.oldPassword(), applicationUser.getPassword());
        if(matches) {
            applicationUser.setPassword(passwordEncoder.encode(request.newPassword()));
            userRepository.save(applicationUser);
        }

        return new PasswordChangeResponse("Password change", matches);
    }

    @Transactional
    public void addRoleByUserId(Integer id, UserDto userDto) {
        checkUserExists(id);
        Role role = roleService.findRoleByName(userDto.role());

        userRepository.addRoleToUser(id, role.getId());
    }

    @Transactional
    public void deleteRoleByUserId(Integer userId, String roleName) {
        checkUserExists(userId);
        Role role = roleService.findRoleByName(roleName);

        userRepository.removeRoleFromUser(userId, role.getId());
    }

    @Transactional
    public void blockUser(Integer id) {
        userRepository.findById(id).ifPresent(
                applicationUser -> applicationUser.setBlocked(true)
        );
    }

    @Transactional
    public void unblockUser(Integer id) {
        userRepository.findById(id).ifPresent(
                applicationUser -> applicationUser.setBlocked(false)
        );
    }

    private String getUserRole(Collection<? extends GrantedAuthority> grantedAuthorities) {
        List<? extends GrantedAuthority> authorities = grantedAuthorities.stream().toList();
        return authorities.get(0).getAuthority();
    }

    private void checkUserExists(Integer userId) {
        if(!userRepository.existsById(userId)) {
            throw new NoSuchEntityException("User with id = %d not found".formatted(userId));
        }
    }
}