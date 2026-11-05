package com.app.sgelasmart.api.auth;

import com.app.sgelasmart.api.auth.dto.LoginRequest;
import com.app.sgelasmart.api.auth.dto.LoginResponse;
import com.app.sgelasmart.api.auth.dto.RegisterRequest;
import com.app.sgelasmart.api.auth.dto.RegisterResponse;
import com.app.sgelasmart.api.auth.dto.UserResponse;
import com.app.sgelasmart.model.Role;
import com.app.sgelasmart.model.User;
import com.app.sgelasmart.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.app.sgelasmart.exception.EmailAlreadyRegisteredException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import java.util.Locale;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public RegisterResponse register(RegisterRequest request) {
        // Emails are stored trimmed and lower-case, so "Ann@x.com" and "ann@x.com" are the same account.
        String email = normalizeEmail(request.email());
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyRegisteredException("Email is already registered");
        }
        Role assignedRole = request.role() != null
                ? request.role()
                : Role.ROLE_STUDENT;
        String hashedPassword = passwordEncoder.encode(request.password());

        User user = new User(email, hashedPassword, request.name().trim(), assignedRole);
        User savedUser;
        try {
            savedUser = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            // Two registrations with the same email at the same moment: the database UNIQUE constraint wins.
            throw new EmailAlreadyRegisteredException("Email is already registered");
        }

        String token = jwtService.generateToken(savedUser.getEmail(), savedUser.getRole().name());

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getName(),
                savedUser.getRole(),
                token,
                "User registered successfully"
        );
    }

    public LoginResponse login(LoginRequest request) {
        // Same message for "no such email" and "wrong password" so the API does not reveal which emails exist.
        User user = userRepository.findByEmail(normalizeEmail(request.email()))
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        if (request.role() != null && user.getRole() != request.role()) {
            throw new IllegalArgumentException("Access denied: Account role (" + user.getRole().name() + ") does not match requested role (" + request.role().name() + ")");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        return new LoginResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                token
        );
    }

    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole()
        );
    }
    
    private static String normalizeEmail(String email) {
    return email.trim().toLowerCase(Locale.ROOT);
    }
}
