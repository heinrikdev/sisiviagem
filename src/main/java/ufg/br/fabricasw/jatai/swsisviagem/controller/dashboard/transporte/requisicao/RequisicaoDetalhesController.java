package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.transporte.requisicao;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ufg.br.fabricasw.jatai.swsisviagem.controller.Constantes;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.RequisicaoPassageiro;
import ufg.br.fabricasw.jatai.swsisviagem.repository.projection.ViagemInfoProjection;
import ufg.br.fabricasw.jatai.swsisviagem.service.RequisicaoPassageiroService;
import ufg.br.fabricasw.jatai.swsisviagem.service.RequisicaoServiceImpl;
import ufg.br.fabricasw.jatai.swsisviagem.service.ViagemService;

/**
 * 
 * @author Ronaldo N. de Sousa
 */
@Controller("transporte_requisicao_detalhes")
@RequestMapping("/dashboard/transporte/requisicoes")
@PreAuthorize("hasAuthority('TRANSPORTE')")
public class RequisicaoDetalhesController {
    
    private static final String ATTR_REQUISICAO_PASSAGEIROS = "requisicaoPassageiros";
    private static final String VIEW_VISUALIZAR_REQUISICAO = "app/dashboard/transporte/requisicao/detalhes";
    private static final String VIAGENS = "viagens";
    
    @Autowired
    private RequisicaoServiceImpl requisicaoServiceImpl;
    
    @Autowired
    private RequisicaoPassageiroService requisicaoPassageiroService;
    
    @Autowired
    private ViagemService viagemService;
    
    @GetMapping("/detalhes/{requisicao_id}")
    public String visualizarDetalhes(Model model, @PathVariable("requisicao_id") Long requisicao_id) {
        
        Requisicao requisicao               = this.requisicaoServiceImpl.findById(requisicao_id);
        return this.viewDetalhesRequisicao(model, requisicao);
    }
    
    @GetMapping("/habilitarOuDesabilitarModoEdicao/{requisicao_id}")
    public String habilitarOuDesabilitarModoEdicao(Model model, @PathVariable("requisicao_id") Long requisicao_id) {
        
        this.requisicaoServiceImpl.habilitarOuDesabilitarModoEdicao(requisicao_id);
        Requisicao requisicao = this.requisicaoServiceImpl.findById(requisicao_id);
        
        return this.viewDetalhesRequisicao(model, requisicao);
    }
    
    @PostMapping("/adicionar_passageiro/{requisicao_id}")
    public String adicionarPassageiro(Model model, RequisicaoPassageiro requisicaoPassageiro, @PathVariable("requisicao_id") Long requisicao_id) {
        
        Requisicao requisicao  = this.requisicaoServiceImpl.findById(requisicao_id);
       
        requisicaoPassageiro.setRequisicao(requisicao);
        requisicao.getRequisicaoPassageiros().add(requisicaoPassageiro);
        
        this.requisicaoPassageiroService.salvar(requisicaoPassageiro);
        requisicao  = this.requisicaoServiceImpl.atualizar(requisicao);
        
        return this.viewDetalhesRequisicao(model, requisicao);
    }
    
    @GetMapping("/remover_viagem/{viagem_id}/{requisicao_id}")
    public String removerViagem(@PathVariable("requisicao_id") Long requisicao_id, @PathVariable("viagem_id") Long viagem_id ) {
        
        this.requisicaoServiceImpl.removerViagem(requisicao_id, viagem_id);
        return "redirect:/dashboard/transporte/requisicoes/detalhes/" + requisicao_id;
    }
    
    private String viewDetalhesRequisicao(Model model, Requisicao requisicao) {
        
        List<ViagemInfoProjection> viagens  = this.viagemService.findAllViagemProgramadaPara(requisicao.getId());
        
        final String titlePage = "Requisição " + requisicao.getId() + " - " + requisicao.getProposto().getNome();
        
        model.addAttribute(Constantes.ATTR_TITLE_PAGE,      titlePage);
        model.addAttribute(Constantes.ATTR_REQUISICAO,      requisicao);
        model.addAttribute(Constantes.ATTR_SESSION_ANEXOS,  requisicao.getAnexos());
        model.addAttribute(ATTR_REQUISICAO_PASSAGEIROS,     requisicao.getRequisicaoPassageiros());
        model.addAttribute(VIAGENS, viagens);
        
        model.addAttribute(Constantes.ATTR_SESSION_TRECHOS_IDA, requisicao.getTrechosDeIda());
        model.addAttribute(Constantes.ATTR_SESSION_TRECHOS_VOLTA, requisicao.getTrechosDeVolta());
        model.addAttribute("requisicaoPassageiro", new RequisicaoPassageiro());
        
        return VIEW_VISUALIZAR_REQUISICAO;
    }
}
