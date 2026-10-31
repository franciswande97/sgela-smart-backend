/*
package com.app.sgelasmart.model;

import com.app.sgelasmart.common.GoalStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "goals")
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoalStatus status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Goal() {}

    public Goal(Long userId, String title, LocalDate deadline, GoalStatus status) {
        this.userId = userId;
        this.title = title;
        this.deadline = deadline;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public GoalStatus getStatus() {
        return status;
    }

    public void setStatus(GoalStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
*/

package com.app.sgelasmart.model;

import com.app.sgelasmart.common.GoalStatus;
import com.app.sgelasmart.common.GoalStage;
import com.app.sgelasmart.common.TemplateType;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "goals")
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoalStatus status;

    @Enumerated(EnumType.STRING)
    @Column
    private GoalStage currentStage = GoalStage.NOT_STARTED;

    @Enumerated(EnumType.STRING)
    @Column
    private TemplateType template = TemplateType.CUSTOM_BARE;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Goal() {}

    public Goal(Long userId, String title, LocalDate deadline, GoalStatus status) {
        this.userId = userId;
        this.title = title;
        this.deadline = deadline;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public GoalStatus getStatus() {
        return status;
    }

    public void setStatus(GoalStatus status) {
        this.status = status;
    }

    public TemplateType getTemplate() {
        return template != null ? template : TemplateType.CUSTOM_BARE;
    }

    public void setTemplate(TemplateType template) {
        this.template = template != null ? template : TemplateType.CUSTOM_BARE;
    }

    public GoalStage getCurrentStage() {
        return currentStage != null ? currentStage : GoalStage.NOT_STARTED;
    }

    public void setCurrentStage(GoalStage currentStage) {
        this.currentStage = currentStage != null ? currentStage : GoalStage.NOT_STARTED;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
