package ufg.br.fabricasw.jatai.swsisviagem.repository;

import ufg.br.fabricasw.jatai.swsisviagem.domain.Pessoa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.UnidadeDepartamento;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoRequisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EUnidadeDepartamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.Solicitacao;

/**
 * Created by thevilela on 02/02/17.
 */
@Repository
public interface RequisicaoRepository extends JpaRepository<Requisicao, Long> {

    // Carrega o que o form usa SEM pegar solicitacoes (para evitar MultipleBagFetch)
    @Query("""
        select distinct r
        from Requisicao r
        left join fetch r.requisicaoPassageiros rp
        left join fetch rp.passageiro p
        left join fetch r.documentoVeiculoProprio vp
        where r.id = :id
    """)
    Optional<Requisicao> fetchForEdicaoBase(@Param("id") Long id);

    // “Aquece” a lista de solicitacoes com outra query (sem fetch join no root)
    @Query("""
        select s
        from Requisicao r
        join r.solicitacoes s
        where r.id = :id
    """)
    List<Solicitacao> findSolicitacoesOf(@Param("id") Long id);

    @Query("select r from Requisicao r where r.estadoRequisicao = :estado order by STR_TO_DATE(r.dataRequisicao, '%d/%m/%Y')")
    List<Requisicao> findByEstado(@Param("estado") EEstadoRequisicao estado);

    @Query("select r from Requisicao r where r.proponente = :username order by STR_TO_DATE(r.dataRequisicao, '%d/%m/%Y')")
    List<Requisicao> listarRequisicoesDoUsuario(@Param("username") String username);

    @Query("select r from Requisicao r where r.proponente = :username and r.dataRequisicao like %:data order by STR_TO_DATE(r.dataRequisicao, '%d/%m/%Y')")
    List<Requisicao> listarByUsuarioAndData(@Param("username") String username, @Param("data") String data);

    @Query("select distinct r from Requisicao r inner join r.solicitacoes s  where r.unidade = :unidade and r.estadoRequisicao like 'SUBMETIDA' order by STR_TO_DATE(r.dataRequisicao, '%d/%m/%Y')")
    List<Requisicao> listarByUnidade(@Param("unidade") UnidadeDepartamento unidadeDepartamento);

    @Query("select r from Requisicao r inner join r.solicitacoes s where s.id = :idSolicitacao order by STR_TO_DATE(r.dataRequisicao, '%d/%m/%Y')")
    List<Requisicao> findBySolicitacao(@Param("idSolicitacao") Long idSolicitacao);

    @Query("select r from Requisicao r inner join  r.solicitacoes s where s.tipoSolicitacao like 'TRANSPORTE' and r.estadoRequisicao like 'SUBMETIDA' order by STR_TO_DATE(r.dataRequisicao, '%d/%m/%Y')")
    List<Requisicao> findBy();

    @Query("select distinct r from Requisicao r inner join  r.solicitacoes s where r.unidade.unidadeDepartamento like :tipo and s.estadoSolicitacao like 'APROVADA' and s.tipoSolicitacao like 'DIARIA' order by STR_TO_DATE(r.dataRequisicao, '%d/%m/%Y')")
    List<Requisicao> findAllAprovadas(@Param("tipo") EUnidadeDepartamento tipo);

    /**
     * Busca todas as requisicoes do proposto.
     * @param id - id do proposto.
     * @param pageable
     * @return 
     */
    @Query(
        "SELECT R FROM Requisicao R WHERE R.proposto.id = :proposto_id " +
        "ORDER BY YEAR(R.dataRequisicao) DESC, R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllByProposto(@Param("proposto_id") Long id, Pageable pageable);
    
    /**
     * Busca o nome do proposto de uma determinada requisição.
     * @param requisicao_id identificação da requisição.
     * @return Nome do proposto.
     */
    @Query(
        "SELECT P.nome FROM Requisicao R INNER JOIN R.proposto P WHERE R.id = :requisicao_id " +
        "ORDER BY YEAR(R.dataRequisicao) DESC, R.dataRequisicao ASC "
    )
    String findPropostoNomeRequisicao(@Param("requisicao_id") Long requisicao_id);

    /**
     * Busca o nome do proposto de uma determinada requisição.
     * @param requisicao_id identificação da requisição.
     * @return Nome do proposto.
     */
    @Query(
        "SELECT P FROM Requisicao R INNER JOIN R.proposto P WHERE R.id = :requisicao_id "
    )
    Pessoa findPropostoRequisicao(@Param("requisicao_id") Long requisicao_id);
    
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
       "S.estadoSolicitacao LIKE 'SUBMETIDA' AND " + 
       "R.estadoRequisicao LIKE 'SUBMETIDA' " + 
       "ORDER BY YEAR(R.dataRequisicao) DESC, R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllRequisicoesTransporte(Pageable pageable);
    
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
       "R.estadoRequisicao LIKE 'SUBMETIDA'         AND " +
       "R.id = :id " +
       "ORDER BY YEAR(R.dataRequisicao) DESC, R.dataRequisicao ASC "
    )
    Page<Requisicao> findAllRequisicoesTransporte(@Param("id") Long id, Pageable pageable);
    
    @Query
    (
        value = "UPDATE requisicao SET modo_edicao_habilitado = IF(modo_edicao_habilitado, 0, 1) WHERE id = :id",
        nativeQuery = true
    )
    @Transactional
    @Modifying
    void habilitarOuDesabilitarModoEdicao(@Param("id") Long requisicao_id);
    
    @Query
    (
        value = "DELETE FROM viagem_requisicaos WHERE viagem_id = :viagem_id AND requisicaos_id = :requisicao_id",
        nativeQuery = true
    )
    @Transactional
    @Modifying
    void removerViagem(@Param("requisicao_id") Long requisicao_id, @Param("viagem_id") Long viagem_id);
    
    @Query
    (
        value = "INSERT INTO requisicao_prestacao_de_contas(requisicao_id, prestacao_de_contas_id) VALUES(:requisicao_id, :anexo_id)",
        nativeQuery = true
    )
    @Transactional
    @Modifying
    void addPrestacaoContas(@Param("requisicao_id") Long requisicao_id, @Param("anexo_id") Long anexo_id);
    
    @Query("UPDATE Requisicao R SET R.descricaoAtividadesDesenvolvidas = :descricaoAtividadesDesenvolvidas WHERE R.id = :id")
    @Modifying
    @Transactional
    void updateDescricaoAtividadesDesenvolvidas(@Param("descricaoAtividadesDesenvolvidas") String descricaoAtividadesDesenvolvidas, @Param("id") Long requisicao_id);
}
