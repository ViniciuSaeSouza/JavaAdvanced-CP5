package consertaJa.ConsertaJa.model;

import consertaJa.ConsertaJa.dto.FerramentaRequestDto;
import consertaJa.ConsertaJa.exception.QuantidadeInvalidaException;
import consertaJa.ConsertaJa.exception.ValorNegativoException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TAB_CONSERTAJA_FERRAMENTA")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Ferramenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ferramenta")
    private Long id;

    @Column(name = "nm_ferramenta", nullable = false, length = 100, unique = true)
    private String nome;

    @Column(name = "tipo_ferramenta", nullable = false)
    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @Column(name = "class_ferramenta", nullable = false)
    @Enumerated(EnumType.STRING)
    private Classificacao classificacao;

    @Column(name = "tam_ferramenta", nullable = false, length = 100)
    private String tamanho;

    @Column(name = "preco_ferramenta", nullable = false)
    private double preco;

    @Column(name = "qt_ferramenta", nullable = false)
    private int quantidade;

    @ManyToOne
    @JoinColumn(name = "id_fornecedor")
    private Fornecedor fornecedor;

    @ManyToOne
    @JoinColumn(name = "id_estoque")
    private Estoque estoque;

    // Construtores personalizados
    public Ferramenta(FerramentaRequestDto dto){
        this.nome = dto.nome();
        this.tipo = dto.tipo();
        this.classificacao = dto.classificacao();
        this.tamanho = dto.tamanho();
        this.preco = dto.preco();
        this.quantidade = dto.quantidade();
    }

    public Ferramenta(FerramentaRequestDto dto, Fornecedor fornecedor, Estoque estoque){
        this.nome = dto.nome();
        this.tipo = dto.tipo();
        this.classificacao = dto.classificacao();
        this.tamanho = dto.tamanho();
        this.preco = dto.preco();
        this.quantidade = dto.quantidade();
        this.fornecedor = fornecedor;
        this.estoque = estoque;
    }

    // Métodos da classe
    public int retirarQuantidade(int quantidadeSub) throws ValorNegativoException, QuantidadeInvalidaException {

        if (quantidadeSub < 0) {
            throw new ValorNegativoException("O valor inserido deve ser positivo");
        }

        if (quantidadeSub > quantidade) {
            throw new QuantidadeInvalidaException("O valor inserido não pode ser maior que a quantidade atual(" + quantidade + ")");
        }

        return quantidade - quantidadeSub;
    }

    public int adicionarQuantidade(int quantidadeAdd) throws ValorNegativoException {
        if (quantidadeAdd < 0) {
            throw new ValorNegativoException("O valor inserido não pode ser negativo");
        }

        return quantidade + quantidadeAdd;
    }
}
