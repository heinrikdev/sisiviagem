package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.transporte.viagem;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
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
import ufg.br.fabricasw.jatai.swsisviagem.componente.relatorio_jasper.RelatorioViagemPdf;
import ufg.br.fabricasw.jatai.swsisviagem.controller.Constantes;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Abastecimento;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Tarefa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Viagem;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.ViagemRelatorio;
import ufg.br.fabricasw.jatai.swsisviagem.domain.arquivo.Anexo;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoViagem;
import ufg.br.fabricasw.jatai.swsisviagem.service.RequisicaoPassageiroService;
import ufg.br.fabricasw.jatai.swsisviagem.service.ViagemRelatorioService;
import ufg.br.fabricasw.jatai.swsisviagem.service.ViagemService;

/**
 * Após criada a viagem, a tela redirecionada será a de visualizar a viagem,
 * nesta view é possível add requisições que serão atendidas, veiculo, motorista
 * e tarefas.
 *  
 * @author ronogue
 */
@Controller("transporte_viagem_detalhes")
@RequestMapping("/dashboard/transporte/viagem")
@PreAuthorize("hasAuthority('TRANSPORTE')")
public class DetalhesViagemController {
    
    private static final String TITULO_PAGINA_DETALHES_VIAGEM = "Informações da Viagem ";
    private static final String VIEW_DETALHES_VIAGEM = "app/dashboard/transporte/viagem/detalhes";
    private static final String ATTR_VIAGEM = "viagem";
    private static final String ATTR_REQUISICAO_PASSAGEIROS = "requisicaoPassageiros";
    private static final String ATTR_TAREFA = "tarefa";
    private static final String ATTR_ANEXO = "anexo";
    private static final String ATTR_ABASTECIMENTO = "abastecimento";
    
    @Autowired
    private ViagemService viagemService;
    
    @Autowired
    private RelatorioViagemPdf relatorioViagemPdf;
    
    @Autowired
    private RequisicaoPassageiroService requisicaoPassageiroService;
    
    @Autowired
    private ViagemRelatorioService viagemRelatorioService;
    
    @GetMapping("/{viagem_id}/detalhes")
    public String index(Model model, @PathVariable("viagem_id") Long viagem_id) {
        return this.viewDetalhesViagem(model, viagem_id);
    }
    
    @GetMapping("/{viagem_id}/detalhes/finalizar")
    public String finalizarViagem(@PathVariable("viagem_id") Long viagem_id) {
        
        Viagem viagem = this.viagemService.find(viagem_id);
        viagem.setEstadoViagem(EEstadoViagem.Finalizada);
        this.viagemService.atualizar(viagem);
        
        return "redirect:/dashboard/transporte/viagem/${viagem_id}/detalhes".replace("${viagem_id}", viagem_id.toString());
    }
    
    @GetMapping( value = "/{viagem_id}/gerar_relatorio",  produces =  MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> gerarRelatorioViagem(@PathVariable("viagem_id") Long viagem_id) throws FileNotFoundException {
        
        Viagem viagem = this.viagemService.find(viagem_id);
        ViagemRelatorio viagemRelatorio = new ViagemRelatorio();
        
        viagemRelatorio.setIdentificador(1);
        viagemRelatorio.setDataEmissao(LocalDate.now());
        viagemRelatorio.setViagem(viagem);
        
        viagemRelatorio = this.viagemRelatorioService.save(viagemRelatorio);
        Long rand = (long) (Math.random() * 10);
        Long codigoVerificacao = Long.parseLong(viagemRelatorio.getId().toString() + viagem.getId().toString() + rand);
        
        viagemRelatorio.setCodigoVerificacao(codigoVerificacao);
        viagemRelatorio = this.viagemRelatorioService.update(viagemRelatorio);
        
        ByteArrayInputStream bis = this.relatorioViagemPdf.gerar(viagemRelatorio);// RelatorioViagem.citiesReport(viagemRelatorio);
        InputStreamResource body = new InputStreamResource(bis);
        HttpHeaders headers = new HttpHeaders();
       
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add("Content-Disposition", "inline; filename=viagem.pdf");
        
        return ResponseEntity.ok().headers(headers).body(body);
    }
    
    @GetMapping("/remover/passageiro/{requisicao_passageiro_id}/{viagem_id}")
    public String modificarListaPassageiro(Model model, @PathVariable("requisicao_passageiro_id") Long requisicao_passageiro_id, @PathVariable("viagem_id") Long viagem_id) {

        this.requisicaoPassageiroService.removerPassageiroDaRequisicao(requisicao_passageiro_id);
        return this.viewDetalhesViagem(model, viagem_id);
    }
    
    private String viewDetalhesViagem(Model model, Long viagem_id) {
        
        Viagem viagem = this.viagemService.find(viagem_id);
        
        StringBuilder builder = new StringBuilder(TITULO_PAGINA_DETALHES_VIAGEM);
        builder.append(viagem.getId().toString());
        
        final String TITULO_PAGINA = builder.toString();
        
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_PAGINA);
        model.addAttribute(ATTR_VIAGEM, viagem);
        model.addAttribute(ATTR_REQUISICAO_PASSAGEIROS, viagem.getRequisicaoPassageiros());
        model.addAttribute(ATTR_TAREFA, new Tarefa());
        model.addAttribute(ATTR_ANEXO, new Anexo());
        model.addAttribute(ATTR_ABASTECIMENTO, new Abastecimento());
        
        return VIEW_DETALHES_VIAGEM;
    }
}
