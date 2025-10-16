package consertaJa.ConsertaJa.controller;

import consertaJa.ConsertaJa.dto.UsuarioRequestDto;
import consertaJa.ConsertaJa.dto.UsuarioResponseDto;
import consertaJa.ConsertaJa.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public String listUsuarios(@RequestParam(value = "search", required = false) String search,
                               @PageableDefault(size = 10) Pageable pageable,
                               Model model) {
        Page<UsuarioResponseDto> usuariosPage;
        if (search != null && !search.isEmpty()) {
            usuariosPage = usuarioService.buscarUsuarios(search, pageable);
        } else {
            usuariosPage = usuarioService.listarTodos(pageable);
        }
        model.addAttribute("usuariosPage", usuariosPage);
        model.addAttribute("search", search);
        return "usuarios/list";
    }

    @GetMapping("/new")
    public String showCreateUserForm(Model model) {
        model.addAttribute("usuario", new UsuarioRequestDto());
        return "usuarios/form";
    }

    @PostMapping
    public String createUser(@Valid @ModelAttribute("usuario") UsuarioRequestDto usuarioDto,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (result.hasErrors()) {
            return "usuarios/form";
        }
        try {
            usuarioService.cadastrarUsuario(usuarioDto);
            redirectAttributes.addFlashAttribute("success", "Usuário criado com sucesso!");
            return "redirect:/usuarios";
        } catch (RuntimeException e) {
            result.rejectValue("email", "error.usuario", e.getMessage());
            result.rejectValue("username", "error.usuario", e.getMessage());
            return "usuarios/form";
        }
    }

    @GetMapping("/{id}")
    public String viewUsuario(@PathVariable Long id, Model model) {
        UsuarioResponseDto usuario = usuarioService.buscarPorId(id);
        model.addAttribute("usuario", usuario);
        return "usuarios/view";
    }

    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        UsuarioResponseDto usuarioResponse = usuarioService.buscarPorId(id);
        UsuarioRequestDto usuarioRequest = new UsuarioRequestDto();
        usuarioRequest.setUsername(usuarioResponse.getUsername());
        usuarioRequest.setEmail(usuarioResponse.getEmail());
        usuarioRequest.setNomeCompleto(usuarioResponse.getNomeCompleto());
        usuarioRequest.setTelefone(usuarioResponse.getTelefone());
        usuarioRequest.setEnabled(usuarioResponse.isEnabled());

        model.addAttribute("usuario", usuarioRequest);
        model.addAttribute("userId", id);
        return "usuarios/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id,
                             @Valid @ModelAttribute("usuario") UsuarioRequestDto usuarioDto,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("userId", id);
            return "usuarios/edit";
        }
        try {
            usuarioService.atualizarUsuario(id, usuarioDto);
            redirectAttributes.addFlashAttribute("success", "Usuário atualizado com sucesso!");
            return "redirect:/usuarios";
        } catch (RuntimeException e) {
            result.rejectValue("email", "error.usuario", e.getMessage());
            result.rejectValue("username", "error.usuario", e.getMessage());
            model.addAttribute("userId", id);
            return "usuarios/edit";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        usuarioService.deletarUsuario(id);
        redirectAttributes.addFlashAttribute("success", "Usuário deletado com sucesso!");
        return "redirect:/usuarios";
    }
}
