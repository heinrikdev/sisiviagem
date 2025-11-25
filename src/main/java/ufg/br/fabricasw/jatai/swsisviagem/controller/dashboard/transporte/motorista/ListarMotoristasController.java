package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.transporte.motorista;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ufg.br.fabricasw.jatai.swsisviagem.controller.Constantes;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Motorista;
import ufg.br.fabricasw.jatai.swsisviagem.service.MotoristaService;

/**
 * Controller responsável por listar os motoristas cadastrados.
 * 
 * @author ronogue
 */
@Controller("transporte_motorista_listar")
@RequestMapping("/dashboard/transporte/motorista/listar")
@PreAuthorize("hasAuthority('TRANSPORTE')")
public class ListarMotoristasController {
    
    private static final String TITULO_LIST_VIEW = "Motoristas";
    
    private static final String VIEW_LISTAR_MOTORISTA   = "app/dashboard/transporte/motorista/listar";
    private static final String ATTR_PAGE_INFO          = "pageMotorista";
    
    @Autowired
    private MotoristaService motoristaService;
    
    @GetMapping
    public String index(Model model, Pageable pageable, HttpServletRequest request) {
        
        Page<Motorista> pageMotoristaAtivos = this.motoristaService.findAllMotoritasAtivos(pageable);
        Constantes.putPageInformationToViewModel(model, pageMotoristaAtivos, request);

        model.addAttribute(ATTR_PAGE_INFO, pageMotoristaAtivos);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LIST_VIEW);
        
        return VIEW_LISTAR_MOTORISTA;
    }
    
    @GetMapping("/todos")
    public String ListarTodos(Model model, Pageable pageable, HttpServletRequest request){
        Page<Motorista> pageMotorista = this.motoristaService.findAll(pageable);
        Constantes.putPageInformationToViewModel(model, pageMotorista, request);

        model.addAttribute(ATTR_PAGE_INFO, pageMotorista);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LIST_VIEW);
        
        return VIEW_LISTAR_MOTORISTA;
    }
    
    @GetMapping("/ativos")
    public String ListarAtivos(Model model, Pageable pageable, HttpServletRequest request){
        Page<Motorista> pageMotoristaAtivos = this.motoristaService.findAllMotoritasAtivos(pageable);
        Constantes.putPageInformationToViewModel(model, pageMotoristaAtivos, request);

        model.addAttribute(ATTR_PAGE_INFO, pageMotoristaAtivos);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LIST_VIEW);
        
        return VIEW_LISTAR_MOTORISTA;
    }
    
    @GetMapping("/inativos")
    public String ListarInativos(Model model, Pageable pageable, HttpServletRequest request){
        Page<Motorista> pageMotoristaInativos = this.motoristaService.findAllMotoritasInativos(pageable);
        Constantes.putPageInformationToViewModel(model, pageMotoristaInativos, request);

        model.addAttribute(ATTR_PAGE_INFO, pageMotoristaInativos);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LIST_VIEW);
        
        return VIEW_LISTAR_MOTORISTA;
    }
}
