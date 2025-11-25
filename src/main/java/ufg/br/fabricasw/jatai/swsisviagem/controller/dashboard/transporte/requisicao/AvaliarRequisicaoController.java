package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.transporte.requisicao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ufg.br.fabricasw.jatai.swsisviagem.controller.Constantes;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Mensagem;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Pessoa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Usuario;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoRequisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoSolicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoViagem;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.Solicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.SolicitacaoTransporte;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Viagem;
import ufg.br.fabricasw.jatai.swsisviagem.service.*;
import ufg.br.fabricasw.jatai.swsisviagem.service.email.EmailDevolucaoSolicitacaoService;
import ufg.br.fabricasw.jatai.swsisviagem.service.email.EmailViagemAgendadaService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * Ao visualizar a requisição perfil do transporte irá aprovar ou recusar a
 * solicitação de transporte.
 *
 * Esta classe é responsável por esse processo. Para aprovar a requisição é
 * exibida uma página contendo uma lista de viagens para o usuário selecionar e
 * adicionar a requisicao. Para recusar, o controller irá mudar o status da
 * solicitação.
 *
 *
 * @author Ronaldo N. de Sousa
 */
@Controller("transporte_requisicao_avaliar")
@RequestMapping("/dashboard/requisicoes/avaliar")
@PreAuthorize("hasAuthority('TRANSPORTE')")
public class AvaliarRequisicaoController {

    private static final String TITULO_PAGINA_APROVACAO = "Aprovar transporte";

    private static final String VIEW_APROVAR_SOLICITACAO = "app/dashboard/transporte/requisicao/aprovar_transporte";
    private static final String REDIRECT_LINK_REQUISICAO_DETALHES = "redirect:/dashboard/transporte/requisicoes/detalhes/";
    
    private static final String VIEW_MENSAGEM_DEVOLUCAO = "app/dashboard/transporte/requisicao/devolucao_requisicao";
    
    private static final String ATTR_REQUISICAO = "requisicao";
    private static final String ATTR_VIAGENS_AGENDADAS = "viagens";
    
    @Autowired
    private RequisicaoServiceImpl requisicaoServiceImpl;
    
    @Autowired
    private ViagemService viagemService;

    @Autowired
    private SolicitacaoServiceImpl solicitacaoService;
    
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmailViagemAgendadaService emailViagemAgendadaService;

    @Autowired
    private EmailDevolucaoSolicitacaoService emailDevolucaoSolicitacaoService;
    
    /**
     * Aqui é exibida a view de avaliação da requisição, nela é mostrada todas
     * as viagens agendadas que podem vir atender a solicitação de transporte da
     * requisição. 
     * 
     * As viagens exibidas são as que estão agendas contando da data de viagem
     * do solicitação para trás até 5. Ou seja, se a data da viagem da solicitação
     * for 5/10, as viagens exibidas serão as do dias 5/10, 4/10, 3/10, 2/10, 1/10.
     * 
     * @param model
     * @param requisicao_id
     * @return 
     */
    @GetMapping("/{requisicao_id}/aprovar")
    public String index(Model model, @PathVariable("requisicao_id")Long requisicao_id) {
        
        Requisicao requisicao = this.requisicaoServiceImpl.find(requisicao_id);
        
        LocalDate to = requisicao.getDataRequisicao();
        LocalDate from = LocalDate.from(to).minusDays(4);
        
        List<Viagem> viagens = this.viagemService.findByRangeDate(requisicao_id, from, to, EEstadoViagem.Aberta);
        
        model.addAttribute(ATTR_VIAGENS_AGENDADAS, viagens);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_PAGINA_APROVACAO);
        model.addAttribute(ATTR_REQUISICAO, requisicao);
        
        model.addAttribute("from", from.toString());
        model.addAttribute("to", to.toString());
        
