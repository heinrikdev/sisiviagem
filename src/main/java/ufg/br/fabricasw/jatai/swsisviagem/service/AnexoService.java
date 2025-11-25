package ufg.br.fabricasw.jatai.swsisviagem.service;

import ufg.br.fabricasw.jatai.swsisviagem.domain.arquivo.Anexo;
import ufg.br.fabricasw.jatai.swsisviagem.repository.AnexoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnexoService implements AbstractService<Anexo, Long> {

    @Autowired
    private AnexoRepository repository;

    @Override
    public Anexo save(Anexo entidade) {
        return repository.save(entidade);
    }

    @Override
    public void apagar(Anexo entidade) {
        repository.delete(entidade);
    }
    
    /**
     * Remove o anexo do banco de dados.
     * @param anexo_id id do anexo
     */
    public void deleteAnexo(Long anexo_id) {
        
        if (anexo_id != null) {
            this.repository.deleteAnexo(anexo_id);
        }
    }
    
    /**
     * Remove o anexo do banco de dados.
     * @param anexo_id id do anexo
     */
    public void deleteAnexoViagem(Long anexo_id) {
        
        if (anexo_id != null) {
            this.repository.deleteAnexoViagem(anexo_id);
        }
    }

    @Override
    public Anexo atualizar(Anexo entidade) {
        return repository.save(entidade);
    }

    @Override
    public Anexo find(Long integer) {
        return repository.getById(integer);
    }

    @Override
    public List<Anexo> findAll() {
        return (List<Anexo>) repository.findAll();
    }

    @Override
    public Anexo find(Anexo entidade) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
