package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.transporte.motorista;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ufg.br.fabricasw.jatai.swsisviagem.controller.Constantes;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Motorista;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Pessoa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.Role;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Usuario;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.ETipoUsuario;
import ufg.br.fabricasw.jatai.swsisviagem.service.MotoristaService;
import ufg.br.fabricasw.jatai.swsisviagem.service.UsuarioService;

/**
 * Controller responsável pelo fluxo de cadastro e atualização dos dados do 
 * motorista.
 * 
 * @author ronogue
 */
@Controller("transporte_motorista")
@RequestMapping("/dashboard/transporte/motorista")
@PreAuthorize("hasAuthority('TRANSPORTE')")
public class FormularioMotoristaController {
    
    private static final String TITULO_NOVO_MOTORISTA = "Adicionar Motorista";
    private static final String TITULO_EDIT_MOTORISTA = "Editar Motorista";
    
    private static final String VIEW_FORMULARIO_MOTORISTA  = "app/dashboard/transporte/motorista/formulario";
    private static final String ATTR_MOTORISTA  = "motorista";
    private static final String ATTR_USUARIO    = "usuario";
    private static final String ATTR_PESSOA     = "pessoa";
    
    private static final String REDIRECT_LINK_LISTAR_MOTORISTAS = "redirect:/dashboard/transporte/motorista/listar";
    
    @Autowired
    private MotoristaService motoristaService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping
    public String index(Model model) {

        Motorista motorista = new Motorista();
        Pessoa pessoa       = new Pessoa();
        Usuario usuario     = new Usuario();
        
        model.addAttribute(ATTR_PESSOA,     pessoa);
        model.addAttribute(ATTR_USUARIO,    usuario);
        model.addAttribute(ATTR_MOTORISTA,  motorista);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_NOVO_MOTORISTA);
        
        return VIEW_FORMULARIO_MOTORISTA;
    }
    
    @GetMapping("/{motorista_id}")
    public String indexEditar(Model model, @PathVariable("motorista_id") Long motorista_id) {
        
        Motorista motorista = this.motoristaService.find(motorista_id);
        
        Pessoa pessoa       = motorista.getPessoa();
        Usuario usuario     = pessoa.getUsuario();
        
        model.addAttribute(ATTR_PESSOA,     pessoa);
        model.addAttribute(ATTR_USUARIO,    usuario);
        model.addAttribute(ATTR_MOTORISTA,  motorista);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_EDIT_MOTORISTA);
        
        return VIEW_FORMULARIO_MOTORISTA;
    }
    
    @GetMapping("/alternar_status/{motorista_id}/{status_atual}")
    public String alterarStatus(@PathVariable("motorista_id") Long id, @PathVariable("status_atual") Boolean status_atual) {
        
        this.motoristaService.alternarStatus(id, status_atual);
        return REDIRECT_LINK_LISTAR_MOTORISTAS;
    }
    
    /**
     * Salva o novo motorista no banco de dados.
     * 
     * O novo motorista é criado com uma senha padrão: 123456, e o usuário do 
     * mesmo é ativado.
     * 
     * @param motorista
     * @param pessoa
     * @param usuario
     * @return 
     */
    @PostMapping
    public String submitFormulario(Motorista motorista, Pessoa pessoa, Usuario usuario) {
        
        usuario.getRoles().add(Role.MOTORISTA);
        usuario.setTipoUsuario(ETipoUsuario.Motorista);
        
        if (motorista.getId() != null) {
            
            Usuario usr = this.usuarioService.find(usuario.getId());
            usuario.setPassword(usr.getPassword());
            usuario.setEnabled(usr.getEnabled());
            
            System.out.println("EXISTS");
        }
        else {
            System.out.println("NOT EXISTS");
            
            usuario.setPassword("e10adc3949ba59abbe56e057f20f883e");
            usuario.setEnabled(true);
        }
        
        pessoa.setUsuario(usuario);
        usuario.setPessoa(pessoa);
        motorista.setPessoa(pessoa);
        
        this.motoristaService.save(motorista);
        return REDIRECT_LINK_LISTAR_MOTORISTAS;
    }
}
