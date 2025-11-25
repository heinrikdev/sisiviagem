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
import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.Solicitacao;

import java.util.Calendar;

/**
 * @author Ronaldo N Sousa
 *         Criado em: 05/08/2019 10:10
 **/
@Service
public class EmailResultadoAvalicaoSolicitacaoService {

    @Value("${spring.mail.host}")
    private String EMAIL;

    @Value("${app.config.host}")
    private String HOST;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    private static Logger log = LoggerFactory.getLogger(EmailSubmissaoRequisicaoService.class.getName());

    private String buildMessage(Pessoa proposto, Long requisicao_id, Solicitacao solicitacao) {

        String message = "A solicitação de " + solicitacao.tipoSolicitacao() + " foi  "
                + solicitacao.getEstadoSolicitacao() + ". Para mais detalhes, acesse: ";
        String link = HOST+"/login?redireciona=/dashboard/proposto/requisicoes/detalhes/"
                + requisicao_id;

        Context context = new Context();

        context.setVariable("message", message);
        context.setVariable("link", link);
        context.setVariable("titulo", this.saudacao() + ", " + proposto.getNome());

        return templateEngine.process("emails/requisicao/padrao", context);
    }

    @Async
    public void send(Pessoa proposto, Long requisicao_id, Solicitacao solicitacao, String subject) {

        String link = HOST+"/login?redireciona=/dashboard/proposto/requisicoes/detalhes/"
                + requisicao_id;

        MimeMessagePreparator messagePreparator = mimeMessage -> {

            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(EMAIL);
            messageHelper.setTo(proposto.getEmail());
            messageHelper.setSubject(subject);
            String content = this.buildMessage(proposto, requisicao_id, solicitacao);
            messageHelper.setText(content, true);
        };

        try {

            this.javaMailSender.send(messagePreparator);

        } catch (MailException e) {

            log.error(e.getMessage());
        }
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
