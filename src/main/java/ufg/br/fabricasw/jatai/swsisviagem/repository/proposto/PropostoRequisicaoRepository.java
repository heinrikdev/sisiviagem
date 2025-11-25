package ufg.br.fabricasw.jatai.swsisviagem.repository.proposto;

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
@Repository("proposto_requisicao_repository")
public interface PropostoRequisicaoRepository  extends JpaRepository<Requisicao, Long> {

    /**
     * Busca todas as requisicoes do proposto.
     * @param id - id do proposto.
     * @param pageable
     * @return 
     */
    @Query
    (
        "SELECT DISTINCT R FROM Requisicao R INNER JOIN R.solicitacoes S " +
        "WHERE R.proposto.id = :proposto_id AND " +
        "S.estadoSolicitacao NOT LIKE 'ENCERRADA' AND S.estadoSolicitacao NOT LIKE 'RECUSADA' "+
        "ORDER BY YEAR(R.dataRequisicao) DESC, R.dataRequisicao ASC "
    )
    Page<Requisicao> findAll(@Param("proposto_id") Long id, Pageable pageable);
    
    /**
     * Busca todas as requisicoes do proposto por status.
     * @param id - id do proposto.
     * @param status - estado da solicitacao.
     * @param pageable
     * @return 
     */
    @Query
    (
       "SELECT DISTINCT R FROM Requisicao R INNER JOIN R.solicitacoes S " + 
       "WHERE "+ 
       "S.estadoSolicitacao LIKE :estado_solicitacao AND "+
       "R.proposto.id = :proposto_id " +
       "ORDER BY YEAR(R.dataRequisicao) DESC, R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllByStatus(@Param("proposto_id") Long id, @Param("estado_solicitacao") EEstadoSolicitacao status, Pageable pageable);
    
    /**
     * Busca todas as requisicoes do proposto por status.
     * @param id - id do proposto.
     * @param requisicao_id - id da requisicao.
     * @param pageable
     * @return 
     */
    @Query
    (
        "SELECT R FROM Requisicao R " + 
        "WHERE R.proposto.id = :proposto_id AND " +
        "R.id = :requisicao_id " +
        "ORDER BY YEAR(R.dataRequisicao) DESC, R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllByRequisicao(@Param("proposto_id") Long id, @Param("requisicao_id") Long requisicao_id, Pageable pageable);
    
    /**
     * Busca todas as requisicoes do proposto por status e ano.
     * @param id - id do proposto.
     * @param status - estado da solicitacao.
     * @param ano
     * @param pageable
     * @return 
     */
    @Query
    (
       "SELECT DISTINCT R FROM Requisicao R INNER JOIN R.solicitacoes S " + 
       "WHERE "+ 
       "S.estadoSolicitacao LIKE :estado_solicitacao AND "+
       "R.proposto.id = :proposto_id AND " +
       "YEAR(R.dataRequisicao) = :ano " +
       "ORDER BY R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllByStatus(@Param("proposto_id") Long id, @Param("estado_solicitacao") EEstadoSolicitacao status, @Param("ano") Integer ano, Pageable pageable);
    
    /**
     * Busca todas as requisicoes do proposto por status em uma data especifica.
     * @param id - id do proposto.
     * @param status - estado da solicitacao.
     * @param data
     * @param pageable
     * @return 
     */
    @Query
    (
       "SELECT DISTINCT R FROM Requisicao R INNER JOIN R.solicitacoes S " + 
       "WHERE "+ 
       "S.estadoSolicitacao LIKE :estado_solicitacao AND "+
       "R.proposto.id = :proposto_id AND " +
       "R.dataRequisicao = :data " +
       "ORDER BY R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllByStatus(@Param("proposto_id") Long id, @Param("estado_solicitacao") EEstadoSolicitacao status, @Param("data") LocalDate data, Pageable pageable);
    
    /**
     * Busca todas as requisicoes do proposto por status em um período específico.
     * @param id - id do proposto.
     * @param status - estado da solicitacao.
     * @param from
     * @param to
     * @param pageable
     * @return 
     */
    @Query
    (
       "SELECT DISTINCT R FROM Requisicao R INNER JOIN R.solicitacoes S " + 
       "WHERE "+ 
       "S.estadoSolicitacao LIKE :estado_solicitacao AND "+
       "R.proposto.id = :proposto_id AND " +
       "R.dataRequisicao BETWEEN :from AND :to " +
       "ORDER BY YEAR(R.dataRequisicao) DESC, R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllByStatus(@Param("proposto_id") Long id, @Param("estado_solicitacao") EEstadoSolicitacao status, @Param("from") LocalDate from, @Param("to") LocalDate to, Pageable pageable);
    
    /**
     * Busca todas as requisicoes do proposto por status e ano.
     * @param id - id do proposto.
     * @param ano
     * @param pageable
     * @return 
     */
    @Query
    (
       "SELECT DISTINCT R FROM Requisicao R INNER JOIN R.solicitacoes S " + 
       "WHERE "+ 
       "R.proposto.id = :proposto_id AND " +
       "YEAR(R.dataRequisicao) = :ano " +
       "ORDER BY R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllByStatus(@Param("proposto_id") Long id, @Param("ano") Integer ano, Pageable pageable);
    
    /**
     * Busca todas as requisicoes do proposto por status em uma data especifica.
     * @param id - id do proposto.
     * @param data
     * @param pageable
     * @return 
     */
    @Query
    (
       "SELECT DISTINCT R FROM Requisicao R INNER JOIN R.solicitacoes S " + 
       "WHERE "+ 
       "R.proposto.id = :proposto_id AND " +
       "R.dataRequisicao = :data " +
       "ORDER BY R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllByStatus(@Param("proposto_id") Long id, @Param("data") LocalDate data, Pageable pageable);
    
    /**
     * Busca todas as requisicoes do proposto por status em um período específico.
     * @param id - id do proposto.
     * @param from
     * @param to
     * @param pageable
     * @return 
     */
    @Query
    (
       "SELECT DISTINCT R FROM Requisicao R INNER JOIN R.solicitacoes S " + 
       "WHERE "+ 
       "R.proposto.id = :proposto_id AND " +
       "R.dataRequisicao BETWEEN :from AND :to " +
       "ORDER BY YEAR(R.dataRequisicao) DESC, R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllByStatus(@Param("proposto_id") Long id, @Param("from") LocalDate from, @Param("to") LocalDate to, Pageable pageable);
}