        return VIEW_APROVAR_SOLICITACAO;
    }
    
    @GetMapping("/{requisicao_id}/aprovar/viagem_filtrada")
    public String indexFiltrarViagem(Model model, @PathVariable("requisicao_id")Long requisicao_id, @RequestParam("from") LocalDate from, @RequestParam("to") LocalDate to) {
        
        Requisicao requisicao = this.requisicaoServiceImpl.find(requisicao_id);
        List<Viagem> viagens = this.viagemService.findByRangeDate(requisicao_id, from, to, EEstadoViagem.Aberta);
        
        model.addAttribute(ATTR_VIAGENS_AGENDADAS, viagens);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_PAGINA_APROVACAO);
        model.addAttribute(ATTR_REQUISICAO, requisicao);
        model.addAttribute("from", from.toString());
        model.addAttribute("to", to.toString());
        
        return VIEW_APROVAR_SOLICITACAO;
    }
    
    /**
     * Aqui é recusado a solicitação de transpote.
     * 
     * @param model
     * @param requisicao_id identificador da requisição
     * @return 
     */
    @GetMapping("/{requisicao_id}/recusar")
    public String recusar(Model model, @PathVariable("requisicao_id")Long requisicao_id) {
        
        Requisicao  requisicao  = this.requisicaoServiceImpl.find(requisicao_id);
        SolicitacaoTransporte transporte = requisicao.getSolicitacaoTransporte();
        
        transporte.setEstadoSolicitacao(EEstadoSolicitacao.RECUSADA);

        this.solicitacaoService.atualizar(transporte);
        
        StringBuilder builder = new StringBuilder(REDIRECT_LINK_REQUISICAO_DETALHES);
        builder.append(requisicao.getId().toString());
            
        return builder.toString();
    }
    
    /**
     * Aqui é aprovado a solicitação de transpote e por conseguinte é add à
     * lista de requisições da viagem a requisição que será atendida pela mesma.
     * 
     * @param model
     * @param requisicao_id identificador da requisição
     * @param viagem_id identificador da viagem.
     * @return 
     */
    @GetMapping("/{requisicao_id}/aprovar/{viagem_id}")
    public String aprovar(Model model, @PathVariable("requisicao_id")Long requisicao_id, @PathVariable("viagem_id")Long viagem_id) {
        
        Viagem      viagem      = this.viagemService.find(viagem_id);
        Requisicao  requisicao  = this.requisicaoServiceImpl.find(requisicao_id);
        SolicitacaoTransporte transporte = requisicao.getSolicitacaoTransporte();
        
        if (transporte != null) {
            
            ZoneId fusoHorarioDeSaoPaulo = ZoneId.of("America/Sao_Paulo");
            transporte.setDataAvaliacaoPelaDepartamento(LocalDateTime.now(fusoHorarioDeSaoPaulo));
            
            transporte.setEstadoSolicitacao(EEstadoSolicitacao.APROVADA);
            
            viagem.getRequisicaos().add(requisicao);
            requisicao.getViagens().add(viagem);
            
            requisicao.atualizarDatasDeEnvioSolicitacoes();
        
            this.viagemService.atualizar(viagem);
            this.solicitacaoService.atualizar(transporte);

            String subject = "Viagem " + viagem.getId() +" agendada";
            this.emailViagemAgendadaService.send(requisicao.getProposto(), viagem, requisicao.getId(), subject);
            
            StringBuilder builder = new StringBuilder(REDIRECT_LINK_REQUISICAO_DETALHES);
            builder.append(requisicao.getId().toString());
            
            return builder.toString();
        }

        return VIEW_APROVAR_SOLICITACAO;
    }
    
    @GetMapping("/{requisicao_id}/aprovar_sem_alterar")
    public String aprovarSemAlterarAprovacaoAnterior(Model model, @PathVariable("requisicao_id")Long requisicao_id) {
        
        Requisicao  requisicao  = this.requisicaoServiceImpl.find(requisicao_id);
        SolicitacaoTransporte transporte = requisicao.getSolicitacaoTransporte();
        
        transporte.setEstadoSolicitacao(EEstadoSolicitacao.APROVADA);
        
        ZoneId fusoHorarioDeSaoPaulo = ZoneId.of("America/Sao_Paulo");
        transporte.setDataAvaliacaoPelaDepartamento(LocalDateTime.now(fusoHorarioDeSaoPaulo));
        this.solicitacaoService.atualizar(transporte);

        return REDIRECT_LINK_REQUISICAO_DETALHES + requisicao.getId().toString();
    }
    
    @GetMapping("/{requisicao_id}/devolver/solicitacao/{solicitacao_id}")
    public String devolverSolicitacao(Model model,@PathVariable("requisicao_id") Long requisicao_id, @PathVariable("solicitacao_id") Long solicitacao_id) {
        
        Solicitacao solicitacao = this.solicitacaoService.find(solicitacao_id);
        Requisicao requisicao   = this.requisicaoServiceImpl.find(requisicao_id);

        final String titlePage = "Devolução da Requisição " + requisicao_id + ", " + requisicaoServiceImpl.findPropostoNomeRequisicao(requisicao_id);
        
        model.addAttribute("mensagens", solicitacao.getMensagens());
        model.addAttribute("requisicao_id", requisicao_id);
        model.addAttribute("solicitacao_id", solicitacao_id);
        model.addAttribute("requisicao", requisicao);
        
        model.addAttribute(Constantes.ATTR_TITLE_PAGE,      titlePage);
        return VIEW_MENSAGEM_DEVOLUCAO;
    }
    
    @PostMapping("/{requisicao_id}/devolver/solicitacao/{solicitacao_id}")
    public String postdevolverSolicitacao(
        @RequestParam("mensagem") String msg, 
        @PathVariable("requisicao_id") Long requisicao_id, 
        @PathVariable("solicitacao_id") Long solicitacao_id) {
        
        Requisicao requisicao   = this.requisicaoServiceImpl.find(requisicao_id);
        Solicitacao solicitacao = this.solicitacaoService.find(solicitacao_id);
        Pessoa proposto         = requisicao.getProposto();
        final String emailSubject = "Devolução da Requisição " + requisicao_id + ", solicitação de " + solicitacao.getTipoSolicitacao();
        
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

        return REDIRECT_LINK_REQUISICAO_DETALHES + requisicao.getId().toString();
    }
}
