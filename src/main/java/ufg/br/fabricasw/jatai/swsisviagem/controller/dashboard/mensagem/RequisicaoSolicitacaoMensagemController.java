package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.mensagem;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Mensagem;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Pessoa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Usuario;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.Role;
import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.Solicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.service.RequisicaoServiceImpl;
import ufg.br.fabricasw.jatai.swsisviagem.service.SolicitacaoServiceImpl;
import ufg.br.fabricasw.jatai.swsisviagem.service.UsuarioService;
import ufg.br.fabricasw.jatai.swsisviagem.service.email.EmailNovaMensagemNoSistemaService;

/**
 *
 * @author Ronaldo N. de Sousa
 */
@Controller
@RequestMapping("/dashboard/requisicoes")
public class RequisicaoSolicitacaoMensagemController {

    private static final String REDIRECT_DETALHES_REQUISICAO_TRANSPORTE = "redirect:/dashboard/transporte/requisicoes/detalhes/";
    private static final String REDIRECT_DETALHES_REQUISICAO_PROPOSTO = "redirect:/dashboard/proposto/requisicoes/detalhes/";
    private static final String REDIRECT_DETALHES_REQUISICAO_UNIDADES = "redirect:/dashboard/unidades/requisicoes/detalhes/";
    private static final String REDIRECT_DETALHES_REQUISICAO_AUDITOR = "redirect:/dashboard/auditor/requisicoes/detalhes/";

    @Autowired
    private RequisicaoServiceImpl requisicaoServiceImpl;

    @Autowired
    private SolicitacaoServiceImpl solicitacaoServiceImpl;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmailNovaMensagemNoSistemaService emailNovaMensagemNoSistemaService;

    @PostMapping("/enviar_mensagem/{requisicao_id}/{solicitacao_id}")
    public String enviarMensagem(
            @RequestParam("mensagem") String msg,
            @PathVariable("requisicao_id") Long requisicao_id,
            @PathVariable("solicitacao_id") Long solicitacao_id) {

        Requisicao requisicao = this.requisicaoServiceImpl.find(requisicao_id);
        Usuario user = this.usuarioService.findUserLogado();
        Solicitacao solicitacao = this.solicitacaoServiceImpl.find(solicitacao_id);
        Mensagem mensagem = new Mensagem();

        mensagem.setData(LocalDateTime.now());
        mensagem.setTexto(msg);
        mensagem.setUsuario(user);
        solicitacao.getMensagens().add(mensagem);

        String REDIRECT_LINK_DETALHES = "";
        StringBuilder builder;

        String emailSubject = "SisViagem: Requisição " + requisicao_id + " - Nova mensagem ("
                + solicitacao.tipoSolicitacao() + ")";
        Pessoa proposto = requisicao.getProposto();

        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);

        if (user.getRoles().contains(Role.TRANSPORTE)) {

            this.emailNovaMensagemNoSistemaService.send(solicitacao, requisicao_id, proposto, msg, emailSubject);

            builder = new StringBuilder(REDIRECT_DETALHES_REQUISICAO_TRANSPORTE);
            REDIRECT_LINK_DETALHES = builder.append(requisicao_id.toString()).toString();
        } else if (user.getRoles().contains(Role.DIARIA) || user.getRoles().contains(Role.PASSAGEM)) {

            this.emailNovaMensagemNoSistemaService.send(solicitacao, requisicao_id, proposto, msg, emailSubject);

            builder = new StringBuilder(REDIRECT_DETALHES_REQUISICAO_UNIDADES);
            REDIRECT_LINK_DETALHES = builder.append(requisicao_id.toString()).toString();
        } else if (user.getRoles().contains(Role.USER)) {

            this.emailNovaMensagemNoSistemaService.send(solicitacao, requisicao_id, emailSubject, msg);

            builder = new StringBuilder(REDIRECT_DETALHES_REQUISICAO_PROPOSTO);
            REDIRECT_LINK_DETALHES = builder.append(requisicao_id.toString()).toString();

        } else if (user.getRoles().contains(Role.AUDITOR)) {

            this.emailNovaMensagemNoSistemaService.send(solicitacao, requisicao_id, proposto, msg, emailSubject);

            builder = new StringBuilder(REDIRECT_DETALHES_REQUISICAO_AUDITOR);
            REDIRECT_LINK_DETALHES = builder.append(requisicao_id.toString()).toString();
        }

        this.solicitacaoServiceImpl.save(solicitacao);
        return REDIRECT_LINK_DETALHES;
    }
}