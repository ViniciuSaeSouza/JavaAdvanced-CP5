package consertaJa.ConsertaJa.config;

import consertaJa.ConsertaJa.model.Role;
import consertaJa.ConsertaJa.model.Usuario;
import consertaJa.ConsertaJa.repository.RoleRepository;
import consertaJa.ConsertaJa.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
        initializeAdminUser();
    }

    private void initializeRoles() {
        if (!roleRepository.existsByNome("ADMIN")) {
            Role adminRole = new Role("ADMIN", "Administrador do sistema");
            roleRepository.save(adminRole);
            log.info("Role ADMIN criada");
        }

        if (!roleRepository.existsByNome("USER")) {
            Role userRole = new Role("USER", "Usuário comum");
            roleRepository.save(userRole);
            log.info("Role USER criada");
        }
    }

    private void initializeAdminUser() {
        if (!usuarioRepository.existsByUsername("admin")) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@consertaja.com");
            admin.setNomeCompleto("Administrador");
            admin.setTelefone("(11) 99999-9999");

            Role adminRole = roleRepository.findByNome("ADMIN").orElseThrow();
            admin.getRoles().add(adminRole);

            usuarioRepository.save(admin);
            log.info("Usuário admin criado com senha: admin123");
        }
    }
}
