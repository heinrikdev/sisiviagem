package ufg.br.fabricasw.jatai.swsisviagem.repository.transporte;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoSolicitacao;

/**
 *
 * @author Ronaldo N. de Sousa
 */
@Repository("transporte_requisicoes")
public interface RequisicaoTransporteRepository extends JpaRepository<Requisicao, Long>{

    /**
     * Busca todas as requisições enviadas para o transporte que não foram 
     * aprovadas ou recusadas.
     * 
     * @param pageable
     * @return 
     */
    @Query
    (
       "SELECT R FROM Requisicao R INNER JOIN R.solicitacoes S " + 
       "WHERE S.tipoSolicitacao LIKE 'TRANSPORTE' AND " + 
       "(S.estadoSolicitacao LIKE 'SUBMETIDA' OR S.estadoSolicitacao LIKE 'DEVOLVIDA') AND " + 
       "(R.estadoRequisicao LIKE 'SUBMETIDA'        OR  " + 
       "R.estadoRequisicao LIKE 'DEVOLVIDA') " +
       "ORDER BY YEAR(R.dataRequisicao) DESC, R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllRequisicoes(Pageable pageable);
    
    @Query
    (
       "SELECT R FROM Requisicao R INNER JOIN R.solicitacoes S " + 
       "WHERE S.tipoSolicitacao LIKE 'TRANSPORTE' AND " + 
       "(S.estadoSolicitacao LIKE 'SUBMETIDA') AND " + 
       "(R.estadoRequisicao LIKE 'SUBMETIDA') " +
       "ORDER BY YEAR(R.dataRequisicao) DESC, R.dataRequisicao DESC "
    )
    Page<Requisicao> findAllSubmetidas(Pageable pageable);
    
    /**
     * Busca a requisição eviada para o transporte conforme o id idependente de
     * ter sida aprovada ou não.
     * 
     * @param id id da requisição
     * @param pageable detem as informações da paginação
     * @return 
     */
    @Query
    (
       "SELECT R FROM Requisicao R INNER JOIN R.solicitacoes S " + 
       "WHERE S.tipoSolicitacao LIKE 'TRANSPORTE'   AND " +  
       "(R.estadoRequisicao LIKE 'SUBMETIDA'        OR  " + 
        "R.estadoRequisicao LIKE 'DEVOLVIDA')       AND " +
       "R.id = :id " +
       "ORDER BY YEAR(R.dataRequisicao) DESC, R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllRequisicoes(@Param("id") Long id, Pageable pageable);
    
    /**
     * Busca a requisição eviada para o transporte conforme o id idependente de
     * ter sida aprovada ou não.
     * 
     * @param proposto_nome nomo do proposto que requisitou
     * @param pageable detem as informações da paginação
     * @return 
     */
    @Query
    (
       "SELECT R FROM Requisicao    R "+ 
       "INNER JOIN R.solicitacoes   S " +  
       "INNER JOIN R.proposto       P " +
       "WHERE S.tipoSolicitacao LIKE 'TRANSPORTE'   AND " +  
       "(R.estadoRequisicao LIKE 'SUBMETIDA'        OR  " + 
        "R.estadoRequisicao LIKE 'DEVOLVIDA')       AND " +
       "P.nome LIKE  %:proposto_nome% " +
       "ORDER BY YEAR(R.dataRequisicao) DESC, R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllRequisicoes(@Param("proposto_nome") String proposto_nome, Pageable pageable);
    
    
    /**
     * Busca a requisição eviada para o transporte conforme o id idependente de
     * ter sida aprovada ou não.
     * 
     * @param estado_solicitacao estado da solicitação - SUBMETIDA, APROVADA, ENCERRADA, RECUSADA
     * @param pageable detem as informações da paginação
     * @return  S
     */
    @Query
    (
       "SELECT R FROM Requisicao R INNER JOIN R.solicitacoes S " + 
       "WHERE S.tipoSolicitacao LIKE 'TRANSPORTE' AND " + 
       "S.estadoSolicitacao LIKE :estado_solicitacao AND " + 
       "(R.estadoRequisicao LIKE 'SUBMETIDA'        OR  " + 
        "R.estadoRequisicao LIKE 'DEVOLVIDA') " +
       "ORDER BY YEAR(R.dataRequisicao) DESC, R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllRequisicoesByEstado(@Param("estado_solicitacao") EEstadoSolicitacao estado_solicitacao, Pageable pageable);
    
    /**
     * Busca a requisição eviada para o transporte conforme o id idependente de
     * ter sida aprovada ou não em um ano especifico.
     * 
     * @param estado_solicitacao estado da solicitação - SUBMETIDA, APROVADA, ENCERRADA, RECUSADA
     * @param ano
     * @param pageable detem as informações da paginação
     * @return  S
     */
    @Query
    (
       "SELECT R FROM Requisicao R INNER JOIN R.solicitacoes S " + 
       "WHERE S.tipoSolicitacao LIKE 'TRANSPORTE' AND " + 
       "S.estadoSolicitacao LIKE :estado_solicitacao AND " + 
       "(R.estadoRequisicao LIKE 'SUBMETIDA'        OR  " + 
       "R.estadoRequisicao LIKE 'DEVOLVIDA') AND " +
       "YEAR(R.dataRequisicao) = :ano " +
       "ORDER BY R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllRequisicoesByEstado(@Param("estado_solicitacao") EEstadoSolicitacao estado_solicitacao, @Param("ano") Integer ano, Pageable pageable);
    
    /**
     * Busca a requisição eviada para o transporte conforme o id idependente de
     * ter sida aprovada ou não em uma data especifica.
     * 
     * @param estado_solicitacao estado da solicitação - SUBMETIDA, APROVADA, ENCERRADA, RECUSADA
     * @param data
     * @param pageable detem as informações da paginação
     * @return  S
     */
    @Query
    (
       "SELECT R FROM Requisicao R INNER JOIN R.solicitacoes S " + 
       "WHERE S.tipoSolicitacao LIKE 'TRANSPORTE' AND " + 
       "S.estadoSolicitacao LIKE :estado_solicitacao AND " + 
       "(R.estadoRequisicao LIKE 'SUBMETIDA'        OR  " + 
       "R.estadoRequisicao LIKE 'DEVOLVIDA') AND " +
       "R.dataRequisicao = :data " +
       "ORDER BY R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllRequisicoesByEstado(@Param("estado_solicitacao") EEstadoSolicitacao estado_solicitacao, @Param("data") LocalDate data, Pageable pageable);
    
    /**
     * Busca a requisição eviada para o transporte conforme o id idependente de
     * ter sida aprovada ou não em uma data especifica.
     * 
     * @param estado_solicitacao estado da solicitação - SUBMETIDA, APROVADA, ENCERRADA, RECUSADA
     * @param from
     * @param to
     * @param pageable detem as informações da paginação
     * @return  S
     */
    @Query
    (
       "SELECT R FROM Requisicao R INNER JOIN R.solicitacoes S " + 
       "WHERE S.tipoSolicitacao LIKE 'TRANSPORTE' AND " + 
       "S.estadoSolicitacao LIKE :estado_solicitacao AND " + 
       "(R.estadoRequisicao LIKE 'SUBMETIDA'        OR  " + 
       "R.estadoRequisicao LIKE 'DEVOLVIDA') AND " +
       "R.dataRequisicao BETWEEN :from AND :to " +
       "ORDER BY YEAR(R.dataRequisicao) DESC, R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllRequisicoesByEstado(@Param("estado_solicitacao") EEstadoSolicitacao estado_solicitacao, @Param("from") LocalDate from, @Param("to") LocalDate to, Pageable pageable);
    
    /**
     * Busca a requisição eviada para o transporte conforme o id idependente de
     * ter sida aprovada ou não em um ano especifico.
     * 
     * @param ano
     * @param pageable detem as informações da paginação
     * @return  S
     */
    @Query
    (
       "SELECT R FROM Requisicao R INNER JOIN R.solicitacoes S " + 
       "WHERE S.tipoSolicitacao LIKE 'TRANSPORTE' AND " + 
       "(R.estadoRequisicao LIKE 'SUBMETIDA'        OR  " + 
       "R.estadoRequisicao LIKE 'DEVOLVIDA') AND " +
       "YEAR(R.dataRequisicao) = :ano " +
       "ORDER BY R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllRequisicoesByEstado(@Param("ano") Integer ano, Pageable pageable);
    
    /**
     * Busca a requisição eviada para o transporte conforme o id idependente de
     * ter sida aprovada ou não em uma data especifica.
     * 
     * @param data
     * @param pageable detem as informações da paginação
     * @return  S
     */
    @Query
    (
       "SELECT R FROM Requisicao R INNER JOIN R.solicitacoes S " + 
       "WHERE S.tipoSolicitacao LIKE 'TRANSPORTE' AND " + 
       "(R.estadoRequisicao LIKE 'SUBMETIDA'        OR  " + 
       "R.estadoRequisicao LIKE 'DEVOLVIDA') AND " +
       "R.dataRequisicao = :data " +
       "ORDER BY R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllRequisicoesByEstado(@Param("data") LocalDate data, Pageable pageable);
    
    /**
     * Busca a requisição eviada para o transporte conforme o id idependente de
     * ter sida aprovada ou não em uma data especifica.
     * 
     * @param from
     * @param to
     * @param pageable detem as informações da paginação
     * @return  S
     */
    @Query
    (
       "SELECT R FROM Requisicao R INNER JOIN R.solicitacoes S " + 
       "WHERE S.tipoSolicitacao LIKE 'TRANSPORTE' AND " + 
       "(R.estadoRequisicao LIKE 'SUBMETIDA'        OR  " + 
       "R.estadoRequisicao LIKE 'DEVOLVIDA') AND " +
       "R.dataRequisicao BETWEEN :from AND :to " +
       "ORDER BY YEAR(R.dataRequisicao) DESC, R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllRequisicoesByEstado(@Param("from") LocalDate from, @Param("to") LocalDate to, Pageable pageable);
}
