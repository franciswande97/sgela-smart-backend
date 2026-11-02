package com.app.sgelasmart.api.auth.dto;

<<<<<<< HEAD
import com.app.sgelasmart.model.Role;
=======
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
>>>>>>> e71a004 (WIP:working - VALIDATION WORK: WIP CHECKPOINT (backend)  Why: the backend accepted bad input silently or crashed with 500s. We're fixing it in 7 small points, each tested in Swagger with seed data before the next.  STATUS 1. Validation switched on + one global error handler ... applied 2. Login and registration rules ................ applied 3. Field rules (required, max lengths, ranges) . applied, fixing compile errors 4. Cross-field rules (event end after start,    focus session task/goal match) .............. applied, fixing compile errors 5. Quiz submission checks ...................... applied, fixing compile errors 6. File upload checks .......................... not started 7. Check AI-generated quiz output .............. not started)

public record LoginRequest(
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    @Size(max = 255, message = "Email must be at most 255 characters")
    String email,
<<<<<<< HEAD
    String password,
    Role role
) {}
=======

    // No minimum length or complexity rule on purpose: login must accept whatever the account was created with.
    // 72 is bcrypt's limit - longer passwords are not supported by the hashing algorithm.
    @NotBlank(message = "Password is required")
    @Size(max = 72, message = "Password must be at most 72 characters")
    String password
) {}
>>>>>>> e71a004 (WIP:working - VALIDATION WORK: WIP CHECKPOINT (backend)  Why: the backend accepted bad input silently or crashed with 500s. We're fixing it in 7 small points, each tested in Swagger with seed data before the next.  STATUS 1. Validation switched on + one global error handler ... applied 2. Login and registration rules ................ applied 3. Field rules (required, max lengths, ranges) . applied, fixing compile errors 4. Cross-field rules (event end after start,    focus session task/goal match) .............. applied, fixing compile errors 5. Quiz submission checks ...................... applied, fixing compile errors 6. File upload checks .......................... not started 7. Check AI-generated quiz output .............. not started)
