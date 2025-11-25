package ufg.br.fabricasw.jatai.swsisviagem.service;

import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Trecho;
import ufg.br.fabricasw.jatai.swsisviagem.repository.TrechoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;
import ufg.br.fabricasw.jatai.swsisviagem.repository.VeiculoProprioRepository;

/**
 * Created by thevilela on 01/02/17.
 */
@Service
public class TrechoServiceImpl implements AbstractService<Trecho, Long> {

    @Autowired
    private TrechoRepository trechoRepository;
    
    @Autowired
    private VeiculoProprioRepository veiculoProprioRepository;

    public Trecho findById(Long id) {
        return trechoRepository.getOne(id);
    }

    @Override
    public Trecho save(Trecho entidade) {
        return trechoRepository.save(entidade);
    }

    @Override
    @Transactional(rollbackFor = {DataAccessException.class, InvalidDataAccessApiUsageException.class, Exception.class})
    public void apagar(Trecho entidade) {
        
        this.trechoRepository.removerRelacionamentoComSolicitacao(entidade.getId());
        this.veiculoProprioRepository.removerTrecho(entidade.getId());
        this.trechoRepository.removerTrecho(entidade.getId());
    }
    
    @Transactional(rollbackFor = {DataAccessException.class, InvalidDataAccessApiUsageException.class, Exception.class})
    public void apagar(Long trecho_id) {
        this.trechoRepository.removerRelacionamentoComSolicitacao(trecho_id);
        this.veiculoProprioRepository.removerTrecho(trecho_id);
        this.trechoRepository.removerTrecho(trecho_id);
    }

    @Override
    public Trecho atualizar(Trecho entidade) {
        return trechoRepository.save(entidade);
    }

    @Override
    public Trecho find(Trecho entidade) {
        return trechoRepository.getOne(entidade.getId());
    }

    @Override
    public Trecho find(Long id) {
        return trechoRepository.getOne(id);
    }

    @Override
    public List<Trecho> findAll() {
        return trechoRepository.findAll();
    }

}
