package consertaJa.ConsertaJa.dto;

import consertaJa.ConsertaJa.model.Classificacao;
import consertaJa.ConsertaJa.model.Ferramenta;
import consertaJa.ConsertaJa.model.Tipo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FerramentaRequestDto(
        @NotBlank(message = "O Nome não pode ter valor nulo")
        String nome,

        @NotNull(message = "O tipo da ferramenta não deve estar nulo")
        Tipo tipo,

        @NotNull(message = "A classificação da ferramenta não pode ser nula")
        Classificacao classificacao,

        @NotBlank(message = "O tamanho da ferramenta não pode estar em branco")
        String tamanho,

        @NotNull(message = "O preço da ferramenta não pode ser nulo")
        double preco,

        @Min(value = 0, message = "Quantidade não pode ser negativa")
        @NotNull(message = "A quantidade da ferramenta não pode estar vazia")
        int quantidade,

        String fornecedorId,

        String estoqueId
) {}
