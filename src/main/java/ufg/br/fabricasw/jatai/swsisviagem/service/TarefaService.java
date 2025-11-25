package ufg.br.fabricasw.jatai.swsisviagem.service;

import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Tarefa;
import ufg.br.fabricasw.jatai.swsisviagem.repository.AbstractCrudService;
import ufg.br.fabricasw.jatai.swsisviagem.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Viagem;

@Service
public class TarefaService implements AbstractCrudService<Tarefa, Long> {

    @Autowired
    private TarefaRepository repository;

    @Override
    public Tarefa save(Tarefa entity) {
        return repository.save(entity);
    }

    @Override
    public Tarefa update(Tarefa entity) {
        return repository.save(entity);
    }

    @Override
    public Tarefa findOne(Long integer) {
        return repository.getOne(integer);
    }

    @Override
    public boolean exists(Long integer) {
        return false;
    }

    @Override
    public List<Tarefa> findAll() {
        return (List<Tarefa>) repository.findAll();
    }

    @Override
    public void delete(Long integer) {
        repository.deleteById(integer);
    }

    @Override
    public void delete(Tarefa entity) {
        repository.delete(entity);
    }

    public void concluirTarefa(Long tarefa_id) {
        this.repository.tarefaConcluida(tarefa_id);
    }

    public void tarefaNaoRealizada(Long tarefa_id) {
        this.repository.tarefaNaoRealizada(tarefa_id);
    }

    public List<Tarefa> findAll(Viagem viagem) {
        return this.repository.findAll(viagem.getId());
    }

    public Long findViagemIdFromTarefa(Long tarefa_id) {
        return this.repository.findViagemByTarefa(tarefa_id);
    }
}
