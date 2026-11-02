package com.app.sgelasmart.api.auth.dto;

import com.app.sgelasmart.model.Role;

public record LoginRequest(
    String email,
    String password,
    Role role
) {}
