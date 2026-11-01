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
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email is already registered");
        }
<<<<<<< HEAD

        Role assignedRole = (request.role() != null) ? request.role() : Role.ROLE_STUDENT;
=======
>>>>>>> 5ec6bd2 (WIP:working - feat(admin): add analytics dashboard API)
        String hashedPassword = passwordEncoder.encode(request.password());
        User user = new User(request.email(), hashedPassword, request.name(), assignedRole);
        User savedUser = userRepository.save(user);

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
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
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
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole()
        );
    }
}
