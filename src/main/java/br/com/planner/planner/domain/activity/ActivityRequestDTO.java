package br.com.planner.planner.domain.activity;

import jakarta.validation.constraints.NotBlank;

public record ActivityRequestDTO(
        @NotBlank
        String title,
        @NotBlank
        String occurs_at) {
}
