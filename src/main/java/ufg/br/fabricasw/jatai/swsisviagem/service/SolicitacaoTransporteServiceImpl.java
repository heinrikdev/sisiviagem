package ufg.br.fabricasw.jatai.swsisviagem.service;

import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.Solicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.SolicitacaoTransporte;
import ufg.br.fabricasw.jatai.swsisviagem.repository.SolicitacaoTransporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by thevilela on 01/02/17.
 */
@Service
public class SolicitacaoTransporteServiceImpl implements AbstractService<SolicitacaoTransporte, Long> {

    @Autowired
    private SolicitacaoTransporteRepository solicitacaoRepository;

    public Solicitacao findById(Long id) {
        return solicitacaoRepository.getOne(id);
    }

    public List<SolicitacaoTransporte> listarSolicitacoesAprovadas() {
        return solicitacaoRepository.listarSolicitacoesAprovadas();
    }

    public List<SolicitacaoTransporte> listarSolicitacoesRecusadas() {
        return solicitacaoRepository.listarSolicitacoesRecusadas();
    }

    public List<SolicitacaoTransporte> listarSolicitacoesEmAvaliacao() {
        return solicitacaoRepository.listarSolicitacoesEmAvaliacao();
    }

    @Override
    public SolicitacaoTransporte save(SolicitacaoTransporte entidade) {
        return solicitacaoRepository.save(entidade);
    }

    @Override
    public void apagar(SolicitacaoTransporte entidade) {
        solicitacaoRepository.delete(entidade);
    }

    @Override
    public SolicitacaoTransporte atualizar(SolicitacaoTransporte entidade) {
        return solicitacaoRepository.save(entidade);
    }

    @Override
    public SolicitacaoTransporte find(SolicitacaoTransporte entidade) {
        return solicitacaoRepository.getOne(entidade.getId());
    }

    @Override
    public SolicitacaoTransporte find(Long id) {
        return solicitacaoRepository.getOne(id);
    }

    @Override
    public List<SolicitacaoTransporte> findAll() {
        return solicitacaoRepository.findAll();
    }

}
