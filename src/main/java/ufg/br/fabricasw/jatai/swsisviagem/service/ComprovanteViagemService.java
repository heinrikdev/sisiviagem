package ufg.br.fabricasw.jatai.swsisviagem.service;

import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.ComprovanteViagem;
import ufg.br.fabricasw.jatai.swsisviagem.repository.AbstractCrudService;
import ufg.br.fabricasw.jatai.swsisviagem.repository.ComprovanteViagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComprovanteViagemService implements AbstractCrudService<ComprovanteViagem, Long> {

    @Autowired
    ComprovanteViagemRepository repository;

    @Override
    public ComprovanteViagem save(ComprovanteViagem entity) {
        return repository.save(entity);
    }

    @Override
    public ComprovanteViagem update(ComprovanteViagem entity) {
        return repository.save(entity);
    }

    @Override
    public ComprovanteViagem findOne(Long integer) {
        return repository.getOne(integer);
    }

    @Override
    public boolean exists(Long integer) {
        return repository.existsById(integer);
    }

    @Override
    public List<ComprovanteViagem> findAll() {
        return (List<ComprovanteViagem>) repository.findAll();
    }

    @Override
    public void delete(Long integer) {
        repository.deleteById(integer);
    }

    @Override
    public void delete(ComprovanteViagem entity) {
        repository.delete(entity);
    }

    public ComprovanteViagem findByCodVerificacao(Long codigo) {
        return repository.findBycodigoVerificacao(codigo);
    }
}
