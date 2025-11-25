package ufg.br.fabricasw.jatai.swsisviagem.repository.auditor;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoSolicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EUnidadeDepartamento;

/**
 * Os auditores fazem o lançamento da prestação de contas e para tal  precisam
 * ver as requições das unidades ao qual são responsáveis.
 * 
 * Este repository é responsável por mostrar as devidas requisições.
 *
 * @author ronogue
 */
@Repository("auditor_requisicao")
public interface AuditorRequisicaoRepository extends JpaRepository<Requisicao, Long> {
    
    @Query
    (
       "SELECT DISTINCT R FROM Requisicao R INNER JOIN R.solicitacoes S " + 
       "WHERE "+ 
       "(S.tipoSolicitacao LIKE 'DIARIA' OR S.tipoSolicitacao LIKE 'PASSAGEM') AND " +
       "(S.estadoSolicitacao LIKE 'APROVADA' OR " +
       "S.estadoSolicitacao LIKE 'DEVOLVIDA') AND "+
       "R.unidade.unidadeDepartamento = :unidade_departamento " +
       "ORDER BY YEAR(R.dataRequisicao) DESC, R.dataRequisicao ASC "
    )
    Page<Requisicao> findAll(@Param("unidade_departamento") EUnidadeDepartamento unidade_departamento, Pageable pageable);
    
