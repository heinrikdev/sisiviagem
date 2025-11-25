package ufg.br.fabricasw.jatai.swsisviagem.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Usuario;

/**
 *
 * @author Ronaldo N. de Sousa
 */
@ControllerAdvice(basePackages = "ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard")
public class AppBaseControllerAdvice {

    @ModelAttribute
    public void handleRequest(HttpServletRequest request, Model model, RedirectAttributes ra) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usr = (Usuario) auth.getPrincipal();

        model.addAttribute("isCadastroCompleto", usr.getPessoa().isCadastroCompleto());
        model.addAttribute("userName", auth.getName());
        model.addAttribute("roles", usr.getRoles());
        model.addAttribute("currentRole", usr.getCurrentRole());
        model.addAttribute("sistemaBloqueadoDiariaPassagem", false);
    }
}
