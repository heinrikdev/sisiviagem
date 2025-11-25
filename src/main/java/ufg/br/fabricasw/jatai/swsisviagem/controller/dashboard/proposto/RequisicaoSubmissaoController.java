package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.proposto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoRequisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoSolicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.service.RequisicaoServiceImpl;
import ufg.br.fabricasw.jatai.swsisviagem.service.email.EmailSubmissaoRequisicaoService;

/**
 * Após o usuário salvar o cadastro da requisição o mesmo poderá submeter a
 * mesma
 * para o transporte, ou deletar a requisição inteira ou solicitações.
 * 
 * Este controller lida com estas operações.
 * 
 * @author Ronaldo N. de Sousa
 */
@Controller("proposto_requisicao_submissao")
@RequestMapping("/dashboard/proposto")
@PreAuthorize("hasAuthority('USER')")
public class RequisicaoSubmissaoController {

    @Autowired
    private RequisicaoServiceImpl requisicaoServiceImpl;

    @Autowired
    private EmailSubmissaoRequisicaoService emailSubmissaoRequisicaoService;

    @GetMapping("/requisicoes/submeter/{requisicao_id}")
    public String submeter(@PathVariable("requisicao_id") Long requisicao_id) {

        Requisicao requisicao = this.requisicaoServiceImpl.find(requisicao_id);

        ZoneId fusoHorarioDeSaoPaulo = ZoneId.of("America/Sao_Paulo");
        LocalDateTime dataSubmissao = LocalDateTime.now(fusoHorarioDeSaoPaulo);

        requisicao.inserirDatasDeEnvioSolicitacoes();

        requisicao.setDataSubmissao(dataSubmissao);
        requisicao.setEstadoRequisicao(EEstadoRequisicao.SUBMETIDA);
        requisicao.getSolicitacoes()
                .forEach(solicitacao -> solicitacao.setEstadoSolicitacao(EEstadoSolicitacao.SUBMETIDA));

        this.requisicaoServiceImpl.salvar(requisicao);
        this.emailSubmissaoRequisicaoService.send(requisicao,
                "Requisição " + requisicao_id + " enviada para avaliação");

        return "redirect:/dashboard/proposto/requisicoes/detalhes/" + requisicao_id.toString();
    }
}
