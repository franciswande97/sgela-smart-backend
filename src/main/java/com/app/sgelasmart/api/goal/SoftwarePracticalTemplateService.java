package com.app.sgelasmart.api.goal;

import com.app.sgelasmart.api.auth.UserRepository;
import com.app.sgelasmart.api.goal.dto.ExternalPromptDto;
import com.app.sgelasmart.api.goal.dto.GoalResponse;
import com.app.sgelasmart.api.goal.dto.GoalTemplateResponse;
import com.app.sgelasmart.api.schedule.TimetableVoidService;
import com.app.sgelasmart.api.settings.UserSettingsRepository;
import com.app.sgelasmart.api.ai.StudyIntelligenceEngine;
import com.app.sgelasmart.model.UserSettings;
import com.app.sgelasmart.api.schedule.dto.TimeSlot;
import com.app.sgelasmart.api.task.TaskRepository;
import com.app.sgelasmart.api.task.dto.TaskResponse;
import com.app.sgelasmart.common.GoalStatus;
import com.app.sgelasmart.common.TaskLifecycleState;
import com.app.sgelasmart.common.TaskType;
import com.app.sgelasmart.common.TemplateType;
import com.app.sgelasmart.model.Goal;
import com.app.sgelasmart.model.GoalMaterial;
import com.app.sgelasmart.model.Task;
import com.app.sgelasmart.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SoftwarePracticalTemplateService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final GoalMaterialRepository goalMaterialRepository;
    private final TimetableVoidService timetableVoidService;
    private final UserSettingsRepository userSettingsRepository;
    private final StudyIntelligenceEngine intelligenceEngine;
    private final ObjectMapper objectMapper;

    public SoftwarePracticalTemplateService(
            GoalRepository goalRepository,
            UserRepository userRepository,
            TaskRepository taskRepository,
            GoalMaterialRepository goalMaterialRepository,
            TimetableVoidService timetableVoidService,
            UserSettingsRepository userSettingsRepository,
            StudyIntelligenceEngine intelligenceEngine,
            ObjectMapper objectMapper) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.goalMaterialRepository = goalMaterialRepository;
        this.timetableVoidService = timetableVoidService;
        this.userSettingsRepository = userSettingsRepository;
        this.intelligenceEngine = intelligenceEngine;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public GoalTemplateResponse applySoftwarePracticalTemplate(Long goalId, String authenticatedEmail) {
        User user = userRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new AccessDeniedException("User not found or unauthenticated"));

        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new NoSuchElementException("Goal not found with id: " + goalId));

        if (!goal.getUserId().equals(user.getId())) {
            throw new AccessDeniedException("Forbidden: You do not own this goal");
        }

        LocalDate startDate = LocalDate.now();
        LocalDate deadline = goal.getDeadline() != null ? goal.getDeadline() : startDate.plusDays(14);

        if (deadline.isBefore(startDate)) {
            throw new IllegalArgumentException("The goal deadline cannot be in the past");
        }

        // Clean sweep stale tasks for this goal
        List<Task> existingTasks = taskRepository.findByGoalId(goalId);
        if (!existingTasks.isEmpty()) {
            taskRepository.deleteAll(existingTasks);
            taskRepository.flush();
        }

        // Inspect goal materials for prompt context
        List<GoalMaterial> materials = goalMaterialRepository.findByGoalIdOrderByCreatedAtDesc(goalId);
        String corpusSummary = materials.stream()
                .filter(m -> m.getExtractedContent() != null && !m.getExtractedContent().isBlank())
                .map(m -> m.getFileName() + ": " + m.getExtractedContent())
                .collect(Collectors.joining("\n"));

        if (corpusSummary.isBlank()) {
            corpusSummary = "Project titled: " + goal.getTitle();
        }

        UserSettings settings = userSettingsRepository.findByUserId(user.getId())
                .orElseGet(() -> new UserSettings(user.getId()));

        int intervalMin = settings.getFocusDurationMinutes() != null ? settings.getFocusDurationMinutes() : 45;
        int breakMin = settings.getBreakDurationMinutes() != null ? settings.getBreakDurationMinutes() : 10;
        int totalTargetMin = intervalMin * 2;

        List<Task> tasksToSchedule = new ArrayList<>();

        // TASK 1: FOCUS_SESSION with interactive milestone checklist objectives
        Map<String, Object> focusPayload = Map.of(
                "targetDurationMinutes", totalTargetMin,
                "intervalMinutes", intervalMin,
                "breakMinutes", breakMin,
                "objectives", List.of(
                        Map.of("id", 1, "text", "Set up project architecture & declare data contracts", "completed", false),
                        Map.of("id", 2, "text", "Implement core practical algorithms & business logic", "completed", false),
                        Map.of("id", 3, "text", "Handle error validation & unexpected edge cases", "completed", false),
                        Map.of("id", 4, "text", "Execute tests & complete deliverable verification", "completed", false)
                )
        );

        String focusPayloadJson;
        try {
            focusPayloadJson = objectMapper.writeValueAsString(focusPayload);
        } catch (Exception e) {
            focusPayloadJson = "{}";
        }

        Task focusTask = new Task(
                goal.getId(),
                user.getId(),
                "Implementation Sprint: " + goal.getTitle(),
                "Focused execution block with trackable milestone objectives.",
                TaskLifecycleState.TODO,
                TaskType.FOCUS_SESSION,
                focusPayloadJson,
                deadline,
                totalTargetMin
        );
        tasksToSchedule.add(focusTask);

        // TASK 2: EXTERNAL_PROMPT (Single targeted architecture & review prompt)
        String promptText = "I am completing a practical project titled '" + goal.getTitle() + "'. Context: " +
                (corpusSummary.length() > 500 ? corpusSummary.substring(0, 500) + "..." : corpusSummary) +
                ". Act as a Senior Software Engineer: 1) Critique my architecture decisions. " +
                "2) Highlight top 3 edge cases to test. 3) Provide unit test scenarios to verify before submission.";

        String promptPayloadJson;
        try {
            promptPayloadJson = objectMapper.writeValueAsString(intelligenceEngine.generatePracticalPrompt(goal.getTitle(), corpusSummary));
        } catch (Exception e) {
            promptPayloadJson = "{}";
        }

        Task promptTask = new Task(
                goal.getId(),
                user.getId(),
                "Architecture & Code Review Prompt",
                "Targeted engineering prompt to audit implementation and edge cases.",
                TaskLifecycleState.TODO,
                TaskType.EXTERNAL_PROMPT,
                promptPayloadJson,
                deadline,
                30
        );
        tasksToSchedule.add(promptTask);

        // Schedule into timetable voids
        List<TimeSlot> slots = new ArrayList<>(timetableVoidService.findFreeVoids(user.getId(), startDate, deadline));
        slots.sort(Comparator.comparing(TimeSlot::date).thenComparing(TimeSlot::startTime));

        for (Task task : tasksToSchedule) {
            int requiredMinutes = task.getEstimatedDurationMinutes() != null ? task.getEstimatedDurationMinutes() : 45;
            for (int i = 0; i < slots.size(); i++) {
                TimeSlot slot = slots.get(i);
                if (slot.durationMinutes() >= requiredMinutes) {
                    LocalTime scheduledEnd = slot.startTime().plusMinutes(requiredMinutes);
                    if (!scheduledEnd.isAfter(slot.endTime())) {
                        task.setScheduledDate(slot.date());
                        task.setStartTime(slot.startTime());
                        task.setEndTime(scheduledEnd);

                        slots.remove(i);
                        int remaining = slot.durationMinutes() - requiredMinutes;
                        if (remaining >= 20) {
                            slots.add(new TimeSlot(slot.date(), scheduledEnd, slot.endTime(), remaining));
                            slots.sort(Comparator.comparing(TimeSlot::date).thenComparing(TimeSlot::startTime));
                        }
                        break;
                    }
                }
            }
        }

        List<Task> savedTasks = taskRepository.saveAll(tasksToSchedule);

        goal.setStatus(GoalStatus.IN_PROGRESS);
        Goal savedGoal = goalRepository.save(goal);

        GoalResponse goalResponse = new GoalResponse(
                savedGoal.getId(),
                savedGoal.getUserId(),
                savedGoal.getTitle(),
                savedGoal.getDeadline(),
                savedGoal.getStatus(),
                savedGoal.getCreatedAt()
        );

        List<TaskResponse> taskResponses = savedTasks.stream()
                .map(t -> new TaskResponse(
                        t.getId(),
                        t.getGoalId(),
                        t.getUserId(),
                        t.getTitle(),
                        t.getDescription(),
                        t.getLifecycleState(),
                        t.getTaskType(),
                        t.getContentPayload(),
                        t.getDueDate(),
                        t.getEstimatedDurationMinutes(),
                        t.getCreatedAt(),
                        t.getScheduledDate(),
                        t.getStartTime(),
                        t.getEndTime(),
                        t.getStartedAt(),
                        t.getExpiresAt()
                ))
                .toList();

        return new GoalTemplateResponse(goalResponse, taskResponses);
    }
}
