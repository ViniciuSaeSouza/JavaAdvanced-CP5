package consertaJa.ConsertaJa.service;

import consertaJa.ConsertaJa.dto.UsuarioRequestDto;
import consertaJa.ConsertaJa.dto.UsuarioResponseDto;
import consertaJa.ConsertaJa.exception.IdNaoEncontradoException;
import consertaJa.ConsertaJa.model.Role;
import consertaJa.ConsertaJa.model.Usuario;
import consertaJa.ConsertaJa.repository.RoleRepository;
import consertaJa.ConsertaJa.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioResponseDto cadastrarUsuario(UsuarioRequestDto dto) {
        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Nome de usuário já existe");
        }

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já está em uso");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setEmail(dto.getEmail());
        usuario.setNomeCompleto(dto.getNomeCompleto());
        usuario.setTelefone(dto.getTelefone());

        // Atribui role USER por padrão
        Role userRole = roleRepository.findByNome("USER")
                .orElseThrow(() -> new RuntimeException("Role USER não encontrada"));
        usuario.getRoles().add(userRole);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return convertToResponseDto(usuarioSalvo);
    }

    public List<UsuarioResponseDto> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDto buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IdNaoEncontradoException("Usuário não encontrado"));
        return convertToResponseDto(usuario);
    }

    public UsuarioResponseDto buscarPorUsername(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IdNaoEncontradoException("Usuário não encontrado"));
        return convertToResponseDto(usuario);
    }

    public UsuarioResponseDto atualizarUsuario(Long id, UsuarioRequestDto dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IdNaoEncontradoException("Usuário não encontrado"));

        // Verificar se o novo username não está em uso por outro usuário
        if (!usuario.getUsername().equals(dto.getUsername()) &&
            usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Nome de usuário já existe");
        }

        // Verificar se o novo email não está em uso por outro usuário
        if (!usuario.getEmail().equals(dto.getEmail()) &&
            usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já está em uso");
        }

        usuario.setUsername(dto.getUsername());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        usuario.setEmail(dto.getEmail());
        usuario.setNomeCompleto(dto.getNomeCompleto());
        usuario.setTelefone(dto.getTelefone());
        usuario.setEnabled(dto.isEnabled());

        // Atualizar roles se especificadas
        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            usuario.getRoles().clear();
            for (String roleName : dto.getRoles()) {
                Role role = roleRepository.findByNome(roleName)
                        .orElseThrow(() -> new RuntimeException("Role não encontrada: " + roleName));
                usuario.getRoles().add(role);
            }
        }

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return convertToResponseDto(usuarioSalvo);
    }

    public void deletarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IdNaoEncontradoException("Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    public void adicionarRole(Long usuarioId, String roleName) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IdNaoEncontradoException("Usuário não encontrado"));

        Role role = roleRepository.findByNome(roleName)
                .orElseThrow(() -> new RuntimeException("Role não encontrada"));

        usuario.getRoles().add(role);
        usuarioRepository.save(usuario);
    }

    public void removerRole(Long usuarioId, String roleName) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IdNaoEncontradoException("Usuário não encontrado"));

        Role role = roleRepository.findByNome(roleName)
                .orElseThrow(() -> new RuntimeException("Role não encontrada"));

        usuario.getRoles().remove(role);
        usuarioRepository.save(usuario);
    }

    public UsuarioResponseDto obterUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return buscarPorUsername(username);
    }

    // Métodos para AdminController
    public Page<UsuarioResponseDto> listarTodos(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(this::convertToResponseDto);
    }

    public Page<UsuarioResponseDto> buscarUsuarios(String search, Pageable pageable) {
        return usuarioRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrNomeCompletoContainingIgnoreCase(
                search, search, search, pageable)
                .map(this::convertToResponseDto);
    }

    public long contarTotalUsuarios() {
        return usuarioRepository.count();
    }

    public long contarUsuariosPorRole(String roleName) {
        return usuarioRepository.countByRoles_Nome(roleName);
    }

    public UsuarioResponseDto criarUsuario(UsuarioRequestDto dto) {
        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Nome de usuário já existe");
        }

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já está em uso");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setEmail(dto.getEmail());
        usuario.setNomeCompleto(dto.getNomeCompleto());
        usuario.setTelefone(dto.getTelefone());
        usuario.setEnabled(dto.isEnabled());

        // Adicionar roles se especificadas
        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            for (String roleName : dto.getRoles()) {
                Role role = roleRepository.findByNome(roleName)
                        .orElseThrow(() -> new RuntimeException("Role não encontrada: " + roleName));
                usuario.getRoles().add(role);
            }
        } else {
            // Atribui role USER por padrão
            Role userRole = roleRepository.findByNome("USER")
                    .orElseThrow(() -> new RuntimeException("Role USER não encontrada"));
            usuario.getRoles().add(userRole);
        }

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return convertToResponseDto(usuarioSalvo);
    }

    public void alternarStatusUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IdNaoEncontradoException("Usuário não encontrado"));
        
        usuario.setEnabled(!usuario.isEnabled());
        usuarioRepository.save(usuario);
    }


    private UsuarioResponseDto convertToResponseDto(Usuario usuario) {
        UsuarioResponseDto dto = new UsuarioResponseDto();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setEmail(usuario.getEmail());
        dto.setNomeCompleto(usuario.getNomeCompleto());
        dto.setTelefone(usuario.getTelefone());
        dto.setEnabled(usuario.isEnabled());
        dto.setDataCriacao(usuario.getDataCriacao());
        dto.setRoles(usuario.getRoles().stream()
                .map(Role::getNome)
                .collect(Collectors.toSet()));
        return dto;
    }
}
