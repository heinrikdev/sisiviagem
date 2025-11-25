package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.meu_perfil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ufg.br.fabricasw.jatai.swsisviagem.controller.Constantes;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Pessoa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Usuario;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.Role;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Passageiro;
import ufg.br.fabricasw.jatai.swsisviagem.repository.UsuarioRepository;
import ufg.br.fabricasw.jatai.swsisviagem.service.PessoaService;
import ufg.br.fabricasw.jatai.swsisviagem.service.UsuarioService;

/**
 *
 * @author ronogue
 */
@Controller
@RequestMapping("/dashboard/meu-perfil")
public class MeuPerfilController {

    private static final String FORM_ATUALIZAR_PERFIL_VIEW  = "app/dashboard/meu-perfil/formulario";
    private static final String LINK_REDIRECT_FORM  = "redirect:/dashboard/meu-perfil";
    private static final String ATTR_USUARIO = "usuario";
    private static final String TITULO_PAGE  = "Atualizar dados de perfil";
    private static final String ATTR_ERROR   = "error";
    private static final String ATTR_INFO_AS_PASSAGEIRO = "infomacaoComoPassageiro";

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public String formAtualizarPerfilLogado(Model model) {

        Usuario usr = this.usuarioService.findUserLogado();

        if (usr.getPessoa().isCadastroCompleto()) {

            model.addAttribute(ATTR_INFO_AS_PASSAGEIRO, usr.getPessoa().getInfomacaoComoPassageiro());
        }
        else {

            model.addAttribute(ATTR_INFO_AS_PASSAGEIRO, new Passageiro());
        }
        model.addAttribute("roles", usr.getRoles());
        model.addAttribute("currentRole", usr.getCurrentRole());
        model.addAttribute(ATTR_USUARIO, usr);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_PAGE);

        return FORM_ATUALIZAR_PERFIL_VIEW;
    }

    @PostMapping("/formulario")
    public String salvarFormularioEmailPerfilLogado(@ModelAttribute("usuario") Usuario usuario) {

        Usuario usr = this.usuarioService.findUserLogado();
        usr.getPessoa().setEmail(usuario.getPessoa().getEmail());

        this.pessoaService.updateEmail(usr.getPessoa().getId(), usuario.getPessoa().getEmail());
        this.usuarioService.atualizarContextoUsuario(usr);

        return LINK_REDIRECT_FORM;
    }

    @PostMapping("/formulario/atualizarRole")
    @Transactional
    public String atualizarRole(@RequestParam("role") Role role) {
        // Pega o login do usuário autenticado
        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        // Recarrega a ENTIDADE gerenciada pelo JPA
        Usuario managed = usuarioRepository.findByLoginUnico(login);

        // Atualiza o currentRole
        managed.setCurrentRole(role);

        // (Opcional) garante que o role exista na lista de roles
        if (!managed.getRoles().contains(role)) {
            managed.getRoles().add(role);
        }

        // Como está @Transactional, nem precisaria chamar save(), mas pode também:
        // usuarioRepository.save(managed);

        usuarioService.atualizarContextoUsuario(managed);
        return LINK_REDIRECT_FORM;
    }

    @PostMapping("/formulario/atualizarCadastro")
    public String atualizarCadastro(@ModelAttribute("infomacaoComoPassageiro") Passageiro passageiro) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usr = usuarioRepository.findByLoginUnico(login);

        Pessoa pessoa   = usr.getPessoa();

        if (pessoa.getInfomacaoComoPassageiro() != null) {

            Long id_passageiro = pessoa.getInfomacaoComoPassageiro().getId();
            passageiro.setId(id_passageiro);
        }

        passageiro.setNome(pessoa.getNome());
        pessoa.setInfomacaoComoPassageiro(passageiro);
        pessoa.setUsuario(usr);

        usr = this.usuarioService.save(usr);
        this.usuarioService.atualizarContextoUsuario(usr);

        return LINK_REDIRECT_FORM;
    }

    @PostMapping("/formulario/password")
    public String salvarFormularioSenhaPerfilLogado(
        @ModelAttribute("current-password") String currentPassword,
        @ModelAttribute("new-password") String newPassword,
        RedirectAttributes redirectAttr) {

        Usuario usr = this.usuarioService.findUserLogado();
        redirectAttr.addFlashAttribute(ATTR_ERROR, false);

        if (usr.getPassword() != null) {

            boolean verifyPassword = this.usuarioService.verifyPassword(currentPassword, usr.getPassword());

            if (verifyPassword) {

                String encryptedPassword = this.usuarioService.encryptPassword(newPassword);

                usr.setPassword(encryptedPassword);

                this.usuarioService.updatePassword(usr.getId(), encryptedPassword);
                this.usuarioService.atualizarContextoUsuario(usr);
            }
            else {

                redirectAttr.addFlashAttribute(ATTR_ERROR, true);
            }
        }

        return LINK_REDIRECT_FORM;
    }

}
