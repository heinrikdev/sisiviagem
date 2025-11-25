package ufg.br.fabricasw.jatai.swsisviagem.repository;

import ufg.br.fabricasw.jatai.swsisviagem.domain.UnidadeDepartamento;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EUnidadeDepartamento;

/**
 * Created by guilherme on 17/10/17.
 */
public interface UnidadeRepository extends JpaRepository<UnidadeDepartamento, Long> {

    public List<UnidadeDepartamento> findByUnidadeDepartamento(EUnidadeDepartamento eUnidadeDepartamento);

    @Override
    public List<UnidadeDepartamento> findAll();

    @Query("select u from UnidadeDepartamento u where u.unidadeDepartamento = 'Unidade'")
    public List<UnidadeDepartamento> findAllUnidade();


    @Query("select u from UnidadeDepartamento u where u.nome like %:nome%")
    public Page<UnidadeDepartamento> findByNome(@Param("nome") String nome, Pageable pageable);


}