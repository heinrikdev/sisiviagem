package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.auditor.requisicao;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ufg.br.fabricasw.jatai.swsisviagem.controller.Constantes;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Mensagem;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Pessoa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Usuario;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoRequisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoSolicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.InfoRequisicaoScdp;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.Solicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.service.JustificativaService;
import ufg.br.fabricasw.jatai.swsisviagem.service.RequisicaoServiceImpl;
import ufg.br.fabricasw.jatai.swsisviagem.service.SolicitacaoServiceImpl;
import ufg.br.fabricasw.jatai.swsisviagem.service.UsuarioService;
import ufg.br.fabricasw.jatai.swsisviagem.service.email.EmailDevolucaoSolicitacaoService;

/**
 *
 * @author Ronaldo N. de Sousa
 */
@Controller("auditor_requisicao_detalhes")
@RequestMapping("/dashboard/auditor/requisicoes")
@PreAuthorize("hasAuthority('AUDITOR')")
public class RequisicaoDetalhesController {

    private static final String ATTR_REQUISICAO_PASSAGEIROS = "requisicaoPassageiros";
    private static final String VIEW_VISUALIZAR_REQUISICAO = "app/dashboard/auditor/requisicao/detalhes";
    private static final String VIEW_MENSAGEM_DEVOLUCAO = "app/dashboard/auditor/requisicao/devolver_unidade_mensagem";

    private static final String ATTR_LINK_JUSTIFICATIVA = "LINK_JUSTIFICATIVA";
    private static final String REDIRECT_TO_DETALHES = "redirect:/dashboard/auditor/requisicoes/detalhes/";
    private static final String INFO_LACAMENTO_SCDP = "infoSCDP";

    @Autowired
    private RequisicaoServiceImpl requisicaoServiceImpl;

    @Autowired
    private SolicitacaoServiceImpl solicitacaoService;

    @Autowired
    private JustificativaService justificativaService;

    @Autowired
    private EmailDevolucaoSolicitacaoService emailDevolucaoSolicitacaoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/detalhes/{requisicao_id}")
    public String visualizarDetalhes(Model model, @PathVariable("requisicao_id") Long requisicao_id) {

        Requisicao requisicao = this.requisicaoServiceImpl.findById(requisicao_id);

        final String titlePage = "Requisição " + requisicao.getId() + " - " + requisicao.getProposto().getNome();
        final String link_justificativa = "/dashboard/auditor/requisicoes/detalhes/justificativa_pedido_fora_prazo_legal/"
                + requisicao_id;

        model.addAttribute(Constantes.ATTR_TITLE_PAGE, titlePage);
        model.addAttribute(Constantes.ATTR_REQUISICAO, requisicao);
        model.addAttribute(Constantes.ATTR_SESSION_ANEXOS, requisicao.getAnexos());
        model.addAttribute(ATTR_REQUISICAO_PASSAGEIROS, requisicao.getRequisicaoPassageiros());
        model.addAttribute(Constantes.ATTR_SESSION_TRECHOS_IDA, requisicao.getTrechosDeIda());
        model.addAttribute(Constantes.ATTR_SESSION_TRECHOS_VOLTA, requisicao.getTrechosDeVolta());
        model.addAttribute(Constantes.ATTR_LISTA_PRESTACAO_CONTAS, requisicao.getPrestacaoDeContas());

        if (requisicao.foiLancadoNoSCDP()) {
            model.addAttribute(INFO_LACAMENTO_SCDP, requisicao.getInfoScdp());
        } else {

            model.addAttribute(INFO_LACAMENTO_SCDP, new InfoRequisicaoScdp());
        }
        model.addAttribute(ATTR_LINK_JUSTIFICATIVA, link_justificativa);

        return VIEW_VISUALIZAR_REQUISICAO;
    }

    @GetMapping("/{requisicao_id}/encerrar/solicitacao/{solicitacao_id}")
    public String encerrarSolicitacao(@PathVariable("requisicao_id") Long requisicao_id,
            @PathVariable("solicitacao_id") Long solicitacao_id) {

        Solicitacao solicitacao = this.solicitacaoService.find(solicitacao_id);

        solicitacao.setEstadoSolicitacao(EEstadoSolicitacao.ENCERRADA);

        this.solicitacaoService.atualizar(solicitacao);

        StringBuilder builder = new StringBuilder(REDIRECT_TO_DETALHES);
        builder.append(requisicao_id.toString());

        return builder.toString();
    }

