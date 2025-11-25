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
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Viagem;
import ufg.br.fabricasw.jatai.swsisviagem.service.ViagemService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;

/**
 * @author Ronaldo N Sousa Criado em: 08/08/2019 09:35
 *
 */
@Service
public class EmailViagemAgendadaService {

    @Value("${spring.mail.host}")
    private String EMAIL;

    @Value("${app.config.host}")
    private String HOST;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private ViagemService viagemService;

    private static Logger log = LoggerFactory.getLogger(EmailViagemAgendadaService.class.getName());

    private String buildMessage(Viagem viagem, Long requisicao_id, String titulo) {

        String dataPartida = viagem.getData().format(DateTimeFormatter.ofPattern("dd/mm/yyyy")) + " "
                + viagem.getStartHora();
        String message = "Para visualizar sua requisição clique <a href='"+ HOST +"/login?redirecionar=/dashboard/proposto/requisicoes/detalhes/"
                + requisicao_id + "'>Aqui</a>";
        Context context = new Context();

        context.setVariable("message", message);
        context.setVariable("dataPartida", dataPartida);
        context.setVariable("motoristas", this.viagemService.fetchMotoristaNomes(viagem.getId()));
        context.setVariable("titulo", titulo);

        return templateEngine.process("emails/viagem/padrao", context);
    }

    @Async
    public void send(Pessoa proposto, Viagem viagem, Long requisicao_id, String subject) {

        MimeMessagePreparator messagePreparator = mimeMessage -> {

            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(EMAIL);
            messageHelper.setTo(proposto.getEmail());
            messageHelper.setSubject(subject);
            String content = this.buildMessage(viagem, requisicao_id, "Sua viagem foi agendada");
            messageHelper.setText(content, true);
        };

        try {

            this.javaMailSender.send(messagePreparator);

        } catch (MailException e) {

            log.error(e.getMessage());
        }
    }

    public void sendAlertChangeViagem(Viagem viagem, List<Requisicao> requisicaos) {

        requisicaos.forEach(requisicao -> this.sendAlertChangeViagem(viagem, requisicao));
    }

    @Async
    public void sendAlertChangeViagem(Viagem viagem, Requisicao requisicao) {

        String subject = "Viagem " + viagem.getId() + " alterada!";

        try {

            Pessoa proposto = requisicao.getProposto();

            MimeMessagePreparator messagePreparator = mimeMessage -> {

                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                messageHelper.setFrom(EMAIL);
                messageHelper.setTo(proposto.getEmail());
                messageHelper.setSubject(subject);
                String content = this.buildMessage(viagem, requisicao.getId(), "Sua viagem foi alterada");
                messageHelper.setText(content, true);
            };

            this.javaMailSender.send(messagePreparator);

        } catch (MailException e) {

            log.error(e.getMessage());
        }
    }

}
