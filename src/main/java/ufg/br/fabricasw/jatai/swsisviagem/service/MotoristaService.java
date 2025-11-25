package ufg.br.fabricasw.jatai.swsisviagem.service;

import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Motorista;
import ufg.br.fabricasw.jatai.swsisviagem.repository.MotoristaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class MotoristaService implements AbstractService<Motorista, Long> {

    @Autowired
    private MotoristaRepository repository;

    @Override
    public Motorista save(Motorista entidade) {
        return repository.save(entidade);
    }

    @Override
    public void apagar(Motorista entidade) {
        repository.delete(entidade);
    }

    @Override
    public Motorista atualizar(Motorista entidade) {
        return repository.save(entidade);
    }

    @Override
    public Motorista find(Motorista entidade) {
        return null;
    }

    @Override
    public Motorista find(Long integer) {
        return repository.getOne(integer);
    }

    @Override
    public List<Motorista> findAll() {
        return (List<Motorista>) repository.findAll();
    }
    
    public Page<Motorista> findAllMotoritasAtivos(Pageable pageable) {
        return this.repository.findAllMotoristaAtivosOrInativos(Boolean.TRUE, pageable);
    }
    
    public Page<Motorista> findAllMotoritasInativos(Pageable pageable) {
        return this.repository.findAllMotoristaAtivosOrInativos(Boolean.FALSE, pageable);
    }

    public List<Motorista> findByNome(String nome) {
        return repository.findByName(nome);
    }
    
    /**
     * Busca todos os motoristas.
     * @param pageable descrição da página.
     * @return 
     */
    public Page<Motorista> findAll(Pageable pageable) {
        return this.repository.findAll(pageable);
    }
    
    /**
     * Alterna o status do motorista.
     * @param id id do motorista
     * @param status_atual o status atual
     */
    public void alternarStatus(Long id, Boolean status_atual) {
        
        status_atual = !status_atual;
        this.repository.alternarStatus(status_atual, id);
    }
    
    /**
     * Busca todos os motoristas, exceto os que foram escalados para uma viagem.
     * @param viagem_id
     * @return 
     */
    public List<Motorista> findAllMotoristaExcetoNaViagem(Long viagem_id) {
        return this.repository.findAllMotoristaExcetoNaViagem(viagem_id);
    }
}
