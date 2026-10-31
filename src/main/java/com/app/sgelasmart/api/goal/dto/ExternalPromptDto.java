package com.app.sgelasmart.api.goal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExternalPromptDto(
        @JsonProperty("recommended_engine") String recommendedEngine,
        @JsonProperty("recommendation_reason") String recommendationReason,
        @JsonProperty("prompt_text") String promptText
) {
    public ExternalPromptDto(String recommendedEngine, String promptText) {
        this(
            recommendedEngine,
            "Optimized for this problem domain and technical specifications.",
            promptText
        );
    }
}
