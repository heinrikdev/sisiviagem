package ufg.br.fabricasw.jatai.swsisviagem.service;

import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.Solicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.SolicitacaoDiaria;
import ufg.br.fabricasw.jatai.swsisviagem.repository.SolicitacaoDiariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by thevilela on 01/02/17.
 */
@Service
public class SolicitacaoDiariaServiceImpl implements AbstractService<SolicitacaoDiaria, Long> {

    @Autowired
    private SolicitacaoDiariaRepository solicitacaoRepository;

    public Solicitacao findById(Long id) {
        return solicitacaoRepository.getOne(id);
    }

    public List<SolicitacaoDiaria> listarSolicitacoesAprovadas() {
        return solicitacaoRepository.listarSolicitacoesAprovadas();
    }

    public List<SolicitacaoDiaria> listarSolicitacoesRecusadas() {
        return solicitacaoRepository.listarSolicitacoesRecusadas();
    }

    public List<SolicitacaoDiaria> listarSolicitacoesEmAvaliacao() {
        return solicitacaoRepository.listarSolicitacoesEmAvaliacao();
    }

    @Override
    public SolicitacaoDiaria save(SolicitacaoDiaria entidade) {
        return solicitacaoRepository.save(entidade);
    }

    @Override
    public void apagar(SolicitacaoDiaria entidade) {
        solicitacaoRepository.delete(entidade);
    }

    @Override
    public SolicitacaoDiaria atualizar(SolicitacaoDiaria entidade) {
        return solicitacaoRepository.save(entidade);
    }

    @Override
    public SolicitacaoDiaria find(SolicitacaoDiaria entidade) {
        return solicitacaoRepository.getOne(entidade.getId());
    }

    @Override
    public SolicitacaoDiaria find(Long id) {
        return solicitacaoRepository.getOne(id);
    }

    @Override
    public List<SolicitacaoDiaria> findAll() {
        return solicitacaoRepository.findAll();
    }

}
