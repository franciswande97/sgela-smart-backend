package com.app.sgelasmart.api.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.sgelasmart.api.admin.dto.ResetStudentPasswordRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/students")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @PostMapping("/{studentId}/reset-password")
    public ResponseEntity<Void> resetStudentPassword(
            @PathVariable Long studentId,
            @Valid @RequestBody ResetStudentPasswordRequest request) {
        adminUserService.resetStudentPassword(studentId, request.newPassword());
        return ResponseEntity.noContent().build();
    }
}