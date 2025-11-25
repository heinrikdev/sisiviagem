package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ufg.br.fabricasw.jatai.swsisviagem.controller.Constantes;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Pessoa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.UnidadeDepartamento;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Usuario;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.ETipoUsuario;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.Role;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Passageiro;
import ufg.br.fabricasw.jatai.swsisviagem.service.PessoaService;
import ufg.br.fabricasw.jatai.swsisviagem.service.UnidadeService;
import ufg.br.fabricasw.jatai.swsisviagem.service.UsuarioService;
import ufg.br.fabricasw.jatai.swsisviagem.service.email.EmailNovoUsuarioBoasVindasService;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.List;

/**
 *
 * @author Ronaldo N. de Sousa
 */
@Controller
@RequestMapping("/dashboard/admin/usuario")
@PreAuthorize("hasAuthority('ADMIN')")
public class UsuariosController {

    /*
        TITULOS DAS PÁGINAS HTML, SERÃO UTILIZADAS PELO THYMELEAF
     */
    private static final String TITULO_LISTAR = "Usuários cadastrados";
    private static final String TITULO_CADASTRAR = "Cadastrar usuário";
    private static final String TITULO_ATUALIZAR = "Atualizar usuário";

    /*
       CAMINHO PARA O HTML/VIEW 
     */
    private static final String VIEW_LISTAR = "app/dashboard/admin/usuario/listar";
    private static final String VIEW_FORMULARIO = "app/dashboard/admin/usuario/formulario";

    /*
        LINKS DE REDIRECIONAMENTO
     */
    private static final String LINK_REDIRECT_LISTAR = "redirect:/dashboard/admin/usuario";

    /*
        NOMES DE ATRIBUTOS UTILIZADO NAS MODELS
     */
    private static final String ATTR_PAGE_USUARIOS = "pageUsuarios";
    private static final String ATTR_USUARIO = "usuario";
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private UnidadeService unidadeService;
    
    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private EmailNovoUsuarioBoasVindasService emailNovoUsuarioBoasVindasService;
    
    
    @GetMapping
    public String listarUsuarios(Model model, Pageable pageable, HttpServletRequest request) {
        
        Page<Usuario> pageUsuarios = this.usuarioService.findAllUsuarios(pageable);
        Constantes.putPageInformationToViewModel(model, pageUsuarios,request);
        
        model.addAttribute(ATTR_PAGE_USUARIOS, pageUsuarios);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LISTAR);
        
        return VIEW_LISTAR;
    }
    
    @GetMapping("/queryBy")
    public String listarUsuariosPorNome(@PathParam("nome")String nome, Model model, Pageable pageable, HttpServletRequest request) {
        
        Page<Usuario> pageUsuarios = this.usuarioService.findAllUsuarios(nome, pageable);
        Constantes.putPageInformationToViewModel(model, pageUsuarios, request);
        
        model.addAttribute(ATTR_PAGE_USUARIOS, pageUsuarios);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LISTAR);
        
        return VIEW_LISTAR;
    }
    
    @GetMapping("/cadastrar")
    public String formCadastrarUsuario(Model model) {
        
        List<UnidadeDepartamento> unidades = this.unidadeService.findAll();
        Usuario usuario = new Usuario();
        usuario.setPessoa(new Pessoa());
        
        model.addAttribute(ATTR_USUARIO, usuario);
        model.addAttribute(Constantes.ATTR_UNIDADES, unidades);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_CADASTRAR);
        
        return VIEW_FORMULARIO;
    }
    
    @GetMapping("/editar/{id}")
    public String formEditarUsuario(Model model,@PathVariable("id")Long id) {
        
        List<UnidadeDepartamento> unidades = this.unidadeService.findAll();
        Usuario usuario = this.usuarioService.find(id);
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("unidades", unidades);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_ATUALIZAR);
        
        return VIEW_FORMULARIO;
    }
    
    /**
     * Faz com que o admin mude de sua visão para a visão do usuário selecionado.
     * 
     * @param idUser
     * @return 
     */
    @GetMapping("/logarComo/{idUser}")
    public String logarAs(@PathVariable("idUser")Long idUser) {
        
        Usuario usr = this.usuarioService.find(idUser);
        this.usuarioService.logarComo(usr);
        
        return Constantes.LINK_REDIRECT_TO_DASHBOARD;
    }
    
    @GetMapping("/ativar_desativar/{usuario_id}/{novo_status}")
    public String desativarAtivar(@PathVariable("usuario_id") Long usuario_id, @PathVariable("novo_status") Boolean novo_status) {
        
        this.usuarioService.desativarAtivar(usuario_id, novo_status);
        return LINK_REDIRECT_LISTAR;
    }
    
    @PostMapping("/formulario") 
    public String salvarFormularioUsuario(@ModelAttribute("usuario") Usuario usuario) {
        
        Pessoa pessoa = usuario.getPessoa();
        pessoa.setUsuario(usuario);
        
        if (pessoa.getId() != null) {
            
            Passageiro infoComoPassageiro = this.pessoaService.fetchPassageiroInfo(pessoa.getId());
            
            if (infoComoPassageiro != null) {
                pessoa.setInfomacaoComoPassageiro(infoComoPassageiro);
            }
        }
        
        usuario.setTipoUsuario(ETipoUsuario.Proponente);
        usuario.getRoles().add(Role.USER);
        
        this.pessoaService.save(pessoa);
        this.emailNovoUsuarioBoasVindasService.send(usuario);
        
        return LINK_REDIRECT_LISTAR;
    }
}
