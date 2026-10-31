package com.app.sgelasmart.model;

import com.app.sgelasmart.common.EventSource;
import com.app.sgelasmart.common.TimetableType;
import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "calendar_events")
public class CalendarEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    @Column(name = "event_date")
    private LocalDate eventDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventSource source;

    @Enumerated(EnumType.STRING)
    @Column(name = "timetable_type")
    private TimetableType timetableType;

    @Column(name = "is_recurring", nullable = false)
    private Boolean isRecurring;

    public CalendarEvent() {}

    public CalendarEvent(Long userId, String title, DayOfWeek dayOfWeek, LocalDate eventDate,
                         LocalTime startTime, LocalTime endTime, EventSource source,
                         TimetableType timetableType, Boolean isRecurring) {
        this.userId = userId;
        this.title = title;
        this.dayOfWeek = dayOfWeek;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.source = source;
        this.timetableType = timetableType;
        this.isRecurring = isRecurring;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public EventSource getSource() { return source; }
    public void setSource(EventSource source) { this.source = source; }
    public TimetableType getTimetableType() { return timetableType; }
    public void setTimetableType(TimetableType timetableType) { this.timetableType = timetableType; }
    public Boolean getIsRecurring() { return isRecurring; }
    public void setIsRecurring(Boolean isRecurring) { this.isRecurring = isRecurring; }
}
