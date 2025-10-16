package consertaJa.ConsertaJa.controller;

import consertaJa.ConsertaJa.dto.UsuarioRequestDto;
import consertaJa.ConsertaJa.dto.UsuarioResponseDto;
import consertaJa.ConsertaJa.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                       @RequestParam(value = "logout", required = false) String logout,
                       Model model) {
        if (error != null) {
            model.addAttribute("error", "Usuário ou senha inválidos!");
        }
        if (logout != null) {
            model.addAttribute("message", "Logout realizado com sucesso!");
        }
        return "auth/login";
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("usuario", new UsuarioRequestDto());
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String processSignup(@Valid @ModelAttribute("usuario") UsuarioRequestDto usuarioDto,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "auth/signup";
        }

        try {
            usuarioService.cadastrarUsuario(usuarioDto);
            redirectAttributes.addFlashAttribute("success",
                "Cadastro realizado com sucesso! Faça login para continuar.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            result.rejectValue("username", "error.usuario", e.getMessage());
            return "auth/signup";
        }
    }

    @GetMapping("/perfil")
    public String perfil(Model model) {
        UsuarioResponseDto usuario = usuarioService.obterUsuarioLogado();
        model.addAttribute("usuario", usuario);
        return "auth/perfil";
    }

    @GetMapping("/editar-perfil")
    public String editarPerfilForm(Model model) {
        UsuarioResponseDto usuario = usuarioService.obterUsuarioLogado();
        UsuarioRequestDto usuarioDto = new UsuarioRequestDto();
        usuarioDto.setUsername(usuario.getUsername());
        usuarioDto.setEmail(usuario.getEmail());
        usuarioDto.setNomeCompleto(usuario.getNomeCompleto());
        usuarioDto.setTelefone(usuario.getTelefone());
        
        model.addAttribute("usuario", usuarioDto);
        return "auth/editar-perfil";
    }

    @PostMapping("/editar-perfil")
    public String atualizarPerfil(@Valid @ModelAttribute("usuario") UsuarioRequestDto usuarioDto,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "auth/editar-perfil";
        }

        try {
            UsuarioResponseDto usuarioLogado = usuarioService.obterUsuarioLogado();
            usuarioService.atualizarUsuario(usuarioLogado.getId(), usuarioDto);
            redirectAttributes.addFlashAttribute("sucesso", "Perfil atualizado com sucesso!");
            return "redirect:/perfil";
        } catch (RuntimeException e) {
            result.rejectValue("email", "error.usuario", e.getMessage());
            result.rejectValue("username", "error.usuario", e.getMessage());
            return "auth/editar-perfil";
        }
    }
}
