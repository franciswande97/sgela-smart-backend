package com.app.sgelasmart.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.app.sgelasmart.common.GoalStage;
import com.app.sgelasmart.common.TaskLifecycleState;
import com.app.sgelasmart.common.TaskPriority;
import com.app.sgelasmart.common.TaskType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "goal_id", nullable = false)
    private Long goalId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "lifecycle_state", nullable = false)
    private TaskLifecycleState lifecycleState = TaskLifecycleState.TODO;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_type", nullable = false)
    private TaskType taskType = TaskType.CHECKLIST;

    @Enumerated(EnumType.STRING)
    @Column(name = "goal_stage")
    private GoalStage goalStage = GoalStage.CUSTOM;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private TaskPriority priority = TaskPriority.MEDIUM;

    @Column(name = "content_payload", columnDefinition = "TEXT")
    private String contentPayload;

    @Column(name = "scheduled_date")
    private LocalDate scheduledDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "estimated_duration_minutes")
    private Integer estimatedDurationMinutes;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public Task() {}

    public Task(Long goalId, Long userId, String title, String description,
                TaskLifecycleState lifecycleState, TaskType taskType, String contentPayload,
                LocalDate dueDate, Integer estimatedDurationMinutes) {
        this.goalId = goalId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.lifecycleState = lifecycleState != null ? lifecycleState : TaskLifecycleState.TODO;
        this.taskType = taskType != null ? taskType : TaskType.CHECKLIST;
        this.contentPayload = contentPayload;
        this.dueDate = dueDate;
        this.estimatedDurationMinutes = estimatedDurationMinutes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getGoalId() { return goalId; }
    public void setGoalId(Long goalId) { this.goalId = goalId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public TaskLifecycleState getLifecycleState() { return lifecycleState; }
    public void setLifecycleState(TaskLifecycleState lifecycleState) { this.lifecycleState = lifecycleState; }
    public TaskType getTaskType() { return taskType; }
    public void setTaskType(TaskType taskType) { this.taskType = taskType; }
    public GoalStage getGoalStage() { return goalStage != null ? goalStage : GoalStage.CUSTOM; }
    public void setGoalStage(GoalStage goalStage) { this.goalStage = goalStage != null ? goalStage : GoalStage.CUSTOM; }
    public TaskPriority getPriority() { return priority != null ? priority : TaskPriority.MEDIUM; }
    public void setPriority(TaskPriority priority) { this.priority = priority != null ? priority : TaskPriority.MEDIUM; }
    public String getContentPayload() { return contentPayload; }
    public void setContentPayload(String contentPayload) { this.contentPayload = contentPayload; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public Integer getEstimatedDurationMinutes() { return estimatedDurationMinutes; }
    public void setEstimatedDurationMinutes(Integer estimatedDurationMinutes) { this.estimatedDurationMinutes = estimatedDurationMinutes; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public LocalDate getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDate scheduledDate) { this.scheduledDate = scheduledDate; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
}
