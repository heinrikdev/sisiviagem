package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.transporte.veiculo;

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
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Veiculo;
import ufg.br.fabricasw.jatai.swsisviagem.service.VeiculoService;

/**
 * Controller responsável por listar os veículos cadastrados no sistema.
 * 
 * @author Ronaldo N. de Sousa
 */
@Controller("transporte_veiculo_listar")
@RequestMapping("/dashboard/transporte/veiculo/listar")
@PreAuthorize("hasAuthority('TRANSPORTE')")
public class ListarVeiculoController {

    private static final String TITULO_LISTAR_VEICULO_VIEW = "Veículos";

    private static final String VIEW_LISTAR_VEICULO = "app/dashboard/transporte/veiculo/listar";
    private static final String ATTR_PAGE_INFO = "pageVeiculo";

    @Autowired
    private VeiculoService veiculoService;

    @GetMapping
    public String index(Model model, Pageable pageable, HttpServletRequest request) {

        Page<Veiculo> pageVeiculoAtivos = this.veiculoService.findAllVeiculosAtivos(pageable);
        Constantes.putPageInformationToViewModel(model, pageVeiculoAtivos, request);

        model.addAttribute(ATTR_PAGE_INFO, pageVeiculoAtivos);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LISTAR_VEICULO_VIEW);

        return VIEW_LISTAR_VEICULO;
    }

    @GetMapping("/todos")
    public String ListarTodos(Model model, Pageable pageable, HttpServletRequest request) {
        Page<Veiculo> pageVeiculoTodos = this.veiculoService.findAll(pageable);
        Constantes.putPageInformationToViewModel(model, pageVeiculoTodos, request);

        model.addAttribute(ATTR_PAGE_INFO, pageVeiculoTodos);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LISTAR_VEICULO_VIEW);

        return VIEW_LISTAR_VEICULO;
    }

    @GetMapping("/ativos")
    public String ListarAtivos(Model model, Pageable pageable, HttpServletRequest request) {
        Page<Veiculo> pageVeiculoAtivos = this.veiculoService.findAllVeiculosAtivos(pageable);
        Constantes.putPageInformationToViewModel(model, pageVeiculoAtivos, request);

        model.addAttribute(ATTR_PAGE_INFO, pageVeiculoAtivos);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LISTAR_VEICULO_VIEW);

        return VIEW_LISTAR_VEICULO;
    }

    @GetMapping("/inativos")
    public String ListarInativos(Model model, Pageable pageable, HttpServletRequest request) {
        Page<Veiculo> pageVeiculoInativos = this.veiculoService.findAllVeiculosInativos(pageable);
        Constantes.putPageInformationToViewModel(model, pageVeiculoInativos, request);

        model.addAttribute(ATTR_PAGE_INFO, pageVeiculoInativos);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LISTAR_VEICULO_VIEW);

        return VIEW_LISTAR_VEICULO;
    }
}
