package com.app.sgelasmart.api.auth.dto;

import com.app.sgelasmart.model.Role;

public record RegisterRequest(
    String email,
    String password,
    String name,
    Role role
) {}
