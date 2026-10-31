package com.app.sgelasmart.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "user_settings")
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "quiz_question_count", nullable = false)
    private Integer quizQuestionCount = 6;

    @Column(name = "quiz_passing_score_percentage", nullable = false)
    private Double quizPassingScorePercentage = 60.0;

    @Column(name = "focus_duration_minutes", nullable = false)
    private Integer focusDurationMinutes = 45;

    @Column(name = "break_duration_minutes", nullable = false)
    private Integer breakDurationMinutes = 10;

    @Column(name = "reschedule_window_start", nullable = false)
    private LocalTime rescheduleWindowStart = LocalTime.of(8, 0);

    @Column(name = "reschedule_window_end", nullable = false)
    private LocalTime rescheduleWindowEnd = LocalTime.of(21, 0);

    @Column(name = "allow_weekend_reschedule", nullable = false)
    private Boolean allowWeekendReschedule = true;

    @Column(name = "exam_buffer_days", nullable = false)
    private Integer examBufferDays = 3;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public UserSettings() {}

    public UserSettings(Long userId) {
        this.userId = userId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

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

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
