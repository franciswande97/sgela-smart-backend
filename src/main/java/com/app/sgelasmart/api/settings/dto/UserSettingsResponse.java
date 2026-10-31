package com.app.sgelasmart.api.settings.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record UserSettingsResponse(
        Long userId,
        Integer quizQuestionCount,
        Double quizPassingScorePercentage,
        Integer focusDurationMinutes,
        Integer breakDurationMinutes,
        LocalTime rescheduleWindowStart,
        LocalTime rescheduleWindowEnd,
        Boolean allowWeekendReschedule,
        Integer examBufferDays,
        LocalDateTime updatedAt
) {}
