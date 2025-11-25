package ufg.br.fabricasw.jatai.swsisviagem.service;

import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.VeiculoProprio;
import ufg.br.fabricasw.jatai.swsisviagem.repository.VeiculoProprioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VeiculoProprioService implements AbstractService<VeiculoProprio, Long> {

    @Autowired
    VeiculoProprioRepository repository;

    @Override
    public VeiculoProprio save(VeiculoProprio entidade) {
        return repository.save(entidade);
    }

    @Override
    public void apagar(VeiculoProprio entidade) {
        repository.delete(entidade);
    }

    @Override
    public VeiculoProprio atualizar(VeiculoProprio entidade) {
        return repository.save(entidade);
    }

    @Override
    public VeiculoProprio find(VeiculoProprio entidade) {
        return this.find(entidade.getId());
    }

    @Override
    public VeiculoProprio find(Long integer) {
        return repository.getOne(integer);
    }

    @Override
    public List<VeiculoProprio> findAll() {
        return (List<VeiculoProprio>) repository.findAll();
    }
}
