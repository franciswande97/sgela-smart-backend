package com.app.sgelasmart.api.analytics;

import com.app.sgelasmart.model.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
}