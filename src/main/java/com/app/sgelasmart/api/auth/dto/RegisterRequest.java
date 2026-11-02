package com.app.sgelasmart.api.auth.dto;

<<<<<<< HEAD
import com.app.sgelasmart.model.Role;
=======
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
>>>>>>> e71a004 (WIP:working - VALIDATION WORK: WIP CHECKPOINT (backend)  Why: the backend accepted bad input silently or crashed with 500s. We're fixing it in 7 small points, each tested in Swagger with seed data before the next.  STATUS 1. Validation switched on + one global error handler ... applied 2. Login and registration rules ................ applied 3. Field rules (required, max lengths, ranges) . applied, fixing compile errors 4. Cross-field rules (event end after start,    focus session task/goal match) .............. applied, fixing compile errors 5. Quiz submission checks ...................... applied, fixing compile errors 6. File upload checks .......................... not started 7. Check AI-generated quiz output .............. not started)

public record RegisterRequest(
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    @Size(max = 255, message = "Email must be at most 255 characters")
    String email,

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).*$", message = "Password must contain at least one letter and one number")
    String password,
<<<<<<< HEAD
    String name,
    Role role
) {}
=======

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be at most 100 characters")
    String name
) {}
>>>>>>> e71a004 (WIP:working - VALIDATION WORK: WIP CHECKPOINT (backend)  Why: the backend accepted bad input silently or crashed with 500s. We're fixing it in 7 small points, each tested in Swagger with seed data before the next.  STATUS 1. Validation switched on + one global error handler ... applied 2. Login and registration rules ................ applied 3. Field rules (required, max lengths, ranges) . applied, fixing compile errors 4. Cross-field rules (event end after start,    focus session task/goal match) .............. applied, fixing compile errors 5. Quiz submission checks ...................... applied, fixing compile errors 6. File upload checks .......................... not started 7. Check AI-generated quiz output .............. not started)
