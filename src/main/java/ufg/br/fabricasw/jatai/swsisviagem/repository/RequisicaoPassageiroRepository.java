package ufg.br.fabricasw.jatai.swsisviagem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.RequisicaoPassageiro;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoPassageiro;

/**
 *
 * @author Ronaldo N. de Sousa
 */
@Repository
public interface RequisicaoPassageiroRepository extends JpaRepository<RequisicaoPassageiro, Long> {

    @Query 
    (
        "UPDATE RequisicaoPassageiro RP SET RP.estadoIda = :estado WHERE RP.id = :id"
    )
    @Transactional
    @Modifying
    void atualizarEstadoIda(@Param("estado") EEstadoPassageiro estado, @Param("id") Long id);
    
    @Query 
    (
        "UPDATE RequisicaoPassageiro RP SET RP.estadoVolta = :estado WHERE RP.id = :id"
    )
    @Transactional
    @Modifying
    void atualizarEstadoVolta(@Param("estado") EEstadoPassageiro estado, @Param("id") Long id);
    
    @Query 
    (
        "DELETE FROM RequisicaoPassageiro RP WHERE RP.id = :id"
    )
    @Transactional
    @Modifying
    void removerPassageiroDaRequisicao(@Param("id")Long requisicao_passageiro_id);
}
