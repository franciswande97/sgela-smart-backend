package com.app.sgelasmart.api.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetStudentPasswordRequest(
        @NotBlank @Size(min = 8) String newPassword
) {
}