package ufg.br.fabricasw.jatai.swsisviagem.service;

import ufg.br.fabricasw.jatai.swsisviagem.domain.Quilometragem;
import ufg.br.fabricasw.jatai.swsisviagem.repository.AbstractCrudService;
import ufg.br.fabricasw.jatai.swsisviagem.repository.QuilometragemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuilometragemService implements AbstractCrudService<Quilometragem, Long> {

    @Autowired
    private QuilometragemRepository repository;

    @Override
    public Quilometragem save(Quilometragem entity) {
        return repository.save(entity);
    }

    @Override
    public Quilometragem update(Quilometragem entity) {
        return repository.save(entity);
    }

    @Override
    public Quilometragem findOne(Long integer) {
        return repository.getOne(integer);
    }

    @Override
    public boolean exists(Long integer) {
        return repository.existsById(integer);
    }

    @Override
    public List<Quilometragem> findAll() {
        return (List<Quilometragem>) repository.findAll();
    }

    @Override
    public void delete(Long integer) {
        repository.deleteById(integer);
    }

    @Override
    public void delete(Quilometragem entity) {
        repository.delete(entity);
    }
}
