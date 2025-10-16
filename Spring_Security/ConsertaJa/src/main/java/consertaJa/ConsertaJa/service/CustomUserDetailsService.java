package consertaJa.ConsertaJa.service;

import consertaJa.ConsertaJa.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        // Primeiro tenta encontrar por email, depois por username
        return usuarioRepository.findByEmailWithRoles(emailOrUsername)
                .or(() -> usuarioRepository.findByUsernameWithRoles(emailOrUsername))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + emailOrUsername));
    }
}
