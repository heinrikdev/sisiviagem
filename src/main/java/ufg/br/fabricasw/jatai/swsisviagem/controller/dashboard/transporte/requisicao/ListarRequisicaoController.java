package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.transporte.requisicao;

import java.time.LocalDate;
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
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoSolicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.service.RequisicaoServiceImpl;

/**
 *
 * @author Ronaldo N. de Sousa
 */
@Controller("transporte_requisicao")
@RequestMapping("/dashboard/transporte/requisicoes")
@PreAuthorize("hasAuthority('TRANSPORTE')")
public class ListarRequisicaoController {

    private static final String TITULO_LIST_VIEW = "Requisições";
    private static final String ATTR_PAGE_INFO = "pageRequisicao";
    private static final String VIEW_LIST_REQUISICAOS = "app/dashboard/transporte/requisicao/listar";
    
    @Autowired
    private RequisicaoServiceImpl requisicaoService;

    @GetMapping
    public String index(Model model, @PageableDefault(size = 10) Pageable pageable, HttpServletRequest request) {

        Page<Requisicao> pageRequisicao = this.requisicaoService.findAllRequisicoesTransporte(pageable);
        Constantes.putPageInformationToViewModel(model, pageRequisicao, request);

        model.addAttribute(ATTR_PAGE_INFO, pageRequisicao);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LIST_VIEW);

        return VIEW_LIST_REQUISICAOS;
    }
    
    @GetMapping("/filtrar/numero")
    public String listarPorId(Model model, @RequestParam("value") Long id, @PageableDefault(size = 10) Pageable pageable, HttpServletRequest request) {

        Page<Requisicao> pageRequisicao = this.requisicaoService.findAllRequisicoesTransporte(id, pageable);
        Constantes.putPageInformationToViewModel(model, pageRequisicao, request);

        model.addAttribute(ATTR_PAGE_INFO, pageRequisicao);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LIST_VIEW);

        return VIEW_LIST_REQUISICAOS;
    }
    
    @GetMapping("/filtrar/proposto")
    public String listarPorProposto(Model model, @RequestParam("nome") String nome, @PageableDefault(size = 10) Pageable pageable, HttpServletRequest request) {

        Page<Requisicao> pageRequisicao = this.requisicaoService.findAllRequisicoesTransporte(nome, pageable);
        Constantes.putPageInformationToViewModel(model, pageRequisicao, request);

        model.addAttribute(ATTR_PAGE_INFO, pageRequisicao);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LIST_VIEW);

        return VIEW_LIST_REQUISICAOS;
    }
    
    @GetMapping("/filtrar/estado")
    public String listarPorEstadoSolicitacao(Model model, @RequestParam("value") EEstadoSolicitacao estado_solicitacao, @PageableDefault(size = 10) Pageable pageable, HttpServletRequest request) {

        Page<Requisicao> pageRequisicao = this.requisicaoService.findAllRequisicoesByEstado(estado_solicitacao, pageable);
        Constantes.putPageInformationToViewModel(model, pageRequisicao, request);

        model.addAttribute(ATTR_PAGE_INFO, pageRequisicao);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LIST_VIEW);

        return VIEW_LIST_REQUISICAOS;
    }
    
    @GetMapping("/filtrar/ano")
    public String listarPorEstadoSolicitacao(
        Model model, 
        @RequestParam("status") EEstadoSolicitacao estado_solicitacao,
        @RequestParam("ano") Integer ano,
        @PageableDefault(size = 10) Pageable pageable, 
        HttpServletRequest request) {

        Page<Requisicao> pageRequisicao = this.requisicaoService.findAllRequisicoesByEstado(estado_solicitacao, ano, pageable);
        Constantes.putPageInformationToViewModel(model, pageRequisicao, request);

        model.addAttribute(ATTR_PAGE_INFO, pageRequisicao);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LIST_VIEW);

        return VIEW_LIST_REQUISICAOS;
    }
    
    @GetMapping("/filtrar/data")
    public String listarPorEstadoSolicitacao(
        Model model, 
        @RequestParam("status") EEstadoSolicitacao estado_solicitacao,
        @RequestParam("data") LocalDate data,
        @PageableDefault(size = 10) Pageable pageable, 
        HttpServletRequest request) {

        Page<Requisicao> pageRequisicao = this.requisicaoService.findAllRequisicoesByEstado(estado_solicitacao, data, pageable);
        Constantes.putPageInformationToViewModel(model, pageRequisicao, request);

        model.addAttribute(ATTR_PAGE_INFO, pageRequisicao);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LIST_VIEW);

        return VIEW_LIST_REQUISICAOS;
    }

    @GetMapping("/filtrar/periodo")
    public String listarPorEstadoSolicitacao(
        Model model, 
        @RequestParam("status") EEstadoSolicitacao estado_solicitacao,
        @RequestParam("from") LocalDate from,
        @RequestParam("to") LocalDate to,
        @PageableDefault(size = 10) Pageable pageable, 
        HttpServletRequest request) {

        Page<Requisicao> pageRequisicao = this.requisicaoService.findAllRequisicoesByEstado(estado_solicitacao, from, to, pageable);
        Constantes.putPageInformationToViewModel(model, pageRequisicao, request);

        model.addAttribute(ATTR_PAGE_INFO, pageRequisicao);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LIST_VIEW);

        return VIEW_LIST_REQUISICAOS;
    }
}
