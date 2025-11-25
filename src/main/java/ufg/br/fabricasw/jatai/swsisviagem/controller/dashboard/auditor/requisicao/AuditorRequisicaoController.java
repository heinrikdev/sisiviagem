package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.auditor.requisicao;

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
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoSolicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.service.auditor.AuditorRequisicaoService;

/**
 * Quando todas as solicitações de uma requisição são atendidas, o auditor fará
 * o lançamento da prestação de contas no SCDP.
 * 
 * Este controller exibi as requisições para o auditor. O auditor só visualiza
 * as requisições aprovadas. Cada tipode unidade possui um auditor responsável.
 * 
 * @author ronogue
 */
@Controller
@RequestMapping("/dashboard/auditor/requisicoes")
@PreAuthorize("hasAuthority('AUDITOR')")
public class AuditorRequisicaoController {
    
    private static final String TITULO_LIST_VIEW = "Requisições";
    private static final String ATTR_PAGE_INFO = "pageRequisicao";
    private static final String VIEW_LIST_REQUISICAOS = "app/dashboard/auditor/requisicao/listar";
    
    @Autowired
    private AuditorRequisicaoService requisicaoService;
    
    @GetMapping
    public String index(Model model, @PageableDefault(size = 10) Pageable pageable, HttpServletRequest request) {

        Page<Requisicao> pageRequisicao = this.requisicaoService.findAll(pageable);
        Constantes.putPageInformationToViewModel(model, pageRequisicao, request);

        model.addAttribute(ATTR_PAGE_INFO, pageRequisicao);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LIST_VIEW);

        return VIEW_LIST_REQUISICAOS;
    }
    
    
    @GetMapping("/filtrar/numero")
    public String listarPorId(Model model, @RequestParam("value") Long id, @PageableDefault(size = 10) Pageable pageable, HttpServletRequest request) {

        Page<Requisicao> pageRequisicao = this.requisicaoService.findAllByRequisicao(id, pageable);
        Constantes.putPageInformationToViewModel(model, pageRequisicao, request);

        model.addAttribute(ATTR_PAGE_INFO, pageRequisicao);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LIST_VIEW);

        return VIEW_LIST_REQUISICAOS;
    }
    
    @GetMapping("/filtrar/proposto")
    public String listarPorProposto(Model model, @RequestParam("nome") String nome, @PageableDefault(size = 10) Pageable pageable, HttpServletRequest request) {

        Page<Requisicao> pageRequisicao = this.requisicaoService.findAllByProposto(nome, pageable);
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

        Page<Requisicao> pageRequisicao = this.requisicaoService.findAll(estado_solicitacao, ano, pageable);
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

        Page<Requisicao> pageRequisicao = this.requisicaoService.findAll(estado_solicitacao, data, pageable);
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

        Page<Requisicao> pageRequisicao = this.requisicaoService.findAll(estado_solicitacao, from, to, pageable);
        Constantes.putPageInformationToViewModel(model, pageRequisicao, request);

        model.addAttribute(ATTR_PAGE_INFO, pageRequisicao);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LIST_VIEW);

        return VIEW_LIST_REQUISICAOS;
    }
}
