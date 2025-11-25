package ufg.br.fabricasw.jatai.swsisviagem.service;

import java.util.ArrayList;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Passageiro;
import ufg.br.fabricasw.jatai.swsisviagem.repository.AbstractCrudService;
import ufg.br.fabricasw.jatai.swsisviagem.repository.PassageiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PassageiroService implements AbstractCrudService<Passageiro, Long> {

    @Autowired
    private PassageiroRepository repository;

    @Override
    @Transactional(rollbackFor = {DataAccessException.class, Exception.class})
    public Passageiro save(Passageiro entity) {
        return repository.save(entity);
    }

    @Override
    public Passageiro update(Passageiro entity) {
        return repository.save(entity);
    }

    @Override
    public Passageiro findOne(Long passageiro_id) {
        return repository.getOne(passageiro_id);
    }
    
    public List<Passageiro> findAllBy(String nome) {
        return this.repository.findByNomeContaining(nome);
    }

    @Override
    public boolean exists(Long integer) {
        return false;
    }

    @Override
    public List<Passageiro> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Long integer) {
        repository.deleteById(integer);
    }

    @Override
    public void delete(Passageiro entity) {
        repository.delete(entity);
    }

    public List<Passageiro> findByViagem(Long idViagem) {
        
        return new ArrayList<>();
        //return repository.findByViagem(idViagem);
    }
}
