package com.selivanov.controller;

import com.selivanov.dto.UserDto;
import com.selivanov.dto.security.PasswordChangeRequest;
import com.selivanov.dto.security.PasswordChangeResponse;
import com.selivanov.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/authentication-user")
    public ResponseEntity<UserDto> getAuthenticationUser(Authentication authentication) {
        UserDto userDto = userService.findAuthenticatedUser(authentication);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserDto userDto) {
        userService.createUser(userDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/admin/users/save")
    public ResponseEntity<?> save(@RequestBody @Valid UserDto userDto) {
        userService.createUser(userDto);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/admin/users/{id}/update-password")
    public ResponseEntity<PasswordChangeResponse> updatePassword(@PathVariable Integer id, @RequestBody PasswordChangeRequest userDto) {
        PasswordChangeResponse response = userService.updatePassword(id, userDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/admin/users/{id}/update-role")
    public ResponseEntity<Void> addRoleByUserId(@PathVariable Integer id, @RequestBody @Valid UserDto userDto) {
        userService.addRoleByUserId(id, userDto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/admin/users/{userId}/role/{role}/remove")
    public ResponseEntity<Void> deleteRoleByUserId(@PathVariable Integer userId,
                                                   @PathVariable String role) {
        userService.deleteRoleByUserId(userId, role);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/admin/users/{id}/block")
    public ResponseEntity<Void> blockUser(@PathVariable Integer id) {
        userService.blockUser(id);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/admin/users/{id}/unblock")
    public ResponseEntity<Void> unblockUser(@PathVariable Integer id) {
        userService.unblockUser(id);

        return ResponseEntity.ok().build();
    }
}