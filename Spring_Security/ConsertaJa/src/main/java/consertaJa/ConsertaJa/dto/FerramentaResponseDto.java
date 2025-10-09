package consertaJa.ConsertaJa.dto;

import consertaJa.ConsertaJa.model.Classificacao;
import consertaJa.ConsertaJa.model.Ferramenta;
import consertaJa.ConsertaJa.model.Tipo;

public record FerramentaResponseDto(
        Long id,
        String nome,
        Tipo tipo,
        Classificacao classificacao,
        String tamanho,
        double preco,
        int quantidade,
        Long fornecedorId,
        Long estoqueId,
        String fornecedorNome,
        String estoqueLocal
) {
    public FerramentaResponseDto(Ferramenta f) {
        this(
                f.getId(),
                f.getNome(),
                f.getTipo(),
                f.getClassificacao(),
                f.getTamanho(),
                f.getPreco(),
                f.getQuantidade(),
                f.getFornecedor() != null ? f.getFornecedor().getId() : null,
                f.getEstoque() != null ? f.getEstoque().getId() : null,
                f.getFornecedor() != null ? f.getFornecedor().getNome() : null,
                f.getEstoque() != null ? f.getEstoque().getLocalArmazenamento() : null
        );
    }
}
