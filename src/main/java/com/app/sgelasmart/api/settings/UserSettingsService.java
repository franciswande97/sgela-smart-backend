package com.app.sgelasmart.api.settings;

import com.app.sgelasmart.api.auth.UserRepository;
import com.app.sgelasmart.api.settings.dto.UserSettingsRequest;
import com.app.sgelasmart.api.settings.dto.UserSettingsResponse;
import com.app.sgelasmart.model.User;
import com.app.sgelasmart.model.UserSettings;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class UserSettingsService {

    private final UserSettingsRepository settingsRepository;
    private final UserRepository userRepository;

    public UserSettingsService(UserSettingsRepository settingsRepository, UserRepository userRepository) {
        this.settingsRepository = settingsRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public UserSettingsResponse getSettingsByUserId(Long targetUserId, String authenticatedEmail) {
        verifyUserOwnership(targetUserId, authenticatedEmail);
        UserSettings settings = getOrCreateSettings(targetUserId);
        return mapToResponse(settings);
    }

    @Transactional
    public UserSettingsResponse patchSettings(Long targetUserId, UserSettingsRequest request, String authenticatedEmail) {
        verifyUserOwnership(targetUserId, authenticatedEmail);
        UserSettings settings = getOrCreateSettings(targetUserId);

        if (request.getQuizQuestionCount() != null) {
            if (request.getQuizQuestionCount() < 3 || request.getQuizQuestionCount() > 20) {
                throw new IllegalArgumentException("Quiz question count must be between 3 and 20");
            }
            settings.setQuizQuestionCount(request.getQuizQuestionCount());
        }

        if (request.getQuizPassingScorePercentage() != null) {
            if (request.getQuizPassingScorePercentage() < 50.0 || request.getQuizPassingScorePercentage() > 90.0) {
                throw new IllegalArgumentException("Quiz passing score percentage must be between 50.0 and 90.0");
            }
            settings.setQuizPassingScorePercentage(request.getQuizPassingScorePercentage());
        }

        if (request.getFocusDurationMinutes() != null) {
            if (request.getFocusDurationMinutes() < 15 || request.getFocusDurationMinutes() > 90) {
                throw new IllegalArgumentException("Focus duration minutes must be between 15 and 90");
            }
            settings.setFocusDurationMinutes(request.getFocusDurationMinutes());
        }

        if (request.getBreakDurationMinutes() != null) {
            if (request.getBreakDurationMinutes() < 5 || request.getBreakDurationMinutes() > 30) {
                throw new IllegalArgumentException("Break duration minutes must be between 5 and 30");
            }
            settings.setBreakDurationMinutes(request.getBreakDurationMinutes());
        }

        if (settings.getBreakDurationMinutes() >= settings.getFocusDurationMinutes()) {
            throw new IllegalArgumentException("Break duration cannot be greater than or equal to focus duration");
        }

        LocalTime newStart = request.getRescheduleWindowStart() != null ? request.getRescheduleWindowStart() : settings.getRescheduleWindowStart();
        LocalTime newEnd = request.getRescheduleWindowEnd() != null ? request.getRescheduleWindowEnd() : settings.getRescheduleWindowEnd();

        if (!newStart.isBefore(newEnd)) {
            throw new IllegalArgumentException("Reschedule window start time must be before end time");
        }

        if (request.getRescheduleWindowStart() != null) {
            settings.setRescheduleWindowStart(request.getRescheduleWindowStart());
        }
        if (request.getRescheduleWindowEnd() != null) {
            settings.setRescheduleWindowEnd(request.getRescheduleWindowEnd());
        }

        if (request.getAllowWeekendReschedule() != null) {
            settings.setAllowWeekendReschedule(request.getAllowWeekendReschedule());
        }

        if (request.getExamBufferDays() != null) {
            if (request.getExamBufferDays() < 0 || request.getExamBufferDays() > 7) {
                throw new IllegalArgumentException("Exam buffer days must be between 0 and 7");
            }
            settings.setExamBufferDays(request.getExamBufferDays());
        }

        settings.setUpdatedAt(LocalDateTime.now());
        UserSettings saved = settingsRepository.save(settings);
        return mapToResponse(saved);
    }

    public UserSettings getOrCreateSettings(Long userId) {
        return settingsRepository.findByUserId(userId)
                .orElseGet(() -> settingsRepository.save(new UserSettings(userId)));
    }

    private void verifyUserOwnership(Long targetUserId, String authenticatedEmail) {
        User user = userRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new AccessDeniedException("User not found or unauthenticated"));
        if (!user.getId().equals(targetUserId)) {
            throw new AccessDeniedException("Forbidden: You cannot access settings for another user");
        }
    }

    private UserSettingsResponse mapToResponse(UserSettings s) {
        return new UserSettingsResponse(
                s.getUserId(),
                s.getQuizQuestionCount(),
                s.getQuizPassingScorePercentage(),
                s.getFocusDurationMinutes(),
                s.getBreakDurationMinutes(),
                s.getRescheduleWindowStart(),
                s.getRescheduleWindowEnd(),
                s.getAllowWeekendReschedule(),
                s.getExamBufferDays(),
                s.getUpdatedAt()
        );
    }
}
