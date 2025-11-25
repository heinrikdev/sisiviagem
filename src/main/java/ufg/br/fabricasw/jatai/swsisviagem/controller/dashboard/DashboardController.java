package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Usuario;
import ufg.br.fabricasw.jatai.swsisviagem.service.UsuarioService;

/**
 *
 * @author Ronaldo N. de Sousa
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private UsuarioService usuarioService;

    private static final String INDEX_PAGE = "app/dashboard/index";
    private String msg = "Informamos que o sistema de diárias e passagens (SCDP) "
            + "não terá inserções de pedidos no recesso de fim/início de ano (2018/2019). "
            + "Os pedidos serão novamente retomados no próximo ano (2019) quando da liberação "
            + "orçamentária pelo Governo Federal.";
    
    @GetMapping
    public String index(Model model) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usr = this.usuarioService.findUserLogado();

        model.addAttribute("user", auth);
        model.addAttribute("titlePage", "Bem vindo, @" +auth.getName());
        model.addAttribute("memorando_interdicao_scdp", msg);
        model.addAttribute("roles", usr.getRoles());
        model.addAttribute("currentRole", usr.getCurrentRole());
        model.addAttribute("usuario", usr);

        return INDEX_PAGE;
    }
}
