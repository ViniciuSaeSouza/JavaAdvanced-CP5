package consertaJa.ConsertaJa.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDto {

    @NotBlank(message = "Nome de usuário é obrigatório")
    @Size(min = 3, max = 50, message = "Nome de usuário deve ter entre 3 e 50 caracteres")
    private String username;

    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    private String password;

    @Email(message = "Email deve ser válido")
    @NotBlank(message = "Email é obrigatório")
    private String email;

    @NotBlank(message = "Nome completo é obrigatório")
    private String nomeCompleto;

    private String telefone;

    private boolean enabled = true;

    private Set<String> roles;

    // Confirmação de senha para validação no frontend
    private String confirmPassword;
}
