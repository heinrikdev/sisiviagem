package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.unidades.requisicao;

import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ufg.br.fabricasw.jatai.swsisviagem.controller.Constantes;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Pessoa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoSolicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.Solicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.service.RequisicaoServiceImpl;
import ufg.br.fabricasw.jatai.swsisviagem.service.SolicitacaoServiceImpl;
import ufg.br.fabricasw.jatai.swsisviagem.service.email.EmailResultadoAvalicaoSolicitacaoService;

/**
 * 
 * @author Ronaldo N. de Sousa
 */
@Controller("unidades_requisicao_avaliar")
@RequestMapping("/dashboard/unidades/requisicoes")
@PreAuthorize("hasAuthority('DIARIA') || hasAuthority('PASSAGEM')")
public class AvaliarRequisicaoController {

    private static final String REDIRECT_LINK_REQUISICAO_DETALHES = "redirect:/dashboard/unidades/requisicoes/detalhes/";
    private static final String VIEW_MENSAGEM_INDEFERIMENTO = "app/dashboard/unidades/requisicao/mensagem_indeferimento_requisicao";

    @Autowired
    private SolicitacaoServiceImpl solicitacaoServiceImpl;

    @Autowired
    private RequisicaoServiceImpl requisicaoServiceImpl;

    @Autowired
    private EmailResultadoAvalicaoSolicitacaoService emailResultadoAvalicaoSolicitacaoService;

    /**
     * Aqui é aprovado a solicitação enviada a unidade.
     * 
     * @param model
     * @param solicitacao_id identificador da requisição
     * @param requisicao_id
     * @return
     */
    @GetMapping("/aprovar/{solicitacao_id}/{requisicao_id}")
    public String aprovar(Model model, @PathVariable("solicitacao_id") Long solicitacao_id,
            @PathVariable("requisicao_id") Long requisicao_id) {

        Pessoa proposto = this.requisicaoServiceImpl.findPropostoRequisicao(requisicao_id);
        Solicitacao solicitacao = this.solicitacaoServiceImpl.find(solicitacao_id);
        String emailSubject = "Resultado da solicitação de " + solicitacao.getTipoSolicitacao() + ", Requisição "
                + requisicao_id;
        ZoneId fusoHorarioDeSaoPaulo = ZoneId.of("America/Sao_Paulo");

        solicitacao.setDataAvaliacaoPelaDepartamento(LocalDateTime.now(fusoHorarioDeSaoPaulo));
        solicitacao.setEstadoSolicitacao(EEstadoSolicitacao.APROVADA);

        this.solicitacaoServiceImpl.atualizar(solicitacao);
        this.emailResultadoAvalicaoSolicitacaoService.send(proposto, requisicao_id, solicitacao, emailSubject);

        return REDIRECT_LINK_REQUISICAO_DETALHES + requisicao_id.toString();
    }

    /**
     * Aqui é recusada a solicitação enviada a unidade.
     * 
     * @param model
     * @param solicitacao_id identificador da requisição
     * @param requisicao_id
     * @return
     */
    @GetMapping("/recusar/{solicitacao_id}/{requisicao_id}")
    public String recusar(Model model, @PathVariable("solicitacao_id") Long solicitacao_id,
            @PathVariable("requisicao_id") Long requisicao_id) {

        Solicitacao solicitacao = this.solicitacaoServiceImpl.find(solicitacao_id);

        solicitacao.setDataAvaliacaoPelaDepartamento(LocalDateTime.now());
        solicitacao.setEstadoSolicitacao(EEstadoSolicitacao.RECUSADA);

        this.solicitacaoServiceImpl.atualizar(solicitacao);

        final String titlePage = "Indeferimento da Requisição " + requisicao_id + ", "
                + requisicaoServiceImpl.findPropostoNomeRequisicao(requisicao_id);

        model.addAttribute("mensagens", solicitacao.getMensagens());
        model.addAttribute("requisicao_id", requisicao_id);
        model.addAttribute("solicitacao_id", solicitacao_id);

        model.addAttribute(Constantes.ATTR_TITLE_PAGE, titlePage);
        return VIEW_MENSAGEM_INDEFERIMENTO;
    }
}
