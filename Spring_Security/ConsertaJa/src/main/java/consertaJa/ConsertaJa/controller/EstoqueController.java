package consertaJa.ConsertaJa.controller;

import consertaJa.ConsertaJa.dto.EstoqueRequestDto;
import consertaJa.ConsertaJa.dto.EstoqueResponseDto;
import consertaJa.ConsertaJa.exception.IdNaoEncontradoException;
import consertaJa.ConsertaJa.service.EstoqueService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/estoques")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("estoques", estoqueService.findAll());
        return "estoques/list";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("estoque", new EstoqueRequestDto(0, ""));
        model.addAttribute("formAction", "/estoques");
        model.addAttribute("isEdit", false);
        return "estoques/form";
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute("estoque") EstoqueRequestDto dto,
                         BindingResult bindingResult,
                         RedirectAttributes ra,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formAction", "/estoques");
            model.addAttribute("isEdit", false);
            return "estoques/form";
        }
        try {
            estoqueService.save(dto);
            ra.addFlashAttribute("mensagem", "Estoque cadastrado com sucesso!");
            return "redirect:/estoques";
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("formAction", "/estoques");
            model.addAttribute("isEdit", false);
            return "estoques/form";
        }
    }

    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            EstoqueResponseDto estoqueResponse = estoqueService.findById(id);
            if (estoqueResponse == null) {
                ra.addFlashAttribute("mensagem", "Estoque não encontrado.");
                return "redirect:/estoques";
            }
            
            EstoqueRequestDto estoqueRequest = new EstoqueRequestDto(
                estoqueResponse.capacidade(),
                estoqueResponse.localArmazenamento()
            );
            
            model.addAttribute("estoque", estoqueRequest);
            model.addAttribute("estoqueInfo", estoqueResponse); // Para mostrar informações adicionais
            model.addAttribute("formAction", "/estoques/" + id);
            model.addAttribute("isEdit", true);
            return "estoques/form";
        } catch (IdNaoEncontradoException e) {
            ra.addFlashAttribute("mensagem", "Estoque não encontrado.");
            return "redirect:/estoques";
        }
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id,
                            @Valid @ModelAttribute("estoque") EstoqueRequestDto dto,
                            BindingResult bindingResult,
                            RedirectAttributes ra,
                            Model model) {
        if (bindingResult.hasErrors()) {
            try {
                EstoqueResponseDto estoqueResponse = estoqueService.findById(id);
                model.addAttribute("estoqueInfo", estoqueResponse);
            } catch (IdNaoEncontradoException e) {
                ra.addFlashAttribute("mensagem", "Estoque não encontrado.");
                return "redirect:/estoques";
            }
            model.addAttribute("formAction", "/estoques/" + id);
            model.addAttribute("isEdit", true);
            return "estoques/form";
        }
        try {
            estoqueService.update(id, dto);
            ra.addFlashAttribute("mensagem", "Estoque atualizado com sucesso!");
            return "redirect:/estoques";
        } catch (IdNaoEncontradoException e) {
            model.addAttribute("erro", e.getMessage());
            try {
                EstoqueResponseDto estoqueResponse = estoqueService.findById(id);
                model.addAttribute("estoqueInfo", estoqueResponse);
            } catch (IdNaoEncontradoException ex) {
                ra.addFlashAttribute("mensagem", "Estoque não encontrado.");
                return "redirect:/estoques";
            }
            model.addAttribute("formAction", "/estoques/" + id);
            model.addAttribute("isEdit", true);
            return "estoques/form";
        }
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) {
        try {
            estoqueService.delete(id);
            ra.addFlashAttribute("mensagem", "Estoque excluído com sucesso!");
        } catch (IdNaoEncontradoException e) {
            ra.addFlashAttribute("mensagem", e.getMessage());
        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao excluir: " + e.getMessage());
        }
        return "redirect:/estoques";
    }
}