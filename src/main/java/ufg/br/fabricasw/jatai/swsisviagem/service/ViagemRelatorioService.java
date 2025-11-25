package ufg.br.fabricasw.jatai.swsisviagem.service;

import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.ViagemRelatorio;
import ufg.br.fabricasw.jatai.swsisviagem.repository.AbstractCrudService;
import ufg.br.fabricasw.jatai.swsisviagem.repository.ViagemRelatorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViagemRelatorioService implements AbstractCrudService<ViagemRelatorio, Long> {

    @Autowired
    ViagemRelatorioRepository repositoty;

    @Override
    public ViagemRelatorio save(ViagemRelatorio entity) {
        return repositoty.save(entity);
    }

    @Override
    public ViagemRelatorio update(ViagemRelatorio entity) {
        return repositoty.save(entity);
    }

    @Override
    public ViagemRelatorio findOne(Long integer) {
        return repositoty.getOne(integer);
    }

    @Override
    public boolean exists(Long integer) {
        return repositoty.existsById(integer);
    }

    @Override
    public List<ViagemRelatorio> findAll() {
        return (List<ViagemRelatorio>) repositoty.findAll();
    }

    @Override
    public void delete(Long integer) {
        repositoty.deleteById(integer);
    }

    @Override
    public void delete(ViagemRelatorio entity) {
        repositoty.delete(entity);
    }

    public ViagemRelatorio findBycodigoVerificacao(Long codigoVerificacao) {
        return repositoty.findBycodigoVerificacao(codigoVerificacao);
    }
}
