package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.proposto;

import java.io.ByteArrayInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ufg.br.fabricasw.jatai.swsisviagem.componente.RelatorioViagem;
import ufg.br.fabricasw.jatai.swsisviagem.controller.Constantes;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoRequisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoSolicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.Solicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.service.RequisicaoServiceImpl;
import ufg.br.fabricasw.jatai.swsisviagem.service.SolicitacaoServiceImpl;

/**
 * O proposto deve ter a possibilidade de visualizar os detalhes da requisição
 * e ter a opção de editá-las quando possível.
 * 
 *  
 * Este controller lida com esta operação.
 * @author Ronaldo N. de Sousa
 */
@Controller("proposto_requisicao_detalhes")
@RequestMapping("/dashboard/proposto")
@PreAuthorize("hasAuthority('USER')")
public class RequisicaoDetalhesController {
    
    private static final String VIEW_DETALHES_REQUISICAO    = "app/dashboard/proposto/requisicao/detalhes";
    private static final String ATTR_REQUISICAO_PASSAGEIROS = "requisicaoPassageiros";
    private static final String LINK_REDIRECT_FORM_EDITAR = "redirect:/dashboard/proposto/requisicoes/editar/";

    @Autowired
    private RequisicaoServiceImpl requisicaoService;
    
    @Autowired
    private SolicitacaoServiceImpl solicitacaoService;
    
    @GetMapping("/requisicoes/detalhes/{requisicao_id}")
    public String verDetalhes(Model model, @PathVariable("requisicao_id") Long requisicao_id) {
        
        Requisicao requisicao = this.requisicaoService.findById(requisicao_id);
        
        final String titlePage = "Requisição " + requisicao.getId() + " - " + requisicao.getProposto().getNome();
        
        model.addAttribute(Constantes.ATTR_TITLE_PAGE,     titlePage);
        model.addAttribute(Constantes.ATTR_REQUISICAO,     requisicao);
        model.addAttribute(Constantes.ATTR_SESSION_ANEXOS, requisicao.getAnexos());
        model.addAttribute(ATTR_REQUISICAO_PASSAGEIROS,    requisicao.getRequisicaoPassageiros());
        
        model.addAttribute(Constantes.ATTR_SESSION_TRECHOS_IDA, requisicao.getTrechosDeIda());
        model.addAttribute(Constantes.ATTR_SESSION_TRECHOS_VOLTA, requisicao.getTrechosDeVolta());
        model.addAttribute(Constantes.ATTR_LISTA_PRESTACAO_CONTAS, requisicao.getPrestacaoDeContas());
        
        return VIEW_DETALHES_REQUISICAO;
    }
    
    @GetMapping("/formulario_diaria_transporte/{requisicao_id}")
    public ResponseEntity<InputStreamResource> gerarFormularioDiariasETransporte(@PathVariable("requisicao_id") Long requisicao_id) {
        
        Requisicao requisicao = this.requisicaoService.find(requisicao_id);
        
        final String filename = "inline; " + requisicao_id + " - formulario_diaria_transporte.pdf";
        
        ByteArrayInputStream bis = RelatorioViagem.formulario(requisicao);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", filename);

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(bis));
    }
    
    @GetMapping("/requisicoes/reenviar_sem_trasnporte/{requisicao_id}")
    public String reenviarSemTrasnporte(@PathVariable("requisicao_id") Long requisicao_id, RedirectAttributes redirectAttributes) {
        
        Requisicao requisicao = this.requisicaoService.find(requisicao_id);
        
        Solicitacao transporte = requisicao.getTransporte();
        
        requisicao.removerSolicitacao(transporte);
        requisicao.setEstadoRequisicao(EEstadoRequisicao.PREENCHENDO);
        requisicao.changeStatusSolicitacoes(EEstadoSolicitacao.PREENCHENDO);
        
        
        this.requisicaoService.salvar(requisicao);
        this.solicitacaoService.apagar(transporte);
        
        redirectAttributes.addFlashAttribute("isReenviarSemTransporte", true);
        
        return LINK_REDIRECT_FORM_EDITAR + requisicao_id + "?#trecho";
    }
}
