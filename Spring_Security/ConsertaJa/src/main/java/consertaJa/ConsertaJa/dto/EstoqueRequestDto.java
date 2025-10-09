package consertaJa.ConsertaJa.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EstoqueRequestDto(
        @NotNull(message = "A capacidade não pode ser nula")
        @Min(value = 1, message = "A capacidade deve ser de no mínimo 1")
        int capacidade,

        @NotBlank(message = "O local de armazenamento não pode estar em branco")
        String localArmazenamento
) {
}