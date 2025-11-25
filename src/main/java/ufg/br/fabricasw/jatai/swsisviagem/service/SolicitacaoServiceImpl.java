package ufg.br.fabricasw.jatai.swsisviagem.service;

import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.Solicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.repository.SolicitacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by thevilela on 01/02/17.
 */
@Service
public class SolicitacaoServiceImpl implements AbstractService<Solicitacao, Long> {

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    public Solicitacao findById(Long id) {
        return solicitacaoRepository.getOne(id);
    }

    public List<Solicitacao> listarSolicitacoesAprovadas() {
        return solicitacaoRepository.listarSolicitacoesAprovadas();
    }

    public List<Solicitacao> listarSolicitacoesRecusadas() {
        return solicitacaoRepository.listarSolicitacoesRecusadas();
    }

    public List<Solicitacao> listarSolicitacoesEmAvaliacao() {
        return solicitacaoRepository.listarSolicitacoesEmAvaliacao();
    }

    @Override
    public Solicitacao save(Solicitacao entidade) {
        return solicitacaoRepository.save(entidade);
    }

    @Override
    public void apagar(Solicitacao entidade) {
        solicitacaoRepository.delete(entidade);
    }

    @Override
    public Solicitacao atualizar(Solicitacao entidade) {
        return solicitacaoRepository.save(entidade);
    }

    @Override
    public Solicitacao find(Solicitacao entidade) {
        return solicitacaoRepository.getOne(entidade.getId());
    }

    @Override
    public Solicitacao find(Long id) {
        return solicitacaoRepository.getOne(id);
    }

    @Override
    public List<Solicitacao> findAll() {
        return solicitacaoRepository.findAll();
    }

}
