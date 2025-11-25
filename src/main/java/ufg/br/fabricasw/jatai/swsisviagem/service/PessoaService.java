package ufg.br.fabricasw.jatai.swsisviagem.service;

import ufg.br.fabricasw.jatai.swsisviagem.domain.Pessoa;
import ufg.br.fabricasw.jatai.swsisviagem.repository.AbstractCrudService;
import ufg.br.fabricasw.jatai.swsisviagem.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Passageiro;

/**
 * Created by guilherme on 06/09/17.
 */
@Service
public class PessoaService implements AbstractCrudService<Pessoa, Long> {

    @Autowired
    private PessoaRepository repository;

    @Override
    public Pessoa save(Pessoa entity) {
        return repository.save(entity);
    }

    @Override
    public Pessoa update(Pessoa entity) {
        return repository.save(entity);
    }

    @Override
    public Pessoa findOne(Long integer) {
        return repository.getOne(integer);
    }

    @Override
    public boolean exists(Long integer) {
        return repository.existsById(integer);
    }

    @Override
    public List<Pessoa> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Long integer) {
        repository.deleteById(integer);
    }

    @Override
    public void delete(Pessoa entity) {
        repository.delete(entity);
    }

    public Pessoa findPessoaBySiape(String siape) {
        return repository.findBySiape(siape);
    }

    public List<Pessoa> findAutorizadorByUnidade(Long idUnidade) {
        return repository.findAutorizadorByUnidade(idUnidade);
    }

    public Passageiro fetchPassageiroInfo(Long id_pessoa) {
        return repository.fetchInfoPassageir(id_pessoa);
    }

    public Pessoa findPessoaByLogin(String login) {
        return repository.findByLogin(login);
    }

    public void updateEmail(Long pessoa_id, String email) {
        this.repository.updateEmail(pessoa_id, email);
    }

    public List<String> listEmailAutorizadorByUnidade(Long id) {
        return this.repository.listEmailAutorizadorByUnidade(id);
    }
}
