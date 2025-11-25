package ufg.br.fabricasw.jatai.swsisviagem.service;

import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Veiculo;
import ufg.br.fabricasw.jatai.swsisviagem.repository.AbstractCrudService;
import ufg.br.fabricasw.jatai.swsisviagem.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by guilherme on 24/11/17.
 */
@Service
public class VeiculoService implements AbstractCrudService<Veiculo, Long> {

    @Autowired
    private VeiculoRepository repository;

    @Override
    public Veiculo save(Veiculo entity) {
        return repository.save(entity);
    }

    @Override
    public Veiculo update(Veiculo entity) {
        return repository.save(entity);
    }

    @Override
    public Veiculo findOne(Long id) {
        return repository.getOne(id);
    }

    @Override
    public boolean exists(Long id) {
        return repository.existsById(id);
    }

    @Override
    public List<Veiculo> findAll() {
        return (List<Veiculo>) repository.findAll();
    }

    public Page<Veiculo> findAllVeiculosAtivos(Pageable pageable) {
        return this.repository.findAllVeiculosAtivosOrInativos(Boolean.TRUE, pageable);
    }

    public Page<Veiculo> findAllVeiculosInativos(Pageable pageable) {
        return this.repository.findAllVeiculosAtivosOrInativos(Boolean.FALSE, pageable);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);

    }

    @Override
    public void delete(Veiculo entity) {
        repository.delete(entity);

    }

    public List<Veiculo> findByInfo(String info) {
        return repository.findByInfo(info);
    }

    /**
     * Busca todas os veiculos por página.
     *
     * @param pageable descrição da página.
     * @return
     */
    public Page<Veiculo> findAll(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    /**
     * Alterna o status do veículo.
     *
     * @param id           id do veículo
     * @param status_atual status atual.
     */
    public void alternarStatus(Long id, Boolean status_atual) {

        status_atual = !status_atual;
        this.repository.alternarStatus(id, status_atual);
    }

    /**
     * Busca todos os veículos, exceto os que foram escalados para uma
     * determinada viagem.
     *
     * @param viagem_id
     * @return
     */
    public List<Veiculo> findAllVeiculoExcetoNaViagem(Long viagem_id) {
        return this.repository.findAllVeiculoExcetoNaViagem(viagem_id);
    }
}
