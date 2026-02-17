package br.com.fiap.cheffy.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record ProfileInputDto(
        @NotBlank
        String profileNameType
) {
}
