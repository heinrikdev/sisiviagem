package ufg.br.fabricasw.jatai.swsisviagem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Pessoa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.Role;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Usuario;
import ufg.br.fabricasw.jatai.swsisviagem.service.UsuarioService;

/**
 * Quando o usuário loga pela primeira vez é necessário verificar os dados cadastrais 
 * e ativá-lo.
 * 
 * Neste controller é feito todo o processo de ativação de usuário.
 * 
 * @author Ronaldo N. de Sousa
 */
@Controller
@RequestMapping("/confirmarDados")
public class AtivarUsuarioController {

    private static final String INDEX_PAGE_VIEW = "app/confirma_dados_login";
    private static final String ATTR_PESSOA = "pessoa";
    private static final String ATTR_USR = "usuario";
    private static final String ATTR_PASS_INVALID = "senhaInvalida";
    private static final String ATTR_DADOS_INVALID = "dadosInvalida";
    private static final String REDIRECT_TO_INDEX = "redirect:/confirmarDados";
    private static final String REDIRECT_TO_DASHBOARD = "redirect:/dashboard";

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String index(Model model) {

        model.addAttribute(ATTR_PESSOA, new Pessoa());
        model.addAttribute(ATTR_USR, new Usuario());

        return INDEX_PAGE_VIEW;
    }

    @PostMapping
    public String ativarUsuario(Model model, Pessoa pessoa, Usuario credentials, RedirectAttributes attr) {

        if (credentials.getPassword().length() < 8) {
            
            attr.addFlashAttribute(ATTR_PASS_INVALID, true);
            return REDIRECT_TO_INDEX;
        }
        
        Usuario usuario = usuarioService.findUserByCpf(pessoa.getCpf());
        
        Boolean isValido = (
            usuario != null && 
            usuario.getPessoa().getDataNascimento().equals(pessoa.getDataNascimento()) && 
            usuario.getPessoa().getCpf().equals(pessoa.getCpf())
        );

        if (!isValido) {
            
            model.addAttribute(ATTR_DADOS_INVALID, true);
            model.addAttribute(ATTR_PESSOA, pessoa);
            model.addAttribute(ATTR_USR, usuario);

            return INDEX_PAGE_VIEW;
        }
        
        String encryptedPassword = this.usuarioService.encryptPassword(credentials.getPassword());
        
        usuario.setPassword(encryptedPassword);
        usuario.getRoles().add(Role.USER);
        usuario.setEnabled(true);
        
        this.usuarioService.save(usuario);
        this.usuarioService.logarComo(usuario);
        
        return REDIRECT_TO_DASHBOARD;
    }
}
