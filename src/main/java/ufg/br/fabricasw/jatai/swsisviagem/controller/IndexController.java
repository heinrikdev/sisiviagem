package ufg.br.fabricasw.jatai.swsisviagem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Usuario;
import ufg.br.fabricasw.jatai.swsisviagem.service.ReCaptchaService;
import ufg.br.fabricasw.jatai.swsisviagem.service.UsuarioService;

/**
 * O index do sistema exibe uma página de login, caso o login não exista, a
 * página é redirecionada para a página de confirmação de login.
 *
 * @author Ronaldo N. de Sousa
 */
@Controller
@RequestMapping("/")
public class IndexController {

    private static final String INDEX_PAGE_LINK = "redirect:/login";
    private static final String CONFIRM_DADOS_LINK = "redirect:/confirmarDados";
    private static final String DASHBOARD_LINK = "redirect:/dashboard";

    private static final String INDEX_PAGE_VIEW = "app/index";
    private static final String USR_NAO_EXISTE_VIEW = "app/usuario_nao_existe";

    private static final String ATTR_ERROR_FORM = "error";

    @Value("${app.config.isProduction}")
    private boolean IS_PRODUCTION;
    @Autowired
    private ReCaptchaService reCaptchaService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public String index() {

        if (usuarioService.findUserLogado() != null) {
            return DASHBOARD_LINK;
        }

        return INDEX_PAGE_LINK;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new Usuario());
        return INDEX_PAGE_VIEW;
    }

    @PostMapping
    public String login(
            Usuario credentials,
            Model model,
            @RequestParam(value = "g-recaptcha-response", required = false) String recaptcha,
            @RequestParam(value = "redirecionar", required = false) String redirecionar) {

        if (IS_PRODUCTION) {
            if(!this.reCaptchaService.isValid(recaptcha)){
                model.addAttribute("errorRecaptcha", true);
                return INDEX_PAGE_VIEW;
            }
        }

        boolean isRedirect = redirecionar != null;
        int result = usuarioService.logarUsuario(credentials);

        switch (result) {
            case UsuarioService.USUARIO_NAO_EXISTE: {

                return USR_NAO_EXISTE_VIEW;
            }
            case UsuarioService.USUARIO_DESATIVADO: {

                return CONFIRM_DADOS_LINK;
            }
            case UsuarioService.USUARIO_SENHA_INVALIDA: {

                model.addAttribute(ATTR_ERROR_FORM, true);
                return INDEX_PAGE_VIEW;
            }
            case UsuarioService.USUARIO_AUTENTICADO: {

                if (isRedirect) {
                    return "redirect:" + redirecionar;
                }
                return DASHBOARD_LINK;
            }
            default: {

                return INDEX_PAGE_VIEW;
            }
        }
    }
}
