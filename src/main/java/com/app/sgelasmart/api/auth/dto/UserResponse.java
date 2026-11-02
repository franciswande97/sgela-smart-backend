package com.app.sgelasmart.api.auth.dto;

import com.app.sgelasmart.model.Role;

public record UserResponse(
    Long id,
    String email,
    String name,
    Role role
) {}
