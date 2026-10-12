package com.nutrichef.service;

import com.nutrichef.dto.UserProfileDto;
import com.nutrichef.entity.User;
import com.nutrichef.exception.ResourceNotFoundException;
import com.nutrichef.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentAuthenticatedUser() {
        try {
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                String email = SecurityContextHolder.getContext().getAuthentication().getName();
                if (email != null && !email.isBlank() && !"anonymousUser".equalsIgnoreCase(email)) {
                    var userOpt = userRepository.findByEmail(email);
                    if (userOpt.isPresent()) {
                        return userOpt.get();
                    }
                }
            }
        } catch (Exception ignored) {}

        // Fallback to default chef account for guest / initial preview loading
        return userRepository.findByEmail("chef@nutrichef.demo")
                .orElseGet(() -> userRepository.findAll().stream().findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("No user accounts found in database")));
    }

    @Transactional(readOnly = true)
    public UserProfileDto getProfile() {
        User user = getCurrentAuthenticatedUser();
        return mapToProfileDto(user);
    }

    @Transactional
    public UserProfileDto updateProfile(UserProfileDto dto) {
        User user = getCurrentAuthenticatedUser();

        if (dto.getFullName() != null) user.setFullName(dto.getFullName());
        if (dto.getDietaryPreference() != null) user.setDietaryPreference(dto.getDietaryPreference());
        if (dto.getDailyCalorieTarget() != null) user.setDailyCalorieTarget(dto.getDailyCalorieTarget());
        if (dto.getDailyProteinTarget() != null) user.setDailyProteinTarget(dto.getDailyProteinTarget());
        if (dto.getDailyCarbsTarget() != null) user.setDailyCarbsTarget(dto.getDailyCarbsTarget());
        if (dto.getDailyFatTarget() != null) user.setDailyFatTarget(dto.getDailyFatTarget());

        User saved = userRepository.save(user);
        return mapToProfileDto(saved);
    }

    public UserProfileDto mapToProfileDto(User user) {
        return new UserProfileDto(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getDietaryPreference(),
                user.getDailyCalorieTarget(),
                user.getDailyProteinTarget(),
                user.getDailyCarbsTarget(),
                user.getDailyFatTarget()
        );
    }
}
