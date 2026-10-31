package com.app.sgelasmart.api.dashboard;

import com.app.sgelasmart.api.auth.UserRepository;
import com.app.sgelasmart.api.dashboard.dto.DashboardResponse;
import com.app.sgelasmart.api.dashboard.dto.DashboardResponse.NextUpTaskDto;
import com.app.sgelasmart.api.goal.GoalRepository;
import com.app.sgelasmart.api.task.TaskRepository;
import com.app.sgelasmart.common.GoalStatus;
import com.app.sgelasmart.common.TaskLifecycleState;
import com.app.sgelasmart.model.Task;
import com.app.sgelasmart.model.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final TaskRepository taskRepository;

    public DashboardService(UserRepository userRepository,
                            GoalRepository goalRepository,
                            TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.goalRepository = goalRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public DashboardResponse getDashboard(String authenticatedEmail) {
        User user = userRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new AccessDeniedException("User not found or unauthenticated"));

        Long userId = user.getId();

        // 1. Goals Count
        long activeGoalsCount = goalRepository.findByUserId(userId).stream()
                .filter(g -> g.getStatus() == GoalStatus.IN_PROGRESS || g.getStatus() == GoalStatus.NOT_STARTED)
                .count();

        // 2. Tasks & Progress Calculation
        List<Task> userTasks = taskRepository.findByUserId(userId);
        long totalTasks = userTasks.size();
        long completedTasks = userTasks.stream()
                .filter(t -> t.getLifecycleState() == TaskLifecycleState.COMPLETED)
                .count();

        int progressPercentage = totalTasks == 0 ? 0 : (int) Math.round(((double) completedTasks / totalTasks) * 100);

        // 3. Resolve Next Up Task
        NextUpTaskDto nextUp = userTasks.stream()
                .filter(t -> t.getLifecycleState() != TaskLifecycleState.COMPLETED)
                .sorted(Comparator.comparing(
                        (Task t) -> t.getScheduledDate() != null ? t.getScheduledDate() : (t.getDueDate() != null ? t.getDueDate() : LocalDate.MAX)
                ).thenComparing(
                        t -> t.getStartTime() != null ? t.getStartTime() : LocalTime.MAX
                ))
                .findFirst()
                .map(this::mapToNextUpDto)
                .orElse(null);

        // 4. Streak
        int streakDays = calculateStreak(userTasks);

        return new DashboardResponse(
                user.getName(),
                progressPercentage,
                totalTasks,
                activeGoalsCount,
                nextUp,
                streakDays
        );
    }

    private NextUpTaskDto mapToNextUpDto(Task task) {
        LocalDate date = task.getScheduledDate() != null ? task.getScheduledDate() : task.getDueDate();
        LocalTime time = task.getStartTime();
        String timingText = formatTimingText(date, time);

        return new NextUpTaskDto(
                task.getId(),
                task.getGoalId(),
                task.getTitle(),
                task.getTaskType(),
                timingText,
                date,
                time
        );
    }

    private String formatTimingText(LocalDate date, LocalTime time) {
        if (date == null) {
            return "No deadline set";
        }

        LocalDate today = LocalDate.now();
        String timeStr = (time != null) ? " at " + time.format(DateTimeFormatter.ofPattern("HH:mm")) : "";

        if (date.isEqual(today)) {
            return "Today" + timeStr;
        } else if (date.isEqual(today.plusDays(1))) {
            return "Tomorrow" + timeStr;
        } else if (date.isBefore(today)) {
            return "Overdue (" + date.format(DateTimeFormatter.ISO_LOCAL_DATE) + ")";
        } else {
            return "Due " + date.format(DateTimeFormatter.ISO_LOCAL_DATE) + timeStr;
        }
    }

    private int calculateStreak(List<Task> tasks) {
        return tasks.isEmpty() ? 0 : 1;
    }
}
