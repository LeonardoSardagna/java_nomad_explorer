package br.com.planner.planner.domain.link;

import jakarta.validation.constraints.NotBlank;

public record LinkRequestDTO(
        @NotBlank(message = "O título do link não pode estar em branco")
        String title,
        @NotBlank(message = "O link precisa de uma url para ser acessada")
        String url) {
}
