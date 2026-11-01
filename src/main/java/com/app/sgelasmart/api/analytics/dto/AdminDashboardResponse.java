package com.app.sgelasmart.api.analytics.dto;

import com.app.sgelasmart.api.analytics.DatePreset;
import com.app.sgelasmart.common.TemplateType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record AdminDashboardResponse(
        DateRange dateRange,
        Filters filters,
        Summary summary,
        List<DailyFocusHours> dailyFocusHours,
        List<TemplateCount> goalsByTemplate,
        BacklogHealth backlogHealth,
        List<ComprehensionPoint> comprehensionTrend,
        List<StudentAttention> attentionNeeded
) {
    public record DateRange(DatePreset preset, LocalDate start, LocalDate end) {}
    public record Filters(TemplateType template, String search) {}
    public record Summary(long activeStudents, double totalFocusHours,
                          double completionRatePercentage, long activeExamGoals) {}
    public record DailyFocusHours(LocalDate date, double hours) {}
    public record TemplateCount(TemplateType template, long count) {}
    public record BacklogHealth(long completed, long inProgress, long overdue, long toDo) {}
    public record ComprehensionPoint(LocalDate date, double averageScorePercentage) {}
        public record StudentAttention(Long userId, String studentName,
                                   String activeGoal, LocalDateTime lastActive,
                                   LocalDate targetDeadline, long overdueTasks) {}
}