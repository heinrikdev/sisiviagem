package ufg.br.fabricasw.jatai.swsisviagem.service;

import ufg.br.fabricasw.jatai.swsisviagem.domain.Mensagem;
import ufg.br.fabricasw.jatai.swsisviagem.repository.AbstractCrudService;
import ufg.br.fabricasw.jatai.swsisviagem.repository.MensagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MensagemService implements AbstractCrudService<Mensagem, Long> {

    @Autowired
    private MensagemRepository repository;

    @Override
    public Mensagem save(Mensagem entity) {
        return repository.save(entity);
    }

    @Override
    public Mensagem update(Mensagem entity) {
        return repository.save(entity);
    }

    @Override
    public Mensagem findOne(Long integer) {
        return repository.getOne(integer);
    }

    @Override
    public boolean exists(Long integer) {
        return false;
    }

    @Override
    public List<Mensagem> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Long integer) {
        repository.deleteById(integer);
    }

    @Override
    public void delete(Mensagem entity) {
        repository.delete(entity);
    }

    public List<Mensagem> findBySolicitacao(Long idSolicitacao) {
        return repository.findBySolicitacao(idSolicitacao);
    }
}
