package ufg.br.fabricasw.jatai.swsisviagem.service;

import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Abastecimento;
import ufg.br.fabricasw.jatai.swsisviagem.repository.AbastecimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbastecimentoService implements AbstractService<Abastecimento, Long> {

    @Autowired
    private AbastecimentoRepository repository;

    @Override
    public Abastecimento save(Abastecimento entidade) {
        return repository.save(entidade);
    }

    @Override
    public void apagar(Abastecimento entidade) {
        repository.delete(entidade);
    }

    @Override
    public Abastecimento atualizar(Abastecimento entidade) {
        return repository.save(entidade);
    }

    @Override
    public Abastecimento find(Abastecimento entidade) {
        return null;
    }

    @Override
    public Abastecimento find(Long integer) {
        return repository.getOne(integer);
    }

    @Override
    public List<Abastecimento> findAll() {
        return (List<Abastecimento>) repository.findAll();
    }
}
