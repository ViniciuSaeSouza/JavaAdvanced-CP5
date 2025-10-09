package consertaJa.ConsertaJa.controller;

import consertaJa.ConsertaJa.service.EstoqueService;
import consertaJa.ConsertaJa.service.FerramentaService;
import consertaJa.ConsertaJa.service.FornecedorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final FornecedorService fornecedorService;
    private final FerramentaService ferramentaService;
    private final EstoqueService estoqueService;

    public HomeController(FornecedorService fornecedorService,
                          FerramentaService ferramentaService,
                          EstoqueService estoqueService) {
        this.fornecedorService = fornecedorService;
        this.ferramentaService = ferramentaService;
        this.estoqueService = estoqueService;
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("totalFornecedores", fornecedorService.listAll().size());
        model.addAttribute("totalFerramentas", ferramentaService.findAll().size());
        model.addAttribute("totalEstoques", estoqueService.findAll().size());
        return "home";
    }
}