    @GetMapping("/{requisicao_id}/devolver/solicitacao/{solicitacao_id}")
    public String devolverSolicitacao(Model model, @PathVariable("requisicao_id") Long requisicao_id,
            @PathVariable("solicitacao_id") Long solicitacao_id) {

        Requisicao requisicao = this.requisicaoServiceImpl.find(requisicao_id);
        Solicitacao solicitacao = this.solicitacaoService.find(solicitacao_id);

        solicitacao.setEstadoSolicitacao(EEstadoSolicitacao.DEVOLVIDA);
        requisicao.setEstadoRequisicao(EEstadoRequisicao.DEVOLVIDA);

        this.solicitacaoService.atualizar(solicitacao);
        this.requisicaoServiceImpl.atualizar(requisicao);

        final String titlePage = "Devolução da Requisição " + requisicao_id + ", "
                + requisicaoServiceImpl.findPropostoNomeRequisicao(requisicao_id);

        model.addAttribute("mensagens", solicitacao.getMensagens());
        model.addAttribute("isDiaria", solicitacao.isDiaria());
        model.addAttribute("requisicao_id", requisicao_id);
        model.addAttribute("solicitacao_id", solicitacao_id);

        model.addAttribute(Constantes.ATTR_TITLE_PAGE, titlePage);

        return VIEW_MENSAGEM_DEVOLUCAO;
    }

    @PostMapping("/{requisicao_id}/devolver/solicitacao/{solicitacao_id}")
    public String postdevolverSolicitacao(
            @RequestParam("mensagem") String msg,
            @PathVariable("requisicao_id") Long requisicao_id,
            @PathVariable("solicitacao_id") Long solicitacao_id) {

        Requisicao requisicao = this.requisicaoServiceImpl.find(requisicao_id);
        Solicitacao solicitacao = this.solicitacaoService.find(solicitacao_id);
        Pessoa proposto = requisicao.getProposto();
        final String emailSubject = "Devolução da Requisição " + requisicao_id + ", solicitação de "
                + solicitacao.getTipoSolicitacao();

        Usuario user = this.usuarioService.findUserLogado();
        Mensagem mensagem = new Mensagem();

        mensagem.setData(LocalDateTime.now());
        mensagem.setTexto("DEVOLUÇÃO PARA REVISÃO: \n" + msg);
        mensagem.setUsuario(user);
        solicitacao.getMensagens().add(mensagem);

        solicitacao.setEstadoSolicitacao(EEstadoSolicitacao.DEVOLVIDA);
        requisicao.setEstadoRequisicao(EEstadoRequisicao.DEVOLVIDA);

        this.solicitacaoService.atualizar(solicitacao);
        this.requisicaoServiceImpl.atualizar(requisicao);

        this.emailDevolucaoSolicitacaoService.send(proposto, requisicao_id, solicitacao, emailSubject);
        return REDIRECT_TO_DETALHES + requisicao.getId().toString();
    }

    @PostMapping("/detalhes/{requisicao_id}/infoSCDP")
    public String salvarInfomacoesSCDP(@PathVariable("requisicao_id") Long requisicao_id,
            @ModelAttribute("infoSCDP") InfoRequisicaoScdp infoSCDP) {

        Requisicao requisicao = this.requisicaoServiceImpl.findById(requisicao_id);

        requisicao.setInfoScdp(infoSCDP);
        this.requisicaoServiceImpl.salvar(requisicao);

        StringBuilder builder = new StringBuilder(REDIRECT_TO_DETALHES);
        builder.append(requisicao_id.toString());

        return builder.toString();
    }

    /**
     * Quando há um pedido fora do prazo legal é nescessário gerar uma
     * justificativa.
     * 
     * @param requisicao_id identificação da requisição
     * @param motivo        motivo
     * @param responsavel   responsável por gerar a justificativa.
     * 
     * @return
     */
    @PostMapping("/detalhes/justificativa_pedido_fora_prazo_legal/{requisicao_id}")
    public ResponseEntity<InputStreamResource> gerarJustificativaPedidoForaPrazoLegal(
            @PathVariable("requisicao_id") Long requisicao_id,
            @RequestParam(value = "motivo") String motivo,
            @RequestParam(value = "responsavel") String responsavel) {

        InputStreamResource justificativaPDF = this.justificativaService
                .gerarJustificativaPedidoForaPrazoLegal(requisicao_id, motivo, responsavel);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=viagem.pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(justificativaPDF);
    }
}
