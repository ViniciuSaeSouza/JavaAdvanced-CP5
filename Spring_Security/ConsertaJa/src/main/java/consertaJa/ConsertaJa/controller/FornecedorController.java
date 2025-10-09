package consertaJa.ConsertaJa.controller;

import consertaJa.ConsertaJa.dto.FornecedorRequestDto;
import consertaJa.ConsertaJa.dto.FornecedorResponseDto;
import consertaJa.ConsertaJa.exception.IdNaoEncontradoException;
import consertaJa.ConsertaJa.service.FornecedorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/fornecedores")
public class FornecedorController {

    private final FornecedorService fornecedorService;

    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @GetMapping
    public String listar(Model model, @ModelAttribute("mensagem") String mensagem) {
        model.addAttribute("fornecedores", fornecedorService.listAll());
        return "fornecedores/list";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("fornecedor", new FornecedorRequestDto("", "", "", ""));
        model.addAttribute("formAction", "/fornecedores");
        model.addAttribute("isEdit", false);
        return "fornecedores/form";
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute("fornecedor") FornecedorRequestDto dto,
                         BindingResult bindingResult,
                         RedirectAttributes ra,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formAction", "/fornecedores");
            model.addAttribute("isEdit", false);
            return "fornecedores/form";
        }
        fornecedorService.save(dto);
        ra.addFlashAttribute("mensagem", "Fornecedor cadastrado com sucesso!");
        return "redirect:/fornecedores";
    }

    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        FornecedorResponseDto fornecedor = fornecedorService.findById(id);
        if (fornecedor == null) {
            ra.addFlashAttribute("mensagem", "Fornecedor não encontrado.");
            return "redirect:/fornecedores";
        }
        model.addAttribute("fornecedor", fornecedor);
        model.addAttribute("formAction", "/fornecedores/" + id);
        model.addAttribute("isEdit", true);
        return "fornecedores/form";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id,
                            @ModelAttribute("fornecedor") FornecedorResponseDto dto,
                            RedirectAttributes ra,
                            Model model) {
        try {
            fornecedorService.update(id, dto);
            ra.addFlashAttribute("mensagem", "Fornecedor atualizado com sucesso!");
            return "redirect:/fornecedores";
        } catch (IdNaoEncontradoException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("formAction", "/fornecedores/" + id);
            model.addAttribute("isEdit", true);
            return "fornecedores/form";
        }
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) {
        try {
            fornecedorService.delete(id);
            ra.addFlashAttribute("mensagem", "Fornecedor excluído com sucesso!");
        } catch (IdNaoEncontradoException e) {
            ra.addFlashAttribute("mensagem", e.getMessage());
        }
        return "redirect:/fornecedores";
    }
}