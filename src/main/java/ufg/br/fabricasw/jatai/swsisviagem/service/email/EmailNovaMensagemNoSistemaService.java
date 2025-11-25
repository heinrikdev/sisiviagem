package ufg.br.fabricasw.jatai.swsisviagem.service.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Pessoa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Usuario;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.ETipoUsuario;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.Role;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.Solicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.service.RequisicaoServiceImpl;
import ufg.br.fabricasw.jatai.swsisviagem.service.UsuarioService;

import java.util.Calendar;
import java.util.List;

/**
 * @author Ronaldo N Sousa
 *         Criado em: 05/08/2019 11:16
 **/
@Service
public class EmailNovaMensagemNoSistemaService {

    @Value("${spring.mail.host}")
    private String EMAIL;

    @Value("${app.config.host}")
    private String HOST;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private RequisicaoServiceImpl requisicaoService;

    private static Logger log = LoggerFactory.getLogger(EmailNovaMensagemNoSistemaService.class);

    private String buildMessage(Pessoa proposto, String mensagem, Long requisicao_id, Solicitacao solicitacao) {

        String message = "Você recebeu nova mensagem no SisViagem: \"" + mensagem
                + "\"\n\nPara visualizar, por favor acesse: ";
        String link = HOST+"/login?redireciona=/dashboard/proposto/requisicoes/detalhes/"
                + requisicao_id;

        Context context = new Context();

        context.setVariable("message", message);
        context.setVariable("link", link);

        if (proposto != null) {

            context.setVariable("titulo", this.saudacao() + ", " + proposto.getNome());

        } else {

            context.setVariable("titulo", this.saudacao());
        }

        return templateEngine.process("emails/requisicao/padrao", context);
    }

    @Async
    public void send(Solicitacao solicitacao, Long requisicao_id, Pessoa proposto, String mensagem, String subject) {

        MimeMessagePreparator messagePreparator = mimeMessage -> {

            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            String content = this.buildMessage(proposto, mensagem, requisicao_id, solicitacao);

            messageHelper.setFrom(EMAIL);
            messageHelper.setTo(proposto.getEmail());
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);
        };

        try {

            this.javaMailSender.send(messagePreparator);

        } catch (MailException e) {

            log.error(e.getMessage());
        }
    }

    @Async
    public void send(Solicitacao solicitacao, Long requisicao_id, String subject, String mensagem) {
        try {

            Requisicao requisicao = requisicaoService.findBySocilitacao(solicitacao.getId()).get(0);
            String[] emails = this.emailToRoleUser(requisicao, solicitacao);

            MimeMessagePreparator messagePreparator = mimeMessage -> {

                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                String content = this.buildMessage(null, mensagem, requisicao_id, solicitacao);

                messageHelper.setFrom(EMAIL);
                messageHelper.setTo(emails);
                messageHelper.setSubject(subject);
                messageHelper.setText(content, true);
            };

            this.javaMailSender.send(messagePreparator);

        } catch (MailException e) {

            log.error(e.getMessage());
        }
    }

    public String[] emailToRoleUser(Requisicao requisicao, Solicitacao solicitacao) {

        Usuario userLogado = usuarioService.findUserLogado();

        if (userLogado.getTipoUsuario() == ETipoUsuario.Desacordo) {

            String[] emails = new String[1];
            emails[0] = requisicao.getProposto().getEmail();
            return emails;
        }

        Role role = (solicitacao.isTransporte()) ? Role.TRANSPORTE : Role.DIARIA;

        if (solicitacao.isTransporte()) {

            List<String> findEmailFromDesacordoTransporte = usuarioService.findEmailFromDesacordoTransporte();
            return findEmailFromDesacordoTransporte.toArray(new String[findEmailFromDesacordoTransporte.size()]);
        }

        List<String> emailFromDesacordo = usuarioService.findEmailFromDesacordo(role, requisicao.getUnidade().getId());
        return emailFromDesacordo.toArray(new String[emailFromDesacordo.size()]);
    }

    private String saudacao() {

        Calendar cal = Calendar.getInstance();
        int hora = cal.get(Calendar.HOUR_OF_DAY);

        if (hora < 12) {

            return ("Bom dia");

        } else if (hora < 18) {

            return ("Boa Tarde");

        } else {

            return ("Boa Noite");
        }
    }
}
