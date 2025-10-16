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
        String endereco,
        List<Ferramenta> ferramentas
) {
    public FornecedorResponseDto(Fornecedor f){
        this(
                f.getId(),
                f.getNome(),
                f.getCnpj(),
                f.getEmail(),
                f.getTelefone(),
                f.getEndereco(),
                Optional.ofNullable(f.getFerramentas())
                        .orElseGet(List::of)
        );
    }
}
