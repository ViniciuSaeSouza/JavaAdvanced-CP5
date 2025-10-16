package consertaJa.ConsertaJa.model;

import consertaJa.ConsertaJa.dto.FornecedorRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "TAB_CONSERTAJA_FORNECEDOR")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fornecedor")
    private Long id;

    @Column(name = "nm_fornecedor", nullable = false, length = 100)
    private String nome;

    @Column(name = "cnpj_fornecedor", nullable = false, length = 18, unique = true)
    private String cnpj;

    @Column(name = "ds_email", unique = true)
    private String email;

    @Column(name = "nr_telefone", unique = true)
    private String telefone;

    @Column(name = "ds_endereco", length = 255)
    private String endereco;

    @OneToMany(mappedBy = "fornecedor")
    private List<Ferramenta> ferramentas;

    // Construtor personalizado
    public Fornecedor(FornecedorRequestDto dto){
        this.nome = dto.nome();
        this.cnpj = dto.cnpj();
        this.email = dto.email();
        this.telefone = dto.telefone();
        this.endereco = dto.endereco();
    }



}