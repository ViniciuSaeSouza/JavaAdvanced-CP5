package consertaJa.ConsertaJa.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "TAB_CONSERTAJA_ESTOQUE")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estoque")
    private Long id;

    @Column(name = "cp_estoque", nullable = false)
    private int capacidade;

    @Column(name = "loc_armazenamento")
    private String localArmazenamento;

    @OneToMany(mappedBy = "estoque", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ferramenta> ferramentas;
}
