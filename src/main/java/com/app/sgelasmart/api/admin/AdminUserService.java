package com.app.sgelasmart.api.admin;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.sgelasmart.api.auth.UserRepository;
import com.app.sgelasmart.common.UserRole;
import com.app.sgelasmart.model.User;
import com.app.sgelasmart.model.Role;

@Service
public class AdminUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void resetStudentPassword(Long studentId, String newPassword) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        if (student.getRole() != Role.ROLE_STUDENT) {
            throw new IllegalArgumentException("Password reset is only available for student accounts");
        }
        student.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(student);
    }
}