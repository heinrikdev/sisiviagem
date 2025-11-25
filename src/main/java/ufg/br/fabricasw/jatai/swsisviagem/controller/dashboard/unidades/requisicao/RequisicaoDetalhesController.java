package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.unidades.requisicao;

import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ufg.br.fabricasw.jatai.swsisviagem.componente.RelatorioViagem;
import ufg.br.fabricasw.jatai.swsisviagem.controller.Constantes;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Justificativa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.service.JustificativaService;
import ufg.br.fabricasw.jatai.swsisviagem.service.RequisicaoServiceImpl;

/**
 *
 * @author Ronaldo N. de Sousa
 */
@Controller("unidades_requisicao_detalhes")
@RequestMapping("/dashboard/unidades/requisicoes")
@PreAuthorize("hasAuthority('DIARIA') || hasAuthority('PASSAGEM')")
public class RequisicaoDetalhesController {

    private static final String ATTR_REQUISICAO_PASSAGEIROS = "requisicaoPassageiros";
    private static final String VIEW_VISUALIZAR_REQUISICAO = "app/dashboard/unidades/requisicao/detalhes";
    private static final String ATTR_IS_DETALHES = "isDetalhes";
    private static final String ATTR_LINK_JUSTIFICATIVA = "LINK_JUSTIFICATIVA";
    
    @Autowired
    private RequisicaoServiceImpl requisicaoServiceImpl;
    
    @Autowired
    private JustificativaService justificativaService;
    
    @GetMapping("/detalhes/{requisicao_id}")
    public String visualizarDetalhes(Model model, @PathVariable("requisicao_id") Long requisicao_id) {
        
        Requisicao requisicao = this.requisicaoServiceImpl.findById(requisicao_id);
        
        final String titlePage = "Requisição " + requisicao.getId() + " - " + requisicao.getProposto().getNome();
        final String link_justificativa = "/dashboard/unidades/requisicoes/detalhes/justificativa_pedido_fora_prazo_legal/" + requisicao_id;
        
        model.addAttribute(Constantes.ATTR_TITLE_PAGE,      titlePage);
        model.addAttribute(Constantes.ATTR_REQUISICAO,      requisicao);
        model.addAttribute(Constantes.ATTR_SESSION_ANEXOS,  requisicao.getAnexos());
        model.addAttribute(ATTR_REQUISICAO_PASSAGEIROS,     requisicao.getRequisicaoPassageiros());
        model.addAttribute(Constantes.ATTR_SESSION_TRECHOS_IDA, requisicao.getTrechosDeIda());
        model.addAttribute(Constantes.ATTR_SESSION_TRECHOS_VOLTA, requisicao.getTrechosDeVolta());
        model.addAttribute(ATTR_IS_DETALHES, true);
        model.addAttribute(Constantes.ATTR_LISTA_PRESTACAO_CONTAS, requisicao.getPrestacaoDeContas());
        model.addAttribute(ATTR_LINK_JUSTIFICATIVA, link_justificativa);
        
        return VIEW_VISUALIZAR_REQUISICAO;
    }
    
    /**
     * Quando há um pedido fora do prazo legal é nescessário gerar uma justificativa.
     * 
     * @param requisicao_id identificação da requisição
     * @param motivo motivo
     * @param responsavel responsável por gerar a justificativa.
     * 
     * @return 
     */
    @PostMapping("/detalhes/justificativa_pedido_fora_prazo_legal/{requisicao_id}")
    public ResponseEntity<InputStreamResource> gerarJustificativaPedidoForaPrazoLegal(
        @PathVariable("requisicao_id") Long requisicao_id, 
        @RequestParam(value = "motivo") String motivo, 
        @RequestParam(value = "responsavel") String responsavel) {
        
        Requisicao requisicao = this.requisicaoServiceImpl.find(requisicao_id);
        Justificativa justificativa = new Justificativa();
        requisicao.setTemJustificativaPedidoForaPrazoLegal(true);
        
        justificativa.setRequisicao(requisicao);
        justificativa.setIdentificador(2);
        justificativa.setPessoa(responsavel);
        justificativa.setMotivo(motivo);

        SimpleDateFormat sdf  = new SimpleDateFormat("dd/MM/yyyy");

        justificativa.setDataEmissao(sdf.format(new Date()));

        Locale BRAZIL = new Locale("pt","BR");
        DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, BRAZIL);
        
        justificativa.setDataFull(df.format(new Date()));
        justificativa = justificativaService.save(justificativa);
        justificativa.setCodigoVerificacao(Long.parseLong(justificativa.getId().toString() + requisicao_id.toString()));
        
        this.requisicaoServiceImpl.salvar(requisicao);

        ByteArrayInputStream bis = RelatorioViagem.justificativa(justificativaService.update(justificativa));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=viagem.pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(bis));
    }
}
