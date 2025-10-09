package consertaJa.ConsertaJa.dto;

import consertaJa.ConsertaJa.model.Estoque;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public record EstoqueResponseDto(
        Long id,
        int capacidade,
        String localArmazenamento,
        List<FerramentaResponseDto> ferramentas
) {
    public EstoqueResponseDto(Estoque estoque) {
        this(
                estoque.getId(),
                estoque.getCapacidade(),
                estoque.getLocalArmazenamento(),
                Optional.ofNullable(estoque.getFerramentas())
                        .orElseGet(List::of)
                        .stream()
                        .map(FerramentaResponseDto::new)
                        .collect(Collectors.toList())
        );
    }

    public int getQuantidadeTotal() {
        return ferramentas.stream()
                .mapToInt(f -> f.quantidade())
                .sum();
    }
}