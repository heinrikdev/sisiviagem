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
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Motorista;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Viagem;

import java.util.Calendar;

/**
 * @author Ronaldo N Sousa
 *         Criado em: 07/08/2019 10:00
 **/
@Service
public class EmailMotoristaViagemService {

    @Value("${spring.mail.host}")
    private String EMAIL;

    @Value("${app.config.host}")
    private String HOST;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    private static Logger log = LoggerFactory.getLogger(EmailMotoristaViagemService.class.getName());

    private String buildMessage(Motorista motorista, Viagem viagem, String message) {

        String link = HOST+"/dashboard/motorista/viagem/detalhes/" + viagem.getId();
        Context context = new Context();

        context.setVariable("message", message);
        context.setVariable("link", link);

        if (motorista != null) {

            context.setVariable("titulo", this.saudacao() + ", " + motorista.getNome());

        } else {

            context.setVariable("titulo", this.saudacao());
        }

        return templateEngine.process("emails/requisicao/padrao", context);
    }

    @Async
    public void avisarMotoristasNovaViagem(Viagem viagem) {

        if (!viagem.getMotoristas().isEmpty()) {

            String message = "Você foi adicionado em uma nova viagem " + viagem.getId().toString()
                    + ". Para vê-la acesse: ";
            String subject = "Nova Viagem: " + viagem.getId();
            String[] emails = viagem.getMotoristas().stream().map(Motorista::getEmail).toArray(String[]::new);

            this.send(null, viagem, subject, message, emails);
        }
    }

    @Async
    public void avisarMotoristaNovaViagem(Motorista motorista, Viagem viagem) {

        String message = "Você foi adicionado em uma nova viagem" + viagem.getId().toString() + ". Para vê-la acesse: ";
        String subject = "Nova Viagem: " + viagem.getId();
        String[] emails = { motorista.getEmail() };

        this.send(motorista, viagem, subject, message, emails);
    }

    @Async
    public void avisarRemocao(Motorista motorista, Viagem viagem) {

        String message = "Você foi removido da viagem " + viagem.getId().toString() + ". Para vê-la acesse: ";
        String subject = "Novo Estado de Viagem: " + viagem.getId();
        String[] emails = { motorista.getEmail() };

        this.send(motorista, viagem, subject, message, emails);
    }

    private void send(Motorista motorista, Viagem viagem, String subject, String mensagem, String[] emails) {

        MimeMessagePreparator messagePreparator = mimeMessage -> {

            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(EMAIL);
            messageHelper.setTo(emails);
            messageHelper.setSubject(subject);
            String content = this.buildMessage(motorista, viagem, mensagem);
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
