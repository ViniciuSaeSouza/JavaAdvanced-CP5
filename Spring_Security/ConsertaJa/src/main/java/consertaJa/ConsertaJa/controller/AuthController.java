package consertaJa.ConsertaJa.controller;

import consertaJa.ConsertaJa.dto.UsuarioRequestDto;
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
}
