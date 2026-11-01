package com.app.sgelasmart.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_attempts")
public class QuizAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "task_id", nullable = false)
    private Long taskId;

    @Column(nullable = false)
    private int score;

    @Column(name = "total_questions", nullable = false)
    private int totalQuestions;

    @Column(nullable = false)
    private double percentage;

    @Column(name = "attempted_at", nullable = false)
    private LocalDateTime attemptedAt = LocalDateTime.now();

    protected QuizAttempt() {}

    public QuizAttempt(Long userId, Long taskId, int score, int totalQuestions, double percentage) {
        this.userId = userId;
        this.taskId = taskId;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.percentage = percentage;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getTaskId() { return taskId; }
    public int getScore() { return score; }
    public int getTotalQuestions() { return totalQuestions; }
    public double getPercentage() { return percentage; }
    public LocalDateTime getAttemptedAt() { return attemptedAt; }
}