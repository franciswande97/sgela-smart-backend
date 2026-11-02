package com.app.sgelasmart.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    ROLE_STUDENT,
    ROLE_ADMIN;

    @JsonCreator
    public static Role fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return ROLE_STUDENT;
        }
        String normalized = value.trim().toUpperCase();
        if (normalized.equals("STUDENT") || normalized.equals("ROLE_STUDENT")) {
            return ROLE_STUDENT;
        }
        if (normalized.equals("ADMIN") || normalized.equals("ROLE_ADMIN")) {
            return ROLE_ADMIN;
        }
        throw new IllegalArgumentException("Unknown role: " + value + ". Allowed: STUDENT, ADMIN");
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}
