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
import ufg.br.fabricasw.jatai.swsisviagem.domain.Usuario;

import java.util.Calendar;

/**
 * @author Ronaldo N Sousa
 *         Criado em: 07/08/2019 10:54
 **/
@Service
public class EmailNovoUsuarioBoasVindasService {

    @Value("${app.config.dlog_email}")
    private String DLOG_EMAIL;

    @Value("${spring.mail.host}")
    private String EMAIL;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    private static Logger log = LoggerFactory.getLogger(EmailNovoUsuarioBoasVindasService.class.getName());

    private String buildMessage(Usuario usuario) {

        String message = "Um usuário criado foi vinculado a este email, caso não tenha sido você, por favor "
                + "entrar em contato com "+ DLOG_EMAIL +" <br><br>" + "usuário: " + usuario.getLoginUnico()
                + "<br>Senha: &lt;senha do login único (sigaa)&gt;";
        Context context = new Context();

        context.setVariable("message", message);
        context.setVariable("link", "");
        context.setVariable("titulo", this.saudacao() + ", " + usuario.getPessoa().getNome());

        return templateEngine.process("emails/requisicao/padrao", context);
    }

    @Async
    public void send(Usuario usuario) {

        MimeMessagePreparator messagePreparator = mimeMessage -> {

            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(EMAIL);
            messageHelper.setTo(usuario.getPessoa().getEmail());
            messageHelper.setSubject("Cadastro no SisViagem");
            String content = this.buildMessage(usuario);
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