    @Query
    (
       "SELECT DISTINCT R FROM Requisicao R INNER JOIN R.solicitacoes S " + 
       "WHERE "+ 
       "(S.tipoSolicitacao LIKE 'DIARIA' OR S.tipoSolicitacao LIKE 'PASSAGEM') AND " +
       "(S.estadoSolicitacao LIKE 'APROVADA' OR " + 
       "S.estadoSolicitacao LIKE 'ENCERRADA' OR "+
       "S.estadoSolicitacao LIKE 'DEVOLVIDA') AND "+
       "R.unidade.unidadeDepartamento = :unidade_departamento AND " +
       "R.id = :requisicao_id " +
       "ORDER BY YEAR(R.dataRequisicao) DESC, R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllByRequisicao(@Param("unidade_departamento") EUnidadeDepartamento unidade_departamento, @Param("requisicao_id") Long requisicao_id, Pageable pageable);
    
    @Query
    (
       "SELECT DISTINCT R FROM Requisicao R INNER JOIN R.solicitacoes S " + 
       "WHERE "+ 
       "(S.tipoSolicitacao LIKE 'DIARIA' OR S.tipoSolicitacao LIKE 'PASSAGEM') AND " +
       "(S.estadoSolicitacao LIKE 'APROVADA' OR " + 
       "S.estadoSolicitacao LIKE 'ENCERRADA' OR "+
       "S.estadoSolicitacao LIKE 'DEVOLVIDA') AND "+
       "R.unidade.unidadeDepartamento = :unidade_departamento AND " +
       "R.proposto.nome LIKE %:proposto_nome% " +
       "ORDER BY YEAR(R.dataRequisicao) DESC, R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllByProposto(@Param("unidade_departamento") EUnidadeDepartamento unidade_departamento,  @Param("proposto_nome") String proposto_nome, Pageable pageable);

    @Query
    (
       "SELECT DISTINCT R FROM Requisicao R INNER JOIN R.solicitacoes S " + 
       "WHERE "+ 
       "(S.tipoSolicitacao LIKE 'DIARIA' OR S.tipoSolicitacao LIKE 'PASSAGEM') AND " +
       "S.estadoSolicitacao LIKE :estado_solicitacao AND " +
       "R.unidade.unidadeDepartamento = :unidade_departamento AND " +
       "YEAR(R.dataRequisicao) = :ano " +
       "ORDER BY R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllByStatus(@Param("unidade_departamento") EUnidadeDepartamento unidade_departamento, @Param("estado_solicitacao") EEstadoSolicitacao status,@Param("ano") Integer ano, Pageable pageable);
    
    @Query
    (
       "SELECT DISTINCT R FROM Requisicao R INNER JOIN R.solicitacoes S " + 
       "WHERE "+ 
       "(S.tipoSolicitacao LIKE 'DIARIA' OR S.tipoSolicitacao LIKE 'PASSAGEM') AND " +
       "S.estadoSolicitacao LIKE :estado_solicitacao AND " +
       "R.unidade.unidadeDepartamento = :unidade_departamento AND " +
       "R.dataRequisicao = :data " +
       "ORDER BY R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllByStatus(@Param("unidade_departamento") EUnidadeDepartamento unidade_departamento, @Param("estado_solicitacao") EEstadoSolicitacao status, @Param("data") LocalDate data, Pageable pageable);
    
    @Query
    (
       "SELECT DISTINCT R FROM Requisicao R INNER JOIN R.solicitacoes S " + 
       "WHERE "+ 
       "(S.tipoSolicitacao LIKE 'DIARIA' OR S.tipoSolicitacao LIKE 'PASSAGEM') AND " +
       "S.estadoSolicitacao LIKE :estado_solicitacao AND " +
       "R.unidade.unidadeDepartamento = :unidade_departamento AND " +
       "R.dataRequisicao BETWEEN :from AND :to " +
       "ORDER BY R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllByStatus(@Param("unidade_departamento") EUnidadeDepartamento unidade_departamento, @Param("estado_solicitacao") EEstadoSolicitacao status, @Param("from") LocalDate from, @Param("to") LocalDate to, Pageable pageable);
    
    @Query
    (
       "SELECT DISTINCT R FROM Requisicao R INNER JOIN R.solicitacoes S " + 
       "WHERE "+ 
       "(S.tipoSolicitacao LIKE 'DIARIA' OR S.tipoSolicitacao LIKE 'PASSAGEM') AND " +
       "(S.estadoSolicitacao LIKE 'APROVADA' OR " + 
       "S.estadoSolicitacao LIKE 'ENCERRADA' OR "+
       "S.estadoSolicitacao LIKE 'DEVOLVIDA') AND "+
       "R.unidade.unidadeDepartamento = :unidade_departamento AND " +
       "YEAR(R.dataRequisicao) = :ano " +
       "ORDER BY R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllByStatus(@Param("unidade_departamento") EUnidadeDepartamento unidade_departamento, @Param("ano") Integer ano, Pageable pageable);
    
    @Query
    (
       "SELECT DISTINCT R FROM Requisicao R INNER JOIN R.solicitacoes S " + 
       "WHERE "+ 
       "(S.tipoSolicitacao LIKE 'DIARIA' OR S.tipoSolicitacao LIKE 'PASSAGEM') AND " +
       "(S.estadoSolicitacao LIKE 'APROVADA' OR " + 
       "S.estadoSolicitacao LIKE 'ENCERRADA' OR "+
       "S.estadoSolicitacao LIKE 'DEVOLVIDA') AND "+
       "R.unidade.unidadeDepartamento = :unidade_departamento AND " +
       "R.dataRequisicao = :data " +
       "ORDER BY R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllByStatus(@Param("unidade_departamento") EUnidadeDepartamento unidade_departamento, @Param("data") LocalDate data, Pageable pageable);
    
    @Query
    (
       "SELECT DISTINCT R FROM Requisicao R INNER JOIN R.solicitacoes S " + 
       "WHERE "+ 
       "(S.tipoSolicitacao LIKE 'DIARIA' OR S.tipoSolicitacao LIKE 'PASSAGEM') AND " +
       "(S.estadoSolicitacao LIKE 'APROVADA' OR " + 
       "S.estadoSolicitacao LIKE 'ENCERRADA' OR "+
       "S.estadoSolicitacao LIKE 'DEVOLVIDA') AND "+
       "R.unidade.unidadeDepartamento = :unidade_departamento AND " +
       "R.dataRequisicao BETWEEN :from AND :to " +
       "ORDER BY R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllByStatus(@Param("unidade_departamento") EUnidadeDepartamento unidade_departamento, @Param("from") LocalDate from, @Param("to") LocalDate to, Pageable pageable);
}
