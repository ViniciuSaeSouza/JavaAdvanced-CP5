package consertaJa.ConsertaJa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDto {

    private Long id;
    private String username;
    private String email;
    private String nomeCompleto;
    private String telefone;
    private boolean enabled;
    private LocalDateTime dataCriacao;
    private Set<String> roles;
}
