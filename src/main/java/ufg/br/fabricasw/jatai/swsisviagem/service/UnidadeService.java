package ufg.br.fabricasw.jatai.swsisviagem.service;

import java.util.ArrayList;
import ufg.br.fabricasw.jatai.swsisviagem.domain.UnidadeDepartamento;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EUnidadeDepartamento;
import ufg.br.fabricasw.jatai.swsisviagem.repository.AbstractCrudService;
import ufg.br.fabricasw.jatai.swsisviagem.repository.UnidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ufg.br.fabricasw.jatai.swsisviagem.controller.Constantes;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.ETipoRequisicao;

/**
 * Created by guilherme on 17/10/17.
 */
@Service
public class UnidadeService implements AbstractCrudService<UnidadeDepartamento, Long> {

    @Autowired
    private UnidadeRepository repository;

    @Override
    public UnidadeDepartamento save(UnidadeDepartamento entity) {
        return repository.save(entity);
    }

    @Override
    public UnidadeDepartamento update(UnidadeDepartamento entity) {
        return repository.save(entity);
    }

    @Override
    public UnidadeDepartamento findOne(Long integer) {
        return repository.getOne(integer);
    }

    @Override
    public boolean exists(Long integer) {
        return repository.existsById(integer);
    }

    @Override
    public List<UnidadeDepartamento> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Long integer) {
        repository.deleteById(integer);

    }

    public Page<UnidadeDepartamento> findAllByPage(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    @Override
    public void delete(UnidadeDepartamento entity) {
        repository.delete(entity);
    }


    public List<UnidadeDepartamento> findAllUnidades() {
        return repository.findAllUnidade();
    }


    public List<UnidadeDepartamento> findByType(EUnidadeDepartamento unidadeDepartamento) {
        return repository.findByUnidadeDepartamento(unidadeDepartamento);
    }

    public Page<UnidadeDepartamento> findByNome(String nome, Pageable pageable) {
        return repository.findByNome(nome, pageable);
    }

    /**
     * Retorna as unidades conforme to tipo de requisição.
     * @param tipoRequisicao - 'unidadesAdministrativa', 'unidadesGraduacao'
     * @return - Lista com as unidades em JSON.
     */
    public List<UnidadeDepartamento> findByTipoRequisicao(String tipoRequisicao) {

        switch (tipoRequisicao) {

            case Constantes.ATTR_TIPO_REQUISICAO_ADM:
            {
                return this.findByType(EUnidadeDepartamento.UnidadeAdministrativa);
            }
            case Constantes.ATTR_TIPO_REQUISICAO_GRA:
            {
                return this.findByType(EUnidadeDepartamento.UnidadeAcademica);
            }
            default:
            {
                return new ArrayList<>();
            }
        }
    }

    /**
     * Retorna as unidades conforme to tipo de requisição.
     * @param tipoRequisicao - 'unidadesAdministrativa', 'unidadesGraduacao', 'unidadesPos'
     * @return - Lista com as unidades em JSON.
     */
    public List<UnidadeDepartamento> findByTipoRequisicao(ETipoRequisicao tipoRequisicao) {

        if (null != tipoRequisicao) switch (tipoRequisicao) {

            case Administrativa:
                return this.findByType(EUnidadeDepartamento.UnidadeAdministrativa);

            case Graduacao:
                return this.findByType(EUnidadeDepartamento.UnidadeAcademica);

            default:
                break;
        }

        return new ArrayList<>();
    }
}
