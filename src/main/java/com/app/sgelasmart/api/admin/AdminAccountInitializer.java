package com.app.sgelasmart.api.admin;

import com.app.sgelasmart.api.auth.UserRepository;
import com.app.sgelasmart.common.UserRole;
import com.app.sgelasmart.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminAccountInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String email;
    private final String password;
    private final String name;

    public AdminAccountInitializer(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.email:}") String email,
            @Value("${app.admin.password:}") String password,
            @Value("${app.admin.name:Administrator}") String name) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (email.isBlank() && password.isBlank()) {
            return;
        }
        if (email.isBlank() || password.isBlank()) {
            throw new IllegalStateException("Both app.admin.email and app.admin.password must be configured");
        }

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newAdmin = new User(email, passwordEncoder.encode(password), name);
            newAdmin.setRole(UserRole.ADMIN);
            return newAdmin;
        });
        if (user.getRole() != UserRole.ADMIN) {
            user.setRole(UserRole.ADMIN);
        }
        userRepository.save(user);
    }
}