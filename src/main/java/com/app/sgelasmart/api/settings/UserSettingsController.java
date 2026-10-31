package com.app.sgelasmart.api.settings;

import com.app.sgelasmart.api.settings.dto.UserSettingsRequest;
import com.app.sgelasmart.api.settings.dto.UserSettingsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{user_id}/settings")
public class UserSettingsController {

    private final UserSettingsService settingsService;

    public UserSettingsController(UserSettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping
    public ResponseEntity<UserSettingsResponse> getSettings(
            @PathVariable("user_id") Long userId,
            Authentication authentication
    ) {
        UserSettingsResponse response = settingsService.getSettingsByUserId(userId, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @PatchMapping
    public ResponseEntity<UserSettingsResponse> patchSettings(
            @PathVariable("user_id") Long userId,
            @RequestBody UserSettingsRequest request,
            Authentication authentication
    ) {
        UserSettingsResponse response = settingsService.patchSettings(userId, request, authentication.getName());
        return ResponseEntity.ok(response);
    }
}
