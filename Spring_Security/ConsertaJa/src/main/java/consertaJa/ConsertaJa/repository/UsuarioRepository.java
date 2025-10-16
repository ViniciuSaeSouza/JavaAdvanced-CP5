package consertaJa.ConsertaJa.repository;

import consertaJa.ConsertaJa.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM Usuario u JOIN FETCH u.roles WHERE u.username = :username")
    Optional<Usuario> findByUsernameWithRoles(String username);

    @Query("SELECT u FROM Usuario u JOIN FETCH u.roles WHERE u.email = :email")
    Optional<Usuario> findByEmailWithRoles(String email);

    Page<Usuario> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrNomeCompletoContainingIgnoreCase(
            String username, String email, String nomeCompleto, Pageable pageable);

    long countByRoles_Nome(String roleName);
}
