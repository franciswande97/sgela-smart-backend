package com.app.sgelasmart.api.settings.dto;

import java.time.LocalTime;

public class UserSettingsRequest {

    private Integer quizQuestionCount;
    private Double quizPassingScorePercentage;
    private Integer focusDurationMinutes;
    private Integer breakDurationMinutes;
    private LocalTime rescheduleWindowStart;
    private LocalTime rescheduleWindowEnd;
    private Boolean allowWeekendReschedule;
    private Integer examBufferDays;

    public Integer getQuizQuestionCount() { return quizQuestionCount; }
    public void setQuizQuestionCount(Integer quizQuestionCount) { this.quizQuestionCount = quizQuestionCount; }

    public Double getQuizPassingScorePercentage() { return quizPassingScorePercentage; }
    public void setQuizPassingScorePercentage(Double quizPassingScorePercentage) { this.quizPassingScorePercentage = quizPassingScorePercentage; }

    public Integer getFocusDurationMinutes() { return focusDurationMinutes; }
    public void setFocusDurationMinutes(Integer focusDurationMinutes) { this.focusDurationMinutes = focusDurationMinutes; }

    public Integer getBreakDurationMinutes() { return breakDurationMinutes; }
    public void setBreakDurationMinutes(Integer breakDurationMinutes) { this.breakDurationMinutes = breakDurationMinutes; }

    public LocalTime getRescheduleWindowStart() { return rescheduleWindowStart; }
    public void setRescheduleWindowStart(LocalTime rescheduleWindowStart) { this.rescheduleWindowStart = rescheduleWindowStart; }

    public LocalTime getRescheduleWindowEnd() { return rescheduleWindowEnd; }
    public void setRescheduleWindowEnd(LocalTime rescheduleWindowEnd) { this.rescheduleWindowEnd = rescheduleWindowEnd; }

    public Boolean getAllowWeekendReschedule() { return allowWeekendReschedule; }
    public void setAllowWeekendReschedule(Boolean allowWeekendReschedule) { this.allowWeekendReschedule = allowWeekendReschedule; }

    public Integer getExamBufferDays() { return examBufferDays; }
    public void setExamBufferDays(Integer examBufferDays) { this.examBufferDays = examBufferDays; }
}
