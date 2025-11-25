package ufg.br.fabricasw.jatai.swsisviagem.service;

import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.Solicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.SolicitacaoPassagem;
import ufg.br.fabricasw.jatai.swsisviagem.repository.SolicitacaoPassagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by thevilela on 01/02/17.
 */
@Service
public class SolicitacaoPassagemServiceImpl implements AbstractService<SolicitacaoPassagem, Long> {

    @Autowired
    private SolicitacaoPassagemRepository solicitacaoRepository;

    public Solicitacao findById(Long id) {
        return solicitacaoRepository.getOne(id);
    }

    public List<SolicitacaoPassagem> listarSolicitacoesAprovadas() {
        return solicitacaoRepository.listarSolicitacoesAprovadas();
    }

    public List<SolicitacaoPassagem> listarSolicitacoesRecusadas() {
        return solicitacaoRepository.listarSolicitacoesRecusadas();
    }

    public List<SolicitacaoPassagem> listarSolicitacoesEmAvaliacao() {
        return solicitacaoRepository.listarSolicitacoesEmAvaliacao();
    }

    @Override
    public SolicitacaoPassagem save(SolicitacaoPassagem entidade) {
        return solicitacaoRepository.save(entidade);
    }

    @Override
    public void apagar(SolicitacaoPassagem entidade) {
        solicitacaoRepository.delete(entidade);
    }

    @Override
    public SolicitacaoPassagem atualizar(SolicitacaoPassagem entidade) {
        return solicitacaoRepository.save(entidade);
    }

    @Override
    public SolicitacaoPassagem find(SolicitacaoPassagem entidade) {
        return solicitacaoRepository.getOne(entidade.getId());
    }

    @Override
    public SolicitacaoPassagem find(Long id) {
        return solicitacaoRepository.getOne(id);
    }

    @Override
    public List<SolicitacaoPassagem> findAll() {
        return solicitacaoRepository.findAll();
    }

}
