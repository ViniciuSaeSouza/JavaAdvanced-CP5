package consertaJa.ConsertaJa.controller;

import consertaJa.ConsertaJa.dto.FerramentaRequestDto;
import consertaJa.ConsertaJa.dto.FerramentaResponseDto;
import consertaJa.ConsertaJa.model.Classificacao;
import consertaJa.ConsertaJa.model.Tipo;
import consertaJa.ConsertaJa.exception.IdNaoEncontradoException;
import consertaJa.ConsertaJa.service.EstoqueService;
import consertaJa.ConsertaJa.service.FerramentaService;
import consertaJa.ConsertaJa.service.FornecedorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ferramentas")
public class FerramentaController {

    private final FerramentaService ferramentaService;
    private final FornecedorService fornecedorService;
    private final EstoqueService estoqueService;

    public FerramentaController(FerramentaService ferramentaService,
                                FornecedorService fornecedorService,
                                EstoqueService estoqueService) {
        this.ferramentaService = ferramentaService;
        this.fornecedorService = fornecedorService;
        this.estoqueService = estoqueService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("ferramentas", ferramentaService.findAll());
        return "ferramentas/list";
    }

    @GetMapping("/nova")
    public String novoForm(Model model) {
        model.addAttribute("ferramenta", new FerramentaRequestDto("", null, null, "", 0.0, 0, null, null));
        model.addAttribute("fornecedores", fornecedorService.listAll());
        model.addAttribute("estoques", estoqueService.findAll());
        model.addAttribute("tipos", Tipo.values());
        model.addAttribute("classificacoes", Classificacao.values());
        model.addAttribute("formAction", "/ferramentas");
        model.addAttribute("isEdit", false);
        return "ferramentas/form";
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute("ferramenta") FerramentaRequestDto dto,
                         BindingResult bindingResult,
                         RedirectAttributes ra,
                         Model model) {
        System.out.println("=== MÉTODO POST CHAMADO ===");
        System.out.println("Dados recebidos: " + dto);
        
        if (bindingResult.hasErrors()) {
            System.out.println("Erros de validação: " + bindingResult.getAllErrors());
            model.addAttribute("fornecedores", fornecedorService.listAll());
            model.addAttribute("estoques", estoqueService.findAll());
            model.addAttribute("tipos", Tipo.values());
            model.addAttribute("classificacoes", Classificacao.values());
            model.addAttribute("formAction", "/ferramentas");
            model.addAttribute("isEdit", false);
            return "ferramentas/form";
        }
        try {
            ferramentaService.save(dto);
            ra.addFlashAttribute("mensagem", "Ferramenta cadastrada com sucesso!");
            return "redirect:/ferramentas";
        } catch (IdNaoEncontradoException e) {
            System.out.println("Erro IdNaoEncontradoException: " + e.getMessage());
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("fornecedores", fornecedorService.listAll());
            model.addAttribute("estoques", estoqueService.findAll());
            model.addAttribute("tipos", Tipo.values());
            model.addAttribute("classificacoes", Classificacao.values());
            model.addAttribute("formAction", "/ferramentas");
            model.addAttribute("isEdit", false);
            return "ferramentas/form";
        } catch (Exception e) {
            System.out.println("Erro geral: " + e.getMessage());
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().contains("Ultrapassou a capacidade do estoque")) {
                return handleCapacidadeError(e.getMessage(), model);
            }
            
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("fornecedores", fornecedorService.listAll());
            model.addAttribute("estoques", estoqueService.findAll());
            model.addAttribute("tipos", Tipo.values());
            model.addAttribute("classificacoes", Classificacao.values());
            model.addAttribute("formAction", "/ferramentas");
            model.addAttribute("isEdit", false);
            return "ferramentas/form";
        }
    }

    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        FerramentaResponseDto ferramenta = ferramentaService.findById(id);
        if (ferramenta == null) {
            ra.addFlashAttribute("mensagem", "Ferramenta não encontrada.");
            return "redirect:/ferramentas";
        }
        model.addAttribute("ferramenta", ferramenta);
        model.addAttribute("fornecedores", fornecedorService.listAll());
        model.addAttribute("estoques", estoqueService.findAll());
        model.addAttribute("tipos", Tipo.values());
        model.addAttribute("classificacoes", Classificacao.values());
        model.addAttribute("formAction", "/ferramentas/" + id);
        model.addAttribute("isEdit", true);
        return "ferramentas/form";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id,
                            @ModelAttribute("ferramenta") FerramentaResponseDto dto,
                            RedirectAttributes ra,
                            Model model) {
        try {
            ferramentaService.update(id, dto);
            ra.addFlashAttribute("mensagem", "Ferramenta atualizada com sucesso!");
            return "redirect:/ferramentas";
        } catch (IdNaoEncontradoException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("fornecedores", fornecedorService.listAll());
            model.addAttribute("estoques", estoqueService.findAll());
            model.addAttribute("tipos", Tipo.values());
            model.addAttribute("classificacoes", Classificacao.values());
            model.addAttribute("formAction", "/ferramentas/" + id);
            model.addAttribute("isEdit", true);
            return "ferramentas/form";
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("Ultrapassou a capacidade do estoque")) {
                return handleCapacidadeError(e.getMessage(), model);
            }
            
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("fornecedores", fornecedorService.listAll());
            model.addAttribute("estoques", estoqueService.findAll());
            model.addAttribute("tipos", Tipo.values());
            model.addAttribute("classificacoes", Classificacao.values());
            model.addAttribute("formAction", "/ferramentas/" + id);
            model.addAttribute("isEdit", true);
            return "ferramentas/form";
        }
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) {
        try {
            ferramentaService.delete(id);
            ra.addFlashAttribute("mensagem", "Ferramenta excluída com sucesso!");
        } catch (IdNaoEncontradoException e) {
            ra.addFlashAttribute("mensagem", e.getMessage());
        }
        return "redirect:/ferramentas";
    }

    private String handleCapacidadeError(String mensagemErro, Model model) {
        model.addAttribute("mensagemErro", mensagemErro);
        
        try {
            String[] parts = mensagemErro.split("\\.");
            if (parts.length >= 2) {
                String primeiraParte = parts[0];
                String segundaParte = parts[1];
                
                if (primeiraParte.contains("'")) {
                    String local = primeiraParte.substring(primeiraParte.indexOf("'") + 1, primeiraParte.lastIndexOf("'"));
                    model.addAttribute("localEstoque", local);
                }
                
                if (segundaParte.contains("Capacidade:")) {
                    String capacidade = extrairNumero(segundaParte, "Capacidade:");
                    model.addAttribute("capacidadeTotal", capacidade);
                }
                
                if (segundaParte.contains("já usado:")) {
                    String usado = extrairNumero(segundaParte, "já usado:");
                    model.addAttribute("capacidadeUsada", usado);
                    
                    try {
                        int total = Integer.parseInt(model.getAttribute("capacidadeTotal").toString());
                        int usadoInt = Integer.parseInt(usado);
                        model.addAttribute("capacidadeDisponivel", String.valueOf(total - usadoInt));
                    } catch (Exception ignored) {}
                }
                
                if (segundaParte.contains("tentando adicionar:")) {
                    String tentativa = extrairNumero(segundaParte, "tentando adicionar:");
                    model.addAttribute("quantidadeTentativa", tentativa);
                }
            }
        } catch (Exception e) {

        }
        
        return "erro-capacidade";
    }
    
    private String extrairNumero(String texto, String chave) {
        int inicio = texto.indexOf(chave);
        if (inicio == -1) return "N/A";
        
        inicio += chave.length();
        int fim = texto.indexOf(",", inicio);
        if (fim == -1) fim = texto.indexOf(".", inicio);
        if (fim == -1) fim = texto.length();
        
        return texto.substring(inicio, fim).trim();
    }
}