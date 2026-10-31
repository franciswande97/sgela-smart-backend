package com.app.sgelasmart.api.dashboard.dto;

import com.app.sgelasmart.common.TaskType;
import java.time.LocalDate;
import java.time.LocalTime;

public record DashboardResponse(
        String studentName,
        int overallProgressPercentage,
        long totalTasksCount,
        long activeGoalsCount,
        NextUpTaskDto nextUp,
        int streakDays
) {
    public record NextUpTaskDto(
            Long taskId,
            Long goalId,
            String title,
            TaskType taskType,
            String timingText,
            LocalDate scheduledDate,
            LocalTime startTime
    ) {}
}
