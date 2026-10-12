package com.nutrichef.controller;

import com.nutrichef.dto.UserProfileDto;
import com.nutrichef.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserProfileDto> getProfile() {
        UserProfileDto profile = userService.getProfile();
        return ResponseEntity.ok(profile);
    }

    @PutMapping
    public ResponseEntity<UserProfileDto> updateProfile(@Valid @RequestBody UserProfileDto dto) {
        UserProfileDto updated = userService.updateProfile(dto);
        return ResponseEntity.ok(updated);
    }
}
