package com.example.llmyapper.dto;

import java.util.Map;

public record AcrionResultDto(
        String action,
        String target,
        int roll,
        int difficulty,
        boolean success,
        String outcome,
        Map<String, Object> effects) {
}
