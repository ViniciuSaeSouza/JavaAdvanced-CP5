package consertaJa.ConsertaJa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TDS_Roles_Ferramentas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nome;

    @Column
    private String descricao;

    public Role(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }
}
