package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.unidades.requisicao;

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
import ufg.br.fabricasw.jatai.swsisviagem.service.unidades.UnidadesRequisicaoService;

/**
 *  
 * @author Ronaldo N. de Sousa
 */
@Controller("unidades_requisicao")
@RequestMapping("/dashboard/diaria_passagens/requisicoes")
@PreAuthorize("hasAuthority('DIARIA') || hasAuthority('PASSAGEM')")
public class ListarRequisicaoController {

    private static final String TITULO_LIST_VIEW = "Requisições";
    private static final String ATTR_PAGE_INFO = "pageRequisicao";
    private static final String VIEW_LIST_REQUISICAOS = "app/dashboard/unidades/requisicao/listar";
    private static final String EMPTY_LIST = "msgEmpty";
    
    
    @Autowired
    private UnidadesRequisicaoService requisicaoService;

    @GetMapping
    public String index(Model model, @PageableDefault(size = 10) Pageable pageable, HttpServletRequest request) {

        Page<Requisicao> pageRequisicao = this.requisicaoService.findAll(pageable);
        Constantes.putPageInformationToViewModel(model, pageRequisicao, request);

        model.addAttribute(ATTR_PAGE_INFO, pageRequisicao);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LIST_VIEW);
        
        if (!pageRequisicao.hasContent()) {
            
            model.addAttribute(EMPTY_LIST, "Você não tem solicitação para ser aprovada, para listar outros status utilize a opção filtrar.");
        }

        return VIEW_LIST_REQUISICAOS;
    }
    
    @GetMapping("/filtrar/numero")
    public String listarPorId(Model model, @RequestParam("value") Long id, @PageableDefault(size = 10) Pageable pageable, HttpServletRequest request) {

        Page<Requisicao> pageRequisicao = this.requisicaoService.findAllByRequisicao(id, pageable);
        Constantes.putPageInformationToViewModel(model, pageRequisicao, request);

        model.addAttribute(ATTR_PAGE_INFO, pageRequisicao);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LIST_VIEW);
        
        if (!pageRequisicao.hasContent()) {
            
            model.addAttribute(EMPTY_LIST, "Não foi encontrada requisição de número " + id + ", use o botão \"filtrar\" acima para mais opções.");
        } 
        return VIEW_LIST_REQUISICAOS;
    }
    
    @GetMapping("/filtrar/proposto")
    public String listarPorProposto(Model model, @RequestParam("nome") String nome, @PageableDefault(size = 10) Pageable pageable, HttpServletRequest request) {

        Page<Requisicao> pageRequisicao = this.requisicaoService.findAllByProposto(nome, pageable);
        Constantes.putPageInformationToViewModel(model, pageRequisicao, request);

        model.addAttribute(ATTR_PAGE_INFO, pageRequisicao);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LIST_VIEW);
        
        if (!pageRequisicao.hasContent()) {
            
            model.addAttribute(EMPTY_LIST, "Não foi encontrada requisição do proposto " + nome + ", use o botão \"filtrar\" acima para mais opções.");
        }

        return VIEW_LIST_REQUISICAOS;
    }
    
    @GetMapping("/filtrar/estado")
    public String listarPorEstadoSolicitacao(Model model, @RequestParam("value") EEstadoSolicitacao estado_solicitacao, @PageableDefault(size = 10) Pageable pageable, HttpServletRequest request) {

        String status_solicitacao = estado_solicitacao.toString().toUpperCase();
        
        Page<Requisicao> pageRequisicao = this.requisicaoService.findAllByStatus(status_solicitacao, pageable);
        Constantes.putPageInformationToViewModel(model, pageRequisicao, request);
        
        if (!pageRequisicao.hasContent()) {
            
            model.addAttribute(EMPTY_LIST, "Não foram econtradas requisições com os parâmetros fornecidos, use o botão \"filtrar\" acima para mais opções.");
        }

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

        Page<Requisicao> pageRequisicao = this.requisicaoService.findAllByStatus(estado_solicitacao, ano, pageable);
        Constantes.putPageInformationToViewModel(model, pageRequisicao, request);

        model.addAttribute(ATTR_PAGE_INFO, pageRequisicao);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LIST_VIEW);
        
        if (!pageRequisicao.hasContent()) {
            
            model.addAttribute(EMPTY_LIST, "Não foram econtradas requisições com os parâmetros fornecidos, use o botão \"filtrar\" acima para mais opções.");
        }

        return VIEW_LIST_REQUISICAOS;
    }
    
    @GetMapping("/filtrar/data")
    public String listarPorEstadoSolicitacao(
        Model model, 
        @RequestParam("status") EEstadoSolicitacao estado_solicitacao,
        @RequestParam("data") LocalDate data,
        @PageableDefault(size = 10) Pageable pageable, 
        HttpServletRequest request) {

        Page<Requisicao> pageRequisicao = this.requisicaoService.findAllByStatus(estado_solicitacao, data, pageable);
        Constantes.putPageInformationToViewModel(model, pageRequisicao, request);

        model.addAttribute(ATTR_PAGE_INFO, pageRequisicao);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LIST_VIEW);
        
        if (!pageRequisicao.hasContent()) {
            
            model.addAttribute(EMPTY_LIST, "Não foram econtradas requisições com os parâmetros fornecidos, use o botão \"filtrar\" acima para mais opções.");
        }

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

        Page<Requisicao> pageRequisicao = this.requisicaoService.findAllByStatus(estado_solicitacao, from, to, pageable);
        Constantes.putPageInformationToViewModel(model, pageRequisicao, request);

        model.addAttribute(ATTR_PAGE_INFO, pageRequisicao);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_LIST_VIEW);
        
        if (!pageRequisicao.hasContent()) {
            
            model.addAttribute(EMPTY_LIST, "Não foram econtradas requisições com os parâmetros fornecidos, use o botão \"filtrar\" acima para mais opções.");
        }

        return VIEW_LIST_REQUISICAOS;
    }
}
