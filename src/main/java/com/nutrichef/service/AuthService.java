package com.nutrichef.service;

import com.nutrichef.config.JwtTokenProvider;
import com.nutrichef.dto.AuthRequest;
import com.nutrichef.dto.AuthResponse;
import com.nutrichef.dto.RegisterRequest;
import com.nutrichef.entity.User;
import com.nutrichef.exception.BadRequestException;
import com.nutrichef.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider tokenProvider,
                       UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }

        User user = new User(
                request.getEmail().toLowerCase().trim(),
                passwordEncoder.encode(request.getPassword()),
                request.getFullName().trim()
        );

        if (request.getDietaryPreference() != null) {
            user.setDietaryPreference(request.getDietaryPreference());
        }
        if (request.getDailyCalorieTarget() != null) {
            user.setDailyCalorieTarget(request.getDailyCalorieTarget());
        }

        User saved = userRepository.save(user);
        String token = tokenProvider.generateToken(saved.getEmail(), saved.getId());

        return new AuthResponse(token, userService.mapToProfileDto(saved));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String token = tokenProvider.generateToken(user.getEmail(), user.getId());
        return new AuthResponse(token, userService.mapToProfileDto(user));
    }
}
