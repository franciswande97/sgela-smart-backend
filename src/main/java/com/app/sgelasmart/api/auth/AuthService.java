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
<<<<<<< HEAD

        Role assignedRole = (request.role() != null) ? request.role() : Role.ROLE_STUDENT;
=======
>>>>>>> 5ec6bd2 (WIP:working - feat(admin): add analytics dashboard API)
        String hashedPassword = passwordEncoder.encode(request.password());
<<<<<<< HEAD
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
=======
        User user = new User(email, hashedPassword, request.name().trim());
        User savedUser;
        try {
            savedUser = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            // Two registrations with the same email at the same moment: the database's UNIQUE constraint wins.
            throw new EmailAlreadyRegisteredException("Email is already registered");
        }
        String token = jwtService.generateToken(savedUser.getEmail());
        
        return new RegisterResponse(
            savedUser.getId(),
            savedUser.getEmail(),
            savedUser.getName(),
            token,
            "User registered successfully"
>>>>>>> e71a004 (WIP:working - VALIDATION WORK: WIP CHECKPOINT (backend)  Why: the backend accepted bad input silently or crashed with 500s. We're fixing it in 7 small points, each tested in Swagger with seed data before the next.  STATUS 1. Validation switched on + one global error handler ... applied 2. Login and registration rules ................ applied 3. Field rules (required, max lengths, ranges) . applied, fixing compile errors 4. Cross-field rules (event end after start,    focus session task/goal match) .............. applied, fixing compile errors 5. Quiz submission checks ...................... applied, fixing compile errors 6. File upload checks .......................... not started 7. Check AI-generated quiz output .............. not started)
        );

    }

    public LoginResponse login(LoginRequest request) {
        // Same message for "no such email" and "wrong password" so the API does not reveal which emails exist.
        User user = userRepository.findByEmail(normalizeEmail(request.email()))
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }
<<<<<<< HEAD

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
=======
        String token = jwtService.generateToken(user.getEmail());
        return new LoginResponse(
            user.getId(),
            user.getEmail(),
            user.getName(),
            token
>>>>>>> e71a004 (WIP:working - VALIDATION WORK: WIP CHECKPOINT (backend)  Why: the backend accepted bad input silently or crashed with 500s. We're fixing it in 7 small points, each tested in Swagger with seed data before the next.  STATUS 1. Validation switched on + one global error handler ... applied 2. Login and registration rules ................ applied 3. Field rules (required, max lengths, ranges) . applied, fixing compile errors 4. Cross-field rules (event end after start,    focus session task/goal match) .............. applied, fixing compile errors 5. Quiz submission checks ...................... applied, fixing compile errors 6. File upload checks .......................... not started 7. Check AI-generated quiz output .............. not started)
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
