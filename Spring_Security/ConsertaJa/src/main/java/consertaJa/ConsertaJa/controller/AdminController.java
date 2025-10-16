package consertaJa.ConsertaJa.controller;

import consertaJa.ConsertaJa.dto.UsuarioRequestDto;
import consertaJa.ConsertaJa.dto.UsuarioResponseDto;
import consertaJa.ConsertaJa.repository.RoleRepository;
import consertaJa.ConsertaJa.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UsuarioService usuarioService;
    private final RoleRepository roleRepository;

    @GetMapping
    public String adminDashboard(Model model) {
        long totalUsuarios = usuarioService.contarTotalUsuarios();
        long totalAdmins = usuarioService.contarUsuariosPorRole("ADMIN");
        long totalUsers = usuarioService.contarUsuariosPorRole("USER");
        
        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("totalAdmins", totalAdmins);
        model.addAttribute("totalUsers", totalUsers);
        
        return "admin/dashboard";
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) String search,
            Model model) {
        
        Page<UsuarioResponseDto> usuarios;
        
        if (search != null && !search.trim().isEmpty()) {
            usuarios = usuarioService.buscarUsuarios(search, pageable);
            model.addAttribute("search", search);
        } else {
            usuarios = usuarioService.listarTodos(pageable);
        }
        
        model.addAttribute("usuarios", usuarios);
        return "admin/usuarios/list";
    }

    @GetMapping("/usuarios/novo")
    public String novoUsuarioForm(Model model) {
        model.addAttribute("usuario", new UsuarioRequestDto());
        model.addAttribute("roles", roleRepository.findAll());
        return "admin/usuarios/form";
    }

    @PostMapping("/usuarios")
    public String criarUsuario(
            @Valid @ModelAttribute("usuario") UsuarioRequestDto usuarioDto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("roles", roleRepository.findAll());
            return "admin/usuarios/form";
        }
        
        try {
            usuarioService.criarUsuario(usuarioDto);
            redirectAttributes.addFlashAttribute("sucesso", "Usuário criado com sucesso!");
            return "redirect:/admin/usuarios";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao criar usuário: " + e.getMessage());
            model.addAttribute("roles", roleRepository.findAll());
            return "admin/usuarios/form";
        }
    }

    @GetMapping("/usuarios/{id}")
    public String visualizarUsuario(@PathVariable Long id, Model model) {
        UsuarioResponseDto usuario = usuarioService.buscarPorId(id);
        model.addAttribute("usuario", usuario);
        return "admin/usuarios/view";
    }

    @GetMapping("/usuarios/{id}/editar")
    public String editarUsuarioForm(@PathVariable Long id, Model model) {
        UsuarioResponseDto usuario = usuarioService.buscarPorId(id);
        UsuarioRequestDto usuarioDto = new UsuarioRequestDto();
        usuarioDto.setUsername(usuario.getUsername());
        usuarioDto.setEmail(usuario.getEmail());
        usuarioDto.setNomeCompleto(usuario.getNomeCompleto());
        usuarioDto.setTelefone(usuario.getTelefone());
        usuarioDto.setEnabled(usuario.isEnabled());
        
        model.addAttribute("usuario", usuarioDto);
        model.addAttribute("usuarioId", id);
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("usuarioRoles", usuario.getRoles());
        return "admin/usuarios/edit";
    }

    @PutMapping("/usuarios/{id}")
    public String atualizarUsuario(
            @PathVariable Long id,
            @Valid @ModelAttribute("usuario") UsuarioRequestDto usuarioDto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("usuarioId", id);
            model.addAttribute("roles", roleRepository.findAll());
            UsuarioResponseDto usuario = usuarioService.buscarPorId(id);
            model.addAttribute("usuarioRoles", usuario.getRoles());
            return "admin/usuarios/edit";
        }
        
        try {
            usuarioService.atualizarUsuario(id, usuarioDto);
            redirectAttributes.addFlashAttribute("sucesso", "Usuário atualizado com sucesso!");
            return "redirect:/admin/usuarios";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao atualizar usuário: " + e.getMessage());
            model.addAttribute("usuarioId", id);
            model.addAttribute("roles", roleRepository.findAll());
            UsuarioResponseDto usuario = usuarioService.buscarPorId(id);
            model.addAttribute("usuarioRoles", usuario.getRoles());
            return "admin/usuarios/edit";
        }
    }

    // Método POST alternativo para atualizar (caso o filtro não funcione)
    @PostMapping("/usuarios/{id}/update")
    public String atualizarUsuarioPost(
            @PathVariable Long id,
            @Valid @ModelAttribute("usuario") UsuarioRequestDto usuarioDto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        return atualizarUsuario(id, usuarioDto, result, model, redirectAttributes);
    }

    @DeleteMapping("/usuarios/{id}")
    public String deletarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.deletarUsuario(id);
            redirectAttributes.addFlashAttribute("sucesso", "Usuário deletado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao deletar usuário: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    // Método POST alternativo para deletar (caso o filtro não funcione)
    @PostMapping("/usuarios/{id}/delete")
    public String deletarUsuarioPost(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return deletarUsuario(id, redirectAttributes);
    }

    @PostMapping("/usuarios/{id}/toggle-status")
    public String alternarStatusUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.alternarStatusUsuario(id);
            redirectAttributes.addFlashAttribute("sucesso", "Status do usuário alterado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao alterar status: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

}
