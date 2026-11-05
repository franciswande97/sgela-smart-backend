package com.app.sgelasmart.api.analytics;

import com.app.sgelasmart.api.analytics.dto.AdminDashboardResponse;
import com.app.sgelasmart.api.analytics.dto.AdminDashboardResponse.*;
import com.app.sgelasmart.api.auth.UserRepository;
import com.app.sgelasmart.api.focussession.FocusSessionRepository;
import com.app.sgelasmart.api.goal.GoalRepository;
import com.app.sgelasmart.api.task.TaskRepository;
import com.app.sgelasmart.common.GoalStatus;
import com.app.sgelasmart.common.TaskLifecycleState;
import com.app.sgelasmart.common.TemplateType;
import com.app.sgelasmart.common.UserRole;
import com.app.sgelasmart.model.FocusSession;
import com.app.sgelasmart.model.Goal;
import com.app.sgelasmart.model.Role;
import com.app.sgelasmart.model.QuizAttempt;
import com.app.sgelasmart.model.Task;
import com.app.sgelasmart.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AdminAnalyticsService {
    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final TaskRepository taskRepository;
    private final FocusSessionRepository focusSessionRepository;
    private final QuizAttemptRepository quizAttemptRepository;

    public AdminAnalyticsService(UserRepository userRepository, GoalRepository goalRepository,
                                 TaskRepository taskRepository, FocusSessionRepository focusSessionRepository,
                                 QuizAttemptRepository quizAttemptRepository) {
        this.userRepository = userRepository;
        this.goalRepository = goalRepository;
        this.taskRepository = taskRepository;
        this.focusSessionRepository = focusSessionRepository;
        this.quizAttemptRepository = quizAttemptRepository;
    }

    @Transactional(readOnly = true)
    public AdminDashboardResponse getDashboard(DatePreset preset, TemplateType template, String search) {
        LocalDate today = LocalDate.now();
        LocalDate start = rangeStart(preset, today);
        LocalDate end = today;
        String normalizedSearch = search == null || search.isBlank() ? null : search.trim().toLowerCase(Locale.ROOT);

        List<User> students = userRepository.findAll().stream()
                .filter(user -> user.getRole() == Role.ROLE_STUDENT)
                        .filter(user -> normalizedSearch == null
                                || user.getName().toLowerCase(Locale.ROOT).contains(normalizedSearch))
                .toList();
        Set<Long> studentIds = students.stream().map(User::getId).collect(Collectors.toSet());
        Map<Long, User> studentsById = students.stream().collect(Collectors.toMap(User::getId, Function.identity()));

        List<Goal> goals = goalRepository.findAll().stream()
                .filter(goal -> studentIds.contains(goal.getUserId()))
                .filter(goal -> template == null || goal.getTemplate() == template)
                .toList();
        Map<Long, Goal> goalsById = goals.stream().collect(Collectors.toMap(Goal::getId, Function.identity()));
        Set<Long> goalIds = goalsById.keySet();
        List<Task> allTasks = taskRepository.findAll().stream()
                .filter(task -> studentIds.contains(task.getUserId()) && goalIds.contains(task.getGoalId()))
                .toList();
        List<Task> rangeTasks = allTasks.stream().filter(task -> scheduledDate(task) != null
                && inRange(scheduledDate(task), start, end)).toList();
        List<FocusSession> allSessions = focusSessionRepository.findAll().stream()
                .filter(session -> studentIds.contains(session.getUserId())
                        && (session.getGoalId() == null ? template == null : goalIds.contains(session.getGoalId())))
                .toList();
        List<FocusSession> rangeSessions = allSessions.stream().filter(session ->
                inRange(session.getSessionDate().toLocalDate(), start, end)).toList();

        List<QuizAttempt> allAttempts = quizAttemptRepository.findAll().stream()
                .filter(attempt -> studentIds.contains(attempt.getUserId())
                        && taskMatches(attempt.getTaskId(), allTasks))
                .toList();
        List<QuizAttempt> rangeAttempts = allAttempts.stream()
                .filter(attempt -> inRange(attempt.getAttemptedAt().toLocalDate(), start, end))
                .toList();

        Set<Long> activeStudentIds = new HashSet<>();
        rangeSessions.forEach(session -> activeStudentIds.add(session.getUserId()));
        allTasks.stream().filter(task -> task.getCompletedAt() != null
                        && inRange(task.getCompletedAt().toLocalDate(), start, end))
                .forEach(task -> activeStudentIds.add(task.getUserId()));

        double focusSeconds = rangeSessions.stream().mapToLong(FocusSession::getDurationSeconds).sum();
        List<DailyFocusHours> dailyFocus = dailyFocus(start, end, rangeSessions);
        List<TemplateCount> templateCounts = Arrays.stream(TemplateType.values())
                .map(type -> new TemplateCount(type, goals.stream().filter(goal -> goal.getTemplate() == type).count()))
                .toList();

        long scheduledCount = rangeTasks.size();
        long completedCount = rangeTasks.stream()
                .filter(task -> task.getLifecycleState() == TaskLifecycleState.COMPLETED).count();
        double completionPercentage = scheduledCount == 0 ? 0 : completedCount * 100.0 / scheduledCount;
        long overdue = rangeTasks.stream().filter(task -> task.getLifecycleState() != TaskLifecycleState.COMPLETED
                && taskDeadline(task) != null && taskDeadline(task).isBefore(today)).count();
        long inProgress = rangeTasks.stream().filter(task -> task.getLifecycleState() == TaskLifecycleState.IN_PROGRESS
                && !(taskDeadline(task) != null && taskDeadline(task).isBefore(today))).count();
        long completed = rangeTasks.stream().filter(task -> task.getLifecycleState() == TaskLifecycleState.COMPLETED).count();
        long toDo = rangeTasks.stream().filter(task -> task.getLifecycleState() == TaskLifecycleState.TODO
                && !(taskDeadline(task) != null && taskDeadline(task).isBefore(today))).count();

        Map<LocalDate, Double> scoresByDate = rangeAttempts.stream().collect(Collectors.groupingBy(
                attempt -> attempt.getAttemptedAt().toLocalDate(),
                TreeMap::new,
                Collectors.averagingDouble(QuizAttempt::getPercentage)));
        List<ComprehensionPoint> comprehensionTrend = scoresByDate.entrySet().stream()
                .map(entry -> new ComprehensionPoint(entry.getKey(), round(entry.getValue())))
                .toList();

        List<StudentAttention> attention = attentionNeeded(studentsById, goalsById, allTasks, allSessions,
                allAttempts, start, end, today);
        long activeExamGoals = goals.stream().filter(goal -> goal.getTemplate() == TemplateType.EXAM_PREPARATION
                && (goal.getStatus() == GoalStatus.NOT_STARTED || goal.getStatus() == GoalStatus.IN_PROGRESS)).count();

        return new AdminDashboardResponse(
                new DateRange(preset, start, end), new Filters(template, normalizedSearch),
                new Summary(activeStudentIds.size(), round(focusSeconds / 3600.0), round(completionPercentage), activeExamGoals),
                dailyFocus, templateCounts, new BacklogHealth(completed, inProgress, overdue, toDo),
                comprehensionTrend, attention);
    }

    private LocalDate rangeStart(DatePreset preset, LocalDate today) {
        return switch (preset) {
            case LAST_7_DAYS -> today.minusDays(6);
            case LAST_30_DAYS -> today.minusDays(29);
            case CURRENT_SEMESTER -> today.getMonthValue() < 7
                    ? LocalDate.of(today.getYear(), 1, 1) : LocalDate.of(today.getYear(), 7, 1);
        };
    }

    private LocalDate scheduledDate(Task task) {
        return task.getScheduledDate() != null ? task.getScheduledDate() : task.getDueDate();
    }

        private LocalDate taskDeadline(Task task) {
                return task.getDueDate() != null ? task.getDueDate() : task.getScheduledDate();
        }

    private boolean inRange(LocalDate date, LocalDate start, LocalDate end) {
        return !date.isBefore(start) && !date.isAfter(end);
    }

    private boolean taskMatches(Long taskId, List<Task> tasks) {
        return tasks.stream().anyMatch(task -> task.getId().equals(taskId));
    }

    private List<DailyFocusHours> dailyFocus(LocalDate start, LocalDate end, List<FocusSession> sessions) {
        Map<LocalDate, Long> secondsByDate = sessions.stream().collect(Collectors.groupingBy(
                session -> session.getSessionDate().toLocalDate(),
                Collectors.summingLong(FocusSession::getDurationSeconds)));
        List<DailyFocusHours> result = new ArrayList<>();
        for (LocalDate day = start; !day.isAfter(end); day = day.plusDays(1)) {
            result.add(new DailyFocusHours(day, round(secondsByDate.getOrDefault(day, 0L) / 3600.0)));
        }
        return result;
    }

    private List<StudentAttention> attentionNeeded(Map<Long, User> students, Map<Long, Goal> goals,
                                                   List<Task> tasks, List<FocusSession> sessions,
                                                   List<QuizAttempt> attempts,
                                                   LocalDate start, LocalDate end, LocalDate today) {
        Map<Long, LocalDateTime> latestActivity = new HashMap<>();
        sessions.forEach(session -> mergeLatest(latestActivity, session.getUserId(), session.getSessionDate()));
        tasks.stream().filter(task -> task.getCompletedAt() != null)
                .forEach(task -> mergeLatest(latestActivity, task.getUserId(), task.getCompletedAt()));
        tasks.stream().filter(task -> task.getStartedAt() != null)
                .forEach(task -> mergeLatest(latestActivity, task.getUserId(), task.getStartedAt()));
        attempts.forEach(attempt -> mergeLatest(latestActivity, attempt.getUserId(), attempt.getAttemptedAt()));

        Map<Long, List<Task>> overdueByStudent = tasks.stream()
                .filter(task -> task.getLifecycleState() != TaskLifecycleState.COMPLETED)
                .filter(task -> taskDeadline(task) != null && taskDeadline(task).isBefore(today)
                        && inRange(taskDeadline(task), start, end))
                .collect(Collectors.groupingBy(Task::getUserId));

        return overdueByStudent.entrySet().stream().map(entry -> {
                    User student = students.get(entry.getKey());
                    List<Task> overdueTasks = entry.getValue();
                    Task nextDeadline = overdueTasks.stream().min(Comparator.comparing(this::taskDeadline)).orElseThrow();
                    Goal activeGoal = goals.get(nextDeadline.getGoalId());
                    return new StudentAttention(student.getId(), student.getName(),
                            activeGoal == null ? null : activeGoal.getTitle(), latestActivity.get(student.getId()),
                            taskDeadline(nextDeadline), overdueTasks.size());
                })
                .sorted(Comparator.comparingLong(StudentAttention::overdueTasks).reversed()
                        .thenComparing(StudentAttention::targetDeadline))
                .toList();
    }

        private void mergeLatest(Map<Long, LocalDateTime> activity, Long userId, LocalDateTime timestamp) {
                activity.merge(userId, timestamp, (current, candidate) -> current.isAfter(candidate) ? current : candidate);
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}