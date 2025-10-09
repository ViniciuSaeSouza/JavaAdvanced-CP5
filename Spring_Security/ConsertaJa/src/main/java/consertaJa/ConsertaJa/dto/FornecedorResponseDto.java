package consertaJa.ConsertaJa.dto;

import consertaJa.ConsertaJa.model.Ferramenta;
import consertaJa.ConsertaJa.model.Fornecedor;

import java.util.List;
import java.util.Optional;

public record FornecedorResponseDto(

        Long id,
        String nome,
        String cnpj,
        String email,
        String telefone,
        List<Ferramenta> ferramentas
) {
    public FornecedorResponseDto(Fornecedor f){
        this(
                f.getId(),
                f.getNome(),
                f.getCnpj(),
                f.getEmail(),
                f.getTelefone(),
                Optional.ofNullable(f.getFerramentas())
                        .orElseGet(List::of)
        );
    }
}
