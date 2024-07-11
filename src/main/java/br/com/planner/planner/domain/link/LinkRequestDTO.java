package br.com.planner.planner.domain.link;

import jakarta.validation.constraints.NotBlank;

public record LinkRequestDTO(
        @NotBlank
        String title,
        @NotBlank
        String url) {
}
