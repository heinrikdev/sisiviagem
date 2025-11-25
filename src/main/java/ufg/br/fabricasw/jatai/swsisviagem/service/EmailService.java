package ufg.br.fabricasw.jatai.swsisviagem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Pessoa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Usuario;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.ETipoUsuario;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.Role;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.Solicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Motorista;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Viagem;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guilherme on 07/11/17.
 */
@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${app.config.dlog_email}")
    private String DLOG_EMAIL;

    @Value("${spring.mail.host}")
    private String EMAIL;

    @Value("${app.config.host}")
    private String HOST;

    @Autowired
    PessoaService pessoaService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    RequisicaoServiceImpl requisicaoService;

    @Autowired
    SolicitacaoServiceImpl solicitacaoService;

    private static Logger log = LoggerFactory.getLogger(EmailService.class.getName());

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void informarMotoristasViagem(Viagem viagem) {

        viagem.getMotoristas().forEach((Motorista motorista) -> {
            this.motoristaViagem(motorista, viagem);
        });
    }

    public void sendEmail(Requisicao requisicao) throws MailException {

        SimpleMailMessage mail = new SimpleMailMessage();
        List<Pessoa> pessoas = pessoaService.findAutorizadorByUnidade(requisicao.getUnidade().getId());
        List<String> emails = new ArrayList<>();

        for (Pessoa p : pessoas) {
            emails.add(p.getEmail());
        }
        String[] a = emails.toArray(new String[0]);
        mail.setTo(a);
        mail.setFrom(EMAIL);
        mail.setSubject("Nova Requisição - Sisviagem");
        mail.setText("Uma nova requisição foi submetida para avaliação.\n"
                + "Para visualizar, acesse: "+ HOST +"/login?redireciona=/dashboard/unidades/requisicoes/detalhes/" + requisicao.getId().toString());
        try {
            javaMailSender.send(mail);
        } catch (Exception e) {
            log.info(e.getMessage());
        }

    }

    public void motoristaCadastrado(Motorista motorista) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(motorista.getPessoa().getEmail());
        mail.setFrom(EMAIL);
        mail.setSubject("Cadastro de Motorista [SisViagem]");
        mail.setText("Você foi cadastrado como motorista no sistema de viagens e diárias da UFG."
                + "\nUsuário: " + usuarioService.findByPessoa(motorista.getPessoa()).getLoginUnico() + "\n"
                + "Senha:123456" + " .Mude esta senha assim que efetuar o login\n"
                + " Acesse: "+ HOST +"/login?redireciona=app/proponente/editar");
        try {
            javaMailSender.send(mail);
            log.info("Enviou email da viagem");
        } catch (Exception e) {
            log.info(e.getMessage());
        }

    }

    public void motoristaViagem(Motorista motorista, Viagem viagem) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(motorista.getPessoa().getEmail());
        mail.setFrom(EMAIL);
        mail.setSubject("Nova Viagem [SisViagem]");
        mail.setText("Você foi adicionado em uma nova viagem. Para vê-la acesse: "+ HOST +"/dashboard/motorista/viagem/detalhes/" + viagem.getId());
        try {
            javaMailSender.send(mail);
            log.info("Enviou email da viagem");
        } catch (Exception e) {
            log.info(e.getMessage());
        }

    }

    public void motoristaViagemRemover(Motorista motorista, Viagem viagem) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(motorista.getPessoa().getEmail());
        mail.setFrom(EMAIL);
        mail.setSubject("Novo Estado de Viagem [SisViagem]");
        mail.setText("Você foi removido da viagem " + viagem.getId().toString() + ". Para vê-la acesse: "+ HOST +"/dashboard/motorista/viagem/detalhes/" + viagem.getId());
        try {

            javaMailSender.send(mail);
            log.info("Enviou email da viagem");

        } catch (Exception e) {
            log.info(e.getMessage());
        }

    }

    public void motoristaTarefa(Viagem viagem) {
        SimpleMailMessage mail = new SimpleMailMessage();
        List<String> emails = new ArrayList<>();
        List<Motorista> motoristas = viagem.getMotoristas();
        for (Motorista m : motoristas) {
            emails.add(m.getPessoa().getEmail());
        }

        String[] a = emails.toArray(new String[0]);
        mail.setTo(a);
        mail.setFrom(EMAIL);
        mail.setSubject("Nova Tarefa [SisViagem]");
        mail.setText("Uma nova tarefa foi adicionada na sua viagem. Para vê-la acesse: "+ HOST +"/login?redireciona=app/motorista/"
                + "ver/" + viagem.getId() + "#tarefas");
        try {
            javaMailSender.send(mail);
            log.info("Enviou email da viagem");
        } catch (Exception e) {
            log.info(e.getMessage());
        }

    }

    public void informarProponente(Usuario usuario, String senha) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(usuario.getPessoa().getEmail());
        mail.setFrom(EMAIL);
        mail.setSubject("Cadastro/Edição de Proponente SisViagem");
        mail.setText("Dados para acesso:\nUsuário: " + usuario.getLoginUnico() + "\nSenha: " + senha
                + "\n"+ HOST +"/");
        try {
            javaMailSender.send(mail);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    public void informarMensagemToProposto(Solicitacao solicitacao, String mensagem, Long requisicao_id, Pessoa proposto) {
        try {

            SimpleMailMessage email = new SimpleMailMessage();

            email.setTo(proposto.getEmail());
            email.setFrom(EMAIL);
            email.setSubject("Nova mensagem (" + solicitacao.tipoSolicitacao() + ") - SisViagem ");

            email.setText(
                    "Você recebeu uma nova mensagem: \n\n"
                    + mensagem
                    + "\n\nPara visualizar, por favor acesse: "
                    + HOST +"/login?redirecionar=/dashboard/proposto/requisicoes/detalhes/"
                    + requisicao_id.toString()
            );

            javaMailSender.send(email);

        } catch (MailException e) {

            log.info("ERROR AO ENVIAR MENSAGEM POR EMAIL");
            log.info(e.getMessage());
        }
    }

    public void informarDevolucaoMensagemToProposto(Solicitacao solicitacao, Long requisicao_id, Pessoa proposto) {
        try {

            SimpleMailMessage email = new SimpleMailMessage();

            email.setTo(proposto.getEmail());
            email.setFrom(EMAIL);
            email.setSubject("Nova mensagem (Solicitação) - SisViagem ");

            email.setText(
                "Sua requisição " + requisicao_id + ", solicitação de " + solicitacao.tipoSolicitacao() +
                ", possui irregularidades e por isso foi devolvida para revisão, verifique-a" +
                "olhando a guia de mensagens nos detalhes da requisição." +
                "Acesse: "
                + HOST +"/login?redirecionar=/dashboard/proposto/requisicoes/detalhes/"
                + requisicao_id.toString()
            );

            javaMailSender.send(email);

        } catch (MailException e) {

            log.info("ERROR AO ENVIAR MENSAGEM");
            log.info(e.getMessage());
        }
    }

    public void informarMensagem(Solicitacao solicitacao, Long requisicao_id) throws MailException {

        Requisicao requisicao = requisicaoService.findBySocilitacao(solicitacao.getId()).get(0);

        SimpleMailMessage mail = new SimpleMailMessage();

        if (usuarioService.findUserLogado().getTipoUsuario() == ETipoUsuario.Desacordo) {

            mail.setTo(requisicao.getProposto().getEmail());
            log.info("Email: Entrou no IF");

        } else {

            List<String> emails = new ArrayList<>();

            if (solicitacao.isDiaria() || solicitacao.isPassagem()) {

                usuarioService.findAllDesacordo()
                        .stream()
                        .filter(usuario -> usuario.getRoles().contains(Role.DIARIA))
                        .filter(usuario -> usuario.getUnidade() == requisicao.getUnidade())
                        .forEach(usuario -> emails.add(usuario.getPessoa().getEmail()));

                String[] a = emails.toArray(new String[0]);
                mail.setTo(a);

                log.info("Email: Entrou no ELSE");
                log.info("Emails: {}", emails);

            } else if (solicitacao.isTransporte()) {

                usuarioService.findAllDesacordo().stream().filter(usuario -> usuario.getRoles().contains(Role.TRANSPORTE)).
                        forEach(usuario -> emails.add(usuario.getPessoa().getEmail()));

                String[] a = emails.toArray(new String[0]);
                mail.setTo(a);

                log.info("Email: Entrou no segundo ELSE");
                log.info("Emails: {}", emails);
            }
        }

        mail.setFrom(EMAIL);
        mail.setSubject("Nova mensagem (Solicitação) - SisViagem ");
        mail.setText("Você recebeu uma nova mensagem.\nPara visualizar, por favor acesse: "
                + HOST +"/login?redirecionar=/dashboard/proposto/requisicoes/detalhes/" + requisicao_id.toString());
        try {

            javaMailSender.send(mail);

        } catch (Exception e) {

            log.info(e.getMessage());
        }
    }

    public void senToProponente(Requisicao requisicao) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();
        Pessoa pessoa = requisicao.getProposto();
        mail.setTo(pessoa.getEmail());
        mail.setFrom(EMAIL);
        mail.setSubject("Nova Requisição (SisViagem)");
        mail.setText("Você fez uma nova requisição no SisViagem.\nPara visualizar, por favor acesse: "
                + HOST +"/login?redireciona=/dashboard/proposto/requisicoes/detalhes/" + requisicao.getId().toString());
        try {
            javaMailSender.send(mail);
        } catch (Exception e) {

        }
    }

    public void informarNovoUsuario(Usuario usuario) {
        SimpleMailMessage mail = new SimpleMailMessage();
        Pessoa pessoa = usuario.getPessoa();
        mail.setTo(pessoa.getEmail());
        mail.setFrom(EMAIL);
        mail.setSubject("Novo Cadastro no Sisviagem");
        mail.setText("Um usuário criado foi vinculado a este email, caso não tenha sido você, por favor "
                + "entrar em contato com "+ DLOG_EMAIL +"\n" + "usuário: " + usuario.getLoginUnico()
                + "\nSenha: <senha do login único (sigaa)>");
        try {
            javaMailSender.send(mail);
        } catch (Exception e) {

        }
    }

    public void informarViagemAgendada(Requisicao requisicao, Viagem viagem) {

        SimpleMailMessage mail = new SimpleMailMessage();
        Pessoa pessoa = requisicao.getProposto();

        mail.setTo(pessoa.getEmail());
        mail.setFrom(EMAIL);
        mail.setSubject("Sua viagem foi agendada");
        mail.setText("Sua Viagem foi agendada\nDados da viagem\nData: "
                + viagem.getData().format(DateTimeFormatter.ofPattern("dd/MM/YYYY")) + "\nHorário da partida: " + viagem.getStartHora()
                + "\n\n Para visualizar sua requisição acesse : "+ HOST +"/login?redirecionar=/dashboard/proposto/requisicoes/detalhes/" + requisicao.getId().toString());
        try {
            javaMailSender.send(mail);
        } catch (Exception e) {

        }
    }

}
