package ufg.br.fabricasw.jatai.swsisviagem.repository.unidades;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;

/**
 * Repository que possui os metodos de busca de requisições para a visão das 
 * unidades.
 * 
 * As unidades são responsáveis por aprovar as requisições de diáris e passagem.
 * 
 * 
 * @author ronogue
 */
@Repository("unidades_requisicao_repository")
public interface UnidadesRequisicaoRepository extends JpaRepository<Requisicao, Long>{
    
    /**
     * Busca uma requisição especifica de uma unidade especifica.
     * 
     * @param unidade_id identificação da unidade.
     * @param requisicao_id identifcação da requisição
     * @param pageable informações da página
     * @return 
     */
    @Query
    (
        value = QueriesUnidades.SELECT_ALL_REQUISICAOS_BY_ID_TO_UNIDADE,
        countQuery = QueriesUnidades.SELECT_COUNT_RESULTS_BY_REQUISICAO_ID,
        nativeQuery = true
    )
    Page<Requisicao> findAllByRequisicaoId(@Param("unidade_id")Long unidade_id, @Param("requisicao_id")Long requisicao_id, Pageable pageable);
    
    /**
     * Busca todas a requisições enviadas para uma unidade especifica.
     * 
     * @param unidade_id identificação da unidade.
     * @param status
     * @param pageable informações da página
     * @return 
     */
    @Query
    (
        value = QueriesUnidades.SELECT_REQUISICAOS_TO_UNIDADE_BY_STATUS,
        countQuery = QueriesUnidades.SELECT_COUNT_RESULTS_BY_STATUS,
        nativeQuery = true
    )
    Page<Requisicao> findAllByStatus(@Param("unidade_id")Long unidade_id, @Param("estado_solicitacao") String status, Pageable pageable);
    
    /**
     * Busca todas a requisições enviadas para uma unidade especifica.
     * 
     * @param unidade_id identificação da unidade.
     * @param proposto_nome
     * @param pageable informações da página
     * @return 
     */
    @Query
    (
        value = QueriesUnidades.SELECT_ALL_REQUISICAOS_BY_PROPOSTO_TO_UNIDADE,
        countQuery = QueriesUnidades.SELECT_COUNT_RESULTS_BY_PROPOSTO,
        nativeQuery = true
    )
    Page<Requisicao> findAllByProposto(@Param("unidade_id")Long unidade_id, @Param("proposto")String proposto_nome , Pageable pageable);
    
    /**
     * Busca todas a requisições enviadas para uma unidade especifica.
     * 
     * @param unidade_id identificação da unidade.
     * @param status
     * @param ano
     * @param pageable informações da página
     * @return 
     */
    @Query
    (
        value = QueriesUnidades.SELECT_REQUISICAOS_BY_STATUS_ANO,
        countQuery = QueriesUnidades.SELECT_COUNT_RESULTS_BY_STATUS_ANO,
        nativeQuery = true
    )
    Page<Requisicao> findAllByStatus(@Param("unidade_id")Long unidade_id, @Param("estado_solicitacao") String status, @Param("ano") Integer ano, Pageable pageable);
    
    /**
     * Busca todas a requisições enviadas para uma unidade especifica.
     * 
     * @param unidade_id identificação da unidade.
     * @param status
     * @param data
     * @param pageable informações da página
     * @return 
     */
    @Query
    (
        value = QueriesUnidades.SELECT_REQUISICAOS_BY_STATUS_DATA,
        countQuery = QueriesUnidades.SELECT_COUNT_RESULTS_BY_STATUS_DATA,
        nativeQuery = true
    )
    Page<Requisicao> findAllByStatus(@Param("unidade_id")Long unidade_id, @Param("estado_solicitacao") String status, @Param("data") LocalDate data, Pageable pageable);
    
    /**
     * Busca todas a requisições enviadas para uma unidade especifica.
     * 
     * @param unidade_id identificação da unidade.
     * @param status
     * @param from
     * @param to
     * @param pageable informações da página
     * @return 
     */
    @Query
    (
        value = QueriesUnidades.SELECT_REQUISICAOS_BY_STATUS_PERIODO,
        countQuery = QueriesUnidades.SELECT_COUNT_RESULTS_BY_STATUS_PERIODO,
        nativeQuery = true
    )
    Page<Requisicao> findAllByStatus(@Param("unidade_id")Long unidade_id, @Param("estado_solicitacao") String status, @Param("from") LocalDate from, @Param("to") LocalDate to, Pageable pageable);
    
     /**
     * Busca todas a requisições enviadas para uma unidade especifica.
     * 
     * @param unidade_id identificação da unidade.
     * @param ano
     * @param pageable informações da página
     * @return 
     */
    @Query
    (
        value = QueriesUnidades.SELECT_REQUISICAOS_BY_ANO,
        countQuery = QueriesUnidades.SELECT_COUNT_RESULTS_BY_ANO,
        nativeQuery = true
    )
    Page<Requisicao> findAllByStatus(@Param("unidade_id")Long unidade_id, @Param("ano") Integer ano, Pageable pageable);
    
    /**
     * Busca todas a requisições enviadas para uma unidade especifica.
     * 
     * @param unidade_id identificação da unidade.
     * @param data
     * @param pageable informações da página
     * @return 
     */
    @Query
    (
        value = QueriesUnidades.SELECT_REQUISICAOS_BY_DATA,
        countQuery = QueriesUnidades.SELECT_COUNT_RESULTS_BY_DATA,
        nativeQuery = true
    )
    Page<Requisicao> findAllByStatus(@Param("unidade_id")Long unidade_id, @Param("data") LocalDate data, Pageable pageable);
    
    /**
     * Busca todas a requisições enviadas para uma unidade especifica.
     * 
     * @param unidade_id identificação da unidade.
     * @param from
     * @param to
     * @param pageable informações da página
     * @return 
     */
    @Query
    (
        value = QueriesUnidades.SELECT_REQUISICAOS_BY_PERIODO,
        countQuery = QueriesUnidades.SELECT_COUNT_RESULTS_BY_PERIODO,
        nativeQuery = true
    )
    Page<Requisicao> findAllByStatus(@Param("unidade_id")Long unidade_id, @Param("from") LocalDate from, @Param("to") LocalDate to, Pageable pageable);
}
