package consertaJa.ConsertaJa.dto;

import consertaJa.ConsertaJa.annotations.interfaces.CNPJ;
import consertaJa.ConsertaJa.annotations.interfaces.Telefone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record FornecedorRequestDto(

        @NotBlank(message = "O nome não pode estar vazio")
        String nome,

        @CNPJ
        @NotBlank(message = "A CNPJ do fornecedor não pode estar vazia")
        String cnpj,

        @Email(message = "Formato inválido, por favor insira no formato: example@gmail.com")
        String email,

        @Telefone
        String telefone
) {
}
