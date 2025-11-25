package ufg.br.fabricasw.jatai.swsisviagem.service.email;

import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.thymeleaf.TemplateEngine;

import org.thymeleaf.context.Context;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Pessoa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.service.PessoaService;

/**
 *
 * @author Ronaldo N Sousa
 * Criado em: 02/08/2019
 */
@Service
public class EmailSubmissaoRequisicaoService {

    @Value("${spring.mail.host}")
    private String EMAIL;

    @Value("${app.config.host}")
    private String HOST;

    private final JavaMailSender javaMailSender;
    
    private static Logger log = LoggerFactory.getLogger(EmailSubmissaoRequisicaoService.class.getName());
    
    @Autowired
    private TemplateEngine templateEngine;
    
    @Autowired
    private PessoaService pessoaService;
    
    @Autowired
    public EmailSubmissaoRequisicaoService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    
    private String buildMessage(Requisicao requisicao, String link, boolean isToProponente) {
        
        String message = "Uma nova requisição foi submetida para avaliação. Para visualizar, acesse: ";
        Context context = new Context();

        context.setVariable("message", message);
        context.setVariable("link", link);

        if (isToProponente) {

            context.setVariable("titulo", this.saudacao() + ", " + requisicao.getProposto().getNome());

        } else {

            context.setVariable("titulo", this.saudacao());
        }

        return templateEngine.process("emails/requisicao/padrao", context);
    }
    
    @Async
    public void send(Requisicao requisicao, String subject) {
        
        try {
            Thread.sleep(10000);
            
            this.sendToAutorizadores(requisicao, subject);
            this.sendToProponente(requisicao, subject);
            
        } catch (InterruptedException ex) {
            
            java.util.logging.Logger.getLogger(EmailSubmissaoRequisicaoService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void sendToProponente(Requisicao requisicao, String subject)  {
        
        Pessoa pessoa = requisicao.getProposto();
        String link = HOST+"/login?redireciona=/dashboard/proposto/requisicoes/detalhes/" + requisicao.getId().toString();

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(EMAIL);
            messageHelper.setTo(pessoa.getEmail());
            messageHelper.setSubject(subject);
            String content = this.buildMessage(requisicao, link, true);
            messageHelper.setText(content, true);
        };
        try {
            
            this.javaMailSender.send(messagePreparator);
           
        } catch (MailException e) {
            
            log.error(e.getMessage());
        }
    }

    private void sendToAutorizadores(Requisicao requisicao, String subject) {
        
        List<String> autorizadores = this.pessoaService.listEmailAutorizadorByUnidade(requisicao.getUnidade().getId());
        String link = HOST+"/login?redireciona=/dashboard/unidades/requisicoes/detalhes/" + requisicao.getId();

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(EMAIL);
            messageHelper.setTo(autorizadores.toArray(new String[0]));
            messageHelper.setSubject(subject);
            String content = this.buildMessage(requisicao, link, false);
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

        if(hora < 12 ) {

            return ("Bom dia");

        }else if(hora < 18) {

            return ("Boa Tarde");

        }else {

            return ("Boa Noite");
        }
    }
}
