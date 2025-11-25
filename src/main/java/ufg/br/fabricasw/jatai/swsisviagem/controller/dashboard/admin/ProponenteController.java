package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.admin;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ufg.br.fabricasw.jatai.swsisviagem.controller.Constantes;
import ufg.br.fabricasw.jatai.swsisviagem.domain.UnidadeDepartamento;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Usuario;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.ETipoUsuario;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.Role;
import ufg.br.fabricasw.jatai.swsisviagem.domain.factories.UsuarioRolesFactorie;
import ufg.br.fabricasw.jatai.swsisviagem.service.UnidadeService;
import ufg.br.fabricasw.jatai.swsisviagem.service.UsuarioService;

/**
 *
 * @author Ronaldo N. de Sousa
 */
@Controller
@RequestMapping("/dashboard/admin/proponente")
@PreAuthorize("hasAuthority('ADMIN')")
public class ProponenteController {
    
    /*
        TITULOS DAS PÁGINAS HTML, SERÃO UTILIZADAS PELO THYMELEAF
    */
    private static final String TITULO_LISTAR           = "Proponentes cadastrados";
    private static final String TITULO_CADASTRAR        = "Cadastrar proponente";
    private static final String TITULO_ATUALIZAR        = "Atualizar proponente";
    
    /*
       CAMINHO PARA O HTML/VIEW 
    */
    private static final String VIEW_LISTAR             = "app/dashboard/admin/proponente/listar";
    private static final String VIEW_FORMULARIO         = "app/dashboard/admin/proponente/formulario";
    
    /*
        LINKS DE REDIRECIONAMENTO
     */
    private static final String LINK_REDIRECT_LISTAR    = "redirect:/dashboard/admin/proponente";

    /*
        NOMES DE ATRIBUTOS UTILIZADO NAS MODELS
     */
    private static final String ATTR_PAGE_USUARIOS      = "pageUsuarios";
    private static final String ATTR_USUARIO            = "proponente";
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private UnidadeService unidadeService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String listarProponentes(Model model, Pageable pageable, HttpServletRequest request) {
        
        Page<Usuario> pageProponentes = this.usuarioService.findAllProponente(pageable);
        Constantes.putPageInformationToViewModel(model, pageProponentes, request);
        
        model.addAttribute(ATTR_PAGE_USUARIOS, pageProponentes);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LISTAR);

        return VIEW_LISTAR;
    }
    
    @GetMapping("/queryBy")
    public String listarProponentesPorNome(@PathParam("nome") String nome, Model model, Pageable pageable, HttpServletRequest request) {

        Page<Usuario> pageProponentes = this.usuarioService.findAllProponente(nome, pageable);
        Constantes.putPageInformationToViewModel(model, pageProponentes, request);
        
        model.addAttribute(ATTR_PAGE_USUARIOS, pageProponentes);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LISTAR);
        
        return VIEW_LISTAR;
    }
    
    @GetMapping("/cadastrar")
    public String formCadastrarUsuario(Model model) {
        
        List<UnidadeDepartamento> unidades = unidadeService.findAll();
        Usuario usr = new Usuario();

        model.addAttribute("allRoles", Role.values());
        model.addAttribute(ATTR_USUARIO, usr);
        model.addAttribute(Constantes.ATTR_UNIDADES, unidades);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_CADASTRAR);
        model.addAttribute(Constantes.ATTR_UNIDADES_DEPARTAMENTO, Constantes.UNIDADES_DEPARTAMENTO);
        
        return VIEW_FORMULARIO;                
    }
    
    @GetMapping("/editar/{id}")
    public String formAtualizarUsuario(Model model, @PathVariable("id") Long id) {
        
        List<UnidadeDepartamento> unidades = unidadeService.findAll();
        Usuario usuario = this.usuarioService.find(id);
        usuario.setPassword("");

        model.addAttribute("allRoles", Role.values());
        model.addAttribute(ATTR_USUARIO, usuario);
        model.addAttribute(Constantes.ATTR_UNIDADES, unidades);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_ATUALIZAR);
        model.addAttribute(Constantes.ATTR_UNIDADES_DEPARTAMENTO, Constantes.UNIDADES_DEPARTAMENTO);
        
        return VIEW_FORMULARIO;                
    }
    
    @GetMapping("/ativar_desativar/{usuario_id}/{novo_status}")
    public String desativarAtivar(@PathVariable("usuario_id") Long usuario_id, @PathVariable("novo_status") Boolean novo_status) {
        
        this.usuarioService.desativarAtivar(usuario_id, novo_status);
        return LINK_REDIRECT_LISTAR;
    }
    
    @PostMapping("/formulario")
    public String salvarFormularioUsuario(@ModelAttribute("proponente") Usuario proponente) {
        System.out.println(proponente.getPerfil());

        proponente.getRoles().stream().forEach(role ->
                System.out.println("Role: " + role));
//        List<Role> roles  = UsuarioRolesFactorie.build(proponente.getPerfil());
        
//        if(!proponente.getPassword().trim().isEmpty()) {
//            
//            String encryptedPsw = this.passwordEncoder.encode(proponente.getPassword());
//            proponente.setPassword(encryptedPsw);
//        }
//        else if (proponente.getId() != null){
//            
//            String encryptedPsw = this.usuarioService.find(proponente.getId()).getPassword();
//            proponente.setPassword(encryptedPsw);
//        }

        String encryptedPsw = this.passwordEncoder.encode(proponente.getPassword());
        proponente.setPassword(encryptedPsw);
        proponente.setTipoUsuario(ETipoUsuario.Desacordo);
        proponente.getPessoa().setUsuario(proponente);
        
        this.usuarioService.save(proponente);
        return LINK_REDIRECT_LISTAR;
    }
}
