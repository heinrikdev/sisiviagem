package ufg.br.fabricasw.jatai.swsisviagem.domain.facade;

import ufg.br.fabricasw.jatai.swsisviagem.domain.Pessoa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoRequisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoSolicitacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.Solicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.SolicitacaoDiaria;
import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.SolicitacaoPassagem;
import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.SolicitacaoTransporte;
import ufg.br.fabricasw.jatai.swsisviagem.service.PessoaService;
import ufg.br.fabricasw.jatai.swsisviagem.service.RequisicaoServiceImpl;
import ufg.br.fabricasw.jatai.swsisviagem.service.SolicitacaoDiariaServiceImpl;
import ufg.br.fabricasw.jatai.swsisviagem.service.SolicitacaoPassagemServiceImpl;
import ufg.br.fabricasw.jatai.swsisviagem.service.SolicitacaoTransporteServiceImpl;
import ufg.br.fabricasw.jatai.swsisviagem.service.UsuarioService;

/**
 * Created by thevilela on 19/03/17.
 */
@Component
public class RequisicaoFacade {

    @Autowired
    private SolicitacaoDiariaServiceImpl solicitacaoDiariaService;

    @Autowired
    private SolicitacaoPassagemServiceImpl solicitacaoPassagemService;

    @Autowired
    private SolicitacaoTransporteServiceImpl solicitacaoTransporteService;

    @Autowired
    private RequisicaoServiceImpl requisicaoService;

    @Autowired
    private UsuarioService userService;

    @Autowired
    private PessoaService pessoaService;

    public Requisicao inserirRequisicao(Requisicao requisicao, Boolean diariaChecked, Boolean transporteChecked,
            Boolean passagemChecked) {

        requisicao.setEstadoRequisicao(EEstadoRequisicao.PREENCHENDO);

        Pessoa pessoa = pessoaService.findPessoaByLogin(userService.findUserLogado().getLoginUnico());
        String proponente = pessoa.getNome();
        requisicao.setProponente(proponente);
        requisicao.setproposto(pessoa);

        List<Solicitacao> solicitacoes = new ArrayList<>();

        if (diariaChecked) {

            SolicitacaoDiaria diaria = new SolicitacaoDiaria();
            diaria.setEstadoSolicitacao(EEstadoSolicitacao.PREENCHENDO);

            solicitacaoDiariaService.save(diaria);

            solicitacoes.add(diaria);
        }

        if (transporteChecked) {

            SolicitacaoTransporte transporte = new SolicitacaoTransporte();
            transporte.setEstadoSolicitacao(EEstadoSolicitacao.PREENCHENDO);

            solicitacaoTransporteService.save(transporte);

            solicitacoes.add(transporte);

        }

        if (passagemChecked) {
            SolicitacaoPassagem passagem = new SolicitacaoPassagem();
            passagem.setEstadoSolicitacao(EEstadoSolicitacao.PREENCHENDO);

            solicitacaoPassagemService.save(passagem);

            solicitacoes.add(passagem);
        }

        requisicao.setSolicitacoes(solicitacoes);

        return requisicaoService.save(requisicao);

    }

    public void atualizarRequisicao(Requisicao requisicao) {

        requisicaoService.atualizar(requisicao);

    }

    public List<Requisicao> listarRequisicoesSubmetidas() {
        return requisicaoService.findSubmetidas();

    }

    public List<Requisicao> listarRequisicoes() {

        return requisicaoService.findAll();

    }

    public List<Requisicao> listarRequisicoesDoUsuario(String username) {
        return requisicaoService.findRequisicoesUsuario(username);
    }

    public List<Requisicao> listarRequisicoesDoUsuarioByDate(String username, String data) {
        return requisicaoService.findRequisicoesUsuarioAndDate(username, data);
    }

    public Requisicao findRequisicao(Long idRequisicao) {
        return requisicaoService.findById(idRequisicao);
    }

    public void submeteRequisicao(Requisicao requisicao) {
        for (Solicitacao s : requisicao.getSolicitacoes()) {

            s.setEstadoSolicitacao(EEstadoSolicitacao.SUBMETIDA);
            if (s instanceof SolicitacaoDiaria) {
                solicitacaoDiariaService.atualizar((SolicitacaoDiaria) s);
            } else if (s instanceof SolicitacaoPassagem) {
                solicitacaoPassagemService.atualizar((SolicitacaoPassagem) s);
            } else if (s instanceof SolicitacaoTransporte) {
                solicitacaoTransporteService.atualizar((SolicitacaoTransporte) s);
            }

        }

        requisicao.setEstadoRequisicao(EEstadoRequisicao.SUBMETIDA);

        requisicaoService.atualizar(requisicao);

    }

}
