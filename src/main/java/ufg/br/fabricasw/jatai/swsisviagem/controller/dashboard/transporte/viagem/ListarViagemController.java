package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.transporte.viagem;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ufg.br.fabricasw.jatai.swsisviagem.controller.Constantes;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Viagem;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoViagem;
import ufg.br.fabricasw.jatai.swsisviagem.service.ViagemService;

/**
 * Controller responsável pelo fluxo de exibição de viagens cadastradas.
 * 
 * @author ronogue
 */
@Controller("transporte_viagem_listar")
@RequestMapping("/dashboard/transporte/viagem/listar")
@PreAuthorize("hasAuthority('TRANSPORTE')")
public class ListarViagemController {

    private static final String VIEW_LISTAR_VIAGEM = "app/dashboard/transporte/viagem/listar";
    private static final String TITULO_LISTAR_VIAGEM = "Viagens ";
    private static final String ATTR_PAGE_INFO_VIAGEM = "pageViagem";

    @Autowired
    private ViagemService viagemService;

    @GetMapping
    public String index(Model model, @PageableDefault(size = 10) Pageable pageable, HttpServletRequest request) {

        Page<Viagem> pageViagem = this.viagemService.findAllByStatus(EEstadoViagem.Aberta, pageable);
        Constantes.putPageInformationToViewModel(model, pageViagem, request);

        model.addAttribute(ATTR_PAGE_INFO_VIAGEM, pageViagem);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LISTAR_VIAGEM);

        return VIEW_LISTAR_VIAGEM;
    }

    @GetMapping("/filtrar/estado_viagem")
    public String porEstadoViagem(Model model, @RequestParam("value") EEstadoViagem value,
            @PageableDefault(size = 10) Pageable pageable, HttpServletRequest request) {

        Page<Viagem> pageViagem = this.viagemService.findAllByStatus(value, pageable);
        Constantes.putPageInformationToViewModel(model, pageViagem, request);

        model.addAttribute(ATTR_PAGE_INFO_VIAGEM, pageViagem);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LISTAR_VIAGEM);

        return VIEW_LISTAR_VIAGEM;
    }

    @GetMapping("/filtrar/numero")
    public String porNumeroViagem(Model model, @RequestParam("value") Long viagem_id,
            @PageableDefault(size = 10) Pageable pageable, HttpServletRequest request) {

        Page<Viagem> pageViagem = this.viagemService.findById(viagem_id, pageable);
        Constantes.putPageInformationToViewModel(model, pageViagem, request);

        model.addAttribute(ATTR_PAGE_INFO_VIAGEM, pageViagem);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LISTAR_VIAGEM);

        return VIEW_LISTAR_VIAGEM;
    }
}